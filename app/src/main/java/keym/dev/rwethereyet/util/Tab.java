package keym.dev.rwethereyet.util;

import android.support.v4.app.Fragment;

/**
 * Created by luka on 22/10/17.
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

    /**
     * Getter.
     * @return
     *  The contained Fragment.
     */
    public Fragment getPage() {
        return this.page;
    }

    /**
     * Getter.
     * @return
     *  The Tab's icon resource id.
     */
    public int getIconResId() {
        return this.icon;
    }

    /**
     * Getter.
     * @return
     *  The Tab's title resource id.
     */
    public int getTitleResId() {
        return this.title;
    }

    /**
     * Setter.
     * @param page
     *  The Fragment to be wrapped by the page.
     */
    public void setPage(final Fragment page) {
        this.page = page;
    }

    /**
     * Setter.
     * @param iconResId
     *  The Tab's icon resource id.
     */
    public void setIconResId(final int iconResId) {
        this.icon = iconResId;
    }

    /**
     * Setter.
     * @param titleResId
     *  The Tab's title resource id.
     */
    public void setTitleResId(final int titleResId) {
        this.title = titleResId;
    }
}
