package org.xtimms.trackbus.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.ScheduleDatabase;
import org.xtimms.trackbus.task.RetrieveDatabaseVersionTask;

import java.util.concurrent.ExecutionException;

public class ConstantUtils {

    public static int getDbVersion() {
        if (NetworkUtils.isNetworkAvailable(App.getInstance().getAppContext())) {
            final RetrieveDatabaseVersionTask task = new RetrieveDatabaseVersionTask();
            try {
                return Integer.parseInt(task.execute("https://rumblur.hrebeni.uk/ridebus/version.txt").get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }
        else {
            SharedPreferences sharedPreferences = App.getInstance().getAppContext().getSharedPreferences(ScheduleDatabase.DB_VERSION_KEY, Context.MODE_PRIVATE);
            return sharedPreferences.getInt(ScheduleDatabase.DB_VERSION_KEY, 0);
        }
    }

    public static final int DB_VERSION = getDbVersion(); //Изменить, если делались изменения в БД

    public static final int MINUTES_PASS = 4; // Минут прошло после отправления автобуса
    public static final int HOURS_AFTER_MIDNIGHT = 3; //Проверка есть ли автобусы после 00:00ч.(до 3-х ночи проверяем)
    public static final int HOUR_PER_DAY = 24; //Часов в сутках
    public static final String MINUTES = App.getInstance().getAppContext().getString(R.string.minutes); //символ минут
    public static final String HOURS = App.getInstance().getAppContext().getString(R.string.hours); //Символ часов

    public static final String EMPTY_STRING = ""; // Пустая строка для очистки textView
    public static final String[] ROUTE_TABS = App.getInstance().getResources().getStringArray(R.array.route_tabs);
    public static final String TIME_DELIM = ":"; // Разделитель в строке времени
    public static final String TIME_EMPTY = "-"; // Время отсутствует
    public static final String TWO_SPACES = "  "; // Два пробела
    public static final String ONE_SPACE = " "; // Один пробел

    public static final int NUMBER_BOOKMARKS = 12; // Количество закладок на Home-экране
    public static final String EMAIL = "mailto:ztimms73@gmail.com";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=org.xtimms.trackbus";
    public static final String RUMBLUR = "https://vk.com/rumblur";
    public static final String TELEGRAM = "https://t.me/ridebus";
}
