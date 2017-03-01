package org.enviapramim.service;

import org.enviapramim.model.Product;
import org.enviapramim.repository.ProductStorageModel;
import org.enviapramim.repository.StorageRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glauco on 17/02/17.
 */
public class StorageService {

    public Product storeProduct(Product product) {
        StorageRepository storageRepository = new StorageRepository();
        return storageRepository.storeProduct(product);
    }

    public List<Product> queryAllProducts() {
        StorageRepository repository = new StorageRepository();
        return repository.queryAllProducts();
    }

    public void deleteProduct(String sku) {
        StorageRepository repository = new StorageRepository();
        repository.deleteProduct(sku);
    }
}
