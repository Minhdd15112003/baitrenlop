package ph25260.fpoly.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Comic {
    private String _id;
    private String name;
    private String description;
    private String author;
    private String year;
    private String coverImage;
    private List<String> contentImages;
    @SerializedName("CateID")
    private Cate CateID;
    private List<CommentObject> commentObjects;

    public Comic() {
    }

    public Comic(String _id, String name, String description, String author, String year, String coverImage, List<String> contentImages, Cate cateID, List<CommentObject> commentObjects) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.year = year;
        this.coverImage = coverImage;
        this.contentImages = contentImages;
        CateID = cateID;
        this.commentObjects = commentObjects;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public List<String> getContentImages() {
        return contentImages;
    }

    public void setContentImages(List<String> contentImages) {
        this.contentImages = contentImages;
    }

    public Cate getCateID() {
        return CateID;
    }

    public void setCateID(Cate cateID) {
        CateID = cateID;
    }

    public List<CommentObject> getCommentObjects() {
        return commentObjects;
    }

    public void setCommentObjects(List<CommentObject> commentObjects) {
        this.commentObjects = commentObjects;
    }
}
