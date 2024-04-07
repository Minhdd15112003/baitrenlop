const objIO = require("socket.io")();

const socketAPI = {
    io: objIO,
}

objIO.on("connection", (socket) => {
    console.log('====================================');
    console.log('Connected to socket.io'+ socket.id);
    console.log('====================================');
});

module.exports = socketAPI;