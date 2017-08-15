package alliancesofware.ru.encashment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import adapters.DocumentAdapter;
import bean.DocumentEncashment;
import database.HelperFactory;
import ftp.DownloadFtp;

import static database.HelperFactory.getHelper;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private DocumentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<DocumentEncashment> documentEncashments = HelperFactory.getHelper().getDocument().queryForAll();
                    ArrayList<DocumentEncashment> docs = new ArrayList<DocumentEncashment>();
                    for (DocumentEncashment doc : documentEncashments) {
                        if (doc.getStatus() == 1) {
                            docs.add(doc);
                        }
                        new UpLoadFtpDocument().execute(docs);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buildDialogConfirmation(adapter.getItem(position));
            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "Долгое нажатие", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }


    private void updateList() {
        ArrayList<DocumentEncashment> encashments = new ArrayList<>();
        try {
            encashments = (ArrayList<DocumentEncashment>) HelperFactory.getHelper().getDocument().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new DocumentAdapter(this, encashments);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {
            buildDialogDownloadDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildDialogDownloadDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Обновление данных");
        builder.setMessage("Обновить базу данных?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DownloadFTPDatabase().execute();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void buildDialogConfirmation(final DocumentEncashment documentEncashment) {
        if (documentEncashment.getStatus() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(documentEncashment.getContractorName());
            builder.setMessage("Подтвердить выполнение задания?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    documentEncashment.setStatus(1);
                    try {
                        getHelper().getDocument().update(documentEncashment);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    List<DocumentEncashment> documentEncashments = new ArrayList<DocumentEncashment>();
                    documentEncashments.add(documentEncashment);
                    new UpLoadFtpDocument().execute(documentEncashments);
                    updateList();
                }
            });

            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else if (documentEncashment.getStatus() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(documentEncashment.getContractorName());
            builder.setMessage("Выгрузить документ на сервер?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<DocumentEncashment> documentEncashments = new ArrayList<DocumentEncashment>();
                    documentEncashments.add(documentEncashment);
                    new UpLoadFtpDocument().execute(documentEncashments);
                }
            });

            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(documentEncashment.getContractorName());
            builder.setMessage("Документ уже выгружен на сервер. Повторить выгрузку документа?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<DocumentEncashment> documentEncashments = new ArrayList<DocumentEncashment>();
                    documentEncashments.add(documentEncashment);
                    new UpLoadFtpDocument().execute(documentEncashments);
                }
            });

            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    private class DownloadFTPDatabase extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Обмен");
            progressDialog.setMessage("Загрузка базы документов...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = new DownloadFtp(MainActivity.this).downloadFileFtp(MainActivity.this);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            updateList();
        }
    }

    private class UpLoadFtpDocument extends AsyncTask<List<DocumentEncashment>, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Обмен");
            progressDialog.setMessage("Выгрузка документов...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(List<DocumentEncashment>... params) {
            String result = "Нет документов для обмена!";
            for (DocumentEncashment doc : params[0]) {
                result = new DownloadFtp(MainActivity.this).upLoadFileFtp(null, doc);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            updateList();
        }
    }

}
