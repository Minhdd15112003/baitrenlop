const jwt = require("jsonwebtoken");
const { token } = require("morgan");

const generateToken = (user) => {
  const payload = {
    id: user.id,
    Email: user.Email,
    Username: user.Username,
  };
  const token = jwt.sign(payload, process.env.JWT_SECRET, {
    expiresIn: "1h",
  });
  return token;
};

const auth = (req, res, next) => {
  const userCookie = req.cookies["user"];

  if (!userCookie) {
    return res
      .status(401)
      .json("Bạn cần đăng nhập để thực hiện chức năng này!");
  }

  try {
    const decoded = jwt.verify(userCookie, process.env.JWT_SECRET); 
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(401).json("Token không hợp lệ!");
  }
};

module.exports = { generateToken, auth };
