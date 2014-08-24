package com.newfivefour.natcher.uicomponent;

/**
 * Used by the UiComponentVanilla class so
 * 1. It knows how to populate a view,
 * 2. If that view is empty to decide which loader (page or component) to show,
 * 3. How to clear that view on empty content from server
 */
public interface Populatable<T> {
    void populateWithContent(T ob);
    /**
     * Used to ascertain if should show the overall page loader (if not empty) or the in page loader (if empty).
     */
    boolean shouldShowInComponentLoading();
    /**
     * Used when we have 'empty' content from the server, and we want to clear the existing cached view
     */
    void clearContent();
}
