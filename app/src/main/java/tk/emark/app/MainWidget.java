package tk.emark.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainWidget extends AppWidgetProvider {

    private static String currentEuro = "loading...";
    private static String currentBtc = "loading...";

    static void UpdateOne(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);

        SpannableString euroSpan = new SpannableString(currentEuro + " EUR");
        euroSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#e8be74")), currentEuro.length(), (currentEuro + " EUR").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        views.setTextViewText(R.id.appwidget_text, euroSpan);

        SpannableString btcSpan = new SpannableString(currentBtc + " BTC");
        btcSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#e8be74")), currentBtc.length(), (currentBtc + " BTC").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        views.setTextViewText(R.id.appwidget_text2, btcSpan);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void UpdateAll(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            UpdateOne(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String euroURL = "https://api.emark.tk/app/euro.txt";
        String btcURL = "https://api.emark.tk/app/btc.txt";

        StringRequest euroRequest = new StringRequest(Request.Method.GET, euroURL,
                response -> {
                    currentEuro = response;
                    UpdateAll(context, appWidgetManager, appWidgetIds);
                },
                error -> {
                    currentEuro = "error: " + error.networkResponse;
                    UpdateAll(context, appWidgetManager, appWidgetIds);
                }
        );
        StringRequest btcRequest = new StringRequest(Request.Method.GET, btcURL,
                response -> {
                    currentBtc = response;
                    UpdateAll(context, appWidgetManager, appWidgetIds);
                },
                error -> {
                    currentBtc = "error: " + error.networkResponse;
                    UpdateAll(context, appWidgetManager, appWidgetIds);
                }
        );

        queue.add(euroRequest);
        queue.add(btcRequest);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}