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
    
        let Avatar = req.file ? req.file.filename : null;
        const newUsers = {
            Email: req.body.Email,
            Username: req.body.Username,
            Fullname: req.body.Fullname,
            Password: req.body.Password,
            Avatar: Avatar,
        };
        if(Avatar == null){
            newUsers.Avatar = "img/user.png";
        }
        console.log("====================================");
        console.log(newUsers);
        console.log("====================================");
        userModel
            .create(newUsers)
            .then((user) => {
                // res.redirect("/getUsers");
                res.status(401).json({ user: user, message: "Thêm thành công" });
            })
            .catch((error) => {
                if (error.name === "ValidationError") {
                    // Lỗi vi phạm tính chất duy nhất (unique)
                     res.status(501).send("Email đã tồn tại");
                } else {
                 res.status(502).send("Thêm thất bại " + error.message);
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
        const id = req.params.id; // ID của người dùng cần cập nhật
        // const id = req.params.id
        let Avatar = req.files ? req.files.filename : null;
        const updatedUser = {
            Email: req.body.Email,
            Username: req.body.Username,
            Fullname: req.body.Fullname,
            Password: req.body.Password,
            Avatar: Avatar,
        };
       console.log('====================================');
       console.log(updatedUser);
       console.log('====================================');
        userModel
            .findByIdAndUpdate(id, updatedUser, { new: true }) // Option { new: true } để trả về người dùng đã được cập nhật
            .then((user) => {
                if (!user) {
                    res.status(500).send("Không tìm thấy người dùng với ID: " + id);
                } else {
                    // res.redirect("/getUsers");
                    res.status(200).json(user);
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
                    //res.redirect("/getUsers");
                    res.json({ message: "Xóa thành công" });
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
                    res.status(501).send("không tìm thấy người dụng vơi Email " + user.Email);
                } else {
                    if (Password == user.Password) {
                        const token = generateToken(user);
                        res.cookie("user", token, {
                            httpOnly: true,
                            sameSite: "Strict",
                            maxAge: 1000 * 60 * 60 * 24,
                        });
                        // res.redirect("/getComic");
                        res.json(user);
                    } else {
                        res.status(500).send("Mật khẩu không chính xác");
                    }
                }
            })
            .catch((error) => {
                res.status(501).json("Có lỗi xảy ra khi tìm người dùng: " + error.message);
            });
    }
    logOut(req, res) {
        try {
            res.cookie("user", "", { maxAge: 1 });
            res.clearCookie("user");
            // res.redirect("/getFormLogin");
            res.json({ message: "Đăng xuất thành công" });
        } catch (error) {
            res.status(500).json({ error: "Có lỗi xảy ra: " + error.message });
        }
    }
}

module.exports = new UserController();
