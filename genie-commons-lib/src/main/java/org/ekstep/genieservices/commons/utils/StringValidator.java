package org.ekstep.genieservices.commons.utils;

import org.ekstep.genieservices.commons.IValidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created on 24/4/17.
 *
 * @author shriharsh
 */

public class StringValidator implements IValidate {
    private final ArrayList<String> errors;
    public Map<String, String> fields;

    public StringValidator(Map<String, String> fields) {
        this.fields = fields;
        this.errors = new ArrayList();
    }

    public boolean isValid() {
        Iterator var1 = this.fields.keySet().iterator();

        while (true) {
            String key;
            do {
                if (!var1.hasNext()) {
                    return this.errors.isEmpty();
                }

                key = (String) var1.next();
            } while (this.fields.get(key) != null && !((String) this.fields.get(key)).isEmpty());

            this.errors.add(String.format("%s can not be empty", new Object[]{key}));
        }
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
