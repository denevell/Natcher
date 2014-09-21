package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.views.LoadingView;

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
public class OnResetComponentTests {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingView loaderInComponent;
    private final LoadingView loaderOutOfComponent;

    public OnResetComponentTests() {
        populatable = mock(Populatable.class);

        loaderInComponent = mock(LoadingView.class);
        loaderOutOfComponent = mock(LoadingView.class);

        uiComponent = new UiComponentVanilla<Object>(populatable);
        uiComponent.setInComponentLoadingDisplay(loaderInComponent);
        uiComponent.setOutOfComponentLoadingDisplay(loaderOutOfComponent);
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