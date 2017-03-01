package org.enviapramim.model.validators;

import org.enviapramim.Utils.ImageValidator;
import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by glauco on 21/02/17.
 */
public class ProductValidator {

    public static final String SKU_VALIDATION_FAIL = " SKU incorreto, utilize somente caracteres alfanuméricos.";
    public static final String TITLE_VALIDATION_FAIL = " O Título não pode ser vazio.";
    public static final String COST_VALIDATION_FAIL = " O Custo deve ser um número.";
    public static final String QUANTITY_VALIDATION_FAIL = " A quantidade deve ser um número inteiro.";
    public static final String DESCRIPTION_VALIDATION_FAIL = " A descrição não pode ser vazia.";
    public static final String EMPTY_MANDATORY_IMAGE_MSG = " A Imagem Principal não pode ser vazia.";
    public static final String IMAGE_VALIDATION_FAIL_FORMAT_STR = " O arquivo da Imagem %s não é uma imagem válida.";
    public static final String PRODUCT_VALIDATION_SUCCESS = "SUCCESS";

    private static final int MANDATORY_IMAGE_VALIDATION_OK = 0;
    private static final int EMPTY_MANDATORY_IMAGE = 1;
    private static final int INVALID_MANDATORY_IMAGE = 2;

    public ValidationError validate(Product product) {
        String error = "";
        if (product.getSku() == null || !product.getSku().matches("[A-Za-z0-9]+")) {
            error += SKU_VALIDATION_FAIL;
        }
        if (product.getTitle() == null) {
            error += TITLE_VALIDATION_FAIL;
        }
        if (product.getCost() == null || !validateCost(product.getCost())) {
            error += COST_VALIDATION_FAIL;
        }
        if (product.getQuantity() == null || !validateQuantity(product.getQuantity())) {
            error += QUANTITY_VALIDATION_FAIL;
        }
        if (product.getDescription() == null) {
            error += DESCRIPTION_VALIDATION_FAIL;
        }
        int errorCode = validateMandatoryImage(product.getImage1());
        switch (errorCode) {
            case INVALID_MANDATORY_IMAGE:
                error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "Principal");
                break;
            case EMPTY_MANDATORY_IMAGE:
                error += EMPTY_MANDATORY_IMAGE_MSG;
                break;
        }
        if (!validateImage(product.getImage2())) {
            error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "2");
        }
        if (!validateImage(product.getImage3())) {
            error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "3");
        }
        if (!validateImage(product.getImage4())) {
            error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "4");
        }
        if (!validateImage(product.getImage5())) {
            error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "5");
        }
        if (!validateImage(product.getImage6())) {
            error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "6");
        }

        if (error.length() != 0) {
            return new ValidationError(error, ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    public ValidationError validateSimpleUpdate(Product product) {
        String error = "";
        if (product.getSku() == null || !product.getSku().matches("[A-Za-z0-9]+")) {
            error += SKU_VALIDATION_FAIL;
        }
        if (product.getTitle() == null) {
            error += TITLE_VALIDATION_FAIL;
        }
        if (product.getCost() == null || !validateCost(product.getCost())) {
            error += COST_VALIDATION_FAIL;
        }
        if (product.getQuantity() == null || !validateQuantity(product.getQuantity())) {
            error += QUANTITY_VALIDATION_FAIL;
        }
        if (product.getDescription() == null) {
            error += DESCRIPTION_VALIDATION_FAIL;
        }

        if (error.length() != 0) {
            return new ValidationError(error, ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    private boolean validateCost(String cost) {
        try {
            Float.parseFloat(cost);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean validateQuantity(String quantity) {
        try {
            Integer.parseInt(quantity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean validateImage(MultipartFile file) {
        ImageValidator imageValidator = new ImageValidator();
        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().length() == 0) {
            return true;
        }
        return imageValidator.validateImage(file);
    }

    private int validateMandatoryImage(MultipartFile file) {
        ImageValidator imageValidator = new ImageValidator();
        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().length() == 0) {
            return EMPTY_MANDATORY_IMAGE;
        }
        if (!imageValidator.validateImage(file)) {
            return INVALID_MANDATORY_IMAGE;
        }
        return MANDATORY_IMAGE_VALIDATION_OK;
    }
}
