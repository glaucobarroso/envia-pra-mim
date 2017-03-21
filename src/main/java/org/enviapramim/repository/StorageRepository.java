package org.enviapramim.repository;


import com.google.cloud.storage.*;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;
import org.enviapramim.Utils.images.ImageTransformer;
import org.enviapramim.model.Product;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by glauco on 17/02/17.
 */
public class StorageRepository {

    private static final String BUCKET_NAME = "x-pulsar-158711.appspot.com";

    EntityManager entityManager;
    private ImageTransformer imageTransformer;

    public StorageRepository() {
        EntityManagerFactory emf = EntityManagerFactory.getInstance();
        entityManager = emf.createDefaultEntityManager();
        imageTransformer = new ImageTransformer();
    }

    public Product storeProduct(Product product) {
        ProductStorageModel productStorageModel = convertToProductStorage(product);

        // generating thumbnail for the main image
        MultipartFile mainImage = product.getImages().get(0);
        byte[] thumbnailBytes = imageTransformer.generateThumbnail(mainImage);
        String thumbnailLink = storeImageBytes(thumbnailBytes, createImageName(mainImage.getOriginalFilename(),
                product.getSku(), "thumb1"));
        productStorageModel.setThumbNailLink(thumbnailLink);

        ArrayList<String> linksList = new ArrayList<String>();
        for (int i = 0; i < product.getImages().size(); i++) {
            String link = storeImage(product.getImages().get(i), product.getSku(), Integer.toString(i));
            linksList.add(link);
        }
        productStorageModel.setLinks(linksList);

        ProductStorageModel retProd = entityManager.insert(productStorageModel);
        return convertFromProductStorage(retProd);
    }

    public void deleteProduct(String sku) {
        entityManager.delete(ProductStorageModel.class, sku);
    }

    public void updateProduct(Product product) {
        ProductStorageModel productStorageModel = entityManager.load(ProductStorageModel.class, product.getSku());
        productStorageModel.setTitle(product.getTitle());
        productStorageModel.setCost(product.getCost());
        productStorageModel.setDescription(product.getDescription());
        productStorageModel.setQuantity(product.getQuantity());
        entityManager.update(productStorageModel);
    }

    private String storeImage(MultipartFile image1, String sku, String number) {
        try {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            List<Acl> acls = new ArrayList<>();
            acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, createImageName(image1.getOriginalFilename(),
                                sku, number)).setAcl(acls).build(),
                                image1.getInputStream());

            return blob.getMediaLink();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String storeImageBytes(byte[] image, String imageName) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, imageName).setAcl(acls).build(), image);
        return blob.getMediaLink();
    }

    private String createImageName(String originalName, String sku, String number) {
        String fileExt = imageTransformer.getImageExt(originalName);
        return sku + number + "." + fileExt;
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

    public Product queryBySKU(String sku) {
        ProductStorageModel productStorage = entityManager.load(ProductStorageModel.class, sku);
        return convertFromProductStorage(productStorage);
    }

    public UserMlData queryUserMl(String username) {
        UserMlData userMlData = entityManager.load(UserMlData.class, username);
        return userMlData;
    }

    public UserMlData addUserMlData(UserMlData userMlData) {
        UserMlData currUserMlData = queryUserMl(userMlData.getUsername());
        if (currUserMlData == null) {
            return entityManager.insert(userMlData);
        } else {
            return entityManager.update(userMlData);
        }
    }

    public List<UserMlData> queryAllUserMlData() {
        EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM user");
        QueryResponse<UserMlData> response = entityManager.executeEntityQueryRequest(UserMlData.class, request);
        return response.getResults();
    }

    public void deleteAllUserInfo() {
        entityManager.deleteAll(UserMlData.class);
    }

    private ProductStorageModel convertToProductStorage(Product product) {
        ProductStorageModel productStorageModel = new ProductStorageModel();
        productStorageModel.setSku(product.getSku());
        productStorageModel.setCost(product.getCost());
        productStorageModel.setDescription(product.getDescription());
        productStorageModel.setTitle(product.getTitle());
        productStorageModel.setTitles(product.getTitles());
        productStorageModel.setQuantity(product.getQuantity());
        return productStorageModel;
    }

    private Product convertFromProductStorage(ProductStorageModel productStorageModel) {
        Product product = new Product();
        product.setSku(productStorageModel.getSku());
        product.setCost(productStorageModel.getCost());
        product.setDescription(productStorageModel.getDescription());
        product.setTitle(productStorageModel.getTitle());
        product.setTitles(productStorageModel.getTitles());
        product.setQuantity(productStorageModel.getQuantity());
        product.setThumbnail(productStorageModel.getThumbNailLink());
        product.setLinks(productStorageModel.getLinks());
        return product;
    }
}
