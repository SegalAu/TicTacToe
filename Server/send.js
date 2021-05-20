module.exports = {
  sendMQMessage(data) {
    var amqp = require('amqplib/callback_api');

    // if (args.length == 0) {
    //   console.log("Usage: rpc_client.js num");
    //   process.exit(1);
    // }

    amqp.connect('amqp://localhost', function(error0, connection) {
      if (error0) {
        throw error0;
      }
      connection.createChannel(function(error1, channel) {
        if (error1) {
          throw error1;
        }
        channel.assertQueue('', {
          exclusive: true
        }, function(error2, q) {
          if (error2) {
            throw error2;
          }
          var correlationId = generateUuid();
          var num = parseInt(data);

          console.log(' [x] Requesting fib(%d)', num);

          channel.consume(q.queue, function(msg) {
            if (msg.properties.correlationId == correlationId) {
              console.log(' [.] Got %s', msg.content.toString());
              return msg.content.toString();
              
            }
          }, {
            noAck: true
          });

          channel.sendToQueue('rpc_queue',
            Buffer.from(num.toString()),{ 
              correlationId: correlationId, 
              replyTo: q.queue });
        });
      });
    });

    function generateUuid() {
      return Math.random().toString() +
            Math.random().toString() +
            Math.random().toString();
    }

  }
};

