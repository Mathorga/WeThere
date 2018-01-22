package keym.dev.rwethereyet.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.R;

/**
 * Created by luka on 14/01/18.
 */

public class WeThereWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Intent intent;

    private final List<String> items;

    public WeThereWidgetDataProvider(Context applicationContext, Intent intent) {
        this.items = new ArrayList<>();
        this.items.add("capra");
        this.items.add("marta");
        this.items.add("eleo");
        this.items.add("mina");
        this.items.add("assssd");

        this.context = applicationContext;
        this.intent = intent;
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

        RemoteViews row = new RemoteViews(this.context.getPackageName(), R.layout.item_distance);

        row.setTextViewText(android.R.id.text1, this.items.get(position));

        return row;


//        if (position == AdapterView.INVALID_POSITION ||
//            this.cursor == null ||
//            !this.cursor.moveToPosition(position)) {
//            return null;
//        }
//
//        RemoteViews rv = new RemoteViews(this.context.getPackageName(), R.layout.item_distance);
//        rv.setTextViewText(R.id.locationDistance, this.cursor.getString(1));
//
//        return rv;
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
//        return this.cursor.moveToPosition(position) ? this.cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
