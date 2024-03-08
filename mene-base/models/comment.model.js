const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  comicId: { type: mongoose.Types.ObjectId, ref: 'Comic' },
  userId: { type: mongoose.Types.ObjectId, ref: 'User' },
  content: { type: String, required: true },
  date: { type: Date, default: Date.now },
});

const ComentModel = mongoose.model('Comment', schema);
module.exports = ComentModel;
