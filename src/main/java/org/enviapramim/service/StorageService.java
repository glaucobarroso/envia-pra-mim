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
        ProductStorageModel productStorageModel = convertToProductStorage(product);
        ProductStorageModel ret = storageRepository.storeProduct(productStorageModel);
        return convertFromProductStorage(ret);
    }

    public List<Product> queryAllProducts() {
        StorageRepository repository = new StorageRepository();
        List<ProductStorageModel> productStorageList = repository.queryAllProducts();
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
        return product;
    }
}
