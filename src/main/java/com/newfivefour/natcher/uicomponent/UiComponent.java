package com.newfivefour.natcher.uicomponent;

/**
 * Allows your component to enjoy all the goodness of a UiComponent
 * i.e. dealing with loading views, error views, empty views, cached content,
 * overall loading view, refreshing from server error, actions on empty content,
 *
 * @see com.newfivefour.natcher.uicomponent.UiComponentVanilla
 */
public interface UiComponent<T> extends
        PopulatableComponent<T>,
        ParentLoadingConnector,
        RefreshableContentConnector,
        EmptiableContentConnector {
}
