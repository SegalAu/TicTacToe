# TicTacToe
Tic Tac Toe simulator
Uses Monte Carlo heuristics to play tic tac toe!
Instructions for Running


navigate to project root file (/383-project) and execute docker-compose build
(estimated time 20-30 seconds)


execute docker-compose up
(estimated time 1.5-2 min)
Wait until react application has started (should link you to localhost:3000)


In a new terminal, navigate to subfolder /Game


Execute javac -cp ./py4j0.10.9.1.jar Game.java


Execute java -cp ./py4j0.10.9.1.jar Game.java


In a new terminal, navigate to subfolder /MessageQueue


Execute python receive.py


Visit localhost:3000 in browser and play the game!


================================================================================================
Languages Used

Python (Receiver)

bridges gap between server and game logic handler


Javascript

React web application (front end)
Node Express Server (back end)


Java

Game logic handler



Cross Language Communication Methods

RabbitMQ

Communication between node express server (JS) and python receiver

2-way communication (request and response)




Py4j

Communication between python receiver and Java Game application

receiver calls Java runGame functionality and returns results from Game application, to reciever, to server, and back to web application
