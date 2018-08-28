package org.ekstep.genieservices.content.bean;

import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SwitchContentContext {

    private List<String> validContentIdsInDestination;
    private File destinationFolder;
    private File contentRootFolder;
    private List<ContentModel> contentsInSource;
    private List<ContentModel> contentsInDestination;

    public SwitchContentContext(File destinationFolder) {
        this.validContentIdsInDestination = new ArrayList<>();
        this.destinationFolder = destinationFolder;
        this.contentsInDestination = new ArrayList<>();
        this.contentsInSource = new ArrayList<>();
    }

    public List<String> getValidContentIdsInDestination() {
        return validContentIdsInDestination;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public List<ContentModel> getContentsInDestination() {
        return contentsInDestination;
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
}
