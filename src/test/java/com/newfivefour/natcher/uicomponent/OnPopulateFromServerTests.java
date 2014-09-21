package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.views.EmptyDisplay;
import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;
import com.newfivefour.natcher.uicomponent.views.ServerErrorDisplay;

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
    private final LoadingDisplay loaderInComponent;
    private final LoadingDisplay loaderOutOfComponent;
    private final EmptyDisplay emptyDisplay;
    private final ServerErrorDisplay serverErrorDisplayInComponent;
    private final ServerErrorDisplay serverErrorDisplayOutOfComponent;

    public OnPopulateFromServerTests() {
        populatable = mock(Populatable.class);

        loaderInComponent = mock(LoadingDisplay.class);
        loaderOutOfComponent = mock(LoadingDisplay.class);
        emptyDisplay = mock(EmptyDisplay.class);
        serverErrorDisplayInComponent = mock(ServerErrorDisplay.class);
        serverErrorDisplayOutOfComponent = mock(ServerErrorDisplay.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
        uiComponent.setInComponentLoadingDisplay(loaderInComponent);
        uiComponent.setOutOfComponentLoadingDisplay(loaderOutOfComponent);
        uiComponent.setOutOfComponentServerErrorDisplay(serverErrorDisplayOutOfComponent);
        uiComponent.setInComponentServerErrorDisplay(serverErrorDisplayInComponent);
        uiComponent.setEmptyDisplay(emptyDisplay);
    }

    @Test
    public void shouldHideServerErrors() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromServer(object);
        verify(serverErrorDisplayInComponent).showServerError(false, 0, null);
        verify(serverErrorDisplayOutOfComponent).showServerError(false, 0, null);
    }

    @Test
    public void shouldHideEmptyView() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromServer(object);
        verify(emptyDisplay).showEmpty(false);
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