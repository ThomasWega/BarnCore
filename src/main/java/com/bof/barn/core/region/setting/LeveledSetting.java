package com.bof.barn.core.region.setting;

public interface LeveledSetting {
    int getCurrentLevel();
    int getMaxLevel();
    float getBasePrice();
    boolean upgradeLevel();
    default boolean isAtMaxLevel() {
        return this.getCurrentLevel() == this.getMaxLevel();
    }

    default float getNextLevelPrice() {
        float priceMultiplier = 1.2f;

        return (float) (this.getBasePrice() * Math.pow(priceMultiplier, this.getCurrentLevel()));
    }
}
