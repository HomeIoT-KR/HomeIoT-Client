package xyz.neonkid.homeiot.main.components.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import xyz.neonkid.homeiot.R;

/**
 * Android Notification Class
 *
 * Created by neonkid on 11/24/2017.
 */

public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    private NotificationCompat.Builder builder;

    @SuppressWarnings("deprecation")
    public NotificationHelper(Context base) {
        super(base);
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getLargeIcon()))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setLights(Color.LTGRAY, 1000, 500);
    }

    /**
     * Method setCategoryAndVisibility
     *
     * 안드로이드 알림 카테고리, 표시여부를 설정하는 메소드
     * 이 메소드는 Android 5.0 이상의 플랫폼에서 사용하기를 권장
     *
     * @param category 카테고리
     * @param visibility 표시여부
     */
    public void setCategoryAndVisibility(String category, int visibility) {
        builder.setCategory(category).setVisibility(visibility);
    }

    /**
     * Method setNotificationText
     *
     * 안드로이드 알림 제목과 내용을 설정하는 메소드
     *
     * @param title 제목
     * @param body 내용
     */
    public void setNotificationText(String title, String body) {
        builder.setContentTitle(title).setContentText(body);
    }

    @DrawableRes
    private int getSmallIcon() {
        return R.mipmap.notification_small;
    }

    @DrawableRes
    private int getLargeIcon() {
        return R.mipmap.notification_large;
    }

    /**
     * 알림 소리 설정
     *
     * @param ringtonUri 알림음 경로
     */
    public void setRingtoneUri(Uri ringtonUri) {
        builder.setSound(ringtonUri);
    }

    public void setVibrate() {
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
    }

    /**
     * Method notify
     *
     * 알림 ID 설정
     * 이 메소드도 Android 5.0 이상의 플랫폼에서 사용하기를 권장
     *
     * @param id 알림 ID
     */
    public void notify(int id) {
        getManager().notify(id, builder.build());
    }

    private NotificationManager getManager() {
        if(manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
}
