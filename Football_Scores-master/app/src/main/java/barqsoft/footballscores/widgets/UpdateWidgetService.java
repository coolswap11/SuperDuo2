package barqsoft.footballscores.widgets;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Random;

import barqsoft.footballscores.R;

/**
 * Created by 597753 on 11-12-2015.
 */
public class UpdateWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        System.out.println("inside updateservice");
        return (new WidgetViewsFactory(this.getApplicationContext(),
                intent));
    }
}
