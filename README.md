# Ex2

# Overview 
This project is an implementation of data structures and algorithms on directional weighted graph in Java.    
It is direct continuation of Ex1, you can find more abut Ex1 in [Here](https://github.com/ortrsa/ex1).  
in this project we will run the "pokemon game".  
The game: the game server will place agent and pokemon with different values on directional weighted graph.  
Every edge have different weight and effect the agent speed.
the goal: the agent's will need to eat as mach point as possible by eating many Pokemon's with high value.  
The main goal of this project is to program the most effective way to get as many points as possible with few calls to server.  

This repo contains the following files: wiki page...
  
    

# How it's work..
This algorithm calculates the shortest path(by weight) from a starting node to all other nodes in a graph.
This implementation also contains a method that can return the shortest path to a specified node.
I found that the easiest way to understand the implementation of this algorithm is with a flow chart:

![alt text](https://i.ibb.co/DV2Mscd/2020-12-15-20-53-12.png)

To understand the algorithm in depth I recommend watching this video: [Dijkstra's Algorithm - Computerphile](https://www.youtube.com/watch?v=GazC3A4OQTE&feature=youtu.be) 



# How To Run
This program has a graphical interface.  
first clone the repo:
```sh
$ git clone https://github.com/ortrsa/Ex2.git

```
then there are 2 option that you can run it:  
1- with your commend line, navigate to the repo folder and enter this code:  
(replace "ID" with your ID and "Level" with the chosen Level. )
 ```sh
 $ java -jar Ex2.jar "ID" "Level"
 
 ```
2- double-click on the Ex2.jar file, enter Id and level at the text field and press start game.  

# Links:
- [Dijkstra's Algorithm - Wikipedia](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
- [Directed_graph - Wikipedia](https://en.wikipedia.org/wiki/Directed_graph)
- [Wiki Test Run Time](https://github.com/ortrsa/Ex1_oop/wiki/Tests-run-time)
- [Ex1](https://github.com/ortrsa/ex1)

# About:
This project is part of oop course of Ariel university and made for study purposes.  
This project made by Or Trabelsi and Nadav Epshtain, for more information please contact me, email - ortrsa@gmail.com.



