const userModel = require("../models/user.model");
const { generateToken, auth } = require("../middleware/jwt");
class UserController {
  //////////////////////////////// getUsers
  getUsers(req, res, next) {
    userModel
      .find({})
      .then(function (userData) {
        res.render("home/userView/getUsers", { userData: userData });
        // res.json({ userData: userData});
      })
      .catch(function (error) {
        res.send("co loi xay ra", error.message);
      });
  }
  getIdUser(req, res, next) {
    const id = req.params.id;
    userModel.findById(id).then(function (userData) {
      if (userData) {
        res.json(userData);
      } else {
        res.send("Không tìm thấy người dùng với ID: " + id);
      }
    });
  }
  //////////////////////////////// insertUsers
  getInsertUsersForm(req, res, next) {
    res.render("home/userView/getFormRegister");
  }
  insertUsers(req, res, next) {
    const { Email, Username, Fullname, Password, PasswordCheck } = req.body;
    const newUsers = {
      Email: Email,
      Username: Username,
      Fullname: Fullname,
      Password: Password,
      PasswordCheck: PasswordCheck,
    };
    userModel
      .create(newUsers)
      .then((user) => {
        if (Password != PasswordCheck) {
          res.status(500).send("Mật khẩu không chính xác");
          //res.json({ user: user });
        } else {
          res.redirect("/getUsers");
        }
      })
      .catch((error) => {
        if (error.name === "ValidationError") {
          // Lỗi vi phạm tính chất duy nhất (unique)
          return res.status(500).send("Email đã tồn tại");
        } else {
          return res.status(500).send("Thêm thất bại " + error.message);
        }
      });
  }
  //////////////////////////////////////////////////////////////// updateUser
  getUpdateUsersForm(req, res, next) {
    const id = req.params.id;
    userModel
      .findById(id)
      .then(function (user) {
        if (user) {
          res.render("home/userView/updateUser", { user: user, id: id });
        } else {
          res.send("Không tìm thấy người dùng với ID: " + id);
        }
      })
      .catch(function (error) {
        res.send("Có lỗi xảy ra", error.message);
      });
  }
  updateUser(req, res, next) {
    const id = req.body.id; // ID của người dùng cần cập nhật
    // const id = req.params.id
    const updatedUser = {
      Email: req.body.Email,
      Username: req.body.Username,
      Fullname: req.body.Fullname,
      Password: req.body.Password,
    };
    userModel
      .findByIdAndUpdate(id, updatedUser, { new: true }) // Option { new: true } để trả về người dùng đã được cập nhật
      .then((user) => {
        if (!user) {
          res.status(500).send("Không tìm thấy người dùng với ID: " + id);
          // console.log("====================================");
          // console.log(req.body);
          // console.log("====================================");
        } else {
          res.redirect("/getUsers");
          //res.json(user);
        }
      })
      .catch((error) => {
        res.status(501).send("Cập nhật thất bại " + error.message);
      });
  }
  //////////////////////////////////////////////////////////////// deleteUser
  deleteUser(req, res) {
    const id = req.params.id;
    userModel
      .findByIdAndDelete(id)
      .then((user) => {
        if (!user) {
          res.send("Không tìm thấy người dùng với ID: " + id);
        } else {
          res.redirect("/getUsers");
        }
      })
      .catch((error) => {
        res.send("Xóa thất bại " + error.message);
      });
  }
  //////////////////////////////////////////////////////////////// getLoginUsersForm
  getLoginUsersForm(req, res) {
    res.render("home/userView/getFormLogin");
  }
  loginUser(req, res) {
    const { Email, Password } = req.body;
    userModel
      .findOne({ Email: Email })
      .then((user) => {
        if (!user) {
          res
            .status(501)
            .send("không tìm thấy người dụng vơi Email " + user.Email);
        } else {
          if (Password == user.Password) {
            const token = generateToken(user);
            res.cookie("user", token, {
              httpOnly: true,
              sameSite: "Strict",
              maxAge: 1000 * 60 * 60 * 24,
            });
            res.redirect("/");
            //res.json(user);
          } else {
            res.status(500).send("Mật khẩu không chính xác");
          }
        }
      })
      .catch((error) => {
        res
          .status(500)
          .json("Có lỗi xảy ra khi tìm người dùng: " + error.message);
      });
  }
  logOut(req, res) {
    res.cookie("user", "", { maxAge: 1 });
    res.redirect("/");
  }
}

module.exports = new UserController();
