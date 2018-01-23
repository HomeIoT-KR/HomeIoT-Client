package xyz.neonkid.homeiot.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import xyz.neonkid.homeiot.main.service.MainMessagingService;

/**
 * Android Device Booting 후,
 * HomeIoT Service 를 구동하는 Receiver Class
 *
 * Created by neonkid on 5/21/17.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, MainMessagingService.class));
            Toast.makeText(context, "Started HomeIoT Service", Toast.LENGTH_SHORT).show();
        }
    }
}
