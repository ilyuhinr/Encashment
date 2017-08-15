package ftp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import bean.DocumentEncashment;
import database.HelperFactory;
import utils.Utils;

/**
 * Created by User on 13.08.2017.
 */

public class DownloadFtp {

    Context context;
    String password;
    String address;
    String login;
    String catalog;
    String port;

    public DownloadFtp(Context context) {
        super();
        this.context = context;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        // then you use
        password = prefs.getString("pref_password", "trade");
        address = prefs.getString("pref_address", "trade");
        login = prefs.getString("pref_login", "trade");
        port = prefs.getString("pref_port", "21");
        catalog = prefs.getString("pref_catalog", "trade1");
    }


    public String upLoadFileFtp(File file, DocumentEncashment document)
            throws NumberFormatException {

        String message = "";

        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect(address, Integer.valueOf(port));
            isConnect();
            if (con.login(login, password)) {
                con.enterLocalPassiveMode(); // Very Important

                con.setFileType(FTP.BINARY_FILE_TYPE); // Very Important
                message = searchFile(con, catalog, document);
                if (message.equals("")) {
                    String data = file.getAbsolutePath();

                    FileInputStream in = new FileInputStream(new File(data));
                    boolean result = con.storeFile(File.separator + catalog
                            + File.separator + Utils.getFileName(document), in);
                    if (result) {
                        message = "��������� ������� ��������� �� ������!";
                        boolean a = con.sendSiteCommand("chmod " + "777 "
                                + "/catalog/" + Utils.getFileName(document));
                        updateDocument(document);
                    } else {
                        message = "��������� �� ���������! ��������� ��������� �����������!";
                    }
                    in.close();

                    Log.v("upload result", "succeeded");
                    con.logout();
                    con.disconnect();

                }
                if (con.isConnected()) {
                    con.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "��������� �� ���������! ��������� ��������� ����������� � ���������� � ����������!";
        }
        return message;
    }

    @SuppressLint("SdCardPath")
    public String downloadFileFtp(Context context) throws NumberFormatException {
        String message = "";

        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect(address, Integer.valueOf(port)); // Its dummy Address
            isConnect();
            if (con.login(login, password)) {
                con.enterLocalPassiveMode(); // Very Important

                con.setFileType(FTP.BINARY_FILE_TYPE); // Very Important

                String data = Utils.getDateBasePath(context) + "agentEncash.db";

                FileOutputStream in = new FileOutputStream(new File(data));
                boolean result = con.retrieveFile(File.separator + catalog
                        + "/agentEncash.db", in);
                in.close();
                if (result) {
                    message = "Обновление прошло успешно!";
                    Utils.setUpdateDBDate(context);
                } else {
                    message = "Произошла ошибка во время обоновления!";
                }
                con.logout();
                con.disconnect();

            } else {
                message = "Не удалось подключиться к FTP серверу. Проверьте логин и пароль";
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "Произошла ошибка при загрузке базы данных! \n " + e.getMessage();
        }
        return message;
    }

    public String isConnect() throws NumberFormatException {

        String message = "";

        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect(address, Integer.valueOf(port));
            // con.setSoTimeout(20000);
            if (con.login(login, password)) {
                message = "Connect successfully!";
            } else {
                message = "Connection not established";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Connection not established.";
        }
        return message;
    }

    public String searchFile(FTPClient con, String catalog, final DocumentEncashment documentEncashment) throws IOException {
        String message = "";
        String dirToSearch = catalog;

        FTPFileFilter filter = new FTPFileFilter() {

            @Override
            public boolean accept(FTPFile ftpFile) {

                return (ftpFile.isFile() && ftpFile.getName().contains(Utils.getFileName(documentEncashment)));
            }
        };

        FTPFile[] result = con.listFiles(dirToSearch, filter);

        if (result != null && result.length > 0) {
            message = "Документ уже выгружался ранее!";
        }
        return message;
    }

    public void updateDocument(DocumentEncashment document) {
        document.setStatus(1);

        try {
            HelperFactory.getHelper().getDocument().update(document);
            HelperFactory.getHelper().getDocument().refresh(document);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
