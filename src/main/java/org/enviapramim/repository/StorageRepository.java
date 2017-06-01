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

    private static final String BUCKET_NAME = "envia-pra-mim.appspot.com";

    EntityManager entityManager;
    private ImageTransformer imageTransformer;

    public StorageRepository() {
        EntityManagerFactory emf = EntityManagerFactory.getInstance();
        entityManager = emf.createDefaultEntityManager();
        imageTransformer = new ImageTransformer();
    }

    public ProductStorageModel storeProduct(Product product, boolean isUpdate) {
        ProductStorageModel productStorageModel = convertToProductStorage(product);

        List<MultipartFile> images = product.getImages();
        if (images != null && images.size() > 0 && images.get(0).getOriginalFilename() != null &&
                images.get(0).getOriginalFilename().length() > 0) {
            // generating thumbnail for the main image
            MultipartFile mainImage = images.get(0);
            byte[] thumbnailBytes = imageTransformer.generateThumbnail(mainImage);
            String thumbnailLink = storeImageBytes(thumbnailBytes, createImageName(mainImage.getOriginalFilename(),
                    product.getSku(), "thumb1"));
            productStorageModel.setThumbNailLink(thumbnailLink);

            ArrayList<String> linksList = new ArrayList<String>();
            for (int i = 0; i < images.size(); i++) {
                String link = storeImage(images.get(i), product.getSku(), Integer.toString(i));
                linksList.add(link);
            }
            productStorageModel.setLinks(linksList);
        }
        ProductStorageModel retProd = null;

        if (isUpdate) {
            retProd = entityManager.update(productStorageModel);
        } else {
            try {
                retProd = entityManager.insert(productStorageModel);
            } catch (Exception e) {
                e.printStackTrace();
                // SKU already exists, just update
                retProd = entityManager.update(productStorageModel);
            }
        }
        return retProd;
    }

    public void deleteProduct(String sku) {
        entityManager.delete(ProductStorageModel.class, sku);
    }

    public void updateProduct(Product product) {
        if (!product.getOldsku().equals(product.getSku())) {
            // SKU changed, needs to delete and add a new product
            deleteProduct(product.getOldsku());
        }
        storeProduct(product, true);
    }

    private String storeImage(MultipartFile image1, String sku, String number) {
        try {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            String fileExt = imageTransformer.getImageExt(image1.getOriginalFilename());
            String contentType = "image/" + fileExt;
            List<Acl> acls = new ArrayList<>();
            acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            Blob blob = storage.create(BlobInfo.newBuilder(BUCKET_NAME, createImageName(image1.getOriginalFilename(),
                                sku, number)).setAcl(acls).setContentType(contentType).build(),
                                image1.getInputStream());
            return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + sku + number + "." + fileExt;
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
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + imageName;
    }

    private String createImageName(String originalName, String sku, String number) {
        String fileExt = imageTransformer.getImageExt(originalName);
        return sku + number + "." + fileExt;
    }

    public List<ProductStorageModel> queryAllProducts() {
        EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM storage");
        QueryResponse<ProductStorageModel> response = entityManager.executeEntityQueryRequest(ProductStorageModel.class, request);
        List<ProductStorageModel> productStorageList = response.getResults();
        return productStorageList;
    }

    public ProductStorageModel queryBySKU(String sku) {
        ProductStorageModel productStorage = entityManager.load(ProductStorageModel.class, sku);
        return productStorage;
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

    public List<ListedItem> queryAllListedItems() {
        EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM listed");
        QueryResponse<ListedItem> response = entityManager.executeEntityQueryRequest(ListedItem.class, request);
        return response.getResults();
    }

    public void deleteAllListedItems() {
        long a = entityManager.deleteAll(ListedItem.class);
    }

    public void addListedItems(List<ListedItem> listedItems) {
        entityManager.insert(listedItems);
    }

    private ProductStorageModel convertToProductStorage(Product product) {
        ProductStorageModel productStorageModel = new ProductStorageModel();
        productStorageModel.setSku(product.getSku());
        productStorageModel.setCost(product.getCost());
        productStorageModel.setDescription(product.getDescription());
        productStorageModel.setTitle(product.getTitle());
        productStorageModel.setTitles(product.getTitles());
        productStorageModel.setQuantity(product.getQuantity());
        productStorageModel.setCategory(product.getCategory());
        return productStorageModel;
    }

}
