package org.enviapramim;

import org.enviapramim.model.Product;
import org.enviapramim.service.MercadoLibreService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12/06/17.
 */
public class TestUtils {

    public static final String DEFAULT_MOCK_FILE_PATH = "src/test/resources/image.png";
    public static final String DEFAULT_MOCK_FILE_TYPE = "image/png";
    public static final String DEFAULT_MOCK_FILE_NAME = "image";
    public static final String DEFAULT_MOCK_FILE_ORIGINALNAME = "image.png";
    public static final String DEFAULT_MOCK_FILE_EXT = "png";

    public Product createValidProduct() {
        Product product = new Product();
        product.setSku("AAA09390");
        product.setCost("11,90");
        product.setQuantity("10");
        product.setDescription("Esta é uma descrição\ndlkfdlkfd \tdlsklfdkdfk \rldsçlsdksç!?:;#&*");
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

    public MultipartFile createMockMultipartFile() {
        return createMockMultipartFile(DEFAULT_MOCK_FILE_PATH, DEFAULT_MOCK_FILE_NAME, DEFAULT_MOCK_FILE_ORIGINALNAME,
                DEFAULT_MOCK_FILE_TYPE);
    }
}
