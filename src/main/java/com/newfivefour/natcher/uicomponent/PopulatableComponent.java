package com.newfivefour.natcher.uicomponent;

/**
 * Used to define what happens when populating a component,
 * like when it starts what should load, when it has cached data what should be loading,
 * when it has empty content from cache what should be loading, etc, etc.
 *
 * Used as part of the UiComponent
 * @see com.newfivefour.natcher.uicomponent.UiComponent
 */
public interface PopulatableComponent<T> {
    void populateStarting();
    void populateFromCache(T ob);
    void populateFromServer(T ob);
    void populateFromServerError(int responseCode);
    void populateWithEmptyContentFromServer();
    void populateWithEmptyContentFromCache();
}
