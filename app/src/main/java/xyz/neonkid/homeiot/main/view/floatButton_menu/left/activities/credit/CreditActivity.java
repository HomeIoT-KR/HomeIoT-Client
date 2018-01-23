package xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.credit;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.adapter.MemberRecyclerAdapter;
import xyz.neonkid.homeiot.main.adapter.model.member.HomeIoTMember;
import xyz.neonkid.homeiot.base.view.BaseActivity;

/**
 * HomeIoT 정보 Activity
 *
 * Created by neonkid on 9/24/17.
 */

public class CreditActivity extends BaseActivity {
    private MemberRecyclerAdapter memberRecyclerAdapter;

    @BindView(R.id.member_view)
    RecyclerView memberView;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_credit;
    }

    @Override
    protected void onCreate() {
        view.setPadding(0, 80, 0, 0);
        memberRecyclerAdapter = new MemberRecyclerAdapter(this);
        memberView.setAdapter(memberRecyclerAdapter);
        memberRecyclerAdapter.addMember(new HomeIoTMember(android.R.color.holo_blue_bright,
                "Server Developer", "E", getString(R.string.develop_Server), "HomeIoT_Server / Arduino 개발 담당"));
        memberRecyclerAdapter.addMember(new HomeIoTMember(android.R.color.holo_purple,
                "Hardware / Architecture", "N", getString(R.string.hardware_arch), "HomeIoT_Sensor / Manual 담당"));
        memberRecyclerAdapter.addMember(new HomeIoTMember(android.R.color.holo_green_light,
                "Android Developer", "K", getString(R.string.develop_Android), "HomeIoT_Android 개발 담당"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getString(R.string.policy_license));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle() == getString(R.string.policy_license))
            startActivity(new Intent(this, LicenseActivity.class));
        return false;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.credit_Menu;
    }
}
