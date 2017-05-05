package org.enviapramim.model.ml;

import java.util.List;

/**
 * Created by root on 04/05/17.
 */
public class OrdersInfo {

    public List<Result> results = null;

    public class Alternative_phone {
        public String area_code;
        public String number;
        public String extension;
    }

    public class Atm_transfer_reference {
        public Object company_id;
        public Object transaction_id;
    }

    public class Billing_info {
        public String doc_type;
        public String doc_number;
    }

    public class Buyer {
        public Integer id;
        public String nickname;
        public String first_name;
        public String last_name;
        public String email;
        public Phone phone;
        public Alternative_phone alternative_phone;
        public Billing_info billing_info;
    }

    public class City {
        public String id;
        public String name;
    }
    public class Collector {
        public Integer id;
    }

    public class Country {
        public String id;
        public String name;
    }

    public class Estimated_delivery {
        public Object date;
        public Object time_from;
        public Object time_to;
    }

    public class Feedback {
        public Object sale;
        public Object purchase;
    }

    public class Item {
        public String id;
        public String title;
        public String condition;
        public Object seller_custom_field;
        public List<Object> variation_attributes = null;
        public String category_id;
        public String warranty;
        public Object variation_id;
    }

    public class Order_item {
        public String currency_id;
        public Object differential_pricing_id;
        public Item item;
        public String listing_type_id;
        public Float sale_fee;
        public Float quantity;
        public Float unit_price;
    }

    public class Order_request {
        public Object change;
        public Object _return;
    }

    public class Payment {
        public Long id;
        public Long order_id;
        public Long payer_id;
        public Collector collector;
        public String currency_id;
        public String status;
        public String status_code;
        public String status_detail;
        public Float transaction_amount;
        public Float shipping_cost;
        public Float overpaid_amount;
        public Float total_paid_amount;
        public Float marketplace_fee;
        public Integer coupon_amount;
        public String date_created;
        public String date_last_modified;
        public Object card_id;
        public String reason;
        public Object activation_uri;
        public String payment_method_id;
        public Integer installments;
        public String issuer_id;
        public Atm_transfer_reference atm_transfer_reference;
        public Long coupon_id;
        public String operation_type;
        public String payment_type;
        public List<Object> available_actions = null;
        public Float installment_amount;
        public Object deferred_period;
        public String date_approved;
        public String authorization_code;
        public String transaction_order_id;
    }

    public class Phone {
        public String area_code;
        public String number;
        public String extension;
        public Boolean verified;
    }

    public class Receiver_address {
        public Long id;
        public String zip_code;
        public Float latitude;
        public Float longitude;
        public String street_number;
        public String street_name;
        public State state;
        public Object comment;
        public String address_line;
        public Country country;
        public City city;
    }

    public class Result {
        public Long id;
        public Object comments;
        public String status;
        public Status_detail status_detail;
        public String date_created;
        public String date_closed;
        public String expiration_date;
        public String date_last_updated;
        public Boolean hidden_for_seller;
        public String currency_id;
        public List<Order_item> order_items = null;
        public Float total_amount;
        public List<Object> mediations = null;
        public List<Payment> payments = null;
        public Shipping shipping;
        public Buyer buyer;
        public Seller seller;
        public Feedback feedback;
        public List<String> tags = null;
        public Order_request order_request;
    }

    public class Seller {
        public Long id;
        public String nickname;
        public String email;
        public Phone phone;
        public Alternative_phone alternative_phone;
        public String first_name;
        public String last_name;
    }

    public class Shipping {
        public Object substatus;
        public String status;
        public Long id;
        public Object service_id;
        public Object currency_id;
        public Object shipping_mode;
        public Object shipment_type;
        public Long sender_id;
        public Object picking_type;
        public Receiver_address receiver_address;
        public Object date_created;
        public Float cost;
        public String date_first_printed;
        public Shipping_option shipping_option;
        public List<Shipping_item> shipping_items = null;
    }

    public class Shipping_item {
        public String id;
        public String description;
        public Integer quantity;
        public String dimensions;
    }

    public class Shipping_option {
        public Integer id;
        public Integer shipping_method_id;
        public String name;
        public String currency_id;
        public Float cost;
        public Speed speed;
        public Estimated_delivery estimated_delivery;
    }

    public class Speed {
        public Object shipping;
        public Object handling;
    }

    public class State {
        public String id;
        public String name;
    }

    public class Status_detail {
        public Object description;
        public String code;
    }

}
