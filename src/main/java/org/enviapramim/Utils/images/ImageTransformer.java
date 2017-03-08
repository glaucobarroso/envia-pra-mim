package org.enviapramim.Utils.images;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by glauco on 08/03/17.
 */
public class ImageTransformer {

    public static final int THUMBNAIL_MAX_SIDE = 70;
    private static final ImageObserver DUMMY_OBSERVER = (img, infoflags, x, y, width, height) -> true;

    public byte[] generateThumbnail(MultipartFile uploadedImage) {
        try {
            String imageExt = getImageExt(uploadedImage.getOriginalFilename());
            BufferedImage image = ImageIO.read(uploadedImage.getInputStream());
            double scale;
            if (image.getWidth() >= image.getHeight()) {
                scale = Math.min(THUMBNAIL_MAX_SIDE, image.getWidth()) / (double) image.getWidth();
            } else {
                scale = Math.min(THUMBNAIL_MAX_SIDE, image.getHeight()) / (double) image.getHeight();
            }
            BufferedImage thumbnail = new BufferedImage((int) (scale * image.getWidth()),
                    (int) (scale * image.getHeight()),
                    image.getType());

            Graphics2D graphics2D = thumbnail.createGraphics();

            AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
            graphics2D.drawImage(image, transform, DUMMY_OBSERVER);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( thumbnail, imageExt, baos );
            baos.flush();
            byte[] thumbnailBytes = baos.toByteArray();
            baos.close();
            return thumbnailBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImageExt(String originalFilename) {
        String extension = "";

        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i+1);
        }
        return extension;
    }

}
