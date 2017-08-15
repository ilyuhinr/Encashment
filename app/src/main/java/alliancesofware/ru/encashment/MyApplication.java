package alliancesofware.ru.encashment;

import android.app.Application;

import database.HelperFactory;

/**
 * Created by User on 15.08.2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
