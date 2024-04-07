package ph25260.fpoly.client.model;

public class CommentObject {
    private String _id;
    private String content;
    private String userId;
    private String username;
    private String date;
    private String avatar;
    private String comicId;

    public CommentObject() {
    }

    public CommentObject(String _id, String content, String userId, String username, String date, String avatar, String comicId) {
        this._id = _id;
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.avatar = avatar;
        this.comicId = comicId;
    }


    public CommentObject(String content, String userId, String username, String date, String comicId) {
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.comicId = comicId;
    }

    public CommentObject(String content, String idUser, String username, String date, String avatar, String comicId) {
        this.content = content;
        this.userId = idUser;
        this.username = username;
        this.date = date;
        this.avatar = avatar;
        this.comicId = comicId;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }
}
