package org.enviapramim.repository;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;
import org.enviapramim.model.Product;

import java.util.List;

/**
 * Created by glauco on 17/02/17.
 */
public class StorageRepository {

    public static final String PROJECT_ID = "";

    EntityManager entityManager;

    public StorageRepository() {
        EntityManagerFactory emf = EntityManagerFactory.getInstance();
        entityManager = emf.createDefaultEntityManager();
    }

    public ProductStorageModel storeProduct(ProductStorageModel product) {
        ProductStorageModel retProd = entityManager.insert(product);
        return retProd;
    }

    public List<ProductStorageModel> queryAllProducts() {
        EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM storage");
        QueryResponse<ProductStorageModel> response = entityManager.executeEntityQueryRequest(ProductStorageModel.class, request);
        return response.getResults();
    }

}
