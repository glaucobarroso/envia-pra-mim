package org.enviapramim.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;

import java.util.List;

/**
 * Created by glauco on 16/02/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(kind = "storage")
public class Product {

    @Identifier
    private String sku;
    private String title;
    private String description;
    private String quantity;
    private String cost;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
