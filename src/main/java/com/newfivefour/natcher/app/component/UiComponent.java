package com.newfivefour.natcher.app.component;

/**
 * Allows your component to enjoy all the goodness of a UiComponent
 * i.e. dealing with loading views, error views, empty views, cached content,
 * overall loading view, refreshing from server error, actions on empty content,
 *
 * @see com.newfivefour.natcher.app.component.UiComponentVanilla
 */
public interface UiComponent<T> extends
        PopulatableComponent<T>,
        ParentLoadingConnector,
        RefreshableContentConnector,
        EmptiableContentConnector {
}
