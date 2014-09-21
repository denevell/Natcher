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
public class OnPopulateFromCacheAsEmpty {

    private final Populatable populatable;
    private final UiComponentVanilla<Object> uiComponent;
    private final LoadingView loaderInComponent;
    private final LoadingView loaderOutOfComponent;
    private final EmptyView emptyView;
    private final ServerErrorView serverErrorViewInComponent;
    private final ServerErrorView serverErrorViewOutOfComponent;

    public OnPopulateFromCacheAsEmpty() {
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
    public void shouldSetEmptyView() {
        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(emptyView).showEmpty(true);
    }

    @Test
    public void shouldClearContent() {
        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(populatable).clearContentOnEmptyResponse();
    }


    @Test
    public void shouldHideServerErrors() {
        // Arrange
        Object object = new Object();

        // Act
        uiComponent.populateFromCache(object);
        verify(serverErrorViewInComponent).showServerError(false, 0, null);
        verify(serverErrorViewOutOfComponent).showServerError(false, 0, null);
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
        when(populatable.showOutOfComponentLoading()).thenReturn(true);
        uiComponent.populateStarting();

        // Act
        uiComponent.populateWithEmptyContentFromCache();
        verify(loaderOutOfComponent).showLoading(true);
    }

    // Don't show loader if callback says no
    // Don't show loader if not set
    // Don't show loader if server has returned

}