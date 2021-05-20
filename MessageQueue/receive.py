import pika, json
import array as arr

# Use py4j to access Java Game Logic
from py4j.java_gateway import JavaGateway
from py4j.java_collections import ListConverter

#Timing Issue


# # Handles Communication between Server.py and Game Logic in Golang (Game.go)
# # ===========================================================================

def process(data):
  gateway = JavaGateway()
  game_app = gateway.entry_point

  int_class = gateway.jvm.int
  int_array = gateway.new_array(int_class,9)

  int_array[0] = data[0]
  int_array[1] = data[1]
  int_array[2] = data[2]
  int_array[3] = data[3]
  int_array[4] = data[4]
  int_array[5] = data[5]
  int_array[6] = data[6]
  int_array[7] = data[7]
  int_array[8] = data[8]

  value = game_app.runGame(int_array)
  print("value at index 3 is: ")
  print(value[2])

  data[0] = value[0]
  data[1] = value[1]
  data[2] = value[2]
  data[3] = value[3]
  data[4] = value[4]
  data[5] = value[5]
  data[6] = value[6]
  data[7] = value[7]
  data[8] = value[8]
  print("return value from process is : ")
  print(data)
  return data


# ===========================================================================



# Handles Requests from web_app
# ===========================================================================
connection = pika.BlockingConnection(
  pika.ConnectionParameters(host='localhost'))

channel = connection.channel()

channel.queue_declare(queue='rpc_queue')


def on_request(ch, method, props, body):
    # n = int(body)

    # print(" [.] fib(%s)" % n)

     # Get request from body
    # package response as variable
    data = json.loads(body)
    print("Received Data :")
    print(data)

    # response = process(body)
    processedData = process(data)

    # response = bytearray(processedData)
    response = json.dumps(processedData)

   
    print("Server: Received request message from web_app")
    request = body
    # write(request, PATH_PYTHON_GO)
    print("Server: Forwarding request to Game")
    # response = read(PATH_PYTHON_GO)
    print("Server: Received response from Game: ")
    print(response)

    

    print("Server: Sending response web_app")
    ch.basic_publish(exchange='',
                     routing_key=props.reply_to,
                     properties=pika.BasicProperties(correlation_id = \
                                                         props.correlation_id),
                     body=response)

    ch.basic_ack(delivery_tag=method.delivery_tag)

channel.basic_qos(prefetch_count=1)
channel.basic_consume(queue='rpc_queue', on_message_callback=on_request)

print(" [x] Awaiting RPC requests")
channel.start_consuming()
# # ===========================================================================