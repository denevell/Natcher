package com.newfivefour.natcher.app;

import android.content.Context;

import com.newfivefour.natcher.networking.NukeSSLCerts;
import com.squareup.otto.Bus;

public class Application extends android.app.Application {
    private static Bus sEventBus;
    private static Application sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.sContext = this;
        NukeSSLCerts.nuke();
    }

    public static Bus getEventBus() {
        if(sEventBus==null) {
            sEventBus = new com.squareup.otto.Bus();
        }
        return sEventBus;
    }

    public static Context getContext() {
        return sContext;
    }

}
