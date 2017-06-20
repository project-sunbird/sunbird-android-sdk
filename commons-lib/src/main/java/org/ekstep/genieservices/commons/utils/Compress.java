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

    private static final String TAG = "Compress";
    private static final int BUFFER = 2048;

    private final File zipFile;
    private final File sourcePath;
    private String fileIdentifier;
    private boolean firstPass;

    public Compress(File sourcePath, File zipFile) {
        this.sourcePath = sourcePath;
        this.zipFile = zipFile;
    }

    public Compress(File sourcePath, File zipFile, String fileIdentifier) {
        this.sourcePath = sourcePath;
        this.zipFile = zipFile;
        this.fileIdentifier = fileIdentifier;
    }

    public boolean zip() throws IOException {
        final int BUFFER = 2048;

        File sourceFile = sourcePath;
        BufferedInputStream origin;

        zipFile.getParentFile().mkdirs();
        zipFile.createNewFile();

        FileOutputStream dest = new FileOutputStream(zipFile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

        if (sourceFile.isDirectory()) {
            zipSubFolder(out, sourceFile, sourceFile.getParent().length());
        } else {
            byte data[] = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(sourcePath);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath.getPath()));
            out.putNextEntry(entry);

            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
        }
        out.close();

        return true;
    }

    private void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {

        File[] fileList = folder.listFiles();
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

                    System.out.println(TAG + ": ZIP SUBFOLDER -- Relative Path : " + relativePath);

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

    private String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    private void copyFileAssets(File sourceFolder, File payloadFolder, String fileIdentifier) throws IOException {
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

    public void zipAPK() throws IOException {
        fileIdentifier = fileIdentifier.substring(0, fileIdentifier.indexOf("/"));

        File payloadFolder = new File(zipFile, fileIdentifier);
        payloadFolder.mkdir();

        if (sourcePath.isDirectory()) {
            copyFileAssets(sourcePath, payloadFolder, fileIdentifier);
        } else {
            throw new IOException("The source folder is not available in the device");
        }
    }

}
