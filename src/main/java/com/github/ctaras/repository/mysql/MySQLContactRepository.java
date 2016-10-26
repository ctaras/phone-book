package com.github.ctaras.repository.mysql;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.repository.ContactRepository;
import com.github.ctaras.repository.DataStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
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
import java.util.Objects;

@Profile("mysql")
@Repository
public class MySQLContactRepository extends JdbcDaoSupport implements ContactRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(MySQLContactRepository.class);

    private final DataStruct dt;

    private final SelectContactByUserId selectContactByUserId;
    private final SelectContactById selectContactById;
    private final InsertContact insertContact;
    private final UpdateContact updateContact;

    @Autowired
    public MySQLContactRepository(JdbcTemplate jdbcTemplate, DataSource dataSource, DataStruct dt) {
        setJdbcTemplate(jdbcTemplate);
        setDataSource(dataSource);

        this.dt = dt;

        this.selectContactByUserId = new SelectContactByUserId();
        this.selectContactById = new SelectContactById();
        this.insertContact = new InsertContact();
        this.updateContact = new UpdateContact();
    }

    @Override
    public List<Contact> findAllByUser(User user) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(dt.USER_ID, user.getId());

        return selectContactByUserId.executeByNamedParam(paramMap);
    }

    @Override
    public Contact insert(Contact contact) {
        return updateSQL(insertContact, contact);
    }

    @Override
    public Contact update(Contact contact) {
        return updateSQL(updateContact, contact);
    }

    @Override
    public Contact findById(String id) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(dt.CON_ID, id);

        return selectContactById.executeByNamedParam(paramMap).stream().findFirst().orElse(null);
    }

    @Override
    public boolean delete(Contact contact) {
        Objects.requireNonNull(contact);
        try {
            final String SQL_DELETE_CONTACT = "DELETE FROM contact WHERE contact_id = ?";
            getJdbcTemplate().update(SQL_DELETE_CONTACT, contact.getId());
        } catch (DataAccessException ex) {
            return false;
        }
        return true;
    }

    private Contact contactOfResultSet(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getString(dt.CON_ID));
        contact.setUserId(rs.getString(dt.USER_ID));
        contact.setLastName(rs.getString(dt.CON_LAST_NAME));
        contact.setFirstName(rs.getString(dt.CON_FIRST_NAME));
        contact.setMiddleName(rs.getString(dt.CON_MIDDLE_NAME));
        contact.setMobilePhoneNumber(rs.getString(dt.CON_MOB_PHONE_NUM));
        contact.setHomePhoneNumber(rs.getString(dt.CON_HOME_PHONE_NUM));
        contact.setAddress(rs.getString(dt.CON_ADDRESS));
        contact.setEmail(rs.getString(dt.CON_EMAIL));

        return contact;
    }

    private Contact updateSQL(SqlUpdate sqlUpdate, Contact contact) {
        Map<String, Object> paramМap = new HashMap<>();
        paramМap.put(dt.CON_ID, contact.getId());
        paramМap.put(dt.USER_ID, contact.getUserId());
        paramМap.put(dt.CON_LAST_NAME, contact.getLastName());
        paramМap.put(dt.CON_FIRST_NAME, contact.getFirstName());
        paramМap.put(dt.CON_MIDDLE_NAME, contact.getMiddleName());
        paramМap.put(dt.CON_MOB_PHONE_NUM, contact.getMobilePhoneNumber());
        paramМap.put(dt.CON_HOME_PHONE_NUM, contact.getHomePhoneNumber());
        paramМap.put(dt.CON_ADDRESS, contact.getAddress());
        paramМap.put(dt.CON_EMAIL, contact.getEmail());

        sqlUpdate.updateByNamedParam(paramМap);

        return contact;
    }

    private class SelectContactByUserId extends MappingSqlQuery<Contact> {
        private static final String SQL_FIND_BY_USER_ID =
                "SELECT contact_id, user_id, last_name, first_name, middle_name, " +
                        "mobile_phone_number, home_phone_number, address, email " +
                        "FROM contact WHERE user_id = :user_id";

        SelectContactByUserId() {
            super(getDataSource(), SQL_FIND_BY_USER_ID);
            super.declareParameter(new SqlParameter(dt.USER_ID, Types.VARCHAR));
        }

        protected Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
            return contactOfResultSet(rs);
        }
    }

    private class SelectContactById extends MappingSqlQuery<Contact> {
        private static final String SQL_FIND_BY_ID =
                "SELECT contact_id, user_id, last_name, first_name, middle_name, " +
                        "mobile_phone_number, home_phone_number, address, email " +
                        "FROM contact WHERE contact_id = :contact_id LIMIT 1";

        SelectContactById() {
            super(getDataSource(), SQL_FIND_BY_ID);
            super.declareParameter(new SqlParameter(dt.CON_ID, Types.VARCHAR));
        }

        protected Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
            return contactOfResultSet(rs);
        }
    }

    private class InsertContact extends SqlUpdate {
        private static final String SQL_INSERT_CONTACT =
                "INSERT INTO contact (contact_id, user_id, last_name, first_name, " +
                        "middle_name, mobile_phone_number, home_phone_number, address, email) " +
                        "VALUES(:contact_id, :user_id, :last_name, :first_name, " +
                        ":middle_name, :mobile_phone_number, :home_phone_number, :address, :email)";

        InsertContact() {
            super(getDataSource(), SQL_INSERT_CONTACT);
            super.declareParameter(new SqlParameter(dt.CON_ID, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_ID, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_LAST_NAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_FIRST_NAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_MIDDLE_NAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_MOB_PHONE_NUM, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_HOME_PHONE_NUM, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_ADDRESS, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_EMAIL, Types.VARCHAR));
        }
    }

    private class UpdateContact extends SqlUpdate {
        private static final String SQL_UPDATE_CONTACT =
                "UPDATE contact SET user_id = :user_id, last_name = :last_name, first_name = :first_name, " +
                        "middle_name = :middle_name, mobile_phone_number = :mobile_phone_number, " +
                        "home_phone_number = :home_phone_number, address = :address, email = :email " +
                        "WHERE contact_id = :contact_id";

        UpdateContact() {
            super(getDataSource(), SQL_UPDATE_CONTACT);
            super.declareParameter(new SqlParameter(dt.CON_ID, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.USER_ID, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_LAST_NAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_FIRST_NAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_MIDDLE_NAME, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_MOB_PHONE_NUM, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_HOME_PHONE_NUM, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_ADDRESS, Types.VARCHAR));
            super.declareParameter(new SqlParameter(dt.CON_EMAIL, Types.VARCHAR));
        }
    }
}
