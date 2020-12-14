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
    private Queue<edge_data> edgeQueue = new LinkedList<>();

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
        arena.setWGraph(ga);
        arena.setPokemons(Arena.json2Pokemons(game.getPokemons()));
        Frame = new MyFrame("Catch them all");
        Frame.setSize(1000,700);
        Frame.update(arena);
        Frame.show();

     List<CL_Pokemon> Pokeda =  arena.getPokemons();
//        ArrayList<CL_Pokemon> Pokeda = Arena.json2Pokemons(game.getPokemons());
        Iterator<CL_Pokemon> itr = Pokeda.iterator();
        PriorityQueue<CL_Pokemon> Pokemons = new PriorityQueue<>();//puts Pokemons in priority queue by their value
        arena.SetQueue(Pokemons);
        while (itr.hasNext()) {
            CL_Pokemon PickaTmp = itr.next();
            Arena.updateEdge(PickaTmp, ga.getGraph());
            Pokemons.add(PickaTmp);
        }

        Queue<CL_Pokemon> TmpQueue = new LinkedList<>();
        while(!Pokemons.isEmpty()){
            TmpQueue.add(Pokemons.poll());
        }
        Pokemons.addAll(TmpQueue);
        int TmpInt = 0;
        boolean flag = true;

        while (flag) {
            if (!TmpQueue.isEmpty()) {
                CL_Pokemon Pickachu = TmpQueue.poll();
                edge_data PickaEdge = Pickachu.get_edge();
                flag = game.addAgent(PickaEdge.getSrc());//Sets the agents at the src of and edge that the pokemon is on
                if (!flag) {
                    TmpInt++;
                }
                else {
                    edgeQueue.add(PickaEdge);
                   TmpInt++;
                }
            } else {
                flag = game.addAgent(TmpInt);//change
                TmpInt++;
            }
        }

    }
    @Override
    public void run() {
        int Level_Num = 11;
        game_service game = Game_Server_Ex2.getServer(Level_Num);
        init(game);
        game.startGame();
        Frame.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;
        while (game.isRunning()) {
            MoveAgents(game,arena.getGraph(),edgeQueue );

            try {
                if(ind%1==0) {Frame.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }
private static void MoveAgents (game_service game, directed_weighted_graph g, Queue<edge_data> edgeQ){
        List<CL_Agent> AgeLst = Arena.getAgents(game.move(),g);
        arena.setAgents(AgeLst);
       Iterator<CL_Agent> Agit= AgeLst.iterator();
       int i=0;
        while (!edgeQ.isEmpty()){
            Agit.next().SetEdge(edgeQ.poll());
        }
        List<CL_Pokemon> PokeList1 = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(PokeList1);
        arena.SetTmpPokeda();
        List<CL_Pokemon> PokeList = arena.GetPokeda();
        Iterator<CL_Agent> itr = AgeLst.iterator();
        while(itr.hasNext()) {
            CL_Agent TmpAgent = itr.next();
            arena.DealWithEaten(TmpAgent );
        }

    Iterator<CL_Pokemon> itr1 = PokeList.iterator();
        while(itr1.hasNext()){
            CL_Pokemon Pickchu = itr1.next();
            Arena.updateEdge(Pickchu,g);
            if(!arena.GetQueue().contains(Pickchu)){
            arena.GetQueue().add(Pickchu);
        }}
   int x=0;

       while(arena.isFree()&&!arena.GetQueue().isEmpty()){
           CL_Pokemon temp = arena.GetQueue().poll();
           arena.setFastest(temp);
       }


     Iterator<CL_Agent> iter = AgeLst.iterator();
       while(iter.hasNext()){
           CL_Agent TmpAgt = iter.next();
           int id = TmpAgt.getID();
           int dest =  TmpAgt.getNextNode();
           int src = TmpAgt.getSrcNode();
           double v = TmpAgt.getValue();

           if(dest==-1) {

               dest = nextNode(TmpAgt,src,g);
               game.chooseNextEdge(TmpAgt.getID(),dest);
               System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
           }
       }
    }


    public static int nextNode(CL_Agent tmpAgt, int src,directed_weighted_graph g  ) {
        arena.MoveHead(tmpAgt.getID());
        if(arena.getPathMap().get(tmpAgt.getID()).isEmpty()){
            arena.getPathMap().remove(tmpAgt.getID());
        arena.getFruitMap().remove(tmpAgt.getID());
        Iterator<edge_data> itr = g.getE(tmpAgt.getSrcNode()).iterator();
        return itr.next().getDest();}
        else{
            tmpAgt.SetEdge(g.getEdge(src, arena.getPathMap().get(tmpAgt.getID()).get(0).getKey()));// check src
            return arena.getPathMap().get(tmpAgt.getID()).get(0).getKey();
        }
    }

}


