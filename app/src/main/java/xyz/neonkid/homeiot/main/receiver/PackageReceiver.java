package xyz.neonkid.homeiot.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import xyz.neonkid.homeiot.main.service.MainMessagingService;

/**
 * HomeIoT App Install, Update 시,
 * HomeIoT Service 를 구동하는 Receiver Class
*
 * Created by neonkid on 5/21/17.
 */

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED))
            context.startService(new Intent(context, MainMessagingService.class));
        else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED))
            context.stopService(new Intent(context, MainMessagingService.class));
        else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))
            context.startService(new Intent(context, MainMessagingService.class));
    }
}
