package database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import bean.DocumentEncashment;

/**
 * Created by User on 13.08.2017.
 */

public class EncashmentDAO extends BaseDaoImpl<DocumentEncashment, Integer> {


    protected EncashmentDAO(ConnectionSource connectionSource, Class<DocumentEncashment> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

}
