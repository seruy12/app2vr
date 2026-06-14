package com.google.vrtoolkit.cardboard;

import android.content.res.AssetManager;
import android.os.Environment;
import java.io.File;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class ConfigUtils {
    public static final String CARDBOARD_CONFIG_FOLDER = "Cardboard";
    public static final String CARDBOARD_DEVICE_PARAMS_FILE = "current_device_params";
    public static final String CARDBOARD_PHONE_PARAMS_FILE = "phone_params";

    public static File getConfigFile(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), CARDBOARD_CONFIG_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            throw new IllegalStateException(file + " already exists as a file, but is expected to be a directory.");
        }
        return new File(file, str);
    }

    public static InputStream openAssetConfigFile(AssetManager assetManager, String str) {
        return assetManager.open(new File(CARDBOARD_CONFIG_FOLDER, str).getPath());
    }
}
