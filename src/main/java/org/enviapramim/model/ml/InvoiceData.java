package org.enviapramim.model.ml;

/**
 * Created by root on 04/05/17.
 */
public class InvoiceData {

    public String fiscal_key;
    public Additional_data additional_data;

    public class Additional_data {
        public String cfop;
        public String company_state_tax_id;
    }
}
