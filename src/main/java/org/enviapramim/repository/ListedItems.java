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
public class ListedItems {

    @Identifier
    private String id;
    private String mlUsername;
    private String username;
    private List<String> itemsSku;
    private List<String> itemsMlId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getItemsSku() {
        return itemsSku;
    }

    public void setItemsSku(List<String> itemsSku) {
        this.itemsSku = itemsSku;
    }

    public List<String> getItemsMlId() {
        return itemsMlId;
    }

    public void setItemsMlId(List<String> itemsMlId) {
        this.itemsMlId = itemsMlId;
    }
}
