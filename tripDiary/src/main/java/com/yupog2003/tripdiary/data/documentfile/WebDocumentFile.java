package com.yupog2003.tripdiary.data.documentfile;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.FileHelper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WebDocumentFile extends DocumentFile {

    public String urlStr;
    boolean isDir;
    public String token;
    static final String serviceURL = TripDiaryApplication.serverURL + "/DocumentFileService.php";
    static final String charset = "UTF-8";
    JSONArray children;
    String content;

    public WebDocumentFile(String token, String urlStr, DocumentFile parent, JSONArray children, boolean isDir) {
        super(parent);
        this.token = token;
        this.isDir = isDir;
        this.children = children;
        this.urlStr = urlStr;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public DocumentFile createFile(String mimeType, String displayName) {
        /*String result = postService("createFile", urlStr, token, "displayName", displayName, "mimeType", mimeType);
            if (result != null && result.startsWith("http://")) {
                return new WebDocumentFile(token, result, this, new JSONArray(), false);
            }*/
        return null;
    }

    @Override
    public DocumentFile createDirectory(String displayName) {
        /*String result = postService("createDirectory", urlStr, token, "displayName", displayName);
        if (result != null && result.startsWith("http://")) {
            return new WebDocumentFile(token, result, this, new JSONArray(), true);
        }*/
        return null;
    }

    @Override
    public Uri getUri() {
        if (urlStr != null) {
            return Uri.parse(urlStr);
        }
        return null;
    }

    @Override
    public String getName() {
        if (urlStr.contains("/")) {
            return urlStr.substring(urlStr.lastIndexOf("/") + 1);
        } else {
            return urlStr;
        }
    }

    @Override
    public String getType() {
        return FileHelper.getMIMEtype(getName());
    }

    @Override
    public boolean isDirectory() {
        return isDir;
    }

    @Override
    public boolean isFile() {
        return !isDir;
    }

    @Override
    public long lastModified() {
        String result = postService("lastModified", urlStr, token);
        if (result != null)
            return Long.parseLong(result);
        return 0;
    }

    @Override
    public long length() {
        String result = postService("length", urlStr, token);
        if (result != null)
            return Long.parseLong(result);
        return 0;
    }

    @Override
    public boolean canRead() {
        return token != null;
    }

    @Override
    public boolean canWrite() {
        return token != null;
    }

    @Override
    public boolean delete() {
        /*String result = postService("delete", urlStr, token);
        return result == null || result.equals("1");*/
        return false;
    }

    @Override
    public boolean exists() {
        /*String result = postService("exists", urlStr, token);
        return result != null && result.equals("1");*/
        return true;
    }

    @Override
    public boolean renameTo(String displayName) {
        return false;
    }

    private File localCache;

    @NonNull
    @Override
    public InputStream getInputStream() {
        if (content != null) {
            return new ByteArrayInputStream(content.getBytes());
        }
        if (localCache != null) {
            if (shouldDeleteLocalCache && localCache.delete()) {
                return new NullInputStream(0);
            }
            try {
                return new FileInputStream(localCache);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    String[] segments = urlStr.substring(TripDiaryApplication.serverURL.length() + 1).split("/");
                    StringBuilder sb = new StringBuilder();
                    sb.append(TripDiaryApplication.serverURL);
                    for (String segment : segments) {
                        sb.append("/").append(URLEncoder.encode(segment, charset));
                    }
                    URLConnection connection = new URL(sb.toString().replace("+", "%20")).openConnection();
                    InputStream is = connection.getInputStream();
                    localCache = new File(TripDiaryApplication.instance.getCacheDir(), String.valueOf(System.currentTimeMillis()));
                    FileOutputStream fos = new FileOutputStream(localCache);
                    FileHelper.copyByStream(is, fos);
                    Log.i("trip", "getInputStream:" + urlStr + ", spend: " + String.valueOf(System.currentTimeMillis() - startTime) + " ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
            if (localCache != null) {
                if (shouldDeleteLocalCache && localCache.delete()) {
                    return new NullInputStream(0);
                } else {
                    return new FileInputStream(localCache);
                }
            }
        } catch (InterruptedException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NullInputStream(0);
    }

    private boolean shouldDeleteLocalCache = false;

    public void deleteLocalCache() {
        shouldDeleteLocalCache = true;
        if (localCache != null) {
            if (localCache.delete())
                localCache = null;
        }
        if (thumbFile != null) {
            thumbFile.deleteLocalCache();
            thumbFile = null;
        }
    }

    @NonNull
    @Override
    public OutputStream getOutputStream() {
        return new NullOutputStream();
    }

    @NonNull
    @Override
    public DocumentFile[] listFiles(int list_type) {
        if (children == null) {
            return new DocumentFile[0];
        }
        try {
            ArrayList<WebDocumentFile> fileList = new ArrayList<>();
            for (int i = 0; i < children.length(); i++) {
                JSONObject child = children.getJSONObject(i);
                String urlStr = URLDecoder.decode(child.getString("self"), charset);
                JSONArray nextChildren = child.getJSONArray("children");
                boolean isDir = child.getString("isDir").equals("1");
                WebDocumentFile file = new WebDocumentFile(token, urlStr, this, nextChildren, isDir);
                if (child.has("content")) {
                    file.setContent(child.getString("content"));
                }
                switch (list_type) {
                    case list_all:
                        fileList.add(file);
                        break;
                    case list_pics:
                        if (FileHelper.isPicture(urlStr)) {
                            fileList.add(file);
                        }
                        break;
                    case list_videos:
                        if (FileHelper.isVideo(urlStr)) {
                            fileList.add(file);
                        }
                        break;
                    case list_audios:
                        if (FileHelper.isAudio(urlStr)) {
                            fileList.add(file);
                        }
                        break;
                    case list_dirs:
                        if (isDir && !file.getName().startsWith(".")) {
                            fileList.add(file);
                        }
                        break;
                    case list_withoutdots:
                        if (!file.getName().startsWith(".")) {
                            fileList.add(file);
                        }
                        break;
                    case list_memory:
                        if (FileHelper.isMemory(urlStr)) {
                            fileList.add(file);
                        }
                        break;
                }
            }
            return fileList.toArray(new WebDocumentFile[fileList.size()]);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new DocumentFile[0];
    }

    private WebDocumentFile thumbFile;

    public InputStream getThumbInputStream() {
        if (!FileHelper.isPicture(getName())) {
            return new NullInputStream(0);
        }
        if (thumbFile == null) {
            thumbFile = new WebDocumentFile(token, urlStr + ".thumb", getParentFile(), new JSONArray(), false);
        }
        if (shouldDeleteLocalCache) {
            thumbFile.deleteLocalCache();
            return new NullInputStream(0);
        }
        return thumbFile.getInputStream();

    }

    public static String postService(final String function, final String url, final String token, final String... params) {
        final StringBuilder resultBuilder = new StringBuilder();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("function=").append(URLEncoder.encode(function, charset));
                    sb.append("&url=").append(URLEncoder.encode(url, charset));
                    sb.append("&token=").append(URLEncoder.encode(token, charset));
                    for (int i = 0; i < params.length - 1; i += 2) {
                        sb.append("&").append(params[i]).append("=").append(URLEncoder.encode(params[i + 1], charset));
                    }
                    long startTime = System.currentTimeMillis();
                    URLConnection connection = new URL(serviceURL).openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Accept-Charset", charset);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
                    OutputStream outputStream = connection.getOutputStream();
                    String query = sb.toString();
                    outputStream.write(query.getBytes(charset));
                    InputStream is = connection.getInputStream();
                    String result = IOUtils.toString(is, charset);
                    resultBuilder.append(result);
                    Log.i("trip", query + ":" + result + ", spend " + String.valueOf(System.currentTimeMillis() - startTime) + " ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
            return resultBuilder.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
