package org.enviapramim.model.ml;

/**
 * Created by root on 05/05/17.
 */
public class InvoiceDataResponse {

    private String id;
    private Additional_data additional_data;
    private String weight;
    private String invoice_serie;
    private String status;
    private String item_title;
    private String last_updated;
    private String date_created;
    private String shipment_id;
    private String invoice_date;
    private String invoice_amount;
    private String invoice_number;
    private String fiscal_key;

    public class Additional_data {
        private String company_state_tax_id;
        private String cfop;
    }
}