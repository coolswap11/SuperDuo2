package barqsoft.footballscores.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresProvider;

/**
 * Created by 597753 on 11-12-2015.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @SuppressWarnings("deprecation")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        System.out.println("inside on update-"+appWidgetIds.length);
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent svcIntent = new Intent(context, UpdateWidgetService.class);

//            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(context.getPackageName(),
                    R.layout.widget_scores_list);
            if(Build.VERSION.SDK_INT >= 14)
            {
                widget.setRemoteAdapter(R.id.scoresList, svcIntent);
            }
            else{
                widget.setRemoteAdapter(appWidgetIds[i],R.id.scoresList, svcIntent);
            }

            Intent clickIntent = new Intent(context, MainActivity.class);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent clickPI = PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            0);

            widget.setPendingIntentTemplate(R.id.scoresList, clickPI);
            widget.setEmptyView(R.id.scoresList, R.id.widget_empty);
            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {


            // We have to convert these numbers for future use
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 1000 * 74);
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 1000 * 74);


        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (DatabaseContract.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scoresList);
        }
    }
}
