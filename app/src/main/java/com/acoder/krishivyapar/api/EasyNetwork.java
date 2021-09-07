package com.acoder.krishivyapar.api;

import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class EasyNetwork {

    public static Builder request(String url){
        return new Builder(url);
    }

    public static class Builder {
        private String url;
        private final Map<String, String> posts;
        private final Map<String, String> gets;
        private final Map<String, String> headers;
        private final Map<String, File> files;

        public Builder(String url) {
            this.url = url;
            posts = new HashMap<>();
            gets = new HashMap<>();
            files = new HashMap<>();
            headers = new HashMap<>();
        }

        public String getUrl() {
            return url;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Map<String, String> getPosts() {
            return posts;
        }

        public Map<String, String> getGets() {
            return gets;
        }

        public Map<String, File> getFiles() {
            return files;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Builder addPost(String key, String value) {
            this.posts.put(key, value);
            return this;
        }

        public Builder addGet(String key, String value) {
            this.gets.put(key, value);
            return this;
        }

        public Builder addFile(String key, File file) {
            this.files.put(key, file);
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder addPosts(Map<String, String> posts) {
            this.posts.putAll(posts);
            return this;
        }

        public Builder addGets(Map<String, String> gets) {
            this.gets.putAll(gets);
            return this;
        }

        public Builder addFiles(Map<String, File> files) {
            this.files.putAll(files);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public ANRequest build() {
            if (url == null) {
                return null;
            } else if (gets.isEmpty() && files.isEmpty() && posts.isEmpty()) {
                ANRequest.GetRequestBuilder builder = AndroidNetworking.get(url)
                        .setPriority(Priority.MEDIUM)
                        .setTag("EASY_NETWORK");
                if (!headers.isEmpty()){
                    builder.addHeaders(headers);
                }
                return builder.build();
            } else if (files.isEmpty() && posts.isEmpty()) {
                ANRequest.GetRequestBuilder builder = AndroidNetworking.get(url)
                        .addQueryParameter(gets)
                        .setPriority(Priority.MEDIUM)
                        .setTag("EASY_NETWORK");
                if (!headers.isEmpty()){
                    builder.addHeaders(headers);
                }
                return builder.build();
            } else if (files.isEmpty()) {
                ANRequest.PostRequestBuilder builder = AndroidNetworking.post(url)
                        .addBodyParameter(posts)
                        .addQueryParameter(gets)
                        .setPriority(Priority.MEDIUM)
                        .setTag("EASY_NETWORK");
                if (!headers.isEmpty())
                    builder.addHeaders(headers);
                return builder.build();
            } else {
                Log.d("easyNet", files.size() + "files size");
                ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(url)
                        .addMultipartFile(files)
                        .addMultipartParameter(posts)
                        .addQueryParameter(gets)
                        .setPriority(Priority.HIGH)
                        .setExecutor(Executors.newSingleThreadExecutor())
                        .setTag("Upload");
                if (!headers.isEmpty())
                    builder.addHeaders(headers);
                return builder.build();
            }
        }
    }
}
