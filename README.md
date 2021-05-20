****Tic Tac Toe simulator****

Uses Monte Carlo heuristics to play tic tac toe! 

Instructions for Running
1) navigate to project root file (/383-project) and execute `docker-compose build`
    (estimated time 20-30 seconds)
2) execute `docker-compose up`
    (estimated time 1.5-2 min)
    Wait until react application has started (should link you to localhost:3000)
3) In a **new terminal**, navigate to subfolder /Game
4) Execute `javac -cp ./py4j0.10.9.1.jar Game.java`
5) Execute `java -cp ./py4j0.10.9.1.jar Game.java`
6) In a **new terminal**, navigate to subfolder /MessageQueue
7) Execute `python receive.py`

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
