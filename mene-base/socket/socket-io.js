const objIO = require("socket.io")();

const socketAPI = {
    io: objIO,
}
objIO.on("connection", (socket) => {
    console.log("Kết nối với client thành công!");

    // Handle disconnection
    socket.on("disconnect", () => {
        console.log("Kết nối với client thất bại!");
    });
});

module.exports = socketAPI;