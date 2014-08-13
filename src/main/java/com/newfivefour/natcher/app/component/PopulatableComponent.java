package com.newfivefour.natcher.app.component;


/**
 * 1. Component removes in-component loading on cache
 * Given the component has no content
 * Given a fetch starts and cache is returned
 * Then the component should show the loading display
 * And then the component remove its loading display on receiving the cache
 *
 * 2. Component removes in-component on loading server data
 * Given the component has no content
 * Given there is no cached content to show
 * Given a fetch starts
 * Then the component should show the loading display
 * And then remove the loading display on receiving good data from server
 *
 * 3. Component keeps overall loading display on cached content
 * Given cached content is given to the component
 * Then the loading display should is removed
 * But the the overall page loading display should remain
 *
 * 4. Component removes overall page loading display on server content
 * Given the component has a reference to the overall page loading display
 * Given the server returns good content
 * Then the overall page loading display should be removed
 *
 * 5. On empty server content, component loading should disappear
 * Given component's isEmpty method is called
 * Then the component loading display is removed
 * Then the overall page loading is removed if there
 * Then the is empty display is shown
 *
 * 6. On server error, with no cache, show error
 * Given there's no cached content in the component
 * Given the populateFromServerError has been called
 * Then show an error in the component
 * And remove overall and component loading if there
 *
 * 7. On server error, with cached content, show overall page error
 * Given there's cached content
 * Given there's a server error call
 * Then the overall page error method should be called
 * And remove overall and component loading if there
 */
public interface PopulatableComponent<T> {
    public void populateFromCache(T ob);
    public void populateFromServer(T ob);
    public void populateFromServerError();
    public void isEmpty();
}
