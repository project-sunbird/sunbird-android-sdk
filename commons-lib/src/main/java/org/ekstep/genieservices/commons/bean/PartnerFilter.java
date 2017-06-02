package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 5/28/2017.
 *
 * @author anil
 */
public class PartnerFilter implements Serializable {

    private MasterDataType masterDataType;
    private List<String> values;

    public MasterDataType getMasterDataType() {
        return masterDataType;
    }

    public void setMasterDataType(MasterDataType masterDataType) {
        this.masterDataType = masterDataType;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
