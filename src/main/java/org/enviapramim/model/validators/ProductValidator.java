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

    public static final String SKU_VALIDATION_FAIL = " SKU incorreto, utilize somente caracteres alfanuméricos.";
    public static final String TITLE_VALIDATION_FAIL = " Títulos não podem ser vazios.";
    public static final String TITLE_VALIDATION_FAIL_FORMAT_STR = " O Título %s é inválido.";
    public static final String COST_VALIDATION_FAIL = " O Custo deve ser um número.";
    public static final String QUANTITY_VALIDATION_FAIL = " A quantidade deve ser um número inteiro.";
    public static final String DESCRIPTION_VALIDATION_FAIL = " A descrição não pode ser vazia.";
    public static final String EMPTY_MANDATORY_IMAGE_MSG = " A Imagem Principal não pode ser vazia.";
    public static final String EMPTY_IMAGE_UPDATE_MSG = " Você deve enviar novas imagens quando modifica o SKU.";
    public static final String IMAGE_VALIDATION_FAIL_FORMAT_STR = " O arquivo da Imagem %s não é uma imagem válida.";
    public static final String CATEGORY_VALIDATION_FAIL = " A categoria é inválida.";
    public static final String PRODUCT_VALIDATION_FAIL = "ERRO";
    public static final String PRODUCT_VALIDATION_SUCCESS = "SUCESSO";

    public static final int MANDATORY_IMAGE_VALIDATION_OK = 0;
    public static final int EMPTY_MANDATORY_IMAGE = 1;
    public static final int INVALID_MANDATORY_IMAGE = 2;

    public static final int TITLE_MAX_SIZE = 60;
    public static final int DESCRIPTION_MAX_SIZE = 1000;

    public ValidationError validate(Product product) {
        StringBuilder error = new StringBuilder("");
        if (product == null) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }
        if (product.getOldsku() != null && product.getOldsku().length() > 0) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }

        error.append(validateCommonFields(product));
        error.append(validateImageList(product.getImages(), EMPTY_MANDATORY_IMAGE_MSG));

        if (error.length() != 0) {
            return new ValidationError(error.toString(), ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    public ValidationError validateUpdate(Product product) {
        StringBuilder error = new StringBuilder("");
        if (product == null) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }
        if (product.getOldsku() == null || !product.getOldsku().matches("[A-Za-z0-9]+")) {
            return new ValidationError(PRODUCT_VALIDATION_FAIL, ValidationError.FAIL);
        }

        error.append(validateCommonFields(product));

        if (product.getSku() != null && !product.getSku().equals(product.getOldsku())) {
            // if SKU changed new images must be uploaded
            error.append(validateImageList(product.getImages(), EMPTY_IMAGE_UPDATE_MSG));
        } else if (!isImageListEmpty(product.getImages())) {
            // if SKU did not change, but images list is not empty new images must be validated
            error.append(validateImageList(product.getImages(), EMPTY_IMAGE_UPDATE_MSG));
        }
        if(product.getCategory() == null || !validateCategory(product.getCategory())) {
            error.append(CATEGORY_VALIDATION_FAIL);
        }

        if (error.length() != 0) {
            return new ValidationError(error.toString(), ValidationError.FAIL);
        }
        return new ValidationError(PRODUCT_VALIDATION_SUCCESS, ValidationError.SUCCESS);
    }

    private String validateCommonFields(Product product) {
        StringBuilder error = new StringBuilder("");
        if (product.getSku() == null || !product.getSku().matches("[A-Za-z0-9]+")) {
            error.append(SKU_VALIDATION_FAIL);
        }
        if (!validateTitle(product.getTitle())) {
            error.append(String.format(TITLE_VALIDATION_FAIL_FORMAT_STR, product.getTitle()));
        }
        if (product.getMlTitles() == null) {
            error.append(TITLE_VALIDATION_FAIL);
        } else {
            error.append(validateTitleList(product.getMlTitles()));
        }
        if (product.getCost() == null || !validateCost(product)) {
            error.append(COST_VALIDATION_FAIL);
        }
        if (product.getQuantity() == null || !validateQuantity(product.getQuantity())) {
            error.append(QUANTITY_VALIDATION_FAIL);
        }
        if (product.getDescription() == null || !validateDescription(product.getDescription())) {
            error.append(DESCRIPTION_VALIDATION_FAIL);
        }
        if(product.getCategory() == null || !validateCategory(product.getCategory())) {
            error.append(CATEGORY_VALIDATION_FAIL);
        }
        return error.toString();
    }

    protected boolean validateCost(Product product) {
        product.setCost(product.getCost().replaceAll(",", "."));
        return validatePositiveFloat(product.getCost());
    }

    protected boolean validatePositiveFloat(String number) {
        Float numberF = parseFloat(number);
        if (numberF != null) {
            if (numberF >= 0) {
                return true;
            }
        }
        return false;
    }

    protected Float parseFloat(String number) {
        Float ret = null;
        try {
            ret = Float.parseFloat(number);
        } catch (Exception e) {
            return ret;
        }
        return ret;
    }

    protected boolean validateQuantity(String quantity) {
        try {
            Integer ret = Integer.parseInt(quantity);
            if (ret < 0) {
                return false;
            }
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
        return categories.contains(category);
    }

    private String validateTitleList(List<String> titles) {
        String ret = "";
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i) == null || titles.get(i).length() == 0) {
                titles.remove(i);
                i--;
            } else {
                if (!validateTitle(titles.get(i))) {
                    ret += String.format(TITLE_VALIDATION_FAIL_FORMAT_STR, titles.get(i));
                    break;
                }
            }
        }
        if (titles.size() == 0) {
            ret += TITLE_VALIDATION_FAIL;
        }
        return ret;
    }

    private boolean validateTitle(String title) {
        if (title == null) {
            return false;
        }
        if (title.length() > TITLE_MAX_SIZE) {
            return false;
        }
        return title.matches("^(?U)[\\p{Alpha}\\-' [0-9]]+");
    }

    private boolean validateDescription(String description) {
        if (description == null) {
            return false;
        }
        if (description.length() > DESCRIPTION_MAX_SIZE) {
            return false;
        }
        return true;
    }
}
