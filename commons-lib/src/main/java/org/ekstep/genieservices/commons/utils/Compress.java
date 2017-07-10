package org.ekstep.genieservices.commons.utils;

import org.ekstep.genieservices.commons.CommonConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class Compress {

    private static final int BUFFER = 2048;

    public static boolean zip(File sourceFolder, File zipFile) throws IOException {
        BufferedInputStream origin;

        zipFile.getParentFile().mkdirs();
        zipFile.createNewFile();

        FileOutputStream dest = new FileOutputStream(zipFile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

        if (sourceFolder.isDirectory()) {
            zipSubFolder(out, sourceFolder, sourceFolder.getParent().length());
        } else {
            byte data[] = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(sourceFolder);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry entry = new ZipEntry(getLastPathComponent(sourceFolder.getPath()));
            out.putNextEntry(entry);

            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
        }
        out.close();

        return true;
    }

    private static void zipSubFolder(ZipOutputStream out, File sourceFolder, int basePathLength) throws IOException {
        File[] fileList = sourceFolder.listFiles();
        BufferedInputStream origin;

        if (fileList != null) {
            for (File file : fileList) {
                //skip manifest.json file from exploded sdcard content
                if (file.getName().equalsIgnoreCase("manifest.json")) {
                    if (file.getAbsolutePath().contains(CommonConstants.GENIE_EXTRACTED_ECAR_FOLDER_PATH)) {
                        continue;
                    }
                }

                if (file.isDirectory()) {
                    zipSubFolder(out, file, basePathLength);
                } else {
                    byte data[] = new byte[BUFFER];

                    String unmodifiedFilePath = file.getPath();
                    String relativePath = unmodifiedFilePath.substring(basePathLength);
                    relativePath = relativePath.substring(relativePath.indexOf("/", 1), relativePath.length());

                    FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                    origin = new BufferedInputStream(fi, BUFFER);

                    ZipEntry entry = new ZipEntry(relativePath);
                    out.putNextEntry(entry);

                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }

                    origin.close();
                }
            }
        }
    }

    private static String getLastPathComponent(String sourceFolderPath) {
        String[] segments = sourceFolderPath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    public static void zipAPK(File sourceFolder, File zipFile, String fileIdentifier) throws IOException {
        fileIdentifier = fileIdentifier.substring(0, fileIdentifier.indexOf("/"));

        File payloadFolder = new File(zipFile, fileIdentifier);
        payloadFolder.mkdir();

        if (sourceFolder.isDirectory()) {
            copyFileAssets(sourceFolder, payloadFolder, fileIdentifier);
        } else {
            throw new IOException("The source folder is not available in the device");
        }
    }

    private static void copyFileAssets(File sourceFolder, File payloadFolder, String fileIdentifier) throws IOException {
        File[] fileList = sourceFolder.listFiles();
        if (fileList != null) {
            for (File file : fileList) {

                //skip manifest.json file from exploded sdcard content
                if (file.getName().equalsIgnoreCase("manifest.json")) {
                    if (file.getAbsolutePath().contains(CommonConstants.GENIE_EXTRACTED_ECAR_FOLDER_PATH)) {
                        continue;
                    }
                }

                if (file.isDirectory()) {
                    if (file.getName().equalsIgnoreCase(fileIdentifier)) {
                        copyFileAssets(file, payloadFolder, "");
                    }
                } else {
                    File childFile = file;
                    String fileName = childFile.getName();
                    File payloadFile = new File(payloadFolder, fileName);
                    payloadFile.createNewFile();
                    FileUtil.cp(childFile, payloadFile);
                }
            }
        }
    }

}
