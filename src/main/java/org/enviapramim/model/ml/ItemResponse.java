package org.enviapramim.model.ml;

import java.util.List;

/**
 * Created by Glauco on 13/03/2017.
 */
public class ItemResponse {

    public Boolean accepts_mercadopago;
    public Shipping shipping;
    public SellerAddress seller_address;
    public String id;
    public String site_id;
    public String title;
    public Object subtitle;
    public Integer seller_id;
    public String category_id;
    public Object official_store_id;
    public Float price;
    public Float base_price;
    public Object original_price;
    public String currency_id;
    public Integer initial_quantity;
    public Integer available_quantity;
    public Integer sold_quantity;
    public String buying_mode;
    public String listing_type_id;
    public String start_time;
    public String stop_time;
    public String end_time;
    public String expiration_time;
    public String condition;
    public String permalink;
    public String thumbnail;
    public String secure_thumbnail;
    public List<Picture> pictures;
    public Object video_id;
    public List<Description> descriptions;
    public List<Object> non_mercado_pago_payment_methods ;
    public String international_delivery_mode;
    public Object seller_contact;
    public Object location;
    public Geolocation geolocation;
    public List<Object> coverage_areas;
    public List<Attribute> attributes;
    public List<Object> warnings;
    public String listing_source;
    public List<Object> variations;
    public String status;
    public List<Object> sub_status;
    public List<String> tags;
    public String warranty;
    public Object catalog_product_id;
    public Object domain_id;
    public Object seller_custom_field;
    public Object parent_item_id;
    public Object differential_pricing;
    public List<Object> deal_ids;
    public Boolean automatic_relist;
    public String date_created;
    public String last_updated;

    public class Picture {
        public String source;
        public String id;
        public String url;
        public String secure_url;
        public String size;
        public String max_size;
        public String quality;
    }

    public class Attribute {

        public String id;
        public String name;
        public String value_id;
        public String value_name;
        public String attribute_group_id;
        public String attribute_group_name;

    }

    public class Description {
        public String id;
    }

    public class Geolocation {
        public Float latitude;
        public Float longitude;
    }
}