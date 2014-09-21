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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class OnPopulateStartingTests {

    private final Runnable hideKeyboard;
    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingView loaderInComponent;
    private final LoadingView loaderOutOfComponent;
    private final EmptyView emptyView;
    private final ServerErrorView serverErrorViewInComponent;
    private final ServerErrorView serverErrorViewOutOfComponent;

    public OnPopulateStartingTests() {
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
    public void shouldHideKeyboard() {
        // Act
        uiComponent.populateStarting();
        verify(hideKeyboard).run();
    }

    @Test
    public void shouldHideServerErrors() {
        // Act
        uiComponent.populateStarting();
        verify(serverErrorViewInComponent).showServerError(false);
        verify(serverErrorViewOutOfComponent).showServerError(false);
    }

    @Test
    public void shouldHideEmptyView() {
        // Act
        uiComponent.populateStarting();
        verify(emptyView).showEmpty(false);
    }

    @Test
    public void shouldShowInComponentLoadingIfCallbackSaysOkay() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(true);

        // Act
        uiComponent.populateStarting();
        verify(loaderInComponent).showLoading(true);
    }

    @Test
    public void shouldNotShowInComponentLoadingIfCallbackSaysNo() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);

        // Act
        uiComponent.populateStarting();
        verify(loaderInComponent, never()).showLoading(true);
    }

    @Test
    public void shouldShowOutOfComponentLoadingIfCallbackSaysOkay() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);

        // Act
        uiComponent.populateStarting();
        verify(loaderOutOfComponent).showLoading(true);
    }

    @Test
    public void shouldNotShowOutOfComponentLoadingIfCallbackSaysNo() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(false);

        // Act
        uiComponent.populateStarting();
        verify(loaderOutOfComponent, never()).showLoading(true);
    }
}