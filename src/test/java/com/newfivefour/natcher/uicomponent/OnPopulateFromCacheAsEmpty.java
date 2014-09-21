package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.views.EmptyDisplay;
import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;
import com.newfivefour.natcher.uicomponent.views.ServerErrorDisplay;

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
public class OnPopulateFromCacheAsEmpty {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingDisplay loaderInComponent;
    private final LoadingDisplay loaderOutOfComponent;
    private final EmptyDisplay emptyDisplay;
    private final ServerErrorDisplay serverErrorDisplayInComponent;
    private final ServerErrorDisplay serverErrorDisplayOutOfComponent;

    public OnPopulateFromCacheAsEmpty() {
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
    public void shouldSetEmptyView() {
        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(emptyDisplay).showEmpty(true);
    }

    @Test
    public void shouldClearContent() {
        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(populatable).clearContentOnEmptyResponse();
    }


    @Test
    public void shouldHideServerErrors() {
        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(serverErrorDisplayInComponent).showServerError(false, 0, null);
        verify(serverErrorDisplayOutOfComponent).showServerError(false, 0, null);
    }

    @Test
    public void shouldHideInComponentLoader() {
        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderInComponent).showLoading(false);
    }

    @Test
    public void shouldShowOutOfComponentLoaderWhenActiveAndCallbackSaysOkay() {
        // Arrange
        uiComponent.populateStarting();
        when(populatable.showOutOfComponentLoading()).thenReturn(true);

        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderOutOfComponent).showLoading(true);
    }

    @Test
    public void shouldNotShowOutOfComponentLoaderWhenActiveAndCallbackSaysNo() {
        // Arrange
        uiComponent.populateStarting();
        when(populatable.showOutOfComponentLoading()).thenReturn(false);

        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

    @Test
    public void shouldNotShowOutOfComponentIfNotSetLoading() {
        // Arrange
        // uiComponent.populateStarting(); i.e. not set

        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

    @Test
    public void shouldNotShowOutOfComponentIfServerReturnedValueAlready() {
        // Arrange
        uiComponent.populateStarting();
        uiComponent.populateFromServer(new Object());

        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

    @Test
    public void shouldNotShowOutOfComponentIfServerReturnedEmptyValueAlready() {
        // Arrange
        uiComponent.populateStarting();
        uiComponent.populateWithEmptyContentFromServer();

        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

}