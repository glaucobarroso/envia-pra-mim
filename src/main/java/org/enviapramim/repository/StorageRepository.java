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
        String link = storeImage(product.getImage1(), product.getSku(), "1");
        productStorageModel.setLink1(link);
        byte[] thumbnailBytes = imageTransformer.generateThumbnail(product.getImage1());
        String thumbnailLink = storeImageBytes(thumbnailBytes, createImageName(product.getImage1().getOriginalFilename(),
                                                product.getSku(), "thumb1"));
        productStorageModel.setThumbNailLink(thumbnailLink);
        if (product.getImage2().getOriginalFilename() != null && product.getImage2().getOriginalFilename().length() > 0) {
            link = storeImage(product.getImage2(), product.getSku(), "2");
            productStorageModel.setLink2(link);
        }
        if (product.getImage3().getOriginalFilename() != null && product.getImage3().getOriginalFilename().length() > 0) {
            link = storeImage(product.getImage3(), product.getSku(), "3");
            productStorageModel.setLink3(link);
        }
        if (product.getImage4().getOriginalFilename() != null && product.getImage4().getOriginalFilename().length() > 0) {
            link = storeImage(product.getImage4(), product.getSku(), "4");
            productStorageModel.setLink4(link);
        }
        if (product.getImage5().getOriginalFilename() != null && product.getImage5().getOriginalFilename().length() > 0) {
            link = storeImage(product.getImage5(), product.getSku(), "5");
            productStorageModel.setLink5(link);
        }
        if (product.getImage6().getOriginalFilename() != null && product.getImage6().getOriginalFilename().length() > 0) {
            link = storeImage(product.getImage6(), product.getSku(), "6");
            productStorageModel.setLink6(link);
        }

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
        product.setLink1(productStorageModel.getLink1());
        return product;
    }
}
