package org.enviapramim.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by glauco on 16/02/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(kind = "storage")
public class Product {

    private String sku;
    private String title;
    private List<String> titles;
    private String description;
    private String cost;
    private String quantity;
    private List<MultipartFile> images;
    private String link1;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
