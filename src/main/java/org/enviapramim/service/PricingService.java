package org.enviapramim.service;

/**
 * Created by glauco on 21/03/17.
 */
public class PricingService {

    public static final float MARGIN = 1.8f;
    public static final float CLASSIC_PERCENTAGE = 1.11f;

    public String getSuggestedPrice(String cost) {
        return String.format("%.2f", Float.parseFloat(cost) * MARGIN * CLASSIC_PERCENTAGE).replaceAll(",", ".");
    }

    public String getSuggestedProfit(String cost) {
        String price = getSuggestedPrice(cost);
        float costF = Float.parseFloat(cost);
        float priceF = Float.parseFloat(price);
        float profitF = priceF - priceF*(CLASSIC_PERCENTAGE-1) - costF;
        return String.format("%.2f", profitF).replaceAll(",", ".");
    }

    public String getFreeShippingPrice(String category) {
        float shippingCost = 20.90f;
        return String.format("%.2f", shippingCost).replaceAll(",", ".");
    }

    public String formatPrice(String price) {
        return String.format("%.2f", Float.parseFloat(price)).replaceAll(",", ".");
    }
}
