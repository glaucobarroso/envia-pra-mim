package org.enviapramim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;
import org.apache.http.HttpStatus;
import org.enviapramim.model.Product;
import org.enviapramim.model.ml.Item;
import org.enviapramim.model.ml.SellerAddress;
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

    public int offerProduct(Product product, String accessToken) {
        try {
            FluentStringsMap params = new FluentStringsMap();
            params.add("access_token", accessToken);
            Response response = meli.post("/items", params, convertProductToItemJson(product));
            return response.getStatusCode();
        } catch (MeliException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String convertProductToItemJson(Product product) {
        Item item = new Item();
        item.title = "Mochila " + product.getTitle() + "6";
        item.available_quantity = 1;
        item.price = 1000.90f;
        //item.category_id = "MLB3530";
        item.category_id = "MLB202885";
        item.currency_id = "BRL";
        item.buying_mode = "buy_it_now";
        item.listing_type_id = "gold_special";
        item.condition = "new";
        item.description = product.getDescription();
        item.accepts_mercadopago = true;
        item.warranty = "90 dias";

        List<Item.Picture> pictureList = new ArrayList<Item.Picture>();
        Item.Picture picture = item.new Picture();
        picture.source = product.getLink1();
        pictureList.add(picture);
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

        /*
        SellerAddress sellerAddress = new SellerAddress();
        //sellerAddress.id = 265539906;265660776
        sellerAddress.id = 265661446;
        sellerAddress.comment = "";
        sellerAddress.address_line = "";
        sellerAddress.zip_code = "";
        SellerAddress.City city = sellerAddress.new City();
        city.id = "BR-SP-42";
        city.name = "Campinas";
        sellerAddress.city = city;

        SellerAddress.State state = sellerAddress.new State();
        state.id = "BR-PR";
        state.name = "Paraná";
        sellerAddress.state = state;

        SellerAddress.Country country = sellerAddress.new Country();
        country.id = "BR";
        country.name = "Brasil";
        sellerAddress.country = country;

        sellerAddress.latitude = -22.88430126f;
        sellerAddress.longitude = -47.0621333f;

        SellerAddress.SearchLocation searchLocation = sellerAddress.new SearchLocation();
        SellerAddress.Neighborhood neighborhood = sellerAddress.new Neighborhood();
        neighborhood.id = "TUxCQlZJTGRhZGNv";
        neighborhood.name = "Vila Rossi";
        searchLocation.neighborhood = neighborhood;

        SellerAddress.City cityLocation = sellerAddress.new City();
        cityLocation.id = "TUxCQ1NQLTc2MDI";
        cityLocation.name = "Campinas";
        searchLocation.city = cityLocation;

        SellerAddress.State stateLocation = sellerAddress.new State();
        stateLocation.id = "TUxCUFBBUkExODBlZA";
        stateLocation.name = "Paraná";
        searchLocation.state = stateLocation;

        sellerAddress.search_location = searchLocation;

        item.seller_address = sellerAddress;
*/
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

