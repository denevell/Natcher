package com.newfivefour.natcher.app.component;

/**
 * Created by user on 15/08/14.
 */
public interface Populatable<T> extends HasEmptyContent {
    void populate(T ob);
}