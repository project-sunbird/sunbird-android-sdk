package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.telemetry.GETransferContentMap;

import java.util.List;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GETransferEventKnowStructure {

    public static final String TRANSFER_DIRECTION_EXPORT = "EXPORT";
    public static final String TRANSFER_DIRECTION_IMPORT = "IMPORT";
    public static final String DATATYPE_TELEMETRY = "TELEMETRY";
    public static final String DATATYPE_CONTENT = "CONTENT";
    public static final String DATATYPE_EXPLODED_CONTENT = "EXPLODEDCONTENT";
    public static final String DATATYPE_PROFILE = "PROFILE";
    public static final String CONTENT_ITEMS_KEY = "contents";
    public static final String FILE_SIZE = "FILE_SIZE";
    public static final String FILE_TYPE = "FILE_TYPE";
    private String direction;
    private String datatype;
    private int count = -1;
    private long size;
    private List<GETransferContentMap> contents;

    public GETransferEventKnowStructure(String direction, String datatype, int count,
                                        long size, List<GETransferContentMap> contents) {
        this.direction = direction;
        this.datatype = datatype;
        this.count = count;
        this.size = size;
        this.contents = contents;
    }

    public String direction() {
        return direction;
    }

    public String dataType() {
        return datatype;
    }

    public int count() {
        return count;
    }

    public List<GETransferContentMap> contents() {
        return contents;
    }

    public long size() {
        return size;
    }
}

