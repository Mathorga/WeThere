package keym.dev.rwethereyet.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;

/**
 * Created by luka on 14/01/18.
 */

public class WeThereRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WeThereRemoteViewsFactory(this.getApplicationContext(),
                                             new LocationParser(this.getApplicationContext()).readAllItems());
    }
}

class WeThereRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "Factory";

    private Context context;

    private final List<LocationItem> items;

    public WeThereRemoteViewsFactory(final Context applicationContext, final List<LocationItem> items) {
        this.context = applicationContext;
        this.items = items;
        Log.wtf(TAG, items.toString());
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(this.context.getPackageName(), R.layout.item_distance_widget);

        views.setTextViewText(R.id.wLocationDistance, items.get(position).getRadius().toString() + " km");
        views.setTextViewText(R.id.wLocationDistanceName, items.get(position).getLabel());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}