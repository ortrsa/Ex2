package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;


public class Ex2 {
    public static void main(String[] args) {
        int i=17;
        game_service game = Game_Server_Ex2.getServer(i);
        dw_graph_algorithms ga = new DWGraph_Algo();


        //loads the graph of this level \/////////////////////////////////////////////


        Path path = Paths.get("output.txt");
        String contents = game.getGraph();
        try {
            Files.writeString(path, contents, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ga.load("output.txt");
System.out.println(ga.getGraph());
System.out.println(game);

        ArrayList<CL_Pokemon> Pokeda = Arena.json2Pokemons(game.getPokemons());
        Iterator<CL_Pokemon> itr = Pokeda.iterator();
        PriorityQueue<CL_Pokemon> Pokemons = new PriorityQueue<>();//puts Pokemons in priority queue by their value
        while (itr.hasNext()){
            CL_Pokemon PickaTmp = itr.next();
            PickaTmp.set_edge(Arena.GetPokEdge(ga.getGraph(),PickaTmp));
            Pokemons.add(PickaTmp);
        }

        int TmpInt=0;
        boolean flag=true;
        while(flag){
            if(!Pokemons.isEmpty()) {
                CL_Pokemon Pickachu = Pokemons.poll();
                edge_data PickaEdge = Pickachu.get_edge();
                flag = game.addAgent(PickaEdge.getSrc());//Sets the agents at the src of and edge that the pokemon is on
                game.chooseNextEdge(TmpInt,PickaEdge.getDest());
                TmpInt++;
            }
                else
                flag=game.addAgent(TmpInt);
                TmpInt++;
        }

        List<CL_Agent> Agentss = Arena.getAgents(game.getAgents(),ga.getGraph());
//        System.out.println(game.getAgents());

//game.startGame();
while (game.isRunning()){
//    String s = game.move();
//    System.out.println(s);



}

    }

}
