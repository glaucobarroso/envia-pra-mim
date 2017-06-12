package org.enviapramim;

import com.google.cloud.storage.*;
import com.jmethods.catatumbo.EntityManager;
import org.enviapramim.Utils.images.ImageTransformer;
import org.enviapramim.model.Product;
import org.enviapramim.repository.ProductStorageModel;
import org.enviapramim.repository.StorageRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import static org.enviapramim.repository.StorageRepository.BUCKET_NAME;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by root on 08/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StorageRepositoryTests {

    @Mock
    EntityManager entityManager;
    @Mock
    ImageTransformer imageTransformer;
    @Mock
    Storage storage;

    private StorageRepository storageRepository;
    private TestUtils testUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        storageRepository = new StorageRepository(entityManager, imageTransformer, storage);
        testUtils = new TestUtils();
    }

    @Test
    public void testStoreImageBytes() {
        when(storage.create(any(BlobInfo.class), any(byte[].class))).thenReturn(null);
        String imageName = "name.jpg";
        String url = storageRepository.storeImageBytes(new byte[1], imageName);
        String expectedUrl = StorageRepository.STORAGE_DOMAIN + BUCKET_NAME + "/" + imageName;
        Assert.assertEquals("testStoreImageBytes failed", expectedUrl, url);
    }

    @Test
    public void testStoreImage() {
        MultipartFile file = testUtils.createMockMultipartFile();
        String sku = "AAAAA";
        String number = "1";
        when(imageTransformer.getImageExt(any(String.class))).thenCallRealMethod();
        when(storage.create(any(BlobInfo.class), any(InputStream.class))).thenReturn(null);
        String expectedUrl = StorageRepository.STORAGE_DOMAIN + StorageRepository.BUCKET_NAME + "/" + sku + number +
                "." + TestUtils.DEFAULT_MOCK_FILE_EXT;
        String url = storageRepository.storeImage(file, sku, number);
        Assert.assertEquals("testStoreImage failed", expectedUrl, url);
    }

    @Test
    public void testStoreProduct() {
        Product product = testUtils.createValidProduct();
        when(imageTransformer.generateThumbnail(any(MultipartFile.class))).thenReturn(new byte[1]);
        when(imageTransformer.getImageExt(any(String.class))).thenCallRealMethod();
        when(storage.create(any(BlobInfo.class), any(byte[].class))).thenReturn(null);
        when(storage.create(any(BlobInfo.class), any(InputStream.class))).thenReturn(null);
        when(entityManager.insert(any(ProductStorageModel.class))).then(new Answer<ProductStorageModel>() {
            @Override
            public ProductStorageModel answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object args[] = invocationOnMock.getArguments();
                return (ProductStorageModel) args[0];
            }
        });

        ProductStorageModel productStorageModel = storageRepository.storeProduct(product, false);

        Assert.assertEquals("SKU is wrong", product.getSku(), productStorageModel.getSku());
        Assert.assertEquals("Title is wrong", product.getTitle(), productStorageModel.getTitle());
        Assert.assertEquals("ML Titles size does not match", product.getMlTitles().size(),
                productStorageModel.getTitles().size());
        for (int i = 0; i < product.getMlTitles().size(); i++) {
            Assert.assertEquals("ML Title is wrong", product.getMlTitles().get(i), productStorageModel.getTitles().get(i));
        }
        Assert.assertEquals("Description is wrong", product.getDescription(), productStorageModel.getDescription());
        Assert.assertEquals("Cost is wrong", product.getCost(), productStorageModel.getCost());
        Assert.assertEquals("Quantity is wrong", product.getQuantity(), productStorageModel.getQuantity());
        Assert.assertEquals("Category is wrong", product.getCategory(), productStorageModel.getCategory());
        Assert.assertEquals("Images size does not match", product.getImages().size(),
                productStorageModel.getLinks().size());
        for (int i = 0; i < product.getImages().size(); i++) {
            String originalName = product.getImages().get(i).getOriginalFilename();
            String fileExt = originalName.substring(originalName.lastIndexOf("."));
            String expectedLink = StorageRepository.STORAGE_DOMAIN + StorageRepository.BUCKET_NAME + "/" +
                    product.getSku() + i + fileExt;
            Assert.assertEquals("Link is wrong", expectedLink, productStorageModel.getLinks().get(i));
        }
        String thumbOriginalFileName = product.getImages().get(0).getOriginalFilename();
        String fileExt = thumbOriginalFileName.substring(thumbOriginalFileName.lastIndexOf("."));
        String expectedTumbLink = StorageRepository.STORAGE_DOMAIN + StorageRepository.BUCKET_NAME + "/" +
                product.getSku() + "thumb1" + fileExt;
        Assert.assertEquals("Link is wrong", expectedTumbLink, productStorageModel.getThumbNailLink());

    }

}

