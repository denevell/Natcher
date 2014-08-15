package com.newfivefour.natcher.app.component;


/**
 * In-component loader display
 * ###########################
 *
 * # Given the component loading call has been called
 * Given we have no cached content
 * Then show the in-component loading display
 *
 * # Given the component has been given cached content
 * Then remove the in-component loading display
 *
 * # Given the component has been given server content
 * Then remove the in-component loading display
 *
 * # Given the component has received a server error
 * Then remove the in-component loading display
 *
 * # Given the component has empty content
 * Then remove the in-component loading display
 *
 * Given we have received empty content from server
 * Then remove the in-component loading display
 *
 * Given we have received empty content from cache
 * Then remove the in-component loading display
 *
 * Overall page-loader display
 * ###########################
 *
 * # Given populate loading is called
 * Given the component's content is there
 * Then the overall page loading display should be set active
 *
 * # Given cached content is returned
 * Given a service hasn't returned data
 * Then show the overall page loader
 *
 * # Given the server returns good content
 * Then the overall page loading display should be removed if it's set
 *
 * # Given the component has received a server error
 * Then the overall page loading display should be removed if it's set
 *
 * Given we have received empty content from server
 * Then hide the server error
 *
 * Given we have received empty content from cache
 * Then don't hide the server error
 *
 * Server error
 * ############
 *
 * # Given a server error
 * Given we have no cached content
 * Then we show the in-component error view
 *
 * # Given a server error
 * Given we have cached content
 * Then do nothing (the parent should show the error)
 *
 * # Given we have received new server content
 * Then hide the server error
 *
 * # Given we have received cached content
 * Then hide the server error
 *
 * # Given we have received empty content from server
 * Then hide the server error
 *
 * # Given we have received empty content from cache
 * Then hide the server error
 *
 * # Given a loading start call
 * Then hide the server error
 *
 * Empty content
 * #############
 *
 * # Given the data is deemed 'empty' from server
 * Then clear content
 * Then show the empty display
 *
 * # Given the data is deemed 'empty' from cache
 * Then clear content
 * Then show the empty display
 *
 * # Given the component has been given cached content
 * Then remove the empty display
 *
 * # Given the component has been given server content
 * Then remove the empty display
 *
 * # Given the component has received a server error
 * Then remove the empty display
 *
 * Given a loading start call
 * Then hide the empty display
 *
 * Refresh callback
 * #################
 *
 * Given we set a refreshing callback
 * Then the widget that deals with errors and empty views is given that
 *
 * Is empty callback
 * #################
 *
 * Given we set a is empty callback
 * Then the widget that deals with errors and empty views is given that
 *
 * ### FIX: Show fragment server error along with component server error?
 * ### FIX: Keeping state on rotation
 * ### QUESTION: Calling the populateStarting before first one can get to populated by server, server error, or is empty
 * ### QUESTION: What if we want to refresh to component when it already has data?
 */
public interface PopulatableComponent<T> {
    public void populateStarting();
    public void populateFromCache(T ob);
    public void populateFromServer(T ob);
    public void populateFromServerError();
    public void populateWithEmptyContentFromServer();
    public void populateWithEmptyContentFromCache();
}
