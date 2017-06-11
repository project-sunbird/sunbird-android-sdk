package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private List<HashMap<String, Object>> items;
    private boolean isChildContent;
    private Map<String, Object> metadata;
    private IDBSession dbSession;

    // Used for import/export
    public ImportContext(IDBSession dbSession, Map<String, Object> metadata) {
        this.dbSession = dbSession;
        this.metadata = metadata;
    }

    public ImportContext(boolean isChildContent, String ecarFilePath, File destinationFolder) {
        this.ecarFile = new File(ecarFilePath);
        this.tmpLocation = new File(FileUtil.getTmpDir(destinationFolder), UUID.randomUUID().toString());

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

    public List<HashMap<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<HashMap<String, Object>> items) {
        this.items = items;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public IDBSession getDBSession() {
        return dbSession;
    }
}
