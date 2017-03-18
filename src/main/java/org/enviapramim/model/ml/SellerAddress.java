package org.enviapramim.model.ml;

/**
 * Created by Glauco on 13/03/2017.
 */
public class SellerAddress {

    public Integer id;
    public String comment;
    public String address_line;
    public String zip_code;
    public City city;
    public State state;
    public Country country;
    public Float latitude;
    public Float longitude;
    public SearchLocation search_location;

    public class Neighborhood {
        public String id;
        public String name;
    }

    public class SearchLocation {

        public Neighborhood neighborhood;
        public City city;
        public State state;

    }

    public class State {

        public String id;
        public String name;
    }

    public class City {

        public String id;
        public String name;
    }

    public class Country {

        public String id;
        public String name;
    }

}
