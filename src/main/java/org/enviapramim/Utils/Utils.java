package org.enviapramim.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.enviapramim.model.Product;

/**
 * Created by Glauco on 19/03/2017.
 */
public class Utils {

    private String DESCRIPTION_PREFFIX_FORMAT_STR = "<p style=\"text-align: center;\"><span style=\"font-family: helvetica; font-size: xx-large;\">%s<br /></span></p><hr /><div><br /><div style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; color: #333333; font-family: Arial, Helvetica, Verdana, sans-serif; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff; text-align: center; font-size: x-large;\"><img src=\"%s\" /></div><p style=\"margin: 0.5em 0px; padding: 0px; border: 3px; outline: 0px; vertical-align: baseline; box-sizing: content-box; color: #333333; font-family: Arial, Helvetica, Verdana, sans-serif; font-size: 13px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;\"><span style=\"font-family: helvetica; font-size: x-large;\"><strong><span style=\"margin: 0px; padding: 0px; border: 0px none; outline: 0px none; vertical-align: baseline; box-sizing: content-box;\">Detalhes do produto</span></strong></span></p><hr/><div style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; color: #333333; font-family: Arial, Helvetica, Verdana, sans-serif; font-size: 13px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;\"><span face=\"arial, helvetica, sans-serif\" size=\"4\" style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; font-family: arial, helvetica, sans-serif; font-size: large;\"><span face=\"arial, helvetica, sans-serif\" size=\"4\" style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; font-family: arial, helvetica, sans-serif; font-size: large;\"></span></span><ul style=\"margin: 1em 0px; padding: 0px 0px 0px 40px; border: 0px; outline: 0px; vertical-align: baseline; list-style: disc inside; box-sizing: content-box; display: block;\">";
    private String DESCRIPTION_LINE_FORMAT_STR = "<li style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; display: list-item;\"><span style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: baseline; box-sizing: content-box; font-size: large; font-family: arial, helvetica, sans-serif;\">%s</span></li>";
    private String DESCRIPTION_SUFFIX = "</div></div>";
    private StringEscapeUtils stringEscapeUtils;

    public String createHtmlDescription(Product product) {
        String htmlDescription = stringEscapeUtils.escapeHtml3(product.getDescription());
        String[] lines = htmlDescription.split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(DESCRIPTION_PREFFIX_FORMAT_STR, product.getTitle(), product.getLinks().get(0)));
        for (String line : lines) {
            stringBuilder.append(String.format(DESCRIPTION_LINE_FORMAT_STR, line));
        }
        stringBuilder.append(DESCRIPTION_SUFFIX);
        return stringBuilder.toString();
    }

}
