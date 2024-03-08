const mongoose = require("mongoose");

const cateSchema = new mongoose.Schema(
  {
    Cate: {
      type: String,
      required: true,
    },
    // Comic:{
    //   type: mongoose.Schema.Types.ObjectId, 
    //   required: true,
    //   ref: "ComicModel"
    // }
  },
  { collection: "categories" }
);
const CateModel = mongoose.model('CateModel', cateSchema);
module.exports = CateModel
