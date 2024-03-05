var express = require("express");
var router = express.Router();
var homeCtrl = require("../controllers/home.controller");
var userCtrl = require("../controllers/user.controller");

/* GET, POST User. */
router.get("/getUsers", userCtrl.getUsers);
router.get("/getUser/:id", userCtrl.getIdUser)
//Login user 
// router.get("/getLoginUsersForm",userCtrl.getLoginUsersForm);
router.get("/getFormLogin",userCtrl.getLoginUsersForm);
router.post("/loginUser", userCtrl.loginUser);
//insert user 
router.get("/getInsertUsersForm",userCtrl.getInsertUsersForm);
router.post("/insertUser", userCtrl.insertUsers);
//update user
router.get("/getUpdateUsersForm/:id",userCtrl.getUpdateUsersForm);
router.post("/updateUser", userCtrl.updateUser);
//delete user
router.get('/deleteUser/:id',userCtrl.deleteUser);

/* GET home page. */
router.get("/", function (req, res, next) {
    res.render("home/index")
});

module.exports = router;
