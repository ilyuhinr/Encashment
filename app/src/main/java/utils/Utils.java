package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import bean.DocumentEncashment;

/**
 * Created by User on 13.08.2017.
 */

public class Utils {

    public static String setDocNo(Context context) {
        String docNo = "";
        SharedPreferences countSettings = context.getSharedPreferences("DocNo", 0);
        int count = countSettings.getInt("DocNo", 0);
        count++;
        final SharedPreferences.Editor edit = countSettings.edit();
        edit.putInt("DocNo", count);
        edit.commit();
        if ((count / 10) < 1) {
            docNo = "000000" + String.valueOf(count);
        } else if ((count / 10) >= 1 && (count / 10) < 10) {
            docNo = "00000" + String.valueOf(count);
        } else if ((count / 10) >= 10 && (count / 10) < 100) {
            docNo = "0000" + String.valueOf(count);
        } else if ((count / 10) >= 100 && (count / 10) < 1000) {
            docNo = "000" + String.valueOf(count);
        } else if ((count / 10) >= 1000 && (count / 10) < 10000) {
            docNo = "00" + String.valueOf(count);
        }
        return docNo;
    }

    public static void setOsnovnoyPlan(Context context, float plan) {
        SharedPreferences countSettings = context.getSharedPreferences("OsnovnoyPlan", 0);
        // float count = countSettings.getFloat("OsnovnoyPlan", 0);
        final SharedPreferences.Editor edit = countSettings.edit();
        edit.putFloat("OsnovnoyPlan", plan);
        edit.commit();

    }

    public static float getOsnovnoyPlan(Context context) {
        float plan = 0f;
        SharedPreferences countSettings = context.getSharedPreferences("OsnovnoyPlan", 0);
        plan = countSettings.getFloat("OsnovnoyPlan", 0f);
        return plan;
    }

    public static String getDateBasePath(Context context) {
        String DB_PATH = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        return DB_PATH;
    }

    public static void setUpdateDBDate(Context context) {
        Date date = Calendar.getInstance().getTime();
        String currentDate = DateFormat.format("dd.MM.yyyy hh:mm", date).toString();
        SharedPreferences countSettings = context.getSharedPreferences("UpdateDbDate", 0);
        // float count = countSettings.getFloat("OsnovnoyPlan", 0);
        final SharedPreferences.Editor edit = countSettings.edit();
        edit.putString("UpdateDbDate", currentDate);
        edit.commit();
    }

    public static String getUpdateDBDate(Context context) {
        String date = "";
        SharedPreferences countSettings = context.getSharedPreferences("UpdateDbDate", 0);
        date = countSettings.getString("UpdateDbDate", "0");
        return date;
    }

    public static boolean getDocLoadStatus(Context context) {
        boolean falg = true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        falg = prefs.getBoolean("pref_doc_visible", true);
        return falg;
    }

    public static int getWorkingDaysBetweenTwoDates() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyyy hh:mm");
        Date date_ot = new Date();
        Date date_do = new Date();
        try {
            date_ot = dateFormat.parse("01." + String.valueOf(month + 1) + "." + String.valueOf(year) + " 00:00");
            date_do = dateFormat.parse("01." + String.valueOf(month + 2) + "." + String.valueOf(year) + " 00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime(date_ot);
        endCal = Calendar.getInstance();
        endCal.setTime(date_do);
        int workDays = 0;
        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) { // &&
                // !holidays.contains((Integer)
                // startCal.get(Calendar.DAY_OF_YEAR))
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
        return workDays;
    }

    public static int getDayOfMonth() {
        Calendar c = Calendar.getInstance();
        int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return monthMaxDays;
    }

    public static String getMoneyFormat(double money) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.ITALY);
        return numberFormatter.format(money);

    }

    public static String getDayOfWeekFormatDocument(Date date) {
        String nameDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        if (nameDay.equals("Sunday")) {
            return "��";
        } else if (nameDay.equals("Monday")) {
            return "��";
        } else if (nameDay.equals("Tuesday")) {
            return "��";
        } else if (nameDay.equals("Wednesday")) {
            return "��";
        } else if (nameDay.equals("Thursday")) {
            return "��";
        } else if (nameDay.equals("Friday")) {
            return "��";
        } else {
            return "��";
        }
    }

    public static String getNameMonth(Date date) {
        String intMonth = (String) android.text.format.DateFormat.format("MM", date);
        String year = (String) android.text.format.DateFormat.format("yyyy", date); // 2013
        String day = (String) android.text.format.DateFormat.format("dd", date);
        int month = Integer.valueOf(intMonth);
        String result = "";
        switch (month) {
            case 1:
                result = "������";
                break;
            case 2:
                result = "�������";
                break;
            case 3:
                result = "�����";
                break;
            case 4:
                result = "������";
                break;
            case 5:
                result = "���";
                break;
            case 6:
                result = "����";
                break;
            case 7:
                result = "����";
                break;
            case 8:
                result = "�������";
                break;
            case 9:
                result = "��������";
                break;
            case 10:
                result = "�������";
                break;
            case 11:
                result = "������";
                break;
            case 12:
                result = "�������";
                break;

        }
        return Integer.valueOf(day) + " " + result + " " + year + " �.";
    }

    public static String getFileName(DocumentEncashment documentEncashment) {
        if (documentEncashment != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            return String.valueOf(documentEncashment.getNumber() + "-" + sdf.format(documentEncashment.getDateDoc()) + ".txt");
        } else {
            return "";
        }
    }

    public static ArrayList<DocumentEncashment> getTestDocuments() {
        ArrayList<DocumentEncashment> encashments = new ArrayList<>();

        DocumentEncashment documentEncashment = new DocumentEncashment();
        documentEncashment.setContractorCode("0001");
        documentEncashment.setContractorName("ИП Передня К.С.");
        documentEncashment.setId(1);
        documentEncashment.setNumber("00004");
        documentEncashment.setStatus(0);
        documentEncashment.setSumm(14255.87f);
        documentEncashment.setDateDoc(Calendar.getInstance().getTime());
        encashments.add(documentEncashment);

        DocumentEncashment documentEncashment1 = new DocumentEncashment();
        documentEncashment1.setContractorCode("0002");
        documentEncashment1.setContractorName("ООО \"Олимп\"");
        documentEncashment1.setId(1);
        documentEncashment1.setNumber("00063");
        documentEncashment1.setStatus(1);
        documentEncashment1.setSumm(11235.17f);
        documentEncashment1.setDateDoc(Calendar.getInstance().getTime());
        encashments.add(documentEncashment1);

        DocumentEncashment documentEncashment2 = new DocumentEncashment();
        documentEncashment2.setContractorCode("0003");
        documentEncashment2.setContractorName("ООО \"Конус\"");
        documentEncashment2.setId(4);
        documentEncashment2.setNumber("00447");
        documentEncashment2.setStatus(2);
        documentEncashment2.setSumm(77855.41f);
        documentEncashment2.setDateDoc(Calendar.getInstance().getTime());
        encashments.add(documentEncashment2);

       /* try {
            HelperFactory.getHelper().getDocument().create(documentEncashment2);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        return encashments;
    }

    public static File generateNoteOnSD(Context context, String sFileName, String sBody) {
        File gpxfile = null;
        try {
            File root = new File(context.getApplicationInfo().dataDir);
            if (!root.exists()) {
                root.mkdirs();
            }
            gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpxfile;
    }


}
