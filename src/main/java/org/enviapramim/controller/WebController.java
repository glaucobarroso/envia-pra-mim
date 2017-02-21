package org.enviapramim.controller;

import org.enviapramim.model.Product;
import org.enviapramim.repository.StorageRepository;
import org.enviapramim.service.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("product", new Product());
        return "register";
    }
}
