package com.newfivefour.natcher.networking;

public abstract class ResponseEmpty {
    private boolean mIsFromCache;

    public void setIsFromCache(boolean fromCache)  {
        mIsFromCache = fromCache;
    }

    public boolean isFromCached() {
        return mIsFromCache;
    }
}
