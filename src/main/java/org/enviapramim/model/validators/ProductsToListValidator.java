package org.enviapramim.model.validators;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.ProductToList;
import org.enviapramim.model.ProductsToList;

/**
 * Created by Glauco on 24/03/2017.
 */
public class ProductsToListValidator extends ProductValidator {

    public static final String CLASSIC = "gold_special";
    public static final String PREMIUM = "gold_pro";
    public static final String GENERIC_ERROR = "error";

    protected static final String PRICE_VALIDATION_FAIL = " O Preço deve ser um número.";

    public ValidationError validate(ProductsToList productsToList) {
        String error = "";
        if (productsToList == null) {
            error += GENERIC_ERROR;
        } else {
            if (productsToList.getProductsToList() == null || productsToList.getProductsToList().size() == 0) {
                error += GENERIC_ERROR;
            } else {
                for (ProductToList productToList : productsToList.getProductsToList()) {
                    error = validate(productToList);
                    if (error.length() > 0) {
                        break;
                    }
                }
            }
        }

        if (error.length() != 0) {
            return new ValidationError(error, ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    public String validate(ProductToList product) {
        String error = "";
        if (product.getSku() == null || !product.getSku().matches("[A-Za-z0-9]+")) {
            error += SKU_VALIDATION_FAIL;
        }
        if (product.getTitle() == null) {
            error += TITLE_VALIDATION_FAIL;
        }
        if (product.price == null || !validatePrice(product)) {
            error += PRICE_VALIDATION_FAIL;
        }
        if (product.type == null || !validateType(product.type)) {
            error += GENERIC_ERROR;
        }
        return error;
    }

    private boolean validatePrice(ProductToList product) {
        product.setPrice(product.getPrice().replaceAll(",", "."));
        return validatePositiveFloat(product.price);
    }

    private boolean validateType(String type) {
        if (!CLASSIC.equals(type) && !PREMIUM.equals(type)) {
            return false;
        }
        return true;
    }
}