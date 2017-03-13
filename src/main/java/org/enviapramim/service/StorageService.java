package org.enviapramim.service;

import org.enviapramim.model.Product;
import org.enviapramim.repository.StorageRepository;
import org.enviapramim.repository.UserMlData;

import java.util.List;

/**
 * Created by glauco on 17/02/17.
 */
public class StorageService {

    private StorageRepository storageRepository;

    public StorageService() {
        storageRepository = new StorageRepository();
    }
    public Product storeProduct(Product product) {
        return storageRepository.storeProduct(product);
    }

    public List<Product> queryAllProducts() {
        return storageRepository.queryAllProducts();
    }

    public void deleteProduct(String sku) {
        storageRepository.deleteProduct(sku);
    }

    public void updateProduct(Product product) {
        storageRepository.updateProduct(product);
    }

    public UserMlData addUserMlData(String username, String accessToken, String refreshAccessToken) {
        UserMlData userMlData = new UserMlData();
        userMlData.setUsername(username);
        userMlData.setMlAccessToken(accessToken);
        userMlData.setMlRefreshToken(refreshAccessToken);
        return storageRepository.addUserMlData(userMlData);
    }

    public List<UserMlData> queryAllUserMlData() {
        return storageRepository.queryAllUserMlData();
    }

    public void deleteAllUserInfo() {
        storageRepository.deleteAllUserInfo();
    }
}

