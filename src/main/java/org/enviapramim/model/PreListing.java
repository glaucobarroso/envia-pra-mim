package org.enviapramim.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by glauco on 21/03/17.
 */
public class PreListing {

    public String sku;
    public String title;
    public List<String> titles;
    public String cost;
    public String suggestedPrice;
    public String thumbnail;
    public String freeShipping;
}
