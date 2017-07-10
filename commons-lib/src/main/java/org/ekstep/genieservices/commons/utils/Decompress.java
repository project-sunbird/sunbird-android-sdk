package org.ekstep.genieservices.commons.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class Decompress {

    public static boolean unzip(File zipFile, File destinationFolder) {
        FileUtil.createFolders(destinationFolder.getPath(), "");

        try {
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry ze;
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    FileUtil.createFolders(destinationFolder.getPath(), ze.getName());
                } else {
                    String relativeFilePath = ze.getName();
                    if (StringUtil.isNullOrEmpty(relativeFilePath)) {
                        return false;
                    }

                    if (relativeFilePath.contains("/")) {
                        String folderContainingFile = relativeFilePath.substring(0, relativeFilePath.lastIndexOf("/"));
                        FileUtil.createFolders(destinationFolder.getPath(), folderContainingFile);
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(new File(destinationFolder, ze.getName()));
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    byte[] readBytes = new byte[1024];
                    int readSize;
                    while ((readSize = zipInputStream.read(readBytes)) >= 0) {
                        bufferedOutputStream.write(readBytes, 0, readSize);
                    }

                    zipInputStream.closeEntry();
                    bufferedOutputStream.close();
                    fileOutputStream.close();
                }
            }
            zipInputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
