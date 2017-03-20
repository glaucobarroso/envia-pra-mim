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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by glauco on 16/02/17.
 */
@Controller
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

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
    public String mlauthcode(@RequestParam(value = "code", required = false) String code, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (code != null && code.length() > 0) {
            MercadoLibreService mercadoLibreService = new MercadoLibreService();
            mercadoLibreService.authenticate(code, username);
        }
        model.addAttribute("name", "Login efetuado com sucesso!");
        return "simpleCallback";
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

    @GetMapping("/offerProduct")
    public String listProduct(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        StorageService storageService = new StorageService();
        MercadoLibreService mercadoLibreService = new MercadoLibreService();
        Product product = storageService.queryBySKU("teste");
        UserMlData userMlData = storageService.queryUserMl(username);
        if (userMlData == null) {
            return "redirect:/loginml";
        }
        if (mercadoLibreService.offerProduct(product, userMlData.getMlAccessToken()) == HttpStatus.OK.value()) {
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }
        model.addAttribute("name", "Produto anunciado com sucesso!");
        return "simpleCallback";
    }

    @RequestMapping(value = "/listProducts", method = RequestMethod.GET)
    public String listProducts(@RequestParam(value="skus[]") String[] skus, Model model) {
        if (skus != null) {
            for (String sku : skus) {
                System.out.println(sku);
            }
        }
        model.addAttribute("name", "It is working!");
        return "simpleCallback";
    }

}
