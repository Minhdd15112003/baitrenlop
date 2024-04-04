var mongoose = require("mongoose");
const uniqueValidator = require('mongoose-unique-validator');
var userSchema = new mongoose.Schema(
  {
    Email: { type: String, required: true, unique: true },
    Username: { type: String, required: true },
    Fullname: { type: String, required: true } ,
    Password: { type: String, required: true },
    Avatar: { type: String },

  },
  { collection: "accounts" }
);
userSchema.plugin(uniqueValidator);
const userModel = mongoose.model("accounts", userSchema);
module.exports = userModel;
