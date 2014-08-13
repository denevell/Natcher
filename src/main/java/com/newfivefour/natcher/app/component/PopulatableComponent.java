package com.newfivefour.natcher.app.component;


/**
 * In-component loader display
 * ###########################
 *
 * Given the component has started
 * Then show the in-component loading display
 *
 * Given the component has been given cached content
 * Then remove the in-component loading display
 *
 * Given the component has been given server content
 * Then remove the in-component loading display
 *
 * Given the component has received a server error
 * Then remove the in-component loading display
 *
 * Overall page-loader display
 * ###########################
 *
 * # Given the component's overall page-loader is set
 * Then the component should start the overall page-loader
 *
 * # Given cached content is given to the component
 * Then the overall page loading display should remain
 *
 * # Given the server returns good content
 * Then the overall page loading display should be removed if it's set
 *
 * # Given the component has received a server error
 * Then the overall page loading display should be removed if it's set
 *
 * ### Is empty stuff
 *
 * 5. On empty server content, component loading should disappear and empty display should appear
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
 * ## Is server error stuff
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
