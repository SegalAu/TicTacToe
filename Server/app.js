const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const sendHelper = require("./send");


const port = process.env.PORT || 4001;
const index = require("./routes/index");

const app = express();

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.use(index);

const server = http.createServer(app);

const io = socketIo(server);

let interval;

io.on("connection", (socket) => {
  console.log("New client connected");
  if (interval) {
    clearInterval(interval);
  }
  interval = setInterval(() => getApiAndEmit(socket), 1000);

  socket.on("move", (data) => {
    console.log("got data: " + data);

    // Call JS function for MessageQueue

    var amqp = require('amqplib/callback_api');

    // if (args.length == 0) {
    //   console.log("Usage: rpc_client.js num");
    //   process.exit(1);
    // }

    // amqp.connect('amqp://localhost', function(error0, connection) {
    amqp.connect('amqp://guest:guest@host.docker.internal:5672', function(error0, connection) {
      if (error0) {
        throw error0;
      }
      connection.createChannel(function(error1, channel) {
        // if (error1) {
        //   throw error1;
        // }
        if (error1) {

        } else {
          channel.assertQueue('', {
            exclusive: true
          }, function(error2, q) {
            if (error2) {
              throw error2;
            }
            var correlationId = generateUuid();
            // var num = parseInt(data);
  
            // console.log(' [x] Requesting fib(%d)', num);
  
            console.log("request body is: " );
            console.log(data);
  
            channel.consume(q.queue, function(msg) {
              if (msg.properties.correlationId == correlationId && msg != undefined) {
  
                console.log("the value of response is : " );
  
                returnList = JSON.parse(msg.content);
                console.log(returnList);
  
  
                io.sockets.emit("move_update", returnList);
                
              }
            }, {
              noAck: true
            });
  
            var json = JSON.stringify(data);
  
            channel.sendToQueue('rpc_queue',
              Buffer.from(json),{ 
                correlationId: correlationId, 
                replyTo: q.queue });
          });
        }
       
      });
    });

    function generateUuid() {
      return Math.random().toString() +
            Math.random().toString() +
            Math.random().toString();
    }   
  });


  socket.on("disconnect", () => {
    console.log("Client disconnected");
    clearInterval(interval);
  });
});

const getApiAndEmit = socket => {
  const response = new Date();
  // Emitting a new message. Will be consumed by the client
  socket.emit("FromAPI", response);
};

server.listen(port, () => console.log(`Listening on port ${port}`));