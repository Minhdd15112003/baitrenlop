var mongoose = require("mongoose");
const uniqueValidator = require('mongoose-unique-validator');
var userSchema = new mongoose.Schema(
  {
    Email: { type: "string", required: true, unique: true },
    Username: { type: "string", required: true },
    Fullname: { type: "string", required: true },
    Password: { type: "string", required: true },

  },
  { collection: "accounts" }
);
userSchema.plugin(uniqueValidator);
var userModel = mongoose.model("accounts", userSchema);

module.exports = userModel;
