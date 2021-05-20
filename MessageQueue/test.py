from py4j.java_gateway import JavaGateway
import array as arr
a = arr.array('i', [1, 2, 3, 4, 5, 6, 7, 8, 9])


gateway = JavaGateway()
# game_app = gateway.entry_point

random = gateway.jvm.java.util.Random()   # create a java.util.Random instance
number1 = random.nextInt(10)              # call the Random.nextInt method
number2 = random.nextInt(10)
print(number1, number2)


int_class = gateway.jvm.int
int_array = gateway.new_array(int_class,9)

int_array[0] = a[1]
int_array[1] = a[1]
int_array[2] = a[1]
int_array[3] = a[1]
int_array[4] = a[1]
int_array[5] = a[1]
int_array[6] = a[1]
int_array[7] = a[1]
int_array[8] = a[1]

game_app = gateway.entry_point
value = game_app.runGame(int_array)
print(value[0])