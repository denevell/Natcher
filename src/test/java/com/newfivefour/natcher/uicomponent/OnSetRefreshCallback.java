package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.events.OnRefreshCallback;
import com.newfivefour.natcher.uicomponent.events.OnRefreshWidget;
import com.newfivefour.natcher.uicomponent.views.EmptyDisplay;
import com.newfivefour.natcher.uicomponent.views.ServerErrorDisplay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class OnSetRefreshCallback {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;

    public OnSetRefreshCallback() {
        populatable = mock(Populatable.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
    }

    @Test
    public void shouldConnectCallbackToRefreshWidget() {
        // Arrange
        OnRefreshCallback refreshCallback = mock(OnRefreshCallback.class);
        OnRefreshWidget refreshWidget = mock(OnRefreshWidget.class);
        uiComponent.setRefreshWidget(refreshWidget);

        // Act
        uiComponent.setRefreshableCallback(refreshCallback);
        verify(refreshWidget).setRefreshableCallback(refreshCallback);
    }

    @Test
    public void shouldConnectCallbackToEmptyDisplay() {
        // Arrange
        OnRefreshCallback refreshCallback = mock(OnRefreshCallback.class);
        EmptyDisplay emptyDisplay = mock(EmptyDisplay.class);
        uiComponent.setEmptyDisplay(emptyDisplay);

        // Act
        uiComponent.setRefreshableCallback(refreshCallback);
        verify(emptyDisplay).setRefreshableCallback(refreshCallback);
    }

    @Test
    public void shouldConnectCallbackToInComponentServerErrorDisplay() {
        // Arrange
        OnRefreshCallback refreshCallback = mock(OnRefreshCallback.class);
        ServerErrorDisplay serverErrorDisplay = mock(ServerErrorDisplay.class);
        uiComponent.setInComponentServerErrorDisplay(serverErrorDisplay);

        // Act
        uiComponent.setRefreshableCallback(refreshCallback);
        verify(serverErrorDisplay).setRefreshableCallback(refreshCallback);
    }
}