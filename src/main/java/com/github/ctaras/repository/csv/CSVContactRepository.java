package com.github.ctaras.repository.csv;

import com.github.ctaras.domain.Contact;
import com.github.ctaras.domain.User;
import com.github.ctaras.repository.ContactRepository;
import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Profile("csv")
@Repository
public class CSVContactRepository extends CSVTemplate
        implements ContactRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(CSVContactRepository.class);

    @Autowired
    public CSVContactRepository(CSVConfig config, DataStruct dt) {
        super(config, dt);
    }

    @Override
    public List<Contact> findAllByUser(User user) {
        List<Contact> list = new ArrayList<>();

        Connection conn = null;

        try {
            PreparedStatement pstmt;
            ResultSet results;

            lock.readLock().lock();

            try {
                conn = config.getConnection();

                pstmt = conn.prepareStatement("SELECT * FROM contact WHERE user_id = ?");

                pstmt.setString(1, user.getId());
                results = pstmt.executeQuery();
            } finally {
                lock.readLock().unlock();
            }

            while (results.next()) {
                Contact contact = new Contact();
                contact.setId(results.getString(dt.CON_ID));
                contact.setUserId(results.getString(dt.USER_ID));
                contact.setLastName(results.getString(dt.CON_LAST_NAME));
                contact.setFirstName(results.getString(dt.CON_FIRST_NAME));
                contact.setMiddleName(results.getString(dt.CON_MIDDLE_NAME));
                contact.setMobilePhoneNumber(results.getString(dt.CON_MOB_PHONE_NUM));
                contact.setHomePhoneNumber(results.getString(dt.CON_HOME_PHONE_NUM));
                contact.setAddress(results.getString(dt.CON_ADDRESS));
                contact.setEmail(results.getString(dt.CON_EMAIL));
                list.add(contact);
            }

            pstmt.close();
        } catch (SQLException e) {
            Log.e(logger, () -> "Error of reading contact database", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Log.e(logger, () -> "Error of close connection contact database", e);
                }
            }
        }

        return list;
    }

    @Override
    public Contact insert(Contact contact) {
        if (!appendData(config.getContactTablePath(), getContactRow(contact),
                logger, "Error of writing contact database")) {
            return null;
        }

        return contact;
    }

    @Override
    public Contact update(Contact contact) {
        String id = contact.getId();
        Path filePath = Paths.get(config.getContactTablePath());

        if (!updateData(filePath, id, getContactRow(contact), logger, "Error of writing contact database")) {
            return null;
        }

        return contact;
    }

    @Override
    public Contact findById(String id) {
        Connection conn = null;
        Contact contact = null;

        try {
            PreparedStatement stmt;
            ResultSet results;
            lock.readLock().lock();
            try {
                conn = config.getConnection();

                stmt = conn.prepareStatement("SELECT * FROM contact " +
                        "WHERE contact_id = ? LIMIT 1");
                stmt.setString(1, id);
                results = stmt.executeQuery();
            } finally {
                lock.readLock().unlock();
            }

            if (results.next()) {
                contact = new Contact();
                contact.setId(results.getString(dt.CON_ID));
                contact.setUserId(results.getString(dt.USER_ID));
                contact.setLastName(results.getString(dt.CON_LAST_NAME));
                contact.setFirstName(results.getString(dt.CON_FIRST_NAME));
                contact.setMiddleName(results.getString(dt.CON_MIDDLE_NAME));
                contact.setMobilePhoneNumber(results.getString(dt.CON_MOB_PHONE_NUM));
                contact.setHomePhoneNumber(results.getString(dt.CON_HOME_PHONE_NUM));
                contact.setAddress(results.getString(dt.CON_ADDRESS));
                contact.setEmail(results.getString(dt.CON_EMAIL));
            }
            stmt.close();
        } catch (SQLException e) {
            Log.e(logger, () -> "Error of reading contact database", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Log.e(logger, () -> "Error of close connection contact database", e);
                }
            }
        }

        return contact;
    }

    @Override
    public boolean delete(Contact contact) {
        String id = contact.getId();
        Path filePath = Paths.get(config.getContactTablePath());
        return deleteData(filePath, id, logger, "Error of writing contact database");
    }

    private String getContactRow(Contact contact) {
        return new StringBuilder()
                .append(value(contact.getId()))
                .append(config.columnSeparator)
                .append(value(contact.getUserId()))
                .append(config.columnSeparator)
                .append(value(contact.getLastName()))
                .append(config.columnSeparator)
                .append(value(contact.getFirstName()))
                .append(config.columnSeparator)
                .append(value(contact.getMiddleName()))
                .append(config.columnSeparator)
                .append(value(contact.getMobilePhoneNumber()))
                .append(config.columnSeparator)
                .append(value(contact.getHomePhoneNumber()))
                .append(config.columnSeparator)
                .append(value(contact.getAddress()))
                .append(config.columnSeparator)
                .append(value(contact.getEmail()))
                .toString();
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}
