package keym.dev.rwethereyet.addlocation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.MapView;

/**
 * Created by luka on 18/05/17.
 * The class implements a scrollable MapView intercepting touch events.
 */

public class ScrollableMapView extends MapView {

    public ScrollableMapView(Context context) {
        super(context);
    }

    public ScrollableMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            // Intercept touch events in order to be able to scroll.
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_DOWN:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
