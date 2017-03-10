package org.enviapramim.controller;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.enviapramim.model.validators.ProductValidator;
import org.enviapramim.repository.StorageRepository;
import org.enviapramim.repository.UserMlData;
import org.enviapramim.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by glauco on 15/02/17.
 */
@Controller
public class WebController {

    @RequestMapping(value = "/queryProduct")
    public String queryProduct(Model model) {
        StorageService storageService = new StorageService();
        List<Product> productList = storageService.queryAllProducts();
        model.addAttribute("products", productList);
        return "queryProducts";
    }

    @RequestMapping(value = "/queryUserMlData")
    public String queryUserMlData(Model model) {
        StorageService storageService = new StorageService();
        List<UserMlData> userMlDataList = storageService.queryAllUserMlData();
        model.addAttribute("usermldata", userMlDataList);
        return "queryUserMlData";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("product", new Product());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid Product product, BindingResult result, Model model) {
        ProductValidator productValidator = new ProductValidator();
        ValidationError validationError = productValidator.validate(product);

        if (validationError.getCode() == ValidationError.FAIL) {
            model.addAttribute("response", validationError.getMessage());
        } else {
            StorageService repositoryService = new StorageService();
            repositoryService.storeProduct(product);
            model.addAttribute("response", "Produto cadastrado com SUCESSO!!!");
        }
        return "register";
    }
}
