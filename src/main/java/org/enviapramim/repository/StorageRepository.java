package org.enviapramim.repository;

import com.google.appengine.tools.cloudstorage.*;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;
import org.enviapramim.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by glauco on 17/02/17.
 */
public class StorageRepository {

    public static final String PROJECT_ID = "";
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final String BUCKET_NAME = "x-pulsar-158711.appspot.com";

    EntityManager entityManager;
    private final GcsService gcsService  = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(30000)
            .build());

    public StorageRepository() {
        EntityManagerFactory emf = EntityManagerFactory.getInstance();
        entityManager = emf.createDefaultEntityManager();
    }

    public Product storeProduct(Product product) {
       // storeImage(product.getImage1(), product.getSku(), "1");
        ProductStorageModel productStorageModel = convertToProductStorage(product);
        ProductStorageModel retProd = entityManager.insert(productStorageModel);
        return convertFromProductStorage(retProd);
    }

    public void deleteProduct(String sku) {
        entityManager.delete(ProductStorageModel.class, sku);
    }

    private void storeImage(MultipartFile image1, String sku, String number) {
        try {
            //Set Option for that file
            GcsFileOptions options = new GcsFileOptions.Builder()
                    .mimeType("image")
                    .acl("public-read")
                    .build();
            GcsFilename fileName = new GcsFilename(BUCKET_NAME, sku + number);
            GcsOutputChannel outputChannel;
            GcsService gcsService1 = GcsServiceFactory.createGcsService();
            outputChannel = gcsService1.createOrReplace(fileName, options);
            copy(image1.getInputStream(), Channels.newOutputStream(outputChannel));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> queryAllProducts() {
        EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM storage");
        QueryResponse<ProductStorageModel> response = entityManager.executeEntityQueryRequest(ProductStorageModel.class, request);
        List<ProductStorageModel> productStorageList = response.getResults();
        List<Product> productList = new ArrayList<Product>();
        for (ProductStorageModel productStorageModel : productStorageList) {
            productList.add(convertFromProductStorage(productStorageModel));
        }
        return productList;
    }

    /**
     * Transfer the data from the inputStream to the outputStream. Then close both streams.
     */
    private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    private ProductStorageModel convertToProductStorage(Product product) {
        ProductStorageModel productStorageModel = new ProductStorageModel();
        productStorageModel.setSku(product.getSku());
        productStorageModel.setCost(product.getCost());
        productStorageModel.setDescription(product.getDescription());
        productStorageModel.setTitle(product.getTitle());
        productStorageModel.setQuantity(product.getQuantity());
        return productStorageModel;
    }

    private Product convertFromProductStorage(ProductStorageModel productStorageModel) {
        Product product = new Product();
        product.setSku(productStorageModel.getSku());
        product.setCost(productStorageModel.getCost());
        product.setDescription(productStorageModel.getDescription());
        product.setTitle(productStorageModel.getTitle());
        product.setQuantity(productStorageModel.getQuantity());
        return product;
    }
}
