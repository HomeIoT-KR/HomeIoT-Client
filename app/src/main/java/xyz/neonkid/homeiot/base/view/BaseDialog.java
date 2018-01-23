package xyz.neonkid.homeiot.base.view;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by neonkid on 7/10/17.
 */

public abstract class BaseDialog extends DialogPreference {
    public BaseDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
