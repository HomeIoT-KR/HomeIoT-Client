package xyz.neonkid.homeiot.main.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import xyz.neonkid.homeiot.main.adapter.ChatRecyclerAdapter;
import xyz.neonkid.homeiot.main.adapter.model.chat.ChatMessage;
import xyz.neonkid.homeiot.base.view.BaseActivity;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;
import xyz.neonkid.homeiot.main.service.MainMessagingService;
import xyz.neonkid.homeiot.main.presenter.MainPresenter;
import xyz.neonkid.homeiot.main.presenter.view.MainPresenterView;
import xyz.neonkid.homeiot.main.view.effect.BlurBuilder;
import xyz.neonkid.homeiot.main.view.effect.DarknessBuilder;
import xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.credit.CreditActivity;
import xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.MonitoringActivity;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities.BuzzerActivity;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities.LEDActivity;
import xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.SettingsActivity;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities.WindowActivity;

/**
 * HomeIoT의 메인 화면.. (음성 인식 Activity)
 * @see BaseActivity
 *
 * Created by neonkid on 5/16/17.
 */

public class MainActivity extends BaseActivity implements MainPresenterView {
    private MainPresenter mainPresenter;
    private final int MY_PERMISSION_CODE_RECORD_AUDIO = 100;
    private SpeechRecognizer mRecognizer;
    private ChatRecyclerAdapter chatMessageAdapter;
    private MainMessagingService mService;
    private TextToSpeech tts;

    private static final String LOG_TAG = "Voice Recognition";

    @BindView(R.id.input_Speech)
    FloatingActionButton inputSpeech;

    @BindView(R.id.main_Layout)
    RelativeLayout mainLayout;

    @BindView(R.id.show_AppSettings)
    FloatingActionMenu showAppSettings;

    @BindView(R.id.chatList)
    RecyclerView chatList;

    /**
     * MainMessagingService 와 MainActivity 를 AIDL 인터페이스로 연동하는 부분입니다.
     * 선 작업으로, 서비스 클래스에서, Binder 를 생성해야 합니다.
     *
     * 생성 이후에는 Activity 에서 불러올 함수를 인터페이스로 정의한 다음,
     * 정의한 Interface Callback 을 register 해줍니다.
     *
     * @see MainMessagingService
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainMessagingService.MqttServiceBinder binder = (MainMessagingService.MqttServiceBinder)service;
            mService = binder.getService();
            mService.registerCallback(mCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { mService = null; }
    };

    /**
     * MainMessagingService 와 연동할 인터페이스 함수를 정의합니다.
     * 실제 서비스 클래스에서 호출할 수 있는 메소드이며, 먼저 서비스 클래스에서 미리 정의하여아 합니다.
     *
     * 서버로부터 받은 메시지를 UI에 그리는 작업은 UI Thread 에서 작업하도록 합니다.
     *
     * @see MainMessagingService
     */
    private MainMessagingService.ICallback mCallback = new MainMessagingService.ICallback() {

        /**
         * Callback Method recvMessage
         *
         * 서버로부터 메시지를 수신 받을 경우, 호출되는 메소드
         * (반드시 자연어로의 변환 과정을 거친다)
         * @param message 서버로 부터 받은 메시지
         */
        @Override
        public void recvMessage(final String message) {
            runOnUiThread(() -> recvView(message));
        }

        /**
         * Callback Method recvToastMessage
         *
         * MainMessagingService 로부터 메시지를 수신받을 경우, 호출되는 메소드
         * (자연어 변환 과정없이 그대로 메시지 전달)
         * @param message 클래스 메시지
         */
        @Override
        public void recvToastMessage(final String message) {
            runOnUiThread(() -> setToastMessage(message));
        }
    };

    @Override
    protected int getContentLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.app_Main;
    }

    @Override
    protected void onCreate() {
        chatMessageAdapter = new ChatRecyclerAdapter(this);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chatList.setAdapter(chatMessageAdapter);

        mainPresenter = new MainPresenter(this, this);

        // 사용자의 바탕 화면을 불러온 뒤, Blur/Darkness 처리를 해줍니다.
        Bitmap wallpaperImage = ((BitmapDrawable)WallpaperManager.getInstance(this).getDrawable()).getBitmap();
        mainLayout.setBackground(modBackgroundImage(wallpaperImage));

        // TTS 언어를 한국어로 설정합니다.
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR)
                tts.setLanguage(Locale.KOREAN);
        });
    }

    /**
     * Method ttsUnder20
     *
     * Lollipop 이하 안드로이드 플랫폼에서 호출하는 TTS 메소드
     * 이 메소드에서는 반드시, HashMap 을 사용하여 ID 를 정의해야 합니다.
     *
     * @deprecated : Android 5.0 이하 버전에서만 동작, 그 이상에서 Deprecated
     * @param msg   안내하고자 하는 메시지
     */
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String msg) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, getString(R.string.app_name));
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, map);
    }

    /**
     * Method ttsGreater21
     *
     * Lollipop 이상 안드로이드 플랫폼에서 호출하는 TTS 메소드
     * @param msg   안내하고자 하는 메시지
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String msg) { tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, this.hashCode() + ""); }

    /**
     * Activity 가 재호출될 경우,
     * MainMessagingService 와 Binding 하여 메시지가 송/수신이
     * MainActivity 에서 이루어지도록 한다.
     *
     * 또한, 설정에서 사용자의 폰트가 변경되었을 경우를 감안하여,
     * 메시지를 다시 불러온다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, MainMessagingService.class), mConnection, Context.BIND_AUTO_CREATE);
        chatMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Activity 가 종료될 경우,
     * Binding 중이던 MainMessagingService 를 끊어주며
     * Google Recognizer 객체도 사용하지 않으므로 사용 해제 한다.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(mRecognizer != null) mRecognizer.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        unbindService(mConnection);
    }

    /**
     * Method onCheckPermissionMic
     *
     * 마이크 권한을 확인하고자 하는 메소드
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void onCheckPermissionMic() {
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO))
                Toast.makeText(this, getString(R.string.error_Permission), Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSION_CODE_RECORD_AUDIO);
        } else onStartListen();
    }

    /**
     * Method onStartListen
     *
     * 음성 인식을 시작하는 메소드
     * 반드시 Manifest, 안드로이드 6.0 이상의 플랫폼에서 사용하고자 하는 경우,
     * 권한을 확인하는 메소드 내에서 호출하여야 한다.
     */
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void onStartListen() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(new RecognitionListener() {

            /**
             * Method onReadyForSpeech
             *
             * 사용자의 음성을 인식할 준비가 된 경우
             * @param params status
             */
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.i(LOG_TAG, "onReadyForSpeech");
                setSpeechColor(R.color.speech_ListenReady);
                inputSpeech.setIndeterminate(true);
            }

            /**
             * Method onBeginningOfSpeech
             *
             * 사용자의 음성을 인식하는 중인 경우
             */
            @Override
            public void onBeginningOfSpeech() {
                Log.i(LOG_TAG, "onBeginningOfSpeech");
                setSpeechColor(R.color.speech_Listening);
            }

            /**
             * Method onRmsChanged
             *
             * 사용자의 음성 소리 크기가 변경되는 경우.
             * @param rmsdB 소리 크기 (단위: dB)
             */
            @Override
            public void onRmsChanged(float rmsdB) {

            }

            /**
             * Method onBufferReceived
             *
             * 새로운 소리가 들리는 경우,
             * onReadyForSpeech / onEndOfSpeech 사이에서 무수히 호출됨
             * @param buffer 메시지
             */
            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            /**
             * Method onEndOfSpeech
             *
             * 사용자의 음성 인식이 끝난 경우
             */
            @Override
            public void onEndOfSpeech() {
                Log.i(LOG_TAG, "onEndOfSpeech");
                setSpeechColor(R.color.color_accent);
            }

            /**
             * Method onError
             *
             * 음성 인식 중에 오류가 발생한 경우
             * @param errCode 오류 번호
             */
            @Override
            public void onError(int errCode) {
                Log.i(LOG_TAG, "onError: " + errCode);
                switch(errCode) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        setToastMessage(getString(R.string.error_Audio));
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        setToastMessage(getString(R.string.error_Permission));
                        break;
                    case SpeechRecognizer.ERROR_NETWORK: case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        setToastMessage(getString(R.string.error_Network));
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        setToastMessage(getString(R.string.error_noMatch));
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        setToastMessage(getString(R.string.error_Busy));
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        setToastMessage(getString(R.string.error_Server));
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        setToastMessage(getString(R.string.error_Timeout));
                        break;
                }
                setSpeechColor(R.color.speech_Fail);
                inputSpeech.setIndeterminate(false);
            }

            /**
             * Method onResults
             *
             * 음성 인식 결과 값 출력 메소드
             * 결과값은 Bundle Data type 으로 정의되어 있지만,
             * 아래의 방법으로 String Data type 으로 도출할 수 있음.
             *
             * ArrayList<String> varName = results.get(SpeechRecognizer.RESULTS_RECOGNITION) or
             * ArrayList<String> varName = results.getStringArrayList(Speech.RESULTS_RECOGNITION)
             *
             * @param results 결과값
             */
            @Override
            public void onResults(Bundle results) {
                String key = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList<String> mResult = results.getStringArrayList(key);
                String[] res = new String[mResult.size()];
                mResult.toArray(res);
                mainPresenter.checkSpeechResult(res);
                setSpeechColor(R.color.speech_Success);
                chatList.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
                inputSpeech.setIndeterminate(false);
            }
            
            /**
             * Method onPartialResults
             *
             * 부분적인 결과를 도출할 때 사용하는 메소드
             * @param partialResults 결과값
             */
            @Override
            public void onPartialResults(Bundle partialResults) {
                // onRessults 와 사용 방법이 동일
            }

            /**
             * Method onEvent
             *
             * 이벤트가 발생했을 시, 사용하는 메소드
             *
             * @param eventType 이벤트 타입
             * @param params 이벤트로부터 전달된 매개 변수
             */
            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        mRecognizer.startListening(intent);
    }

    /**
     * Callback Method onRequestPermissionsResult
     *
     * 권한 요청에 대한 결과 이벤트 발생 메소드
     *
     * @see android.support.v4.app.ActivityCompat
     * @param requestCode   요청 권한 코드
     * @param permissions   권한 이름
     * @param grantResults  권한 요청 결과
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSION_CODE_RECORD_AUDIO:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setToastMessage(getString(R.string.success_PermissionMic));
                    break;
                } else {
                    setToastMessage(getString(R.string.error_PermissionMic));
                    break;
                }
            default:
                break;
        }
    }

    /**
     * Method setSpeechColor
     *
     * View 내 마이크 버튼 색깔 변경 이벤트 메소드
     * @param colorCode 색깔 코드
     */
    @SuppressWarnings("deprecation")
    public void setSpeechColor(int colorCode) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            inputSpeech.setColorNormal(getResources().getColor(colorCode));
        else
            inputSpeech.setColorNormal(getColor(colorCode));
    }

    /**
     * Click Event Method onListenClick
     *
     * View 내 마이크 버튼을 터치한 경우 발생하는 이벤트 메소드
     * @exception SecurityException: Manifest 에 마이크, 녹음 관련 권한 누락 or
     *                                  6.0 이상에서 마이크, 녹음 관련 권한을 받지 못한 경우.
     */
    @OnClick(R.id.input_Speech)
    public void onListenClick() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                onStartListen();
            } catch (SecurityException ex) {
                setToastMessage(ex.getMessage());
                ex.printStackTrace();
            }
        }
        else
            onCheckPermissionMic();
    }

    @OnClick(R.id.settings_Main)
    public void onSettingsMainClick() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @OnClick(R.id.monitoring)
    public void onMonitoringClick() {
        startActivity(new Intent(this, MonitoringActivity.class));
    }

    @OnClick(R.id.credit)
    public void onCreditClick() {
        startActivity(new Intent(this, CreditActivity.class));
    }

    @OnClick(R.id.settings_Window)
    public void onSettingsWindowClick() {
        startActivity(new Intent(this, WindowActivity.class));
    }

    @OnClick(R.id.settings_Light)
    public void onSettingsLEDClick() {
        startActivity(new Intent(this, LEDActivity.class));
    }

    @OnClick(R.id.settings_Buzzer)
    public void onSettingsBuzzerClick() { startActivity(new Intent(this, BuzzerActivity.class)); }

    /**
     * PresenterView Method sendView
     *
     * View 내에서 음성 인식을 통해, 메시지를 전달한 경우,
     * 화면에 렌더링, 서버에 전송하는 메소드
     * @param message 메시지 내용
     */
    @Override
    public void sendView(String message) {
        chatMessageAdapter.addChat(new ChatMessage(false, message));
        mService.getsendFilterMessage(message);
    }

    /**
     * PresenterView Method recvView
     *
     * 서버에서 메시지를 받은 경우,
     * 화면에 렌더링, TTS 메소드를 통해 음성으로 사용자에게 전달
     * @param message 메시지 내용
     */
    @SuppressWarnings("deprecation")
    @Override
    public void recvView(String message) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            ttsUnder20(message);
        else
            ttsGreater21(message);
        chatMessageAdapter.addChat(new ChatMessage(true, message));
        chatList.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    /**
     * Method modBackgroundImage
     *
     * MainActivity 의 배경으로 사용되는 이미지를
     * Blur, Darkness 효과 처리
     *
     * @param originalImage 대상 이미지
     * @return  효과가 적용된 이미지를 Drawable 형태로 반환
     */
    private Drawable modBackgroundImage(Bitmap originalImage) {
        Bitmap resultImage = BlurBuilder.blur(this, originalImage);
        DarknessBuilder.darkness(resultImage);

        return new BitmapDrawable(getResources(), resultImage);
    }
}
