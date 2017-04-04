package org.enviapramim.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;

import java.util.List;

/**
 * Created by glauco on 03/04/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(kind = "listed")
public class ListedItem {

    @Identifier
    private String mlId;
    private String mlUsername;
    private String username;
    private String sku;

    public String getMlId() {
        return mlId;
    }

    public void setMlId(String mlId) {
        this.mlId = mlId;
    }

    public String getMlUsername() {
        return mlUsername;
    }

    public void setMlUsername(String mlUsername) {
        this.mlUsername = mlUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}
