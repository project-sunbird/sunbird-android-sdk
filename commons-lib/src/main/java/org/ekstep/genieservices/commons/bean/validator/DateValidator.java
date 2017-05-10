package org.ekstep.genieservices.commons.bean.validator;

import org.ekstep.genieservices.commons.IValidate;
import org.ekstep.genieservices.commons.bean.DayMonth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created on 24/4/17.
 * @author shriharsh
 */

public class DateValidator implements IValidate{
    private final ArrayList<String> errors;
    public Map<String, DayMonth> fields;

    public DateValidator(HashMap<String, DayMonth> validationDateFields) {
        this.fields = validationDateFields;
        this.errors = new ArrayList();
    }

    public boolean isValid() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        Iterator var2 = this.fields.values().iterator();

        while (var2.hasNext()) {
            DayMonth dateObj = (DayMonth) var2.next();

            try {
                if (dateObj.isParsable()) {
                    dateFormat.parse(dateObj.toString());
                }
            } catch (ParseException var5) {
                this.errors.add("invalid date, field: " + dateObj.toString());
            }
        }

        return this.errors.isEmpty();
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
