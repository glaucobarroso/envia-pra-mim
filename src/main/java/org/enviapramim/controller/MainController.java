package org.enviapramim.controller;

import org.enviapramim.Utils.ValidationError;
import org.enviapramim.model.FiscalData;
import org.enviapramim.model.Product;
import org.enviapramim.model.ProductToList;
import org.enviapramim.model.ProductsToList;
import org.enviapramim.model.ml.ItemResponse;
import org.enviapramim.model.ml.ListingInfo;
import org.enviapramim.model.ml.MlUserInfo;
import org.enviapramim.model.validators.ProductValidator;
import org.enviapramim.model.validators.ProductsToListValidator;
import org.enviapramim.repository.ListedItem;
import org.enviapramim.repository.StorageRepository;
import org.enviapramim.repository.UserMlData;
import org.enviapramim.service.FiscalDataService;
import org.enviapramim.service.MercadoLibreService;
import org.enviapramim.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by glauco on 16/02/17.
 */
@Controller
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class MainController {

    private final String NEEDS_LOGIN = "Entre no sistema para anunciar.";

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity deleteListItems() {
        StorageRepository repository = new StorageRepository();
        repository.deleteAllListedItems();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity("SUCCESS", httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
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
        String url = mercadoLibreService.getRedirectUrl();
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

    @RequestMapping(value = "/listProducts", method = RequestMethod.GET)
    public String listProducts(@RequestParam(value="skus[]") String[] skus, Model model) {
        StorageService storageService = new StorageService();
        model.addAttribute("products", storageService.queryProducts(skus));
        return "preListing";
    }
    

    @RequestMapping(value = "/listProducts2", method = RequestMethod.POST)
    public ResponseEntity listProducts2(@RequestBody ProductsToList productsToList) {

        StorageService storageService = new StorageService();
        MercadoLibreService mercadoLibreService = new MercadoLibreService();
        String respFormat = "{\"message\": \"%s\"}";

        // creating header response
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // get logged user
        UserMlData userMlData = getLoggedUser(storageService);
        if (userMlData == null) { // needs to login again
            SecurityContextHolder.clearContext();
            return new ResponseEntity(String.format(respFormat, NEEDS_LOGIN), httpHeaders, HttpStatus.BAD_REQUEST);
        }

        // validating input
        ProductsToListValidator validator = new ProductsToListValidator();
        ValidationError validationError = validator.validate(productsToList);
        if (validationError.getCode() == ValidationError.FAIL) {
            return new ResponseEntity(String.format(respFormat, validationError.getMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
        }

        List<Product> productDetailsList = storageService.queryAllProducts();
        Product currProductDb = null;

        // list of products listed by this list correlates sku with the offer Id
        List<ListedItem> listedItems = new ArrayList<ListedItem>();

        if (productsToList.getProductsToList() != null && productsToList.getProductsToList().size() > 0) {
            for (ProductToList productWeb : productsToList.getProductsToList()) {
                if (currProductDb == null || !currProductDb.getSku().equals(productWeb.getSku())) {
                    // find correct product details
                    //TODO improve this and do not search sequentially
                    for (Product productDetails : productDetailsList) {
                        if (productDetails.getSku().equals(productWeb.getSku())) {
                            currProductDb = productDetails;
                            break;
                        }
                    }
                }

                if (productWeb.getTitle() != null && productWeb.getTitle().length() > 0 &&
                        productWeb.getPrice() != null && productWeb.getPrice().length() > 0) {
                    ListingInfo info = mercadoLibreService.createListingInfo(currProductDb, productWeb);
                    ItemResponse item = offerProduct(info, mercadoLibreService, userMlData);
                    if (item == null) { // needs to login again
                        SecurityContextHolder.clearContext();
                        return new ResponseEntity(String.format(respFormat, NEEDS_LOGIN), httpHeaders, HttpStatus.BAD_REQUEST);
                    }
                    ListedItem listedItem = mercadoLibreService.createListedItem(item.id, productWeb.sku, userMlData);
                    listedItems.add(listedItem);
                }
            }

        }
        storageService.addListedItems(listedItems);
        return new ResponseEntity(String.format(respFormat, "OK"), httpHeaders, HttpStatus.OK);

    }

    @RequestMapping(value = "/confirmListProducts", method = RequestMethod.GET)
    public ResponseEntity confirmListProducts() {
        StorageService storageService = new StorageService();
        MercadoLibreService mercadoLibreService = new MercadoLibreService();
        String respFormat = "{\"mluser\": \"%s\"}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserMlData userMlData = storageService.queryUserMl(username);
        if (userMlData == null) {
            return new ResponseEntity("{}", httpHeaders, HttpStatus.OK);
        }
        MlUserInfo mlUserInfo = mercadoLibreService.getUserInfo(userMlData.getMlAccessToken());
        return new ResponseEntity(String.format(respFormat, mlUserInfo.email), httpHeaders, HttpStatus.OK);
    }

    private ItemResponse offerProduct(ListingInfo info, MercadoLibreService mercadoLibreService, UserMlData userMlData) {

        ItemResponse item = mercadoLibreService.offerProduct(info, userMlData.getMlAccessToken());
        MlUserInfo mlUserInfo = mercadoLibreService.getUserInfo(userMlData.getMlAccessToken());
        if (item != null) {
            mercadoLibreService.updateHtmlDescription(mercadoLibreService.createHtmlDescription(info), item.id, userMlData.getMlAccessToken());
        }
        return item;

    }

    private UserMlData getLoggedUser(StorageService storageService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return storageService.queryUserMl(username);
    }

    @GetMapping("/fiscalData")
    public String handleFiscalXml(Model model) {
        //model.addAttribute("product", new Product());
        return "fiscalData";
    }

    @PostMapping("/fiscalDataUpload")
    public String handleFileUpload(@RequestParam("fiscalxml") MultipartFile file) {

        try {
            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            FiscalDataService fiscalDataService = new FiscalDataService();
            List<String> nfeXmlList = fiscalDataService.unzip(zipInputStream);
            List<FiscalData> fiscalDataList = fiscalDataService.getFiscalData(nfeXmlList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

}
