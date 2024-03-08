var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
var partials = require("express-partials");
var mongoose = require("mongoose");
var multer = require("multer");
var cateModel = require("./models/cate.model");
var indexRouter = require("./routes/index");

var app = express();
// middleware Category
app.use(function (req, res, next) {
  cateModel
    .find({})
    .then(function (cateData) {
      // Thêm cateData vào res.locals
      res.locals.cateData = cateData;
      // Gọi next() để tiếp tục xử lý yêu cầu
      next();
    })
    .catch(function (error) {
      res.send("co loi middleware Category xay ra", error.message);
    });
});
app.use(express.static("uploads"));
// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "ejs");

app.use(logger("dev"));
app.use(express.json());
app.use(partials());
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

app.use("/", indexRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

mongoose
  .connect(process.env.MONGODB_CONNECTION_STRING)
  .then((success) => console.log("Connected to mongodb server!"))
  .catch((err) =>
    console.log("Error, Couldn't connect to mongodb server!!!\n> Stack: " + err)
  );

module.exports = app;
