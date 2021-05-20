****Tic Tac Toe simulator****

Uses Monte Carlo heuristics to play tic tac toe! 

Instructions for Running
1) navigate to project root file (/383-project) and execute `docker-compose build`
    (estimated time 1-2 minutes)
2) execute `docker-compose up`
    (estimated time 1.5-2 min)
    Wait until react application has started (should link you to localhost:3000)
    ![image](https://user-images.githubusercontent.com/9510917/119019980-926ce900-b952-11eb-962b-f659d19162a7.png)

    *Check that web-app has started correctly, if it fails, might need to perform a npm install within the front end folder* <br>
    *Check that server has required dependencies (navigate to server and run npm install)* <br>
    *Run general npm install (root level) to be safe* <br>
3) In a **new terminal**, navigate to subfolder /Game
4) Execute `javac -cp ./py4j0.10.9.1.jar Game.java`
5) Execute `java -cp ./py4j0.10.9.1.jar Game.java`


![image](https://user-images.githubusercontent.com/9510917/119020055-a57fb900-b952-11eb-9eb9-1175f1da01d5.png)
    
7) In a **new terminal**, navigate to subfolder /MessageQueue
8) Execute `python receive.py`
    
![image](https://user-images.githubusercontent.com/9510917/119020107-b5979880-b952-11eb-87c8-0a9db4775796.png)

8) Visit localhost:3000 in browser and play the game!
    

** [TODO] Add game and receiver execution to docker-compose (single command startup)

===============================================================

**Languages Used**
- Python (Receiver)
    - bridges gap between server and game logic handler
- Javascript
    - React web application (front end)
    - Node Express Server (back end)
- Java 
    - Game logic handler

**Cross Language Communication Methods**

- RabbitMQ
    - Communication between node express server (JS) and python receiver
        - 2-way communication (request and response)
- Py4j
    - Communication between python receiver and Java Game application
        - receiver calls Java `runGame` functionality and returns results from Game application, to reciever, to server, and back to web application
