package barqsoft.footballscores.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by srg1191 on 11/12/2015.
 */
public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context context=null;
    private int appWidgetId;
    private Cursor data = null;
    Bitmap bitmapTarget;
    public WidgetViewsFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        System.out.println("inside datatchanged");
        if (data != null) {
            data.close();
        }
        final long identityToken = Binder.clearCallingIdentity();

        // set the date for today
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // load the fixtures of today
        data = context.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                null,
                null,
                new String[] { simpleDateFormat.format(date) },
                DatabaseContract.scores_table.TIME_COL +" ASC, "+ DatabaseContract.scores_table.HOME_COL +" ASC");

        // and restore the identity again
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(i)) {
            return null;
        }
        System.out.println("inside getview at");
        // get the list item layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_list_item);

        views.setTextViewText(R.id.home_name, data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_COL)));
        views.setTextViewText(R.id.away_name, data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)));
        views.setTextViewText(R.id.data_textview, data.getString(data.getColumnIndex(DatabaseContract.scores_table.TIME_COL)));
        views.setTextViewText(R.id.score_textview, Utilies.getScores(
                data.getInt(data.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)),
                data.getInt(data.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL))));


        try {
            bitmapTarget = Picasso.with(context)
                    .load(data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_LOGO_URL)))
                    .error(R.drawable.no_icon)
                    .get();
            views.setImageViewBitmap(R.id.home_crest, bitmapTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bitmapTarget = Picasso.with(context)
                    .load(data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_LOGO_URL)))
                    .error(R.drawable.no_icon)
                    .get();
            views.setImageViewBitmap(R.id.away_crest, bitmapTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.scores_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        if (data.moveToPosition(i))
            return data.getLong(0);
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
