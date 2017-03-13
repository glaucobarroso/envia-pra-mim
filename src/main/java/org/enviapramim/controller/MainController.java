package org.enviapramim.controller;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.enviapramim.model.validators.ProductValidator;
import org.enviapramim.repository.UserMlData;
import org.enviapramim.service.MercadoLibreService;
import org.enviapramim.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by glauco on 16/02/17.
 */
@Controller
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class MainController {

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseEntity delete(@RequestParam("sku") String sku) {
        StorageService storageService = new StorageService();
        storageService.deleteProduct(sku);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity("SUCCESS", httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity update(Product product) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);

        StorageService storageService = new StorageService();
        ProductValidator productValidator = new ProductValidator();
        ValidationError validationError = productValidator.validateSimpleUpdate(product);
        if (validationError.getCode() == ValidationError.FAIL) {
            return new ResponseEntity(validationError.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
        }
        storageService.updateProduct(product);
        return new ResponseEntity("SUCCESS", httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/loginml", method = RequestMethod.GET)
    public String loginMl() {
        MercadoLibreService mercadoLibreService = new MercadoLibreService();
        return "redirect:" + mercadoLibreService.getRedirectUrl();
    }

    @RequestMapping(value = "/mlauth", method = RequestMethod.GET)
    public String mlauthcode(@RequestParam(value = "code", required = false) String code) {
        if (code != null && code.length() > 0) {
            MercadoLibreService mercadoLibreService = new MercadoLibreService();
            mercadoLibreService.authenticate(code);
        }
        return "mlLoginCallback";
    }

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

    @GetMapping("/deleteAllUsers")
    public ResponseEntity deleteAlUsers() {
        StorageService storageService = new StorageService();
        storageService.deleteAllUserInfo();
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity("SUCCESS", httpHeaders, HttpStatus.OK);
    }
}
