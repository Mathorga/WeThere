package keym.dev.rwethereyet;

import android.support.v4.app.Fragment;

/**
 * Implements a wrapper for the Fragments used to display the tabs.
 * It has an icon field, which represents the icon of the specific tab.
 */
public class Tab {

    private Fragment page;
    private int icon;
    private int title;

    public Tab(final Fragment page, final int iconResId, final int titleResId) {
        this.page = page;
        this.icon = iconResId;
        this.title = titleResId;
    }

    public Fragment getPage() {
        return this.page;
    }

    public int getIconResId() {
        return this.icon;
    }

    public int getTitleResId() {
        return this.title;
    }

    public void setPage(final Fragment page) {
        this.page = page;
    }

    public void setIconResId(final int iconResId) {
        this.icon = iconResId;
    }

    public void setTitleResId(final int titleResId) {
        this.title = titleResId;
    }
}
