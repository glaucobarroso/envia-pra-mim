package org.enviapramim.Utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by glauco on 21/02/17.
 */
public class ImageValidator {

    public boolean validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if(contentType.startsWith("image")) {
            return true;
        } else {
            return false;
        }
    }

}
