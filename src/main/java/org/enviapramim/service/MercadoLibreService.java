package org.enviapramim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;
import org.enviapramim.model.Product;
import org.enviapramim.model.ml.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glauco on 01/03/2017.
 */
public class MercadoLibreService {

    private Meli meli;
    private static final Long APP_ID = 7164282521643532L;
    private static String APP_SECRET = "Fa4Zcl3Kp41nvNGcyKmFf9jB5Fch1cf3";
    private static final String AUTH_CALLBACK = "https://localhost:8443/mlauth";

    public MercadoLibreService() {
        meli = new Meli(APP_ID, APP_SECRET);
    }

    public String getRedirectUrl() {
        return meli.getAuthUrl(AUTH_CALLBACK);
    }

    public boolean authenticate(String code) {
        try {
            meli.authorize(code, AUTH_CALLBACK);
            StorageService storageService = new StorageService();
            storageService.addUserMlData("usernameA", meli.getAccessToken(), meli.getRefreshToken());
        } catch (AuthorizationFailure authorizationFailure) {
            authorizationFailure.printStackTrace();
            return false;
        }
        return true;
    }

    public String convertProductToItemJson(Product product) {
        Item item = new Item();
        item.setTitle(product.getTitle());
        item.setAvailableQuantity(9999);
        item.setPrice(1000.90f);
        item.setCategoryId("MLB3530");
        item.setCurrencyId("BRL");
        item.setBuyingMode("buy_it_now");
        item.setListingTypeId("free");
        item.setCondition("new");
        item.setDescription(product.getDescription());
        item.setWarranty("90 dias");
        item.setVideoId(null);

        List<Item.Picture> pictureList = new ArrayList<Item.Picture>();
        Item.Picture picture = item.new Picture();
        picture.setSource(product.getLink1());
        pictureList.add(picture);
        item.setPictures(pictureList);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }
}
