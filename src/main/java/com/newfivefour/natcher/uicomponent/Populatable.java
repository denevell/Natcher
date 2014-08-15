package com.newfivefour.natcher.uicomponent;

/**
 * Used by the UiComponentVanilla class so
 * 1. It knows how to populate a view,
 * 2. If that view is empty to decide which loader (page or component) to show,
 * 3. How to clear that view on empty content from server
 */
public interface Populatable<T> extends HasEmptyContent {
    void populate(T ob);
}
