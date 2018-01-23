package xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.credit;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BaseActivity;

/**
 * Created by neonkid on 11/20/17.
 */

public class LicenseActivity extends BaseActivity {
    private WebSettings licenseViewSettings;

    @BindView(R.id.license_web)
    WebView licenseView;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_license;
    }

    @Override
    protected void onCreate() {
        view.setPadding(0, 80, 0, 0);

        // WebView Settings
        licenseView.setWebViewClient(new WebViewClient());
        licenseViewSettings = licenseView.getSettings();
        licenseViewSettings.setJavaScriptEnabled(false);
        licenseViewSettings.setBuiltInZoomControls(false);
        licenseViewSettings.setDefaultFontSize(12);
        licenseView.loadUrl("https://raw.githubusercontent.com/NEONKID/SmartDiary/master/license.txt");
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.policy_license;
    }
}
