package org.enviapramim.model.ml;

import java.util.List;

/**
 * Created by Glauco on 13/03/2017.
 */
public class Item {
    public String title;
    public String category_id;
    public Float price;
    public String currency_id;
    public Integer available_quantity;
    public String buying_mode;
    public String listing_type_id;
    public String condition;
    public String description;
    public String warranty;
    public List<Picture> pictures;
    public boolean accepts_mercadopago;
    public Shipping shipping;
    public SellerAddress seller_address;

    public class Picture {
        public String source;
    }
}
