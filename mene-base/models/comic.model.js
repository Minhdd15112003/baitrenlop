const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  name: { type: String, required: true },
  description: { type: String },
  author: { type: String },
  year: { type: Number },
  coverImage: { type: String },
  contentImages: [{ type: String }],
});

module.exports = mongoose.model('Comic', schema);
