package northeastern.cs5520fa23.greenthumbs.model.weather;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;

public class WeatherCheckWorker extends Worker {

    public WeatherCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WeatherService weatherService = new WeatherService();
        Context context = getApplicationContext();

        WeatherAlertHelper weatherAlertHelper = new WeatherAlertHelper(weatherService, context);
        weatherAlertHelper.checkWeatherAndNotify();

        return Result.success();
    }
}

