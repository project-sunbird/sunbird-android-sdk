package org.ekstep.genieservices.content.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ImportContext {

    private File ecarFile;
    private File tmpLocation;
    private Map<String, Object> metadata;
    private List<String> skippedItemsIdentifier;
    private String manifestVersion;
    private List<HashMap<String, Object>> items;
    private boolean isChildContent;

    public ImportContext(File ecarFile, File tmpLocation) {
        this.ecarFile = ecarFile;
        this.tmpLocation = tmpLocation;
        this.metadata = new HashMap<>();
        this.skippedItemsIdentifier = new ArrayList<>();
        items = new ArrayList<>();
    }

    public File getEcarFile() {
        return ecarFile;
    }

    public void setEcarFile(File ecarFile) {
        this.ecarFile = ecarFile;
    }

    public File getTmpLocation() {
        return tmpLocation;
    }

    public void setTmpLocation(File tmpLocation) {
        this.tmpLocation = tmpLocation;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public List<String> getSkippedItemsIdentifier() {
        return skippedItemsIdentifier;
    }

    public void setSkippedItemsIdentifier(List<String> skippedItemsIdentifier) {
        this.skippedItemsIdentifier = skippedItemsIdentifier;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public void setManifestVersion(String manifestVersion) {
        this.manifestVersion = manifestVersion;
    }

    public List<HashMap<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<HashMap<String, Object>> items) {
        this.items = items;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public void setChildContent(boolean childContent) {
        isChildContent = childContent;
    }
}
