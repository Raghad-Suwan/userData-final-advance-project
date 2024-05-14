package edu.najah.cap.data.download;

public abstract class StorageServiceTemplate {
    protected abstract void authorize();

    protected abstract void specificUpload(String filePath);

    protected abstract void finalizeUpload();

}