package org.enviapramim.controller;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.Product;
import org.enviapramim.model.validators.ProductValidator;
import org.enviapramim.repository.StorageRepository;
import org.enviapramim.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

/**
 * Created by glauco on 16/02/17.
 */
@Controller
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class MainController {

    @PostMapping("/register")
    public ResponseEntity register(@Valid Product product, BindingResult result) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ProductValidator productValidator = new ProductValidator();
        ValidationError validationError = productValidator.validate(product);
        if (validationError.getCode() == ValidationError.FAIL) {
            httpHeaders.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(validationError.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
        }
        StorageService repositoryService = new StorageService();
        repositoryService.storeProduct(product);
        String jsonInString = "{}";
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(jsonInString, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity("SUCCESS", httpHeaders, HttpStatus.OK);
    }

}
