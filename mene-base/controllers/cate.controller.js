const cateModel = require("../models/cate.model")
const comicModel = require("../models/comic.model");
class cateController{
  //////////////////////////////// insertCate
  getFormCate(req, res, next) {
    cateModel
      .find({})
      .then(function (cateData) {
        res.render("home/cateView/formInsertCate", { cateData: cateData });
      })
      .catch(function (error) {
        res.status(500).json("co loi xay ra", error.message);
      });
  }
  insertCate(req, res, next) {
    const newCate = req.body;
    cateModel
      .create(newCate)
      .then(() => {
        res.redirect("/getFormCate");
      })
      .catch((error) => {
        res.status(500).json("Them that bai " + error.message);
      });
  }

  //////////////////////////////// deleteCate

  deleteCate(req, res) {
    const id = req.params.id;
    cateModel
      .findByIdAndDelete(id)
      .then((user) => {
        if (!user) {
          res.send("Không tìm thấy người dùng với ID: " + id);
        } else {
          res.redirect("/getFormCate");
        }
      })
      .catch((error) => {
        res.status(500).json("Xóa thất bại " + error.message);
      });
  }

  //////////////////////////////////////////////////////////////// getCategoryById
  getCategoryById(req, res, next) {
    const CateID = req.params.id;
    comicModel
      .find({ CateID: CateID })
      .then((comicData) => {
        if (comicData) {
          res.render("home/cateView/getComic", {
            comicData: comicData,
          });
        } else {
          res.status(500).json("Không tìm thấy sản phẩm trong danh mục: " + CateID);
        }
      })
      .catch((error) => {
        res.status(500).json("lỗi chức năng : " + error.message);
      });
  }
}

module.exports = new cateController;