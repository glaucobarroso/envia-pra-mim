package org.enviapramim.model.ml;

import java.util.List;

/**
 * Created by Glauco on 15/03/2017.
 */
public class Shipping {

    public String mode;
    public Boolean localPickUp;
    public Boolean free_shipping;
    public Object methods;
    public List<FreeShipping> free_methods;
    public Object dimensions;
    public List<Object> tags;
    public String logistic_type;
}
