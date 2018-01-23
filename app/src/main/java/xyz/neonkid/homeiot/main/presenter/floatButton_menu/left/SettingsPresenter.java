package xyz.neonkid.homeiot.main.presenter.floatButton_menu.left;

import xyz.neonkid.homeiot.base.presenter.BasePrefPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.left.SettingsPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.SettingsActivity;

/**
 * Created by neonkid on 5/19/17.
 */

public class SettingsPresenter extends BasePrefPresenter<SettingsPresenterView, SettingsActivity> {
    public SettingsPresenter(SettingsPresenterView view, SettingsActivity context) {
        super(view, context);
    }
}
