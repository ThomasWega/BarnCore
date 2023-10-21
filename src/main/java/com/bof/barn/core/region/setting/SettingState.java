package com.bof.barn.core.region.setting;

public enum SettingState {
    ON, OFF, LOCKED;

    public boolean isToggled() {
        return this == ON;
    }

    public boolean isUnlocked() {
        return this != LOCKED;
    }
}
