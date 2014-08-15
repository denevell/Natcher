package com.newfivefour.natcher.app.component;

/**
 * Created by user on 15/08/14.
 */
public interface UIComponent<T> extends
        PopulatableComponent<T>,
        ParentLoadingConnector,
        RefreshableContentConnector,
        EmptiableContentConnector {
}
