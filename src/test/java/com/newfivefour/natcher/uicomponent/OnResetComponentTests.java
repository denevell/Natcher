package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.views.EmptyView;
import com.newfivefour.natcher.uicomponent.views.LoadingView;
import com.newfivefour.natcher.uicomponent.views.ServerErrorView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class OnResetComponentTests {

    private final Runnable hideKeyboard;
    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingView loaderInComponent;
    private final LoadingView loaderOutOfComponent;
    private final EmptyView emptyView;
    private final ServerErrorView serverErrorViewInComponent;
    private final ServerErrorView serverErrorViewOutOfComponent;

    public OnResetComponentTests() {
        populatable = mock(Populatable.class);

        hideKeyboard = mock(Runnable.class);
        loaderInComponent = mock(LoadingView.class);
        loaderOutOfComponent = mock(LoadingView.class);
        emptyView = mock(EmptyView.class);
        serverErrorViewInComponent = mock(ServerErrorView.class);
        serverErrorViewOutOfComponent = mock(ServerErrorView.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
        uiComponent.setHideKeyboard(hideKeyboard);
        uiComponent.setInComponentLoadingDisplay(loaderInComponent);
        uiComponent.setOutOfComponentLoadingDisplay(loaderOutOfComponent);
        uiComponent.setOutOfComponentServerErrorDisplay(serverErrorViewOutOfComponent);
        uiComponent.setInComponentServerErrorDisplay(serverErrorViewInComponent);
        uiComponent.setEmptyDisplay(emptyView);
    }

    @Test
    public void shouldUnsetInComponentLoader() {
        uiComponent.onResetComponent();
        verify(loaderInComponent).showLoading(false);
    }

    @Test
    public void shouldNotUnsetOutOfComponentLoaderIfNotSet() {
        uiComponent.onResetComponent();
        verify(loaderOutOfComponent, never()).showLoading(false);
    }

    @Test
    public void shouldUnsetOutOfComponentLoaderIfSet() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);

        // Act
        uiComponent.populateStarting();
        uiComponent.onResetComponent();
        verify(loaderOutOfComponent).showLoading(false);
    }

}