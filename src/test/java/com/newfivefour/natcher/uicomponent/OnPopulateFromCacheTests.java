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
public class OnPopulateFromCacheTests {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingDisplay loaderInComponent;
    private final LoadingDisplay loaderOutOfComponent;
    private final EmptyDisplay emptyDisplay;
    private final ServerErrorDisplay serverErrorDisplayInComponent;
    private final ServerErrorDisplay serverErrorDisplayOutOfComponent;

    public OnPopulateFromCacheTests() {
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
        uiComponent.populateFromCache(object);
        verify(serverErrorDisplayInComponent).showServerError(false, 0, null);
        verify(serverErrorDisplayOutOfComponent).showServerError(false, 0, null);
    }

    @Test
    public void shouldHideEmptyView() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromCache(object);
        verify(emptyDisplay).showEmpty(false);
    }

    @Test
    public void shouldHideInComponentLoader() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromCache(object);
        verify(loaderInComponent).showLoading(false);
    }

    @Test
    public void shouldPopulateOnCachedResult() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromCache(object);
        verify(populatable).populateOnSuccessfulResponse(object);
    }

    @Test
    public void shouldSetOutOfComponentLoadingIfCallbackSaysOkay() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);

        // Act
        uiComponent.populateFromCache(new Object());
        verify(loaderOutOfComponent).showLoading(true);
    }

    @Test
    public void shouldNotSetOutOfComponentLoadingIfCallbackSaysNo() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(false);

        // Act
        uiComponent.populateFromCache(new Object());
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

    @Test
    public void shouldNotSetOutOfComponentLoadingIfServerHasReturnedGoodData() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);
        uiComponent.populateFromServer(new Object());

        // Act
        uiComponent.populateFromCache(new Object());
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

    @Test
    public void shouldNotSetOutOfComponentLoadingIfServerHasReturnedEmptyData() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);
        uiComponent.populateWithEmptyContentFromServer();

        // Act
        uiComponent.populateFromCache(new Object());
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

    @Test
    public void shouldNotSetOutOfComponentLoadingIfServerHasReturnedError() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);
        uiComponent.populateFromServerError(300, "a");

        // Act
        uiComponent.populateFromCache(new Object());
        verify(loaderOutOfComponent, never()).showLoading(true);
    }

}