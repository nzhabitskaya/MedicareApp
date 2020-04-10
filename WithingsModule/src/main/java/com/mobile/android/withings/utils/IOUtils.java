package com.mobile.android.withings.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mobile.android.withings.service.helper.HttpHelper;

import android.content.Context;
import android.os.Environment;

public class IOUtils {

    public static String convertStreamToString(InputStream is) {

        if (is == null)
            return "";

        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            int result = bis.read();
            while (result != -1) {
                byte b = (byte)result;
                buf.write(b);
                result = bis.read();
            }
        } catch (IOException e) {
        } finally {
            IOUtils.closeStream(is);
        }
        return buf.toString();
    }

    public static final void closeStream(Closeable stream) {
        if (stream != null)
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static final File downloadFile(final Context ctx, String sUrl, String fileName)
            throws IllegalStateException, Exception {
        URL url = new URL(sUrl);
        HttpURLConnection c = (HttpURLConnection)url.openConnection();
        c.setRequestMethod("GET");
        c.setDoOutput(true);
        c.connect();

        FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_WORLD_READABLE);

        InputStream is = getInputStream(sUrl);// c.getInputStream();

        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len1);
        }
        fos.flush();

        IOUtils.closeStream(fos);
        IOUtils.closeStream(is);

        String filePath = Environment.getDataDirectory() + "/data/"
                + ctx.getApplicationInfo().packageName + "/files";
        File file = new File(new File(filePath), fileName);

        return file;
    }

    void downloadFileToDir(final Context ctx, String sUrl, String dirName) {

    }

    public static InputStream getInputStream(String url) throws IllegalStateException, Exception {
        HttpHelper client = new HttpHelper(url);
        return client.execute().getEntity().getContent();
    }
}
