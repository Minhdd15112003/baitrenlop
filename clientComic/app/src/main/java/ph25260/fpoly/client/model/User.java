package ph25260.fpoly.client.model;

public class User {
    String _id;
    String Email;
    String Username;
    String Fullname;
    String Password;
    String Avatar;

    public User(String _id, String email, String username, String fullname, String password, String avatar) {
        this._id = _id;
        Email = email;
        Username = username;
        Fullname = fullname;
        Password = password;
        Avatar = avatar;
    }
    public User() {
    }

    public User(String email, String username, String fullname, String password) {
        Email = email;
        Username = username;
        Fullname = fullname;
        Password = password;
    }

    public User(String email, String password) {
        Email = email;
        Password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
