const objIO = require("socket.io")();

const socketAPI = {
    io: objIO,
}
objIO.on("connection", (socket) => {
    console.log("Kết nối với client thành công!");
});

module.exports = socketAPI;