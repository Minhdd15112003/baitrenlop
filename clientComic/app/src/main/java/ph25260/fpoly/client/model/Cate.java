package ph25260.fpoly.client.model;

public class Cate {
    String _id;
    String Cate;

    public Cate(String _id, String cate) {
        this._id = _id;
        Cate = cate;
    }
    public Cate() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCate() {
        return Cate;
    }

    public void setCate(String cate) {
        Cate = cate;
    }
}
