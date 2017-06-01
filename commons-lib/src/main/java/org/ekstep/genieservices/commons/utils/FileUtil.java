package org.ekstep.genieservices.commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created on 20/4/17.
 *
 * @author shriharsh
 */
public class FileUtil {

    public static String readFileFromClasspath(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(FileUtil.class.getResourceAsStream("/" + filename)));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDownloadedFileLocation(FileInputStream fileInputStream) {
        File targetFile = null;
        try {
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            targetFile = getTempLocation(new File(System.currentTimeMillis() + ".ecar"));
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if (targetFile != null) {
            return targetFile.toString();
        }

        return null;
    }

    public static boolean rm(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                rm(child);
            }
        }

        return fileOrDirectory.delete();
    }

    public static boolean rm(File fileOrDirectory, String skippDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                if (!skippDirectory.equals(child.getName())) {
                    rm(child, skippDirectory);
                }
            }
        }

        return fileOrDirectory.delete();
    }

    public static String getFileExtension(String fileName) {
        String ext = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
        if (ext.isEmpty()) {
            return "";
        } else {
            return ext.toLowerCase(Locale.US);
        }

        // TODO: 5/23/2017
//        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
//            return fileName.substring(fileName.lastIndexOf(".") + 1);
//        } else {
//            return "";
//        }
    }

    public static void cp(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static File getTmpDir(File externalFilesDir) {
        return new File(externalFilesDir.getPath() + "/tmp");
    }

    public static File getContentRootDir(File externalFilesDir) {
        return new File(externalFilesDir.getPath() + "/content");
    }

    public static String getTmpDirFilePath(File externalFilesDir) {
        File tmpLocation = getTmpDir(externalFilesDir);
        return tmpLocation.getAbsolutePath();
    }

    /**
     * Delete all files in the tmp directory that are older than 1 days.
     *
     * @param file
     */
    public static void deleteTmpFiles(File file) {
        if (file != null && file.exists()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteTmpFiles(f);
                }
            }

            //find the diff for file last modified date&time
            long diff = System.currentTimeMillis() - file.lastModified();
            if (diff > TimeUnit.DAYS.toMillis(1)) {
                file.delete();
            }
        }
    }

    public static void createFolders(String loc, String dir) {
        File f = new File(loc, dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public static File getTempLocation(File externalFilesDir) {
        createFolders(getTmpDirFilePath(externalFilesDir), "");
        return new File(getTmpDir(externalFilesDir), UUID.randomUUID().toString());
    }

    public static File getTempLocation(File externalFilesDir, String fileName) {
        createFolders(getTmpDirFilePath(externalFilesDir), "");
        return new File(getTmpDir(externalFilesDir), fileName);
    }

    public static long getFreeUsableSpace(File externalFilesDir) {
        if (externalFilesDir != null) {
            return externalFilesDir.getUsableSpace();
        }
        return 0;
    }

    public static String getExportTelemetryFilePath(File externalFilesDir) {
        File file = getTempLocation(externalFilesDir, "transfer.gsa");
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    public static boolean doesFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String readManifest(File tmpLocation) {
        String json = null;
        try {
            File manifest = new File(tmpLocation.getPath(), "/manifest.json");
            InputStream is = new FileInputStream(manifest);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
