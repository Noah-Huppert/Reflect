package com.noahhuppert.reflect.views;

/**
 * A interface that is responsible for switching fragments
 */
public interface FragmentSwitcher {
    /**
     * Switches to the specified fragment
     * @param fragmentId The fragment to switch to
     */
    void switchFragment(@FragmentId int fragmentId);
}
