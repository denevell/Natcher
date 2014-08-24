package com.newfivefour.natcher.networking;

import com.squareup.okhttp.internal.huc.HttpsURLConnectionImpl;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
* Created by user on 20/08/14.
*/
public class NukeSSLCerts {
    protected static final String TAG = "NukeSSLCerts";
    public static SSLContext sc;
    static TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                    return myTrustedAnchors;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
    };
    static {
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void nuke() {
        try {
            HttpsURLConnectionImpl.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnectionImpl.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
