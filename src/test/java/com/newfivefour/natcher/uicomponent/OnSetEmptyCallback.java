package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.events.OnEmptyCallback;
import com.newfivefour.natcher.uicomponent.views.EmptyDisplay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class OnSetEmptyCallback {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;

    public OnSetEmptyCallback() {
        populatable = mock(Populatable.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
    }

    @Test
    public void shouldConnectCallbackToEmptyDisplay() {
        // Arrange
        OnEmptyCallback emptyCallback = mock(OnEmptyCallback.class);
        EmptyDisplay emptyDisplay = mock(EmptyDisplay.class);
        uiComponent.setEmptyDisplay(emptyDisplay);

        // Act
        uiComponent.setEmptyCallback(emptyCallback);
        verify(emptyDisplay).setEmptyCallback(emptyCallback);
    }

}