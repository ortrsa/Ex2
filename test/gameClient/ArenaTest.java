package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.game_service;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {
    public Arena SetArena(){
        game_service game = Game_Server_Ex2.getServer(11);
        Arena arena = new Arena();
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.load("data/A2");
        arena.setWGraph(ga);
        String s ="{\"Pokemons\":[{\"Pokemon\":{\"value\":5.0,\"type\":-1,\"pos\":\"35.20273974670703,32.10439601193746,0.0\"}},{\"Pokemon\":{\"value\":8.0,\"type\":-1,\"pos\":\"35.189541903742466,32.10714473742062,0.0\"}},{\"Pokemon\":{\"value\":13.0,\"type\":1,\"pos\":\"35.198546018801096,32.10442041371198,0.0\"}},{\"Pokemon\":{\"value\":5.0,\"type\":-1,\"pos\":\"35.20418622066997,32.10618391544376,0.0\"}},{\"Pokemon\":{\"value\":9.0,\"type\":-1,\"pos\":\"35.207511563168026,32.10516145234799,0.0\"}},{\"Pokemon\":{\"value\":12.0,\"type\":-1,\"pos\":\"35.19183431463849,32.106897389061444,0.0\"}}]}";
        arena.setPokemons(arena.json2Pokemons(s));
        Iterator<CL_Pokemon> itr = arena.getPokemons().iterator();
        while(itr.hasNext()){
            arena.updateEdge(itr.next(),ga.getGraph());
        }
       game.addAgent(10);
        game.addAgent(20);
        game.addAgent(21);
        game.startGame();
        List<CL_Agent> AgeLst = Arena.getAgents(game.move(), ga.getGraph());
        arena.setAgents(AgeLst);
     System.out.println(arena.getAgents().size());
        return arena;
    }



    @Test
    void isFree() {

    }

    @Test
    void setPokeCatch() {
Arena arena = SetArena();
arena.SetPokeCatch(arena.getAgents().get(0));
int i =0;
while(arena.getPathMap().get(i)!=null){
    System.out.println(arena.getPathMap().get(i));
    i++;
}
    }

    @Test
    void dealWithEaten() {
    }

    @Test
    void hasPoke() {
    }
}