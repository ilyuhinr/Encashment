package alliancesofware.ru.encashment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ftp.DownloadFtp;

/**
 * Created by User on 13.08.2017.
 */

public class SettingActivity extends PreferenceActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_connect);

        Preference connectToNewComputer= findPreference("pref_verifi");
        connectToNewComputer.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new IsConnectFtp().execute();
                return false;
            }
        });
    }


    public class MyPreferenceFragment extends PreferenceActivity
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_connect);


        }
    }

    public class IsConnectFtp extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(SettingActivity.this);
            dialog.setTitle("Проверка");
            dialog.setMessage("Проверить соединение с ftp");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... passing) {
            String message = "";

            message = new DownloadFtp(getBaseContext()).isConnect();

            return message;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }

    }
}
