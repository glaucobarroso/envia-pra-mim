package org.enviapramim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;
import org.enviapramim.model.Product;
import org.enviapramim.model.ml.Item;

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

    public boolean authenticate(String code) {
        try {
            meli.authorize(code, AUTH_CALLBACK);
            StorageService storageService = new StorageService();
            storageService.addUserMlData("testUser", meli.getAccessToken(), meli.getRefreshToken());
        } catch (AuthorizationFailure authorizationFailure) {
            authorizationFailure.printStackTrace();
            return false;
        }
        return true;
    }

    public String offerProduct(Product product, String accessToken) {
        try {
            FluentStringsMap params = new FluentStringsMap();
            params.add("access_token", accessToken);
            Response response = meli.post("/items", params, convertProductToItemJson(product));
            return response.getResponseBody();
        } catch (MeliException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertProductToItemJson(Product product) {
        Item item = new Item();
        item.title = product.getTitle();
        item.available_quantity = 1;
        item.price = 1000.90f;
        item.category_id = "MLB3530";
        item.currency_id = "BRL";
        item.buying_mode = "buy_it_now";
        item.listing_type_id = "free";
        item.condition = "new";
        item.description = product.getDescription();
        item.warranty = "90 dias";

        List<Item.Picture> pictureList = new ArrayList<Item.Picture>();
        Item.Picture picture = item.new Picture();
        picture.source = product.getLink1();
        pictureList.add(picture);
        item.pictures = pictureList;

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
