package org.enviapramim.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.enviapramim.model.RegisterData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by glauco on 16/02/17.
 */
@RestController
public class MainController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegisterData form) {
        String a = form.toString();
        System.out.println("*********");
        System.out.println(a);
        System.out.println("************");
        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = "{}";
       /* try {
            jsonInString = mapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(jsonInString, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public void register() {
        System.out.println("**************************");
        System.out.println("najhbjbsjlfndjkdlsançkm");
        System.out.println("**************************");
    }
}
