package org.ekstep.genieservices.content.bean;

import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/18/2017.
 *
 * @author anil
 */
public class MoveContentContext {

    private List<String> contentIds;
    private List<String> validContentIdsInDestination;
    private File destinationFolder;
    private File contentRootFolder;
    private List<ContentModel> contentsInSource;
    private List<ContentModel> contentsInDestination;

    public MoveContentContext(List<String> contentIds, File destinationFolder) {
        this.contentIds = contentIds;
        this.validContentIdsInDestination = new ArrayList<>();
        this.destinationFolder = destinationFolder;
        this.contentsInSource = new ArrayList<>();
        this.contentsInDestination = new ArrayList<>();
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public List<String> getValidContentIdsInDestination() {
        return validContentIdsInDestination;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public File getContentRootFolder() {
        return contentRootFolder;
    }

    public void setContentRootFolder(File contentRootFolder) {
        this.contentRootFolder = contentRootFolder;
    }

    public List<ContentModel> getContentsInSource() {
        return contentsInSource;
    }

    public List<ContentModel> getContentsInDestination() {
        return contentsInDestination;
    }

}
