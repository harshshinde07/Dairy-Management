package com.kshitijharsh.dairymanagement.utils;

import android.os.Environment;

import java.io.File;

import static com.kshitijharsh.dairymanagement.utils.Constants.CONST.EXT_DIRECTORY;

public class FileUtils {

    public static String getAppDir() {
        return Environment.getExternalStorageDirectory() + EXT_DIRECTORY;
    }

    public static File createDirIfNotExist(String path) {
        File dir = new File(path);
        if( !dir.exists() ){
            dir.mkdir();
        }
        return dir;
    }

    /* Checks if external storage is available for read and write */
    static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
