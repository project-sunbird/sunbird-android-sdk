package org.ekstep.genieservices.content.bean;

import org.ekstep.genieservices.commons.utils.FileUtil;

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
public class ImportContentContext {

    private File ecarFile;
    private File tmpLocation;
    private File destinationFolder;
    private List<String> skippedItemsIdentifier;
    private String manifestVersion;
    private List<Map<String, Object>> items;
    private boolean isChildContent;
    private Map<String, Object> metadata;
    private List<String> identifiers = new ArrayList<>();

    public ImportContentContext(boolean isChildContent, String ecarFilePath, File destinationFolder) {
        this.ecarFile = new File(ecarFilePath);
        this.tmpLocation = FileUtil.getTempLocation(destinationFolder);

        this.destinationFolder = destinationFolder;
        this.metadata = new HashMap<>();
        this.skippedItemsIdentifier = new ArrayList<>();
        this.items = new ArrayList<>();
        this.isChildContent = isChildContent;
    }

    public File getEcarFile() {
        return ecarFile;
    }

    public File getTmpLocation() {
        return tmpLocation;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<String> getSkippedItemsIdentifier() {
        return skippedItemsIdentifier;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public void setManifestVersion(String manifestVersion) {
        this.manifestVersion = manifestVersion;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifier) {
        this.identifiers.add(identifier);
    }

}
