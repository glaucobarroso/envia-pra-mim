package org.enviapramim.service;

/**
 * Created by glauco on 21/03/17.
 */
public class PricingService {

    public static final float MARGIN_FACTOR = 2;

    public String getSuggestedPrice(String cost) {
        return String.format("%.2f", Float.parseFloat(cost) * MARGIN_FACTOR).replaceAll(",", ".");
    }

    public String getFreeShippingPrice(String category) {
        float shippingCost = 20.90f;
        return String.format("%.2f", shippingCost).replaceAll(",", ".");
    }

    public String formatPrice(String price) {
        return String.format("%.2f", Float.parseFloat(price)).replaceAll(",", ".");
    }
}
