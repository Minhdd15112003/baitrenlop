const mongoose = require('mongoose');

const ComicSchema = new mongoose.Schema({
  name: { type: String, required: true },
  description: { type: String },
  author: { type: String },
  year: { type: String },
  coverImage: { type: String },
  contentImages: [{ type: String }],
  CateID: { 
    type: mongoose.Schema.Types.ObjectId, 
    required: true,
    ref: "CateModel"
  },
});

const ComicModel = mongoose.model('Comic', ComicSchema);
module.exports = ComicModel;