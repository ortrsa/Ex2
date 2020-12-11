package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Ex2 implements Runnable {
    private static MyFrame Frame;
    private static Arena arena;

    public static void main(String[] args) {
        Thread client = new Thread(new Ex2());
        client.start();
    }
    private void init(game_service game){
        dw_graph_algorithms ga = new DWGraph_Algo();
        Path path = Paths.get("output.txt");
        String contents = game.getGraph();
        try {
            Files.writeString(path, contents, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ga.load("output.txt");

        arena = new Arena();
        arena.setGraph(ga.getGraph());
        arena.setPokemons(Arena.json2Pokemons(game.getPokemons()));
        Frame = new MyFrame("Catch them all");
        Frame.setSize(1000,700);
        Frame.update(arena);
        Frame.show();

     List<CL_Pokemon> Pokeda =  arena.getPokemons();
//        ArrayList<CL_Pokemon> Pokeda = Arena.json2Pokemons(game.getPokemons());
        Iterator<CL_Pokemon> itr = Pokeda.iterator();
        PriorityQueue<CL_Pokemon> Pokemons = new PriorityQueue<>();//puts Pokemons in priority queue by their value
        while (itr.hasNext()) {
            CL_Pokemon PickaTmp = itr.next();
            Arena.updateEdge(PickaTmp, ga.getGraph());
            Pokemons.add(PickaTmp);
        }
        Queue<CL_Pokemon> TmpQueue = new LinkedList<>();
        int TmpInt = 0;
        boolean flag = true;
        while (flag) {
            if (!Pokemons.isEmpty()) {
                CL_Pokemon Pickachu = Pokemons.poll();
                edge_data PickaEdge = Pickachu.get_edge();
                TmpQueue.add(Pickachu);
                flag = game.addAgent(PickaEdge.getSrc());//Sets the agents at the src of and edge that the pokemon is on
                if (!flag) {
                    TmpQueue.remove(Pickachu);
                    Pokemons.add(Pickachu);
                    TmpInt++;
                }
                else {
                    game.chooseNextEdge(TmpInt, PickaEdge.getDest());
                    TmpInt++;
                }
            } else {
                flag = game.addAgent(TmpInt);
                TmpInt++;
            }
        }

    }
    @Override
    public void run() {
        int Level_Num = 20;
        game_service game = Game_Server_Ex2.getServer(Level_Num);
        init(game);
//        dw_graph_algorithms ga = new DWGraph_Algo();
//
//
//        //loads the graph of this level \/////////////////////////////////////////////
//
//
//        Path path = Paths.get("output.txt");
//        String contents = game.getGraph();
//        try {
//            Files.writeString(path, contents, StandardCharsets.UTF_8);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        ga.load("output.txt");
//
//
//        ArrayList<CL_Pokemon> Pokeda = Arena.json2Pokemons(game.getPokemons());
//        Iterator<CL_Pokemon> itr = Pokeda.iterator();
//        PriorityQueue<CL_Pokemon> Pokemons = new PriorityQueue<>();//puts Pokemons in priority queue by their value
//        while (itr.hasNext()) {
//            CL_Pokemon PickaTmp = itr.next();
//            Arena.updateEdge(PickaTmp, ga.getGraph());
//            Pokemons.add(PickaTmp);
//        }
//
//        /////////////////////////////////////////
//        //creating a stack for current fruit for every agent
//
//        Queue<CL_Pokemon> TmpQueue = new LinkedList<>();
//        int TmpInt = 0;
//        boolean flag = true;
//        while (flag) {
//            if (!Pokemons.isEmpty()) {
//                CL_Pokemon Pickachu = Pokemons.poll();
//                edge_data PickaEdge = Pickachu.get_edge();
//                TmpQueue.add(Pickachu);
//                flag = game.addAgent(PickaEdge.getSrc());//Sets the agents at the src of and edge that the pokemon is on
//                if (!flag) {
//                    TmpQueue.remove(Pickachu);
//                    Pokemons.add(Pickachu);
//                    TmpInt++;
//                }
//                else {
//                    game.chooseNextEdge(TmpInt, PickaEdge.getDest());
//                    TmpInt++;
//                }
//            } else
//                flag = game.addAgent(TmpInt);
//            TmpInt++;
//        }
//        System.out.println(game.getAgents());
//
//
//        List<CL_Agent> Agents = Arena.getAgents(game.getAgents(), ga.getGraph());
//        Iterator<CL_Agent> ItrAge = Agents.iterator();
//        while (ItrAge.hasNext()) {
//            CL_Agent TmpAge = ItrAge.next();
//            TmpAge.set_curr_fruit(TmpQueue.poll());
//        }

        ///////////////////////////////////////////////////
        //Starts the game
//game.startGame();
        while (game.isRunning()) {
            System.out.println(game.move());

        }

    }
private static void MoveAgents (game_service game, directed_weighted_graph g){
        List<CL_Agent> AgeLst = Arena.getAgents(game.move(),g);
        arena.setAgents(AgeLst);
        List<CL_Pokemon> PokeList = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(PokeList);
}
}


