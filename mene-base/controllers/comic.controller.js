const comicModel = require("../models/comic.model");
const cateModel = require("../models/cate.model");
class ComicController {
  getComic(req, res, next) {
    comicModel
      .find({})
      .populate("CateID")
      .then(function (comicData) {
        if (comicData) {
          res.render("home/comicView/getComic", { comicData: comicData });
          //res.json(comicData)
        } else {
          res.status(501).json("Không tìm thấy người dùng" + err.message);
        }
      })
      .catch(function (error) {
        res.status(500).send("co loi xay ra", error.message);
      });
  }
  formPostComic(req, res) {
    cateModel
      .find({})
      .then(function (cateData) {
        res.render("home/comicView/formComic", { cateData: cateData });
      })
      .catch(function (error) {
        res.status(500).json("co loi xay ra", error.message);
      });
  }
  postComic(req, res, next) {
    const newComic = {
      name: req.body.name,
      description: req.body.description,
      author: req.body.author,
      year: req.body.year,
      coverImage: req.files.coverImage
        ? req.files.coverImage[0].filename
        : null,
      contentImages: req.files.contentImages
        ? req.files.contentImages.map((file) => file.filename)
        : [],
      CateID: req.body.CateID,
    };
    console.log(newComic);
    comicModel
      .create(newComic)
      .then((comicData) => {
        if (comicData) {
          res.redirect("/getComic");
          //res.json(comicData)
        } else {
          res.status(501).json("Không tìm thấy người dùng" + err.message);
        }
      })
      .catch((error) => {
        res.status(500).json("Lỗi lưu trữ: " + error.message);
      });
  }
  getDetailComic(req, res) {
    const id = req.params.id;
    comicModel
      .findById(id)
      .populate("CateID")
      .then(function (comicData) {
        if (comicData) {
          res.render("home/comicView/getDetailComic", {
            comicData: comicData,
            id: id,
          });
        } else {
          res.send("Không tìm thấy người dùng với ID: " + id);
        }
      })
      .catch((error) => {
        res.status(500).send("Có lỗi xảy ra: " + error.message);
      });
  }
  async readComic(req, res) {
    var id = req.params.id;
    var comicData = await comicModel.findById(id);
    if (comicData) {
      res.render("home/comicView/readComic", {
        comicData: comicData,
        contentImages: comicData.contentImages,
      });
    } else {
      res.send("Error");
    }

    res.render("home/comicView/readComic");
  }
  async commentComic(req, res) {
    const comicId = req.params.id;
    const userId = req.user.id;
    const username = req.user.Username;
    const content = req.body.content;
    try {
      // Tạo comment mới
      const newComment = {
        content: content,
        userId: userId,
        date: Date.now(),
        username: username,
      };
      // Cập nhật truyện
      await comicModel.findByIdAndUpdate(comicId, {
        $push: { commentObjects: newComment }, // Thêm comment mới vào truyện
      });
      res.redirect(`/getDetailComic/${comicId}`);
    } catch (error) {
      res.status(500).send("Lỗi khi bình luận: " + error.message);
    }
  }
  deleteComment(req, res) {
    const comicId = req.params.comicId;
    const commentId = req.params.commentId;
    comicModel
      .findByIdAndUpdate(comicId, {
        $pull: { commentObjects: { _id: commentId } }, // Xóa bình luận dựa trên commentId
      })
      .then(function (comicData) {
        if (!comicData) {
          res.status(500).json("Không tìm thấy truyện với ID: " + comicId);
        } else {
          res.redirect(`/getDetailComic/${comicId}`);
        }
      })
      .catch(function (error) {
        res.status(501).send("Xóa bình luận thất bại: " + error.message);
      });
  }
  getFormUpdateComic(req, res) {
    var id = req.params.id;
    comicModel
      .findById(id)
      .then(function (comicData) {
        res.render("home/comicView/formUpdateComic", { comicData: comicData });
      })
      .catch(function (error) {
        res.status(500).json("Không tìm thấy người dùng với ID: " + id);
      });
  }
  updateComic(req, res) {
    const id = req.params.id;
    const updateComic = {
      name: req.body.name,
      description: req.body.description,
      author: req.body.author,
      year: req.body.year,
      CateID: req.body.CateID,
    };
    // Update images only if new files were uploaded
    if (req.files["coverImage"] && req.files["coverImage"].length != 0) {
      updateComic.coverImage = req.files["coverImage"][0].filename;
    }
    if (req.files["contentImages"] && req.files["contentImages"].length != 0) {
      updateComic.contentImages = req.files["contentImages"].map(
        (file) => file.filename
      );
    }
    comicModel
      .findByIdAndUpdate(id, updateComic, { new: true })
      .then(function (comicData) {
        if (!comicData) {
          res.status(500).json("Không tìm thấy comicData với ID: " + id);
        } else {
          res.redirect("/getComic");
        }
      })
      .catch(function (error) {
        res.status(501).send("Cập nhật thất bại " + error.message);
      });
  }
  deleteComic(req, res) {
    const id = req.params.id;

    comicModel
      .findByIdAndDelete(id)
      .then((comicData) => {
        if (!comicData) {
          res.send("Không tìm sản phẩm với ID: " + id);
        } else {
          res.redirect("/getComic");
        }
      })
      .catch((error) => {
        res.send("Xóa thất bại " + error.message);
      });
  }
  searchComic(req, res) {
    const keyword = req.query.keyword;
    comicModel
      .find({ name: { $regex: keyword, $options: "i" } })
      .populate("CateID")
      .then(function (comicData) {
        if (comicData) {
          res.render("home/comicView/getComic", { comicData: comicData });
        } else {
          res.status(501).json("Không tìm thấy người dùng" + err.message);
        }
      })
      .catch(function (error) {
        res.status(500).send("Có lỗi xảy ra: " + error.message);
      });
  }

  
}

module.exports = new ComicController();
