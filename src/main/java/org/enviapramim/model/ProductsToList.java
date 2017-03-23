package org.enviapramim.model;

import java.util.List;

/**
 * Created by Glauco on 22/03/2017.
 */

public class ProductsToList {

    public List<ProductToList> productsToList;

    public List<ProductToList> getProductsToList() {
        return productsToList;
    }

    public void setProductsToList(List<ProductToList> productsToList) {
        this.productsToList = productsToList;
    }

    public class ProductToList {
        private String sku;
        private String title;
        private String price;
        private boolean freeShipping;
        private String type;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isFreeShipping() {
            return freeShipping;
        }

        public void setFreeShipping(boolean freeShipping) {
            this.freeShipping = freeShipping;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
