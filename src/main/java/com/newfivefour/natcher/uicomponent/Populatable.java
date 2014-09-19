package com.newfivefour.natcher.uicomponent;

/**
 * Used by the UiComponentVanilla class so
 * 1. It knows how to populate a view,
 * 2. If that view is empty to decide which loader (page or component) to show,
 * 3. How to clear that view on empty content from server
 */
public interface Populatable<T> {
    void populateOnSuccessfulResponse(T ob);

    /**
     * Used when we have 'empty' content from the server, and we want to clear the existing cached view
     */
    void clearContentOnEmptyResponse();

    /**
     * Used to ascertain if should show the error view in the component
     */
    boolean showInComponentServerError();

    /**
     * Used to ascertain if should show the error view outside the component (for when we don't show it in-component)
     */
    boolean showOutOfComponentServerError();

    /**
     * Used to ascertain if should show the in component loader (if the content has no content, for example)
     */
    boolean showInComponentLoading();

    /**
     * Used to ascertain if should show the overall page loader for when we're not showing it in-component.
     */
    boolean showOutOfComponentLoading();

}
