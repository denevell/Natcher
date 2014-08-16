package com.newfivefour.natcher.uicomponent;

public interface HasEmptyContent {
    /**
     * Used to ascertain if should show the overall page loader (if not empty) or the in page loader (if empty).
     */
    boolean hasEmptyContent();
    /**
     * Used when we have 'empty' content from the server, and we want to clear the existing cached view
     */
    void clearExistingContent();
}
