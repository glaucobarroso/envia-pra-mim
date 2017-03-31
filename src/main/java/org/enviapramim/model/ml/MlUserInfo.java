package org.enviapramim.model.ml;

import java.util.List;

public class MlUserInfo {

    public Integer id;
    public String nickname;
    public String registration_date;
    public String first_name;
    public String last_name;
    public String country_id;
    public String email;
    public Identification identification;
    public Address address;
    public Phone phone;
    public Alternative_phone alternative_phone;
    public String user_type;
    public List<String> tags = null;
    public Object logo;
    public Integer points;
    public String site_id;
    public String permalink;
    public List<String> shipping_modes = null;
    public String seller_experience;
    public Seller_reputation seller_reputation;
    public Buyer_reputation buyer_reputation;
    public Status status;
    public Credit credit;


    public class Address {

        public String state;
        public String city;
        public String address;
        public String zip_code;

    }

    public class Alternative_phone {

        public String area_code;
        public String number;
        public String extension;

    }

    public class Billing {

        public Boolean allow;
        public List<Object> codes = null;

    }

    public class Buy {

        public Boolean allow;
        public List<Object> codes = null;
        public Immediate_payment_ immediate_payment;

    }

    public class Buyer_reputation {

        public Integer canceled_transactions;
        public Transactions_ transactions;
        public List<Object> tags = null;

    }

    public class Canceled {

        public Object total;
        public Object paid;

    }

    public class Credit {

        public Float consumed;
        public String credit_level_id;

    }

    public class Identification {

        public String type;
        public String number;

    }

    public class Immediate_payment {

        public Boolean required;
        public List<Object> reasons = null;

    }

    public class Immediate_payment_ {

        public Boolean required;
        public List<Object> reasons = null;

    }

    public class Immediate_payment__ {

        public Boolean required;
        public List<Object> reasons = null;

    }


    public class Not_yet_rated {

        public Object total;
        public Object paid;
        public Object units;

    }

    public class Phone {

        public String area_code;
        public String number;
        public String extension;
        public Boolean verified;

    }

    public class Ratings {

        public Integer positive;
        public Integer negative;
        public Integer neutral;

    }

    public class Sell {

        public Boolean allow;
        public List<Object> codes = null;
        public Immediate_payment__ immediate_payment;

    }

    public class Seller_reputation {

        public Object level_id;
        public Object power_seller_status;
        public Transactions transactions;

    }

    public class Status {

        public String site_status;
        public MlList list;
        public Buy buy;
        public Sell sell;
        public Billing billing;
        public Boolean mercado_Pago_tc_accepted;
        public String mercado_Pago_account_type;
        public String mercado_Envios;
        public Boolean immediate_payment;
        public Boolean confirmed_email;
        public String user_type;
        public String required_action;

    }

    public class MlList {

        public Boolean allow;
        public List<Object> codes = null;
        public Immediate_payment immediate_payment;

    }

    public class Transactions {

        public String period;
        public Integer total;
        public Integer completed;
        public Integer canceled;
        public Ratings ratings;

    }

    public class Transactions_ {

        public String period;
        public Object total;
        public Object completed;
        public Canceled canceled;
        public Unrated unrated;
        public Not_yet_rated not_yet_rated;

    }

    public class Unrated {

        public Object total;
        public Object paid;

    }
}

