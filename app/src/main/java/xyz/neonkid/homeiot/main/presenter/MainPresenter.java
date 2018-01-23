package xyz.neonkid.homeiot.main.presenter;

import android.util.Log;

import xyz.neonkid.homeiot.base.presenter.BasePresenter;
import xyz.neonkid.homeiot.main.command.CommandManager;
import xyz.neonkid.homeiot.main.presenter.view.MainPresenterView;
import xyz.neonkid.homeiot.main.view.MainActivity;

import static android.content.ContentValues.TAG;

/**
 * MainActivity 의 Presenter
 *
 * @see BasePresenter
 *
 * Created by neonkid on 5/15/17.
 */

public class MainPresenter extends BasePresenter<MainPresenterView, MainActivity> {
    private CommandManager commandManager;

    public MainPresenter(MainPresenterView view, MainActivity context) {
        super(view, context);
        commandManager = new CommandManager(context);
    }

    /**
     * Method checkSpeechResult
     *
     * API 를 통해서 받은 문자열 중,
     * 일치율이 높은 문자열을 sendView로 보낸다.
     *
     * 만약, 일치하는 문자열을 찾지 못하면, 0번으로 전송
     *
     * @param res 인식 문자열
     */
    public void checkSpeechResult(String[] res) {
        for(String msg : res) {
            Log.e(TAG, "checkSpeechResult: " + msg);
            if(commandManager.isCorrectValue(msg)) {
                getView().sendView(msg);
                return;
            }
        }
        getView().sendView(res[0]);
    }
}
