package org.enviapramim.service;

import org.enviapramim.model.PreListing;
import org.enviapramim.model.Product;
import org.enviapramim.repository.ProductStorageModel;
import org.enviapramim.repository.StorageRepository;
import org.enviapramim.repository.UserMlData;

import java.util.ArrayList;
import java.util.Arrays;
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
        return convertToProduct(storageRepository.storeProduct(product));
    }

    public List<Product> queryAllProducts() {
        List<ProductStorageModel> productStorageList = storageRepository.queryAllProducts();
        List<Product> productList = new ArrayList<Product>();
        for (ProductStorageModel productStorageModel : productStorageList) {
            productList.add(convertToProduct(productStorageModel));
        }
        return productList;
    }

    public List<PreListing> queryProducts(String[] skus) {
        List<String> skusList = Arrays.asList(skus);
        List<ProductStorageModel> productStorageList = storageRepository.queryAllProducts();
        List<PreListing> preListing = new ArrayList<PreListing>();
        for (ProductStorageModel product : productStorageList) {
            if (skusList.contains(product.getSku())) {
                preListing.add(convertToPreListing(product));
            }
        }
        return preListing;
    }

    public Product queryBySKU(String sku) {
        return convertToProduct(storageRepository.queryBySKU(sku));
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

    public UserMlData queryUserMl(String username) {
        return storageRepository.queryUserMl(username);
    }

    public List<UserMlData> queryAllUserMlData() {
        return storageRepository.queryAllUserMlData();
    }

    public void deleteAllUserInfo() {
        storageRepository.deleteAllUserInfo();
    }

    private Product convertToProduct(ProductStorageModel productStorageModel) {
        Product product = new Product();
        product.setSku(productStorageModel.getSku());
        product.setCost(productStorageModel.getCost());
        product.setDescription(productStorageModel.getDescription());
        product.setTitle(productStorageModel.getTitle());
        product.setTitles(productStorageModel.getTitles());
        product.setQuantity(productStorageModel.getQuantity());
        product.setThumbnail(productStorageModel.getThumbNailLink());
        return product;
    }

    private PreListing convertToPreListing(ProductStorageModel productStorageModel) {
        PricingService pricingService = new PricingService();
        PreListing preListingInfo = new PreListing();
        preListingInfo.sku = productStorageModel.getSku();
        preListingInfo.cost = pricingService.formatPrice(productStorageModel.getCost());
        preListingInfo.thumbnail = productStorageModel.getThumbNailLink();
        preListingInfo.titles = productStorageModel.getTitles();
        preListingInfo.title = productStorageModel.getTitle();
        preListingInfo.suggestedPrice = pricingService.getSuggestedPrice(productStorageModel.getCost());
        preListingInfo.freeShipping = pricingService.getFreeShippingPrice("NONE");
        return preListingInfo;
    }
}

