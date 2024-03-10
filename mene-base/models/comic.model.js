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
  commentObjects: [{
    content: { type: String, required: true },
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'userModel', required: true },
    username:{ type: mongoose.Schema.Types.String, ref: 'userModel', required: true },
    date: { type: Date, default: Date.now },
  }],
});

const ComicModel = mongoose.model('Comic', ComicSchema);
module.exports = ComicModel;