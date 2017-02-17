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

    public Product storeProduct(Product product) {
        Product retProd = entityManager.insert(product);
        System.out.println("**aaaaaaaaa*****************************");
        System.out.println(product.getSku());
        System.out.println(product.getDescription());
        System.out.println(product.getCost());
        System.out.println(product.getQuantity());
        System.out.println(product.getTitle());
        System.out.println("*****aaaaaaaaaaaa**************************");
        return retProd;
    }

    public List<Product> queryAllProducts() {
        EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM storage");
        QueryResponse<Product> response = entityManager.executeEntityQueryRequest(Product.class, request);
        return response.getResults();
    }
}
