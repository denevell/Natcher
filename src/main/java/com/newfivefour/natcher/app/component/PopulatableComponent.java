package com.newfivefour.natcher.app.component;

public interface PopulatableComponent<T> {
    void populateStarting();
    void populateFromCache(T ob);
    void populateFromServer(T ob);
    void populateFromServerError();
    void populateWithEmptyContentFromServer();
    void populateWithEmptyContentFromCache();
}
