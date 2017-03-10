package org.enviapramim.service;

import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;

/**
 * Created by Glauco on 01/03/2017.
 */
public class MercadoLibreService {

    private Meli meli;
    private static final Long APP_ID = 7164282521643532L;
    private static String APP_SECRET = "Fa4Zcl3Kp41nvNGcyKmFf9jB5Fch1cf3";
    private static final String AUTH_CODE_CALLBACK = "https://x-pulsar-158711.appspot.com/mlauthcode";
    private static final String AUTH_CALLBACK = "https://x-pulsar-158711.appspot.com/mlauth";
//    private static final String AUTH_CODE_CALLBACK = "https://localhost:8443/mlauthcode";
//    private static final String AUTH_CALLBACK = "https://localhost:8443/mlauth";

    public MercadoLibreService() {
        meli = new Meli(APP_ID, APP_SECRET);
    }

    public String getRedirectUrl() {
        return meli.getAuthUrl(AUTH_CODE_CALLBACK);
    }

    public boolean authenticate(String code) {
        try {
            meli.authorize(code, AUTH_CALLBACK);
        } catch (AuthorizationFailure authorizationFailure) {
            authorizationFailure.printStackTrace();
            return false;
        }
        return true;
    }
}
