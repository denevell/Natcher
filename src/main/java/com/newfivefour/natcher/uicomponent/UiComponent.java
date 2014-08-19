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
        RefreshableConnector,
        EmptiableContentConnector {

    /**
     * Called when the component is destroyed.
     *
     * Used to revert all external state changes it's made for itself, but not had
     * the chance to revert yet.
     *
     * An example is setting the overall app loading spinner active, but not had the
     * chance to dis-activate as yet, since the network hasn't returned.
     */
    void onResetComponent();
}
