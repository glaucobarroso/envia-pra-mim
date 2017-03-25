package org.enviapramim.model.ml;

/**
 * Created by Glauco on 25/03/2017.
 */
public class FreeShipping {

    public Integer id;
    public Rule rule;

    public class Rule {
        public String free_mode;
        public Object value;
    }

}
