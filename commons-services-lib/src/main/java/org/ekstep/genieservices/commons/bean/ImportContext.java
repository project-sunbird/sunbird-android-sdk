package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.db.operations.IDBSession;
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
public class ImportContext {

    private File ecarFile;
    private File tmpLocation;
    private File destinationFolder;
    private List<String> skippedItemsIdentifier;
    private String manifestVersion;
    private List<Map<String, Object>> items;
    private boolean isChildContent;
    private Map<String, Object> metadata;
    private List<String> identifiers = new ArrayList<>();
    private IDBSession dbSession;   // External DB

    private Map<String, Object> manifest;

    // Used for import/export
    public ImportContext(IDBSession dbSession, Map<String, Object> metadata) {
        this.dbSession = dbSession;
        this.metadata = metadata;
    }

    /**
     * Used for Export Content
     */
    public ImportContext(File destinationFolder, File ecarFile) {
        this.destinationFolder = destinationFolder;
        this.ecarFile = ecarFile;

        this.manifest = new HashMap<>();
        this.tmpLocation = FileUtil.getTempLocation(destinationFolder);
    }

    public ImportContext(boolean isChildContent, String ecarFilePath, File destinationFolder) {
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

    public IDBSession getDBSession() {
        return dbSession;
    }

    public void setDbSession(IDBSession dbSession) {
        this.dbSession = dbSession;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifier) {
        this.identifiers.add(identifier);
    }

    public Map<String, Object> getManifest() {
        return manifest;
    }
}
