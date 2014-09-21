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
public class OnPopulateStartingTests {

    private final Runnable hideKeyboard;
    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingDisplay loaderInComponent;
    private final LoadingDisplay loaderOutOfComponent;
    private final EmptyDisplay emptyDisplay;
    private final ServerErrorDisplay serverErrorDisplayInComponent;
    private final ServerErrorDisplay serverErrorDisplayOutOfComponent;

    public OnPopulateStartingTests() {
        populatable = mock(Populatable.class);

        hideKeyboard = mock(Runnable.class);
        loaderInComponent = mock(LoadingDisplay.class);
        loaderOutOfComponent = mock(LoadingDisplay.class);
        emptyDisplay = mock(EmptyDisplay.class);
        serverErrorDisplayInComponent = mock(ServerErrorDisplay.class);
        serverErrorDisplayOutOfComponent = mock(ServerErrorDisplay.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
        uiComponent.setHideKeyboard(hideKeyboard);
        uiComponent.setInComponentLoadingDisplay(loaderInComponent);
        uiComponent.setOutOfComponentLoadingDisplay(loaderOutOfComponent);
        uiComponent.setOutOfComponentServerErrorDisplay(serverErrorDisplayOutOfComponent);
        uiComponent.setInComponentServerErrorDisplay(serverErrorDisplayInComponent);
        uiComponent.setEmptyDisplay(emptyDisplay);
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
        verify(serverErrorDisplayInComponent).showServerError(false, 0, null);
        verify(serverErrorDisplayOutOfComponent).showServerError(false, 0, null);
    }

    @Test
    public void shouldHideEmptyView() {
        // Act
        uiComponent.populateStarting();
        verify(emptyDisplay).showEmpty(false);
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