package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 9/3/18.
 *
 * @author anil
 */
public class CourseBatchesResponse {

    private int count;
    private List<Batch> content;

    public int getCount() {
        return count;
    }

    public List<Batch> getBatches() {
        return content;
    }
}
