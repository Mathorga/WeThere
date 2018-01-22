package keym.dev.rwethereyet.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import keym.dev.rwethereyet.MainActivity;
import keym.dev.rwethereyet.R;

/**
 * Implementation of App Widget functionality.
 */
public class WeThereAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // PendingIntent to launch the MainActivity when the widget is clicked.
        Intent launchMain = new Intent(context, MainActivity.class);
        PendingIntent pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain, 0);

        // Construct the RemoteViews object which defines the view of out widget.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.we_there_app_widget);
        views.setRemoteAdapter(R.id.wDistanceList, new Intent(context, WeThereRemoteViewService.class));

        // Set action on title click.
        views.setOnClickPendingIntent(R.id.wTitle, pendingMainIntent);

//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.wDistanceList);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

//        super.onUpdate(context, appWidgetManager, appWidgetIds);
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

