package org.enviapramim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringEscapeUtils;
import org.enviapramim.model.Product;
import org.enviapramim.model.ProductToList;
import org.enviapramim.model.ml.*;

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
    private static final String WARRANTY = "90 dias para defeitos de fabricação";
    private String DESCRIPTION_PREFFIX_FORMAT_STR = "<p style=\"text-align: center;\"><span style=\"font-family: helvetica; font-size: xx-large;\">%s<br /></span></p><hr /><div><br /><div style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; color: #333333; font-family: Arial, Helvetica, Verdana, sans-serif; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff; text-align: center; font-size: x-large;\"><img src=\"%s\" style=\"width:500px;height:500px;\" /></div><p style=\"margin: 0.5em 0px; padding: 0px; border: 3px; outline: 0px; vertical-align: baseline; box-sizing: content-box; color: #333333; font-family: Arial, Helvetica, Verdana, sans-serif; font-size: 13px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;\"><span style=\"font-family: helvetica; font-size: x-large;\"><strong><span style=\"margin: 0px; padding: 0px; border: 0px none; outline: 0px none; vertical-align: baseline; box-sizing: content-box;\">Detalhes do produto</span></strong></span></p><hr/><div style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; color: #333333; font-family: Arial, Helvetica, Verdana, sans-serif; font-size: 13px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;\"><span face=\"arial, helvetica, sans-serif\" size=\"4\" style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; font-family: arial, helvetica, sans-serif; font-size: large;\"><span face=\"arial, helvetica, sans-serif\" size=\"4\" style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; font-family: arial, helvetica, sans-serif; font-size: large;\"></span></span><ul style=\"margin: 1em 0px; padding: 0px 0px 0px 40px; border: 0px; outline: 0px; vertical-align: baseline; list-style: disc inside; box-sizing: content-box; display: block;\">";
    private String DESCRIPTION_LINE_FORMAT_STR = "<li style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; display: list-item;\"><span style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; font-size: large; font-family: arial, helvetica, sans-serif;\">%s</span></li>";
    private String DESCRIPTION_SUFFIX = "</div></div>";

    public static final String CATEGORY_BACKPACK_SCHOOLL_MALE = "MLB202974"; //Calçados, Roupas e Bolsas > Mochilas > Escolar > Outras Marcas > Masculino
    public static final String CATEGORY_BAG_LEATHER_MALE = "MLB133465"; // Calçados, Roupas e Bolsas >  Bolsas  > Masculinas   Couro Sintético   Outras Marcas
    public static final String CATEGORY_BACKPACK_SCHOOL_GIRLS = "MLB202975"; // Calçados, Roupas e Bolsas   Mochilas   Escolar   Outras Marcas   Meninas
    public static final String CATEGORY_BACKPACK_SCHOOL_BOYS = "MLB202976"; // Calçados, Roupas e Bolsas   Mochilas   Escolar   Outras Marcas   Meninos
    public static final String CATEGORY_BACKPACK_NOTEBOOK_MALE = "MLB202885"; // Calçados, Roupas e Bolsas   Mochilas   Notebooks   Outras Marcas   Masculino
    public static final String CATEGORY_BACKPACK_CAMPING_MALE = "MLB202712"; // Calçados, Roupas e Bolsas   Mochilas   Camping   Outras Marcas   Masculino
    public static final String CATEGORY_BAG_LEATHER_FEMALE = "MLB104263"; // Calçados, Roupas e Bolsas   Bolsas   Femininas   Couro Sintético   Outras Marcas
    public static final String CATEGORY_TRAVEL_BAG_SET = "MLB199560"; //  Calçados, Roupas e Bolsas   Malas e Carteiras   Malas   Conjuntos de Malas   Outros

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

    public ItemResponse offerProduct(ListingInfo info, String accessToken) {
        try {
            FluentStringsMap params = new FluentStringsMap();
            params.add("access_token", accessToken);
            Response response = meli.post("/items", params, convertProductToItemJson(info));
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
            return response.getStatusCode();
        } catch (MeliException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public MlUserInfo getUserInfo(String accessToken) {
        try {
            FluentStringsMap params = new FluentStringsMap();
            params.add("access_token", accessToken);
            Response response = null;
            response = meli.get("/users/me", params);
            String body = response.getResponseBody();
            Gson gson = new Gson();
            return gson.fromJson(body, MlUserInfo.class);
        } catch (MeliException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertProductToItemJson(ListingInfo info) {
        Item item = new Item();
        item.title = info.title;
        item.available_quantity = info.quantity;
        item.price = info.price;
        item.category_id = info.category;
        item.currency_id = "BRL";
        item.buying_mode = "buy_it_now";
        item.listing_type_id = info.type;
        item.condition = "new";
        item.accepts_mercadopago = true;
        item.warranty = WARRANTY;

        List<Item.Picture> pictureList = new ArrayList<Item.Picture>();
        for (String link : info.pictureLinks) {
            Item.Picture picture = item.new Picture();
            picture.source = link;
            pictureList.add(picture);
        }
        item.pictures = pictureList;

        Shipping shipping = new Shipping();
        shipping.mode = "me2";
        shipping.localPickUp = false;
        shipping.free_shipping = info.freeShipping;
        if (shipping.free_shipping) {
            FreeShipping freeShipping = new FreeShipping();
            freeShipping.id = 100009;
            FreeShipping.Rule rule = freeShipping.new Rule();
            rule.free_mode = "country";
            rule.value = null;
            freeShipping.rule = rule;
            shipping.free_methods = new ArrayList<FreeShipping>();
            shipping.free_methods.add(freeShipping);
        }
        shipping.methods = null;
        shipping.dimensions = null;
        shipping.tags = new ArrayList<Object>();
        shipping.logistic_type = "drop_off";
        item.shipping = shipping;

        item.seller_custom_field = info.sku;

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public ListingInfo createListingInfo(Product productDb, ProductToList productWeb) {
        ListingInfo info = new ListingInfo();
        info.title = productWeb.title;
        info.mainTitle = productDb.getTitle();
        info.category = productDb.getCategory();
        info.pictureLinks = productDb.getLinks();
        info.price = Float.parseFloat(productWeb.price);
        // TODO change hardcoded quantity
        info.quantity = 1000;
        info.type = productWeb.type;
        info.description = productDb.getDescription();
        info.freeShipping = productWeb.freeShipping;
        info.sku = productDb.getSku();
        return info;
    }

    public String createHtmlDescription(ListingInfo info) {
        String htmlDescription = StringEscapeUtils.escapeHtml3(info.description);
        String[] lines = htmlDescription.split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(DESCRIPTION_PREFFIX_FORMAT_STR, StringEscapeUtils.escapeHtml3(info.mainTitle), info.pictureLinks.get(0)));
        for (String line : lines) {
            stringBuilder.append(String.format(DESCRIPTION_LINE_FORMAT_STR, line));
        }
        stringBuilder.append(DESCRIPTION_SUFFIX);
        return stringBuilder.toString();
    }

}
