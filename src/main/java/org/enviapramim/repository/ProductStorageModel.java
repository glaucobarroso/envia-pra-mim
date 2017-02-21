package org.enviapramim.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;

/**
 * Created by glauco on 21/02/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(kind = "storage")
public class ProductStorageModel {

    @Identifier
    private String sku;
    private String title;
    private String description;
    private String cost;
    private String quantity;

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
