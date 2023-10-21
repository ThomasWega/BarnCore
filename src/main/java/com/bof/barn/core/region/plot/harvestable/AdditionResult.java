package com.bof.barn.core.region.plot.harvestable;

/**
 * Enumeration representing the possible results of an addition operation to a {@link com.bof.barn.core.region.plot.container.ContainerPlot}.
 */
public enum AdditionResult {
    /**
     * Indicates that the addition operation was successful.
     */
    SUCCESS,

    /**
     * Indicates that the player's inventory is full during the addition.
     */
    INV_FULL,

    /**
     * Indicates that the storage container is full during the addition.
     */
    CONTAINER_FULL
}