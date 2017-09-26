package org.ekstep.genieservices.content.bean;

import org.ekstep.genieservices.content.db.model.ContentModel;

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
    private File contentRootFolder;
    private List<ContentModel> contentsInSource;
    private List<ContentModel> contentsInDestination;

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

    public File getContentRootFolder() {
        return contentRootFolder;
    }

    public void setContentRootFolder(File contentRootFolder) {
        this.contentRootFolder = contentRootFolder;
    }

    public List<ContentModel> getContentsInSource() {
        return contentsInSource;
    }

    public void setContentsInSource(List<ContentModel> contentsInSource) {
        this.contentsInSource = contentsInSource;
    }

    public List<ContentModel> getContentsInDestination() {
        return contentsInDestination;
    }

    public void setContentsInDestination(List<ContentModel> contentsInDestination) {
        this.contentsInDestination = contentsInDestination;
    }
}
