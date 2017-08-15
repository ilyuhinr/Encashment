package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import bean.DocumentEncashment;

/**
 * Created by User on 13.08.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    // имя файла базы данных который будет храниться в
    // /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME = "agentEncash.db";

    // с каждым увеличением версии, при нахождении в устройстве БД с предыдущей
    // версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    // ссылки на DAO соответсвующие сущностям, хранимым в БД


    private EncashmentDAO document = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DocumentEncashment.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, DocumentEncashment.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public EncashmentDAO getDocument() throws SQLException {
        if (document == null) {
            document = new EncashmentDAO(getConnectionSource(), DocumentEncashment.class);
        }
        return document;
    }

    @Override
    public void close() {
        super.close();
        document = null;
    }

}
