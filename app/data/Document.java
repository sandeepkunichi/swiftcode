package data;

import com.google.api.services.drive.Drive;

import java.io.File;

/**
 * Created by Sandeep.K on 9/5/2016.
 */
public class Document {
    private Drive service;
    private String title;
    private String description;
    private String parentId;
    private String mimeType;
    private File file;

    public Document(Drive service, String title, String description, String parentId, String mimeType, File file) {
        this.service = service;
        this.title = title;
        this.description = description;
        this.parentId = parentId;
        this.mimeType = mimeType;
        this.file = file;
    }

    public Drive getService() {
        return service;
    }

    public void setService(Drive service) {
        this.service = service;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
