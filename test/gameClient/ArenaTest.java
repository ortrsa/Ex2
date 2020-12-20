package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.game_service;
import api.node_data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        arena.setGraph(ga.getGraph());
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
        return arena;
    }
    @Test
    void isFree() {
        Arena arena = SetArena();
        arena.SetPokeCatch(arena.getAgents().get(0));
        arena.SetPokeCatch(arena.getAgents().get(1));
        arena.SetPokeCatch(arena.getAgents().get(2));
        assertFalse(arena.isFree());
        arena.getPokemons().remove(arena.getFruitMap().get(0));
        arena.DealWithEaten(arena.getAgents().get(0));
        assertTrue(arena.isFree());
    }

    @Test
    void setPokeCatch() {
        Arena arena = SetArena();
        arena.SetPokeCatch(arena.getAgents().get(0));
        List<node_data> CrntList = new ArrayList<>();
        CrntList.add(arena.getGraph().getNode(10));
        CrntList.add(arena.getGraph().getNode(9));
        CrntList.add(arena.getGraph().getNode(23));
        assertEquals(arena.getPathMap().get(0).get(0),CrntList.get(0),"should be the same path for the shortest path to the closet pokemon");
        assertEquals(arena.getPathMap().get(0).get(1),CrntList.get(1),"should be the same path for the shortest path to the closet pokemon");
        assertEquals(arena.getPathMap().get(0).get(2),CrntList.get(2),"should be the same path for the shortest path to the closet pokemon");
    }

    @Test
    void dealWithEaten() {
        Arena arena = SetArena();
        arena.SetPokeCatch(arena.getAgents().get(0));
        arena.DealWithEaten(arena.getAgents().get(0));
        assertNotNull(arena.getFruitMap().get(0));
        arena.getPokemons().remove(arena.getFruitMap().get(0));
        arena.DealWithEaten(arena.getAgents().get(0));
        assertNull(arena.getFruitMap().get(0));
    }



}