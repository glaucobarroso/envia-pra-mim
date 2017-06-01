package org.enviapramim.model.validators;

import org.enviapramim.Utils.ImageValidator;
import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.enviapramim.service.MercadoLibreService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glauco on 21/02/17.
 */
public class ProductValidator {

    protected static final String SKU_VALIDATION_FAIL = " SKU incorreto, utilize somente caracteres alfanuméricos.";
    protected static final String TITLE_VALIDATION_FAIL = " O Título não pode ser vazio.";
    protected static final String COST_VALIDATION_FAIL = " O Custo deve ser um número.";
    protected static final String QUANTITY_VALIDATION_FAIL = " A quantidade deve ser um número inteiro.";
    protected static final String DESCRIPTION_VALIDATION_FAIL = " A descrição não pode ser vazia.";
    protected static final String EMPTY_MANDATORY_IMAGE_MSG = " A Imagem Principal não pode ser vazia.";
    protected static final String EMPTY_IMAGE_UPDATE_MSG = " Você deve enviar novas imagens quando modifica o SKU.";
    protected static final String IMAGE_VALIDATION_FAIL_FORMAT_STR = " O arquivo da Imagem %s não é uma imagem válida.";
    protected static final String CATEGORY_VALIDATION_FAIL = " A categoria é inválida.";
    protected static final String PRODUCT_VALIDATION_FAIL = "ERRO";
    protected static final String PRODUCT_VALIDATION_SUCCESS = "SUCESSO";

    protected static final int MANDATORY_IMAGE_VALIDATION_OK = 0;
    protected static final int EMPTY_MANDATORY_IMAGE = 1;
    protected static final int INVALID_MANDATORY_IMAGE = 2;

    public ValidationError validate(Product product) {
        String error = "";
        if (product == null) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }
        if (product.getSku() == null || !product.getSku().matches("[A-Za-z0-9]+")) {
            error += SKU_VALIDATION_FAIL;
        }
        if (product.getTitles() == null) {
            error += TITLE_VALIDATION_FAIL;
        } else {
            List<String> titles = product.getTitles();
            for (int i = 0; i < titles.size(); i++) {
                if (titles.get(i) == null || titles.get(i).length() == 0) {
                    titles.remove(i);
                    i--;
                }
            }
            product.setTitles(titles);
            if (product.getTitles().size() == 0) {
                error += TITLE_VALIDATION_FAIL;
            }
        }
        if (product.getCost() == null || !validateCost(product)) {
            error += COST_VALIDATION_FAIL;
        }
        if (product.getQuantity() == null || !validateQuantity(product.getQuantity())) {
            error += QUANTITY_VALIDATION_FAIL;
        }
        if (product.getDescription() == null) {
            error += DESCRIPTION_VALIDATION_FAIL;
        }
        error += validateImageList(product.getImages(), EMPTY_MANDATORY_IMAGE_MSG);

        if(product.getCategory() == null || !validateCategory(product.getCategory())) {
            error += CATEGORY_VALIDATION_FAIL;
        }

        if (error.length() != 0) {
            return new ValidationError(error, ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    public ValidationError validateUpdate(Product product) {
        String error = "";
        if (product == null) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }
        if (product.getOldsku() == null || !product.getOldsku().matches("[A-Za-z0-9]+")) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }
        if (product.getSku() == null || !product.getSku().matches("[A-Za-z0-9]+")) {
            error += SKU_VALIDATION_FAIL;
        }
        if (product.getTitles() == null) {
            error += TITLE_VALIDATION_FAIL;
        } else {
            List<String> titles = product.getTitles();
            for (int i = 0; i < titles.size(); i++) {
                if (titles.get(i) == null || titles.get(i).length() == 0) {
                    titles.remove(i);
                    i--;
                }
            }
            product.setTitles(titles);
            if (product.getTitles().size() == 0) {
                error += TITLE_VALIDATION_FAIL;
            }
        }
        if (product.getCost() == null || !validateCost(product)) {
            error += COST_VALIDATION_FAIL;
        }
        if (product.getQuantity() == null || !validateQuantity(product.getQuantity())) {
            error += QUANTITY_VALIDATION_FAIL;
        }
        if (product.getDescription() == null) {
            error += DESCRIPTION_VALIDATION_FAIL;
        }

        if (product.getSku() != null && !product.getOldsku().equals(product.getSku())) {
            error += validateImageList(product.getImages(), EMPTY_IMAGE_UPDATE_MSG);
        } else if (!isImageListEmpty(product.getImages())) {
            error += validateImageList(product.getImages(), EMPTY_IMAGE_UPDATE_MSG);
        }
        if(product.getCategory() == null || !validateCategory(product.getCategory())) {
            error += CATEGORY_VALIDATION_FAIL;
        }

        if (error.length() != 0) {
            return new ValidationError(error, ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    protected boolean validateCost(Product product) {
        product.setCost(product.getCost().replaceAll(",", "."));
        return validateFloat(product.getCost());
    }

    protected boolean validateFloat(String number) {
        try {
            Float.parseFloat(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected boolean validateQuantity(String quantity) {
        try {
            Integer.parseInt(quantity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected String validateImageList(List<MultipartFile> images, String emptyListMessage) {
        String error = "";
        if (images == null || images.size() == 0) {
            error += emptyListMessage;
        } else {
            boolean mandatoryImage = true;
            for (MultipartFile image : images) {
                if (mandatoryImage) {
                    int errorCode = validateMandatoryImage(image);
                    switch (errorCode) {
                        case INVALID_MANDATORY_IMAGE:
                            error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "Principal");
                            break;
                        case EMPTY_MANDATORY_IMAGE:
                            error += EMPTY_MANDATORY_IMAGE_MSG;
                            break;
                    }
                    mandatoryImage = false;
                } else {
                    if (!validateImage(image)) {
                        error += String.format(IMAGE_VALIDATION_FAIL_FORMAT_STR, "2");
                    }
                }
            }
        }
        return error;
    }

    private boolean validateImage(MultipartFile file) {
        ImageValidator imageValidator = new ImageValidator();
        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().length() == 0) {
            return true;
        }
        return imageValidator.validateImage(file);
    }

    private boolean isImageListEmpty(List<MultipartFile> images) {
        String name = images.get(0).getOriginalFilename();
        return !(images != null && images.get(0) != null && images.get(0).getOriginalFilename() != null && images.get(0).getOriginalFilename().length() > 0);
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

    private boolean validateCategory(String category) {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add(MercadoLibreService.CATEGORY_BACKPACK_SCHOOLL_MALE);
        categories.add(MercadoLibreService.CATEGORY_BAG_LEATHER_MALE);
        categories.add(MercadoLibreService.CATEGORY_BACKPACK_SCHOOL_GIRLS);
        categories.add(MercadoLibreService.CATEGORY_BACKPACK_SCHOOL_BOYS);
        categories.add(MercadoLibreService.CATEGORY_BACKPACK_NOTEBOOK_MALE);
        categories.add(MercadoLibreService.CATEGORY_BACKPACK_CAMPING_MALE);
        categories.add(MercadoLibreService.CATEGORY_BAG_LEATHER_FEMALE);
        categories.add(MercadoLibreService.CATEGORY_TRAVEL_BAG_SET);

        for (String cat : categories) {
            if (cat.equals(category)) {
                return true;
            }
        }
        return false;
    }
}
