# Ex2

# Overview 
This project is an implementation of data structures and algorithms on directional weighted graph in Java.    
It is direct continuation of Ex1, you can find more abut Ex1 in [Here](https://github.com/ortrsa/ex1).  
in this project we will run the "pokemon game".  
###The game:  
the game server will place agent and pokemon with different values on directional weighted graph.  
Every edge have different weight and affects the agent speed.
###the goal:
the agent's will need to eat as many points as possible by eating many Pokemon's with high value.  
The main goal of this project is to program the most effective way to get as many points as possible with few calls to server.  

This repo contains the following files: [WikiPage](https://github.com/ortrsa/Ex2/wiki/Files). 
  
    

# How it's work..
This algorithm calculates the shortest path from each agent to the closest free pokemon.
I found that the easiest way to understand the implementation of this algorithm is with a flow chart:

![alt text](https://i.ibb.co/M5WHNcV/2020-12-20-17-07-53.png)

- Run(): run the thread.
- Init(): before each level load the relevant graph, open GUI class, first placed of Pokemon's and place  
 agents closest as possible to Pokemon's with high value.
- Is running: if the time ends stop the game and return the result, else keep moving agents.
- Move agent(): this method responsible for the course of the game, updates Pokemon's and agent location continuously,  
and call to all the following function...
- Deal With Eaten(): After getting updated information abput the Pokemons we have we check if the pokemons that the agents 
are chasing now are in the updated list of Pokemons and if not we know the agents ate them so we need to clear the agents
so they'll be free to chase other Pokemons and we need to remove the pokemons from the being chaased list as well so well know weren't chasing them any more.  
- Set Poke Catch(): Gets an agent and checks whos the closest Pokemon to this agent (only from the pokemons that aren't being chased yet) .
Then sets the agent List of nodes to the shortestpath to the pokemon location and now the agent will move thowards that pokemon.  
- NextNode(): Returns the next node the agent needs to go to get to his destination by going to the HashMap (PathMap) and look at the agent Path and every time removing the head
of the list and sending him to the first node on the list (because we removing the head every time he'll always need to go to the first node on the list)
then we set his edge by his src (the node his on right now) and his dest that we choosed from the list. we also changing the dt to reduce unnessecery calls from the server.  
              


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
- [Game result - Wiki page](https://github.com/ortrsa/Ex2/wiki/result)
- [Ex1](https://github.com/ortrsa/ex1)

# About:
This project is part of oop course of Ariel university and made for study purposes.  
This project made by Or Trabelsi and Nadav Epstein, for more information please contact me, email - ortrsa@gmail.com.



