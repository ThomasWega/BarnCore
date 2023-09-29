package com.bof.core.region.plot.selling;


import com.bof.core.region.plot.Plot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SellingPlot extends Plot {
    /**
     * @return ItemStacks that are stored in this container
     */
    @NotNull List<ItemStack> getStored();

    /**
     * @return Max capacity the container can take
     */
    int getCapacity();

    /**
     * @return Whether the {@link #getFilledAmount()} exceeds or equals {@link #getCapacity()}
     */
    boolean isFull();

    /**
     * @return Get amount of crops stored
     */
    int getFilledAmount();

    /**
     * @return Filled amount in percentage
     * @see #getFilledPercentageRounded(int)
     */
    float getFilledPercentage();

    /**
     * @param roundNum Decimal numbers to round up to
     * @return Filled amount in percentage rounded up
     * @see #getFilledPercentage()
     */
    float getFilledPercentageRounded(int roundNum);

    boolean isAutoSell();

    void setAutoSell(boolean autoSell);
}
