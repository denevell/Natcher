package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.views.EmptyView;
import com.newfivefour.natcher.uicomponent.views.LoadingView;
import com.newfivefour.natcher.uicomponent.views.ServerErrorView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class OnPopulateFromServerTests {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingView loaderInComponent;
    private final LoadingView loaderOutOfComponent;
    private final EmptyView emptyView;
    private final ServerErrorView serverErrorViewInComponent;
    private final ServerErrorView serverErrorViewOutOfComponent;

    public OnPopulateFromServerTests() {
        populatable = mock(Populatable.class);

        loaderInComponent = mock(LoadingView.class);
        loaderOutOfComponent = mock(LoadingView.class);
        emptyView = mock(EmptyView.class);
        serverErrorViewInComponent = mock(ServerErrorView.class);
        serverErrorViewOutOfComponent = mock(ServerErrorView.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
        uiComponent.setInComponentLoadingDisplay(loaderInComponent);
        uiComponent.setOutOfComponentLoadingDisplay(loaderOutOfComponent);
        uiComponent.setOutOfComponentServerErrorDisplay(serverErrorViewOutOfComponent);
        uiComponent.setInComponentServerErrorDisplay(serverErrorViewInComponent);
        uiComponent.setEmptyDisplay(emptyView);
    }

    @Test
    public void shouldHideServerErrors() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromServer(object);
        verify(serverErrorViewInComponent).showServerError(false, 0, null);
        verify(serverErrorViewOutOfComponent).showServerError(false, 0, null);
    }

    @Test
    public void shouldHideEmptyView() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromServer(object);
        verify(emptyView).showEmpty(false);
    }

    @Test
    public void shouldHideInComponentLoader() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromServer(object);
        verify(loaderInComponent).showLoading(false);
    }

    @Test
    public void shouldHideOutOfComponentLoaderWhenActive() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);
        Object object = new Object();

        // Act
        uiComponent.populateStarting();
        uiComponent.populateFromServer(object);
        verify(loaderOutOfComponent).showLoading(false);
    }

    @Test
    public void shouldPopulateOnServerResult() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromServer(object);
        verify(populatable).populateOnSuccessfulResponse(object);
    }

}