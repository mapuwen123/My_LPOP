package com.mapuw.lpop.utils.glide.integration.okhttp;

public interface ProgressListener {

    void progress(long bytesRead, long contentLength, boolean done);

}
