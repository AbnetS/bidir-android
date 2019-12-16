package com.gebeya.mobile.bidir.data.base.remote;

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;

/**
 * Helpful class for loading mock responses from Json files.
 */
public final class ResponseLoader {

    /**
     * Open a given file path as a {@link JsonArray}
     *
     * @param path File path to open to.
     * @return Opened JsonArray
     * @throws Exception if there was something wrong during the parsing.
     */
    public static JsonArray openArray(@NonNull String path) throws Exception {
        return new JsonParser().parse(open(path)).getAsJsonArray();
    }

    /**
     * Open a given file path as a {@link JsonObject}
     *
     * @param path File path to open to.
     * @return Opened JsonObject
     * @throws Exception if there was something wrong during the parsing.
     */
    public static JsonObject openObject(@NonNull String path) throws Exception {
        return new JsonParser().parse(open(path)).getAsJsonObject();
    }

    /**
     * Helpful class to open a file and return its contents as a string.
     */
    private static String open(@NonNull String path) throws Exception {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream stream = loader.getResourceAsStream(path);
        final StringBuilder stringBuilder = new StringBuilder();
        int i;
        byte[] b = new byte[4096];
        while ((i = stream.read(b)) != -1) {
            stringBuilder.append(new String(b, 0, i));
        }
        return stringBuilder.toString();
    }
}
