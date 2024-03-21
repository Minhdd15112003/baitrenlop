var express = require("express");
var multer = require("multer");
var router = express.Router();
var { generateToken, auth } = require("../middleware/jwt");
var { cpUpload, storage } = require("../middleware/utils");
var userCtrl = require("../controllers/user.controller");
var comicCtrl = require("../controllers/comic.controller");
var cateCtrl = require("../controllers/cate.controller");
/* GET, POST User. */
router.get("/getUsers", auth, userCtrl.getUsers);
router.get("/getUserByID/:id", userCtrl.getIdUser);
//Login user
// router.get("/getLoginUsersForm",userCtrl.getLoginUsersForm);
router.get("/getFormLogin", userCtrl.getLoginUsersForm);
router.post("/loginUser", userCtrl.loginUser);
//logOUt
router.get("/logOut", userCtrl.logOut);
//insert user
router.get("/getInsertUsersForm", userCtrl.getInsertUsersForm);
router.post("/insertUser", userCtrl.insertUsers);
//update user
router.get("/getUpdateUsersForm/:id", userCtrl.getUpdateUsersForm);
router.post("/updateUser", userCtrl.updateUser);
//delete user
router.get("/deleteUser/:id", userCtrl.deleteUser);

/* GET, POST Comic. */
router.get("/getComic",auth, comicCtrl.getComic);
router.post("/postComic", cpUpload, comicCtrl.postComic);
router.get("/formPostComic", comicCtrl.formPostComic);
router.get("/deleteComic/:id", comicCtrl.deleteComic);
router.get('/getDetailComic/:id/getFormUpdateComic', comicCtrl.getFormUpdateComic);
router.post("/updateComic/:id", cpUpload, comicCtrl.updateComic);
router.get("/getDetailComic/:id", auth, comicCtrl.getDetailComic);
router.get("/getDetailComic/:id/readComic", comicCtrl.readComic);
//commentComic     
router.post("/getDetailComic/:id/commentComic", auth, comicCtrl.commentComic);
router.get("/deleteComment/:comicId/:commentId", auth, comicCtrl.deleteComment);
/* GET, POST Category. */
router.get("/getCategoryById/:id", cateCtrl.getCategoryById);
router.get("/getFormCate", cateCtrl.getFormCate);
router.post("/insertCate", cateCtrl.insertCate);
router.get("/deleteCate/:id", cateCtrl.deleteCate);
/* GET Seach page. */
router.get('/searchComic', comicCtrl.searchComic);
/* GET home page. */
router.get("/", function (req, res, next) {
  res.render("home/index");
});

module.exports = router;
