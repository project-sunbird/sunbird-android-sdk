package org.ekstep.genieservices.contentservice.storagemanagement;

public class MountPoint {
    private final String mountPath;
    private final String storageState;
    private final StorageType storageType;
    
    public MountPoint(
            String mountPath,
            String storageState,
            StorageType storageType) {
        this.mountPath = mountPath;
        this.storageState = storageState;
        this.storageType = storageType;
    }

   

    public String getMountPath() {
        return mountPath;
    }

    public String getStorageState() {
        return storageState;
    }

    public StorageType getStorageType() {
        return storageType;
    }
    
    public enum StorageType {
        /**
         * Internal storage.
         */
        INTERNAL,

        /**
         * External storage.
         */
        EXTERNAL,

        /**
         * USB storage.
         */
        USB
    }
	
	

}
