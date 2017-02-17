package org.enviapramim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enviapramim.model.Product;
import org.enviapramim.repository.StorageRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by glauco on 16/02/17.
 */
@RestController
public class MainController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody Product product) {
        StorageRepository repository = new StorageRepository();
        repository.storeProduct(product);
        String jsonInString = "{}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(jsonInString, httpHeaders, HttpStatus.OK);
    }

    /*
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public String uploadFiles(@RequestBody Product product, @RequestParam("sku") String[] sku,
                                   @RequestParam("file") MultipartFile[] files) {
        return "OK";
    }*/
}
