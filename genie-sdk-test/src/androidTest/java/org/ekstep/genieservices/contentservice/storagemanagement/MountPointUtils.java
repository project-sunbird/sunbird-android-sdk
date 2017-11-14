package org.ekstep.genieservices.contentservice.storagemanagement;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 8/6/17.
 */

public class MountPointUtils {
    /**
     * Retrieves a {@code List} of all available {@link MountPoint}s
     *
     * @return a {@code List} of available {@link MountPoint}s
     * @see #getMountPointsFromSystemEnv()
     * @see #getMountPointsFromVold()
     */
    @NonNull
    public static List<MountPoint> getMountPoints() {
        final List<MountPoint> mountPoints = getMountPointsFromSystemEnv();

        // fallback: parse file 'vold.fstab' and try to find all external storage
        if (mountPoints.size() == 1) {
            final List<MountPoint> mountPointsFromVold = getMountPointsFromVold();

            for (MountPoint mountPoint : mountPointsFromVold) {
                if (!mountPoint.getStorageType().equals(MountPoint.StorageType.INTERNAL)) {
                    mountPoints.add(mountPoint);
                }
            }
        }

        return mountPoints;
    }

    /**
     * Check if the given {@link MountPoint} is mounted or not:
     * <ul>
     * <li>{@code Environment.MEDIA_MOUNTED}</li>
     * <li>{@code Environment.MEDIA_MOUNTED_READ_ONLY}</li>
     * </ul>
     *
     * @param mountPoint the given {@link MountPoint} to check
     * @return {@code true} if the gieven {@link MountPoint} is mounted, {@code false} otherwise
     */
//    public static boolean isMounted(@NonNull final MountPoint mountPoint) {
//        return mountPoint.getStorageState()
//                .equals(Environment.MEDIA_MOUNTED) || mountPoint.getStorageState()
//                .equals(Environment.MEDIA_MOUNTED_READ_ONLY);
//    }


    /**
     * Retrieves a {@code List} of {@link MountPoint}s from
     * {@code 'vold.fstab'} system file.
     *
     * @return a {@code List} of available {@link MountPoint}s
     */
    @NonNull
    private static List<MountPoint> getMountPointsFromVold() {
        final List<MountPoint> mountPoints = new ArrayList<>();
        final File voldFstabFile = new File("/system/etc/vold.fstab");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        if (voldFstabFile.exists()) {
            try {
                fileReader = new FileReader(voldFstabFile);
                bufferedReader = new BufferedReader(fileReader);
                String line;
                MountPoint.StorageType storageType = null;

                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtil.isNullOrEmpty(line)) {
                        continue;
                    }

                    line = line.trim();

                    // parse line comment
                    if (line.startsWith("#")) {
                        if (line.contains("internal")) {
                            storageType = MountPoint.StorageType.INTERNAL;
                        } else if (line.contains("external")) {
                            storageType = MountPoint.StorageType.EXTERNAL;
                        } else if (line.contains("usb")) {
                            storageType = MountPoint.StorageType.USB;
                        } else {
                            // storage type not found from line comment. Continue anyway
                            storageType = null;
                        }
                    }

                    // parse 'media_type' only it the storage type was not found from line comment
                    if (line.startsWith("media_type") && (storageType == null)) {
                        String[] tokens = line.split("\\s");

                        if (tokens.length == 3) {
                            if (tokens[2].contains("usb")) {
                                storageType = MountPoint.StorageType.USB;
                            }
                        }
                    }

                    // parse 'dev_mount'
                    if (line.startsWith("dev_mount") && (storageType != null)) {
                        String[] tokens = line.split("\\s");

                        if (tokens.length >= 3) {
                            File mountPath = new File(tokens[2]);
                            String storageState = Environment.MEDIA_UNMOUNTED;

                            if (mountPath.isDirectory()) {
                                if (mountPath.canWrite()) {
                                    storageState = Environment.MEDIA_MOUNTED;
                                } else if (mountPath.canRead()) {
                                    storageState = Environment.MEDIA_MOUNTED_READ_ONLY;
                                }

                                mountPoints.add(new MountPoint(tokens[2], storageState, storageType));
                            }
                        }
                    }
                }
            } catch (IOException ioe) {

            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException ioe) {
                    }
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ioe) {

                    }
                }
            }
        }

        return mountPoints;
    }

    /**
     * Retrieves a {@code List} of {@link MountPoint}s from {@code System}
     * environment.
     *
     * @return a {@code List} of available {@link MountPoint}s
     */
    @NonNull
    private static List<MountPoint> getMountPointsFromSystemEnv() {
        final List<MountPoint> mountPoints = new ArrayList<>();

        final String externalStorage = System.getenv("EXTERNAL_STORAGE");

        if (StringUtil.isNullOrEmpty(externalStorage)) {
            mountPoints.add(new MountPoint(Environment.getExternalStorageDirectory().getAbsolutePath(), Environment.getExternalStorageState(), MountPoint.StorageType.INTERNAL));
        } else {
            mountPoints.add(new MountPoint(externalStorage, Environment.getExternalStorageState(), MountPoint.StorageType.INTERNAL));
        }

        final String secondaryStorage = System.getenv("SECONDARY_STORAGE");

        if (!StringUtil.isNullOrEmpty(secondaryStorage)) {
            final String[] paths = secondaryStorage.split(":");
            boolean firstSecondaryStorage = true;

            for (String path : paths) {
                final File file = new File(path);
                String storageState = Environment.MEDIA_UNMOUNTED;

                if (file.isDirectory()) {

                    if (file.canWrite()) {
                        storageState = Environment.MEDIA_MOUNTED;
                    } else if (file.canRead()) {
                        storageState = Environment.MEDIA_MOUNTED_READ_ONLY;
                    }

                    mountPoints.add(new MountPoint(path, storageState, (firstSecondaryStorage) ? MountPoint.StorageType.EXTERNAL : MountPoint.StorageType.USB));
                    firstSecondaryStorage = false;
                }
            }
        }

        return mountPoints;
    }

    /**
     * get external secondary storage file path
     *
     * @return
     */
    public static String getExternalSecondaryStorage(Context context) {
        List<String> filePathList = new ArrayList<String>();
        String secondaryStorage = System.getenv("SECONDARY_STORAGE");

        if (StringUtil.isNullOrEmpty(secondaryStorage)) {
            secondaryStorage = System.getenv("EXTERNAL_SDCARD_STORAGE");
        }

        if (!StringUtil.isNullOrEmpty(secondaryStorage)) {
            if (secondaryStorage.contains(":")) {
                secondaryStorage = secondaryStorage.substring(0, secondaryStorage.indexOf(":"));
            }

            File externalFilePath = new File(secondaryStorage);
            if (externalFilePath.exists() && externalFilePath.canWrite() && externalFilePath.listFiles() != null) {
                secondaryStorage = externalFilePath.getAbsolutePath();
            } else {
                secondaryStorage = null;
            }
        }

        if (!StringUtil.isNullOrEmpty(secondaryStorage)) {
            filePathList.add(secondaryStorage);
        } else {
            filePathList.add("/storage/sdcard1");
            filePathList.add("/storage/sdcard2");
            filePathList.add("/mnt/sdcard/external_sd");
            filePathList.add("/mnt/extSdCard");
            filePathList.add("/mnt/sdcard2");
        }

        for (String filePath : filePathList) {
            File path = new File(filePath);
            if (path != null && path.exists() && (path.listFiles() != null) && path.listFiles().length > 0) {
                secondaryStorage = path.getAbsolutePath();
                return secondaryStorage;
            }
        }

        if (StringUtil.isNullOrEmpty(secondaryStorage)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                File[] externalFilesDir = context.getExternalFilesDirs(null);

                if (externalFilesDir != null && externalFilesDir.length == 2) {
                    File file = externalFilesDir[1];
                    if (file != null) {
                        String sdcardPath = file.getAbsolutePath();
                        if (!StringUtil.isNullOrEmpty(sdcardPath)) {
                            try {
                                secondaryStorage = sdcardPath.substring(0, sdcardPath.indexOf("/Android"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        if (!StringUtil.isNullOrEmpty(secondaryStorage)) {
            File sdcardPath = new File(secondaryStorage);
            if (sdcardPath != null && sdcardPath.exists()) {
                return sdcardPath.getAbsolutePath();
            }
        }

        return null;
    }

}


