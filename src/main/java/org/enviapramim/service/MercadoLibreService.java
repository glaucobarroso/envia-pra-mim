package org.enviapramim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;
import org.enviapramim.model.Product;
import org.enviapramim.model.ml.Description;
import org.enviapramim.model.ml.Item;
import org.enviapramim.model.ml.ItemResponse;
import org.enviapramim.model.ml.Shipping;

import java.io.IOException;
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

    public boolean authenticate(String code, String username) {
        try {
            meli.authorize(code, AUTH_CALLBACK);
            StorageService storageService = new StorageService();
            storageService.addUserMlData(username, meli.getAccessToken(), meli.getRefreshToken());
        } catch (AuthorizationFailure authorizationFailure) {
            authorizationFailure.printStackTrace();
            return false;
        }
        return true;
    }

    public ItemResponse offerProduct(Product product, String accessToken) {
        try {
            FluentStringsMap params = new FluentStringsMap();
            params.add("access_token", accessToken);
            Response response = meli.post("/items", params, convertProductToItemJson(product));
            String body = response.getResponseBody();
            Gson gson = new Gson();
            return gson.fromJson(body, ItemResponse.class);
        } catch (MeliException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {

        }
        return null;
    }

    public int updateHtmlDescription(String description, String itemId, String accessToken) {
        try {
            FluentStringsMap params = new FluentStringsMap();
            params.add("access_token", accessToken);
            String path = "/items/" + itemId + "/description";
            Description descriptionObj = new Description();
            descriptionObj.text = description;
            String jsonString = "";
            ObjectMapper mapper = new ObjectMapper();
            try {
                jsonString = mapper.writeValueAsString(descriptionObj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Response response = meli.put(path, params, jsonString);
            String body = response.getResponseBody();
            return response.getStatusCode();
        } catch (MeliException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String convertProductToItemJson(Product product) {
        Item item = new Item();
        item.title = product.getTitle();
        item.available_quantity = 1;
        item.price = 1000.90f;
        //item.category_id = "MLB3530";
        item.category_id = "MLB202885";
        item.currency_id = "BRL";
        item.buying_mode = "buy_it_now";
        item.listing_type_id = "gold_special";
        item.condition = "new";
//        item.description = product.getDescription();
        item.accepts_mercadopago = true;
        item.warranty = "90 dias";

        List<Item.Picture> pictureList = new ArrayList<Item.Picture>();
        for (String link : product.getLinks()) {
            Item.Picture picture = item.new Picture();
            picture.source = link;
            pictureList.add(picture);
        }
        item.pictures = pictureList;

        Shipping shipping = new Shipping();
        shipping.mode = "me2";
        shipping.localPickUp = false;
        shipping.freeShipping = false;
        shipping.methods = null;
        shipping.dimensions = null;
        shipping.tags = new ArrayList<Object>();
        shipping.logistic_type = "drop_off";
        item.shipping = shipping;

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}

