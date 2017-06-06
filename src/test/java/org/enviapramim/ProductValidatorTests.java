package org.enviapramim;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.enviapramim.model.validators.ProductValidator;
import org.enviapramim.service.MercadoLibreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by glauco on 21/02/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductValidatorTests {

    ProductValidator productValidator;

    @Before
    public void setUp() {
        productValidator = new ProductValidator();
    }

    @Test
    public void testNullProduct() throws Exception {
        assertValidationError(productValidator.validate(null), ValidationError.FAIL,
                ProductValidator.PRODUCT_VALIDATION_FAIL);
    }

    @Test
    public void testEmptyProduct() throws Exception {
        Product product = new Product();
        ValidationError validationError = productValidator.validate(product);
        Assert.assertEquals("Wrong ERROR CODE", ValidationError.FAIL, validationError.getCode());
        String errorMsg = validationError.getMessage();
        Assert.assertNotNull("Null ERROR MESSAGE", errorMsg);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing category fail message", errorMsg
                .contains(ProductValidator.CATEGORY_VALIDATION_FAIL));
        errorMsg = removeSubstring(errorMsg, ProductValidator.CATEGORY_VALIDATION_FAIL);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing cost fail message", errorMsg
                .contains(ProductValidator.COST_VALIDATION_FAIL));
        errorMsg = removeSubstring(errorMsg, ProductValidator.COST_VALIDATION_FAIL);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing description fail message", errorMsg
                .contains(ProductValidator.DESCRIPTION_VALIDATION_FAIL));
        errorMsg = removeSubstring(errorMsg, ProductValidator.DESCRIPTION_VALIDATION_FAIL);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing mandatory image fail message", errorMsg
                .contains(ProductValidator.EMPTY_MANDATORY_IMAGE_MSG));
        errorMsg = removeSubstring(errorMsg, ProductValidator.EMPTY_MANDATORY_IMAGE_MSG);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing quantity fail message", errorMsg
                .contains(ProductValidator.QUANTITY_VALIDATION_FAIL));
        errorMsg = removeSubstring(errorMsg, ProductValidator.QUANTITY_VALIDATION_FAIL);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing description sku message", errorMsg
                .contains(ProductValidator.SKU_VALIDATION_FAIL));
        errorMsg = removeSubstring(errorMsg, ProductValidator.SKU_VALIDATION_FAIL);

        Assert.assertTrue("Wrong ERROR MESSAGE - missing title fail message", errorMsg
                .contains(ProductValidator.TITLE_VALIDATION_FAIL));
        errorMsg = removeSubstring(errorMsg, ProductValidator.TITLE_VALIDATION_FAIL);

        Assert.assertTrue("Wrong ERROR Message - message = " + validationError.getMessage(), errorMsg.length() == 0);
    }

    @Test
    public void testValidProduct() throws Exception {
        Product product = createValidProduct();
        ValidationError validationError = productValidator.validate(product);
        assertValidationError(productValidator.validate(product), ValidationError.SUCCESS,
                ProductValidator.PRODUCT_VALIDATION_SUCCESS);
    }

    @Test
    public void testWrongSKU() throws Exception {
        Product product = createValidProduct();
        product.setSku("$5aaaa!");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.SKU_VALIDATION_FAIL);
    }

    @Test
    public void testWrongCategory() throws Exception {
        Product product = createValidProduct();
        product.setCategory("WRONG");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.CATEGORY_VALIDATION_FAIL);
    }

    @Test
    public void testEmptyImage() throws Exception {
        Product product = createValidProduct();
        List<MultipartFile> images = new ArrayList<MultipartFile>();
        images.add(null);
        product.setImages(images);
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                String.format(ProductValidator.EMPTY_MANDATORY_IMAGE_MSG, "Principal"));
    }

    @Test
    public void testWrongImage1() throws Exception {
        Product product = createValidProduct();
        List<MultipartFile> images = new ArrayList<MultipartFile>();
        images.add(createMockMultipartFile("src/test/resources/text.txt", "text", "text.txt", "text/plain"));
        product.setImages(images);
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                String.format(ProductValidator.IMAGE_VALIDATION_FAIL_FORMAT_STR, "Principal"));
    }

    @Test
    public void testWrongImage2() throws Exception {
        Product product = createValidProduct();
        List<MultipartFile> images = new ArrayList<MultipartFile>();
        images.add(createMockMultipartFile("src/test/resources/image.png", "image", "image.png", "image/png"));
        images.add(createMockMultipartFile("src/test/resources/text.txt", "text", "text.txt", "text/plain"));
        product.setImages(images);
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                String.format(ProductValidator.IMAGE_VALIDATION_FAIL_FORMAT_STR, "2"));
    }

    @Test
    public void testValidCostWithComma() throws Exception {
        Product product = createValidProduct();
        product.setCost("1,99");
        assertValidationError(productValidator.validate(product), ValidationError.SUCCESS,
                ProductValidator.PRODUCT_VALIDATION_SUCCESS);
    }

    @Test
    public void testWrongCost1() throws Exception {
        Product product = createValidProduct();
        product.setCost("-1");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.COST_VALIDATION_FAIL);
    }

    @Test
    public void testWrongCost2() throws Exception {
        Product product = createValidProduct();
        product.setCost("aaaaa");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.COST_VALIDATION_FAIL);
    }

    @Test
    public void testWrongDescription() throws Exception {
        Product product = createValidProduct();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < ProductValidator.DESCRIPTION_MAX_SIZE + 1; i++) {
            builder.append("a");
        }
        product.setDescription(builder.toString());
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.DESCRIPTION_VALIDATION_FAIL);
    }

    @Test
    public void testWrongQuantity1() throws Exception {
        Product product = createValidProduct();
        product.setQuantity("-1");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.QUANTITY_VALIDATION_FAIL);
    }

    @Test
    public void testWrongQuantity2() throws Exception {
        Product product = createValidProduct();
        product.setQuantity("1.11");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.QUANTITY_VALIDATION_FAIL);
    }

    @Test
    public void testWrongQuantity3() throws Exception {
        Product product = createValidProduct();
        product.setQuantity("aaaa");
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.QUANTITY_VALIDATION_FAIL);
    }

    @Test
    public void testWrongTitle1() throws Exception {
        Product product = createValidProduct();
        String title = "aaaaa bbbb $#";
        product.setTitle(title);
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                String.format(ProductValidator.TITLE_VALIDATION_FAIL_FORMAT_STR, title));
    }

    @Test
    public void testWrongTitle2() throws Exception {
        Product product = createValidProduct();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < ProductValidator.TITLE_MAX_SIZE + 1; i++) {
            builder.append("a");
        }
        String title = builder.toString();
        product.setTitle(title);
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                String.format(ProductValidator.TITLE_VALIDATION_FAIL_FORMAT_STR, title));
    }

    @Test
    public void testWrongMlTitles1() throws Exception {
        Product product = createValidProduct();
        List<String> titles = new ArrayList<String>();
        titles.add(null);
        product.setMlTitles(titles);
        assertValidationError(productValidator.validate(product), ValidationError.FAIL,
                ProductValidator.TITLE_VALIDATION_FAIL);
    }

    @Test
    public void testMisc() throws Exception {
        Product product = new Product();
        List<String> titles = new ArrayList<String>();
        String[] test = {"Jean-Marie Le'Blanc\n", "Żółć", "Ὀδυσσεύς", null, "原田雅彦", ""};
        String description = "Jean-Marie Le'Blanc" + "Żółć" + "Ὀδυσσεύς\t.:\r;!?" + "原田雅彦" + "";
        for (String str : test) {
            titles.add(str);
        }
        product.setMlTitles(titles);
        ProductValidator productValidator = new ProductValidator();
        //boolean error = productValidator.validateDescription(description);
        List<String> newTitles = product.getMlTitles();

    }

    private void assertValidationError(ValidationError validationError, int expectedErrorCode, String expectedErrorMsg) {
        Assert.assertNotNull("Null VALIDATION ERROR Object", validationError);
        Assert.assertNotNull("Null ERROR MESSAGE", validationError.getMessage());
        Assert.assertEquals("Wrong ERROR CODE - errorCode = " + validationError.getCode(), expectedErrorCode,
                validationError.getCode());
        Assert.assertEquals("Wrong ERROR MESSAGE - errorMessage = " + validationError.getMessage(),
                validationError.getMessage(), expectedErrorMsg);
    }

    public MultipartFile createValidMultipartFile() {
        return null;
    }

    public MultipartFile createInvalidMultipartFile() {
        return null;
    }

    private Product createValidProduct() {
        Product product = new Product();
        product.setSku("AAA09390");
        product.setCost("11,90");
        product.setQuantity("10");
        product.setDescription("Esta é uma descrição\ndlkfdlkfd \tdlsklfdkdfk \rldsçlsdksç");
        product.setTitle("Título do produto");
        product.setCategory(MercadoLibreService.CATEGORY_BACKPACK_CAMPING_MALE);
        List<String> titles = new ArrayList<String>();
        titles.add("dodfsfpó fddê lsdlkfsd - ");
        titles.add("aaaaaaaa");
        product.setMlTitles(titles);
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        files.add(createMockMultipartFile("src/test/resources/image.png", "image", "image.png", "image/png"));
        files.add(createMockMultipartFile("src/test/resources/guarani.jpg", "guarani", "guarani.jpg", "image/jpg"));
        product.setImages(files);
        return product;
    }

    public MultipartFile createMockMultipartFile(String pathStr, String name, String originalName, String type) {

        try {
            Path path = Paths.get(pathStr);
            return new MockMultipartFile(name, originalName, type, Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    String removeSubstring(String string, String toRemove) {
        return string.replace(toRemove, "");
    }
}
