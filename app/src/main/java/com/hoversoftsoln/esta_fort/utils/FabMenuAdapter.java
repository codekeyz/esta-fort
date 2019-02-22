package com.hoversoftsoln.esta_fort.utils;

import android.content.Context;
import android.widget.TextView;

import com.hoversoftsoln.esta_fort.R;

import org.jetbrains.annotations.NotNull;

import uk.co.markormesher.android_fab.SpeedDialMenuAdapter;
import uk.co.markormesher.android_fab.SpeedDialMenuItem;

public class FabMenuAdapter extends SpeedDialMenuAdapter {

    @Override
    public int getCount() {
        return 1;
    }

    @NotNull
    @Override
    public SpeedDialMenuItem getMenuItem(@NotNull Context context, int position) {
        switch (position) {
            case 0:
                return new SpeedDialMenuItem(context, R.drawable.ic_child_friendly_white_24dp, "Request Cab");
            default:
                return new SpeedDialMenuItem(context, R.drawable.ic_child_friendly_white_24dp, "Request Cab");
        }
    }

    @Override
    public void onPrepareItemLabel(@NotNull Context context, int position, @NotNull TextView label) {
        label.setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public float fabRotationDegrees() {
        return 135F;
    }
}
