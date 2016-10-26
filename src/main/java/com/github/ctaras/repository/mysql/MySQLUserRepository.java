package com.github.ctaras.repository.mysql;

import com.github.ctaras.domain.User;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("mysql")
@Repository
public class MySQLUserRepository extends JdbcDaoSupport implements UserRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(MySQLUserRepository.class);

    private final DataStruct dt;

    private final SelectAllUsers selectAllUsers;
    private final InsertUser insertUser;
    private final UpdateUser updateUser;
    private final SelectUserByLogin selectUserByLogin;
    private final SelectUserById selectUserById;

    @Autowired
    public MySQLUserRepository(JdbcTemplate jdbcTemplate, DataSource dataSource, DataStruct dt) {
        setJdbcTemplate(jdbcTemplate);
        setDataSource(dataSource);

        this.dt = dt;

        this.selectAllUsers = new SelectAllUsers();
        this.insertUser = new InsertUser();
        this.updateUser = new UpdateUser();
        this.selectUserByLogin = new SelectUserByLogin();
        this.selectUserById = new SelectUserById();
    }

    @Override
    public List<User> findAll() {
        return selectAllUsers.execute();
    }

    @Override
    public User insert(User user) {
        return updateSQL(insertUser, user);
    }

    @Override
    public User update(User user) {
        return updateSQL(updateUser, user);
    }

    private User updateSQL(SqlUpdate sqlUpdate, User user) {
        Map<String, Object> paramМap = new HashMap<>();
        paramМap.put(dt.USER_ID, user.getId());
        paramМap.put(dt.USER_LOGIN, user.getLogin());
        paramМap.put(dt.USER_PASSWD, user.getPassword());
        paramМap.put(dt.USER_FULL_NAME, user.getFullName());

        sqlUpdate.updateByNamedParam(paramМap);

        return user;
    }

    @Override
    public User findById(String id) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(dt.USER_ID, id);

        return selectUserById.executeByNamedParam(paramMap).stream().findFirst().orElse(null);
    }

    @Override
    public User findByLogin(String login) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(dt.USER_LOGIN, login);

        return selectUserByLogin.executeByNamedParam(paramMap).stream().findFirst().orElse(null);
    }

    private User userOfResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString(dt.USER_ID));
        user.setLogin(rs.getString(dt.USER_LOGIN));
        user.setPassword(rs.getString(dt.USER_PASSWD));
        user.setFullName(rs.getString(dt.USER_FULL_NAME));

        return user;
    }

    private class SelectAllUsers extends MappingSqlQuery<User> {

        static final String SQL_SELECT_ALL_USERS =
                "SELECT user_id, login, password, full_name FROM user;";

        SelectAllUsers() {
            super(getDataSource(), SQL_SELECT_ALL_USERS);
        }

        @Override
        protected User mapRow(ResultSet rs, int i) throws SQLException {
            return userOfResultSet(rs);
        }
    }

    private class InsertUser extends SqlUpdate {
        private static final String SQL_INSERT_USER =
                "INSERT INTO user (user_id, login, password, full_name) " +
                        "VALUES(:user_id,:login,:password,:full_name)";

        InsertUser() {
            super(getDataSource(), SQL_INSERT_USER);
            super.declareParameter(new SqlParameter(dt.USER_ID, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_LOGIN, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_PASSWD, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_FULL_NAME, Types.VARCHAR));
        }
    }

    private class UpdateUser extends SqlUpdate {
        private static final String SQL_UPDATE_USER =
                "UPDATE user SET login = :login, password = :password, full_name = :full_name " +
                        "WHERE user_id = :user_id";

        UpdateUser() {
            super(getDataSource(), SQL_UPDATE_USER);
            super.declareParameter(new SqlParameter(dt.USER_ID, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_LOGIN, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_PASSWD, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_FULL_NAME, Types.VARCHAR));
        }
    }

    private class SelectUserByLogin extends MappingSqlQuery<User> {
        private static final String SQL_FIND_BY_LOGIN =
                "SELECT user_id, login, password, full_name FROM user WHERE login = :login LIMIT 1";

        SelectUserByLogin() {
            super(getDataSource(), SQL_FIND_BY_LOGIN);
            super.declareParameter(new SqlParameter(dt.USER_LOGIN, Types.VARCHAR));
        }

        protected User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return userOfResultSet(rs);
        }
    }

    private class SelectUserById extends MappingSqlQuery<User> {
        private static final String SQL_FIND_BY_ID =
                "SELECT user_id, login, password, full_name FROM user WHERE user_id = :user_id LIMIT 1";

        SelectUserById() {
            super(getDataSource(), SQL_FIND_BY_ID);
            super.declareParameter(new SqlParameter(dt.USER_ID, Types.VARCHAR));
        }

        protected User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return userOfResultSet(rs);
        }
    }
}
