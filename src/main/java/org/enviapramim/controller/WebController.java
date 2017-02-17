package org.enviapramim.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.enviapramim.model.Product;
import org.enviapramim.repository.StorageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by glauco on 15/02/17.
 */
@Controller
public class WebController {

    @RequestMapping(value = "/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping(value = "/queryProduct")
    public String queryProduct(Model model) {
      StorageRepository repository = new StorageRepository();
        List<Product> productList = repository.queryAllProducts();
        if (productList != null) {
            for (Product product : productList) {
                System.out.println("*******************************");
                System.out.println(product.getSku());
                System.out.println(product.getDescription());
                System.out.println(product.getCost());
                System.out.println(product.getQuantity());
                System.out.println(product.getTitle());
                System.out.println("*******************************");
            }
        }
        model.addAttribute("products", productList);
        return "queryProducts";
    }
}
