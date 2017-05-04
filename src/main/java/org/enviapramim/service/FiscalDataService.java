package org.enviapramim.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.enviapramim.model.FiscalData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by glauco on 04/05/17.
 */
public class FiscalDataService {

    private static final String PRODUCTID_NFE_TAG = "cProd";
    private static final String CFOP_NFE_TAG = "CFOP";
    private static final String BUYER_NAME_NFE_TAG = "xNome";
    private static final String BUYER_CPF_NFE_TAG = "CPF";
    private static final String NFEID_NFE_TAG1 = "chNFe";
    private static final String NFEID_NFE_TAG2 = "infNFe";
    private static final String BUYERINFO_NFE_TAG = "dest";

    public List<String> unzip(ZipInputStream zipInputStream) {
        ArrayList<String> ret = new ArrayList<String>();
        try {

            ZipEntry ze = zipInputStream.getNextEntry();
            while(ze != null){
                byte[] buffer = new byte[1024];

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                byte[] decompressedBytes = new byte[outputStream.size()];
                decompressedBytes = outputStream.toByteArray();
                String decompressedContent = new String(decompressedBytes);
                outputStream.close();
                ret.add(decompressedContent);
                ze = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List<FiscalData> getFiscalData(List<String> nfeXmlList) {
        ArrayList<FiscalData> fiscalDataList = new ArrayList<FiscalData>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbFactory.newDocumentBuilder();
            for (String nfeXml : nfeXmlList) {
                FiscalData fiscalData = new FiscalData();
                Document doc = docBuilder.parse(new ByteArrayInputStream(nfeXml.getBytes("utf-8")));
                fiscalData.nfeId = getNfeId(doc);
                fiscalData.buyerCpf = getBuyerCpf(doc);
                fiscalData.buyerName = getBuyerName(doc);
                fiscalData.cfOp = getCfop(doc);
                fiscalData.productId = getProductId(doc);
                fiscalDataList.add(fiscalData);
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fiscalDataList;
    }

    private String getBuyerName(Document doc) {
        Element element = (Element) getFirstNode(doc, BUYERINFO_NFE_TAG);
        NodeList nodeList = element.getElementsByTagName(BUYER_NAME_NFE_TAG);
        return getFirstNodeValue(nodeList, BUYER_NAME_NFE_TAG);
    }

    private String getBuyerCpf(Document doc) {
        return getFirstNodeValue(doc, BUYER_CPF_NFE_TAG);
    }

    private String getProductId(Document doc) {
        return getFirstNodeValue(doc, PRODUCTID_NFE_TAG);
    }

    private String getCfop(Document doc) {
        return getFirstNodeValue(doc, CFOP_NFE_TAG);
    }

    private String getNfeId(Document doc) {
        String nfeId = getFirstNodeValue(doc, NFEID_NFE_TAG1);
        if (nfeId != null && nfeId.length() > 0) {
            return nfeId;
        }
        return getFirstNodeAttribute(doc, NFEID_NFE_TAG2);
    }

    private Node getFirstNode(Document doc, String tag) {
        NodeList nodeList = doc.getElementsByTagName(tag);
        return getFirstNode(nodeList, tag);
    }

    private Node getFirstNode(NodeList nodeList, String tag) {
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    return node;
                }
            }
        }
        return null;
    }

    private String getFirstNodeValue(Document doc, String tag) {
        return getFirstNode(doc, tag).getTextContent();
    }

    private String getFirstNodeValue(NodeList nodeList, String tag) {
        return getFirstNode(nodeList, tag).getTextContent();
    }

    private String getFirstNodeAttribute(Document doc, String tag) {
        NodeList nodeist = doc.getElementsByTagName(tag);
        if (nodeist != null && nodeist.getLength() > 0) {
            if (nodeist != null) {
                for (int i = 0; i < nodeist.getLength(); i++) {
                    Node node = nodeist.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String rawNfeId = element.getAttribute("Id");
                        return rawNfeId.substring(rawNfeId.indexOf("NFe") + 3);
                    }
                }
            }
        }
        return null;
    }
}
