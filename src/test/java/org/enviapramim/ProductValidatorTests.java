package org.enviapramim;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.enviapramim.model.validators.ProductValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by glauco on 21/02/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductValidatorTests {

    @Test
    public void testValidProduct() throws Exception {
        Product product = createValidProduct();
        ProductValidator productValidator = new ProductValidator();
        ValidationError validationError = productValidator.validate(product);
        Assert.assertEquals("Wrong ERROR CODE", ValidationError.SUCCESS, validationError.getCode());
        Assert.assertEquals("Wrong ERROR MESSAGE", ProductValidator.PRODUCT_VALIDATION_SUCCESS);
    }

    public MultipartFile createValidMultipartFile() {
        return null;
    }

    public MultipartFile createInvalidMultipartFile() {
        return null;
    }

    public Product createValidProduct() {
        Product product = new Product();
        product.setSku("AAA09390");
        product.setCost("11,90");
        product.setQuantity("10");
        product.setDescription("Esta é uma descrição");
        product.setTitle("Título do produto");
        // TODO create valid files
        return product;
    }
}
