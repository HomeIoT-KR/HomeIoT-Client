package xyz.neonkid.homeiot.main.view.custom_dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Build;
import android.preference.Preference;

import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Date;

import xyz.neonkid.homeiot.base.view.BaseDialog;

/**
 * 수동 제어에서, 시간 설정을 위한 컴포넌트 클래스
 *
 * Created by neonkid on 7/10/17.
 */

public class TimePreference extends BaseDialog implements Preference.OnPreferenceChangeListener {
    private TimePicker picker = null;

    private final long DEFAULT_VALUE = 0;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnPreferenceChangeListener(this);
    }

    protected void setTime(final long time) {
        persistLong(time);
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
    }

    protected void updateSummary(final long time) {
        final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        final Date date = new Date(time);

        setSummary(dateFormat.format(date.getTime()));
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Calendar getPersistedTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getPersistedLong(DEFAULT_VALUE));

        return calendar;
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(android.text.format.DateFormat.is24HourFormat(getContext()));
        return picker;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("deprecation")
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        final Calendar calendar = getPersistedTime();
        switch(Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.N:
                picker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                picker.setMinute(calendar.get(Calendar.MINUTE));
                break;

            default:
                picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("deprecation")
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        switch(Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.N:
                if(positiveResult) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.MINUTE, picker.getMinute());
                    calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());

                    if(!callChangeListener(calendar.getTimeInMillis()))
                        return;

                    setTime(calendar.getTimeInMillis());
                }
                break;

            default:
                if(positiveResult) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.MINUTE, picker.getCurrentMinute());
                    calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());

                    if(!callChangeListener(calendar.getTimeInMillis()))
                        return;

                    setTime(calendar.getTimeInMillis());
                }
                break;
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) { return a.getInteger(index, 0); }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onSetInitialValue(final boolean restorePersistedValue, final Object defaultValue) {
        long time;
        if(defaultValue == null)
            time = restorePersistedValue ? getPersistedLong(DEFAULT_VALUE) : DEFAULT_VALUE;
        else if(defaultValue instanceof Long)
            time = restorePersistedValue ? getPersistedLong((Long)defaultValue) : (Long)defaultValue;
        else if(defaultValue instanceof Calendar)
            time = restorePersistedValue ? getPersistedLong(((Calendar)defaultValue).getTimeInMillis()) : ((Calendar)defaultValue).getTimeInMillis();
        else
            time = restorePersistedValue ? getPersistedLong(DEFAULT_VALUE) : DEFAULT_VALUE;

        setTime(time);
        updateSummary(time);
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, final Object newValue) {
        ((TimePreference)preference).updateSummary((Long)newValue);
        return false;
    }
}
