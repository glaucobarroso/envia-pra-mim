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
    private String description;
    private String cost;
    private String quantity;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    private MultipartFile image4;
    private MultipartFile image5;
    private MultipartFile image6;

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

    public MultipartFile getImage1() {
        return image1;
    }

    public void setImage1(MultipartFile image1) {
        this.image1 = image1;
    }

    public MultipartFile getImage2() {
        return image2;
    }

    public void setImage2(MultipartFile image2) {
        this.image2 = image2;
    }

    public MultipartFile getImage3() {
        return image3;
    }

    public void setImage3(MultipartFile image3) {
        this.image3 = image3;
    }

    public MultipartFile getImage4() {
        return image4;
    }

    public void setImage4(MultipartFile image4) {
        this.image4 = image4;
    }

    public MultipartFile getImage5() {
        return image5;
    }

    public void setImage5(MultipartFile image5) {
        this.image5 = image5;
    }

    public MultipartFile getImage6() {
        return image6;
    }

    public void setImage6(MultipartFile image6) {
        this.image6 = image6;
    }

}
