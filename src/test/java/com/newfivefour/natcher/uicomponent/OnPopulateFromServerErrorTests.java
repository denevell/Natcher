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
public class OnPopulateFromServerErrorTests {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingDisplay loaderInComponent;
    private final LoadingDisplay loaderOutOfComponent;
    private final EmptyDisplay emptyDisplay;
    private final ServerErrorDisplay serverErrorDisplayInComponent;
    private final ServerErrorDisplay serverErrorDisplayOutOfComponent;

    public OnPopulateFromServerErrorTests() {
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
    public void shouldHideEmptyView() {
        // Act
        uiComponent.populateFromServerError(400, "a");
        verify(emptyDisplay).showEmpty(false);
    }

    @Test
    public void shouldHideInComponentLoader() {
        // Act
        uiComponent.populateFromServerError(400, "a");
        verify(loaderInComponent).showLoading(false);
    }

    @Test
    public void shouldHideOutOfComponentLoaderWhenActive() {
        // Arrange
        when(populatable.showInComponentLoading()).thenReturn(false);
        when(populatable.showOutOfComponentLoading()).thenReturn(true);
        uiComponent.populateStarting();

        // Act
        uiComponent.populateFromServerError(400, "a");
        verify(loaderOutOfComponent).showLoading(false);
    }

    @Test
    public void shouldShowErrorViewInComponent() {
        when(populatable.showInComponentServerError()).thenReturn(true);

        // Act
        uiComponent.populateFromServerError(400, "a");
        verify(serverErrorDisplayInComponent).showServerError(true, 400, "a");
        verify(serverErrorDisplayOutOfComponent).showServerError(false, 0, null);
    }

    @Test
    public void shouldShowOutOfErrorViewInComponent() {
        when(populatable.showInComponentServerError()).thenReturn(false);
        when(populatable.showOutOfComponentServerError()).thenReturn(true);

        // Act
        uiComponent.populateFromServerError(400, "a");
        verify(serverErrorDisplayInComponent).showServerError(false, 0, null);
        verify(serverErrorDisplayOutOfComponent).showServerError(true, 400, "a");
    }

}