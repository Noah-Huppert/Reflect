package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

/**
 * A interface that is used to switch the pages in the {@link FirstTimeSetupFragment}
 */
public interface FirstTimeSetupPageSwitcher {
    /**
     * Switches the page based on the index provider. If the index is out of range the method call
     * will be ignored
     * @param index The index of the page to switch to
     */
    void switchPage(int index);

    /**
     * Gets the current page index
     * @return The current page index
     */
    int getCurrentPageIndex();
}
