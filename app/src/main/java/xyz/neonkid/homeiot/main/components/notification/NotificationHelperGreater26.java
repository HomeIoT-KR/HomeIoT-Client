package xyz.neonkid.homeiot.main.components.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;

import xyz.neonkid.homeiot.R;

/**
 * Android 8.0 Oreo Notification Channel Class
 *
 * Created by neonkid on 11/22/17.
 */

public class NotificationHelperGreater26 extends ContextWrapper {
    private NotificationManager manager;

    /**
     * Constructor
     *
     * 반드시, Android Oreo OS 임을 체크해야 한다.
     *
     * @param base context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelperGreater26(Context base) {
        super(base);
        NotificationChannel mainChannel = new NotificationChannel(getString(R.string.app_name),
                "DEFAULT_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
        mainChannel.setLightColor(Color.GREEN);
        mainChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelperGreater26(Context base, Uri ringtone) {
        super(base);
        NotificationChannel mainChannel = new NotificationChannel(getString(R.string.app_name),
                "DEFAULT_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
        AudioAttributes audio = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        mainChannel.setLightColor(Color.LTGRAY);
        mainChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mainChannel.setSound(ringtone, audio);
    }

    /**
     * 기본 알림으로만 셋팅
     *
     * @param title 알림 제목
     * @param body 알림 내용
     * @return 알림 박스
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), getString(R.string.app_name))
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallicon())
                .setAutoCancel(true);
    }

    @DrawableRes
    private int getSmallicon() {
        return R.mipmap.notification_small;
    }

    /**
     * 사용자에게 알림 전송
     *
     * @param id 알림ID
     * @param notification 알림 객체
     */
    private void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    private NotificationManager getManager() {
        if(manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
}
