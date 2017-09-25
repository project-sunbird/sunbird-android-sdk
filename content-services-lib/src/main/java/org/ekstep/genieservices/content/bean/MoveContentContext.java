package org.ekstep.genieservices.content.bean;

import java.io.File;
import java.util.List;

/**
 * Created on 9/18/2017.
 *
 * @author anil
 */
public class MoveContentContext {

    private List<String> contentIds;
    private File destinationFolder;

    public MoveContentContext(List<String> contentIds, File destinationFolder) {
        this.contentIds = contentIds;
        this.destinationFolder = destinationFolder;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }
}
