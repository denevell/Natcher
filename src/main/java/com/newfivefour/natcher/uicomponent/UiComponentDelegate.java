package com.newfivefour.natcher.uicomponent;

/**
 * This is so your view can implement all the goodness of ui components,
 * i.e. dealing with loading views, error views, empty views, cached content,
 * overall loading view, refreshing from server error, actions on empty content,
 * whilst avoiding a class hierarchy nightmare.
 */
public interface UiComponentDelegate<T> {
    UiComponent<T> getUiComponentDelegate();
}
