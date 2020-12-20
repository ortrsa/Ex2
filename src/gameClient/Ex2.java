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
    private static login_page login;
    private static Arena arena;
    private static int id;
    private static int level = -2;
    private static comparator c = new comparator();
    private Queue<edge_data> edgeQueue = new LinkedList<>();
    private String[] arg;
    private static long dt;



    public static void main(String[] args) {

        Thread client = new Thread(new Ex2(args));
        client.start();
    }

    public Ex2(String[] arg) {
        this.arg = arg;
    }

    /**
     * Sets the arena for the game by loading the graph, creating new Arena and set the Pokemons and grpah to it.
     * Sets the Frame thaht we see the graph in by the width and height we want and update the arena in it.
     * Creating a priority queue(by values and distance algorithm)  for the Pokemons and updating their edge by their location and type.
     * Then we are adding agents to the graph to the Pokemons edge srcs and if there more Agents then Pokemons were putting the spare
     * Agents randomly on the graph nodes. were also saving a tmp queue for the agents edges in the first run of "MoveAgent" function.
     *
     * @param game
     */
    private void init(game_service game) {
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
        Frame.setSize(1000, 700);
        Frame.update(arena);
        Frame.show();

        List<CL_Pokemon> Pokeda = arena.getPokemons();
        Iterator<CL_Pokemon> itr = Pokeda.iterator();

//        c.updategg(ga.getGraph());
        PriorityQueue<CL_Pokemon> Pokemons = new PriorityQueue<>(c);//puts Pokemons in priority queue by their value
        arena.SetQueue(Pokemons);
        while (itr.hasNext()) {
            CL_Pokemon PickaTmp = itr.next();
            Arena.updateEdge(PickaTmp, ga.getGraph());
            Pokemons.add(PickaTmp);
        }

        Queue<CL_Pokemon> TmpQueue = new LinkedList<>();
        while (!Pokemons.isEmpty()) {
            TmpQueue.add(Pokemons.poll());
        }
        Pokemons.addAll(TmpQueue);
        int TmpInt = ga.getGraph().nodeSize();
        boolean flag = true;

        while (flag) {
            if (!TmpQueue.isEmpty()) {
                CL_Pokemon Pickachu = TmpQueue.poll();
                edge_data PickaEdge = Pickachu.get_edge();
                flag = game.addAgent(PickaEdge.getSrc());
                if (flag) {
                    edgeQueue.add(PickaEdge);
                }
            } else {
                flag = game.addAgent((int) (Math.random() * TmpInt));
                TmpInt++;
            }
        }

    }

    /////////////////////////////////////////////
    @Override
    public void run() {

        if (arg.length != 2) {
            login = new login_page();
            login.register(this);
            login.show();

            int i = 1;
            while (level == -2) {
                while (i % 999999999 == 0) {
                    i = 1;
                    System.out.print(".");
                }
                i++;

            }
        } else {
            level = Integer.parseInt(arg[1]); /// need try catch
            id = Integer.parseInt(arg[0]);
        }


        game_service game = Game_Server_Ex2.getServer(level);
        System.out.println(game);
        System.out.println(game.getPokemons());
        System.out.println(game.getAgents());
        //  game.login(id);
        init(game);
        game.startGame();
        Frame.setTitle("Ex2 - OOP: (NONE trivial Solution)");
        int ind = 0;
        dt = 100;
        while (game.isRunning()) {
            MoveAgents(game, arena.getGraph(), edgeQueue);

            try {
                if (ind % 1 == 0) {
                    Frame.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    /**
     * This function is the main of the game here we decide how to move the agents on the graph.
     * every time were in this function first of all we use Arena functions to get the updated Agents and Pokemons Lists while moving the agents to their next node.
     * Then were checking if there are Pokemons that were targeted by agents before and no longer at the updtaed list of pokemons
     * if so it means we ate them and we need to "free" the agent so he can chase other Pokemons we do it in function "DealWithEaten"
     * then we check if there are Pokemons that arent targeted and not in the priority queue if so we add them to the priority queue.
     * then we check if there is free agent (IsFree function) and if there are Pokemons that arent been Targeted yet (if the queue isnt empty)
     * if so we poll the most valued pokemons and send it to setFastest function which choose the closer agent from the once who are free
     * and make him move towards the pokemon direction. then were checking if the agents are moving or they are on a node by their dest
     * if the dest is -1 we need to set their next node towards the pokemon their chasing by next node function that well explain below.
     *
     * @param game
     * @param g
     * @param edgeQ
     */
    private static void MoveAgents(game_service game, directed_weighted_graph g, Queue<edge_data> edgeQ) {
        List<CL_Agent> AgeLst = Arena.getAgents(game.move(), g);
        arena.setAgents(AgeLst);
        Iterator<CL_Agent> Agit = AgeLst.iterator();
        while (!edgeQ.isEmpty()) {
            Agit.next().SetEdge(edgeQ.poll());
        }
        List<CL_Pokemon> PokeList1 = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(PokeList1);

        arena.SetTmpPokeda();
        List<CL_Pokemon> PokeList = arena.GetPokeda();
        Iterator<CL_Agent> itr = AgeLst.iterator();
        while (itr.hasNext()) {
            CL_Agent TmpAgent = itr.next();
            arena.DealWithEaten(TmpAgent);//
        }
        Iterator<CL_Pokemon> itr1 = PokeList.iterator();
        while (itr1.hasNext()) {
            CL_Pokemon Pickchu = itr1.next();
            Arena.updateEdge(Pickchu, g);
        }
        Iterator<CL_Agent> itr2 = AgeLst.iterator();//
        while (arena.isFree() && arena.hasPoke()) {//
            CL_Agent Broke = itr2.next();//
            if (!arena.getFruitMap().containsKey(Broke.getID())) {//
                arena.SetPokeCatch(Broke);//
            }
        }
       double maxi = 0;
        for (CL_Agent max: AgeLst) {
            if(max.getSpeed()>maxi){
                maxi=max.getSpeed();
            }
        }

        Iterator<CL_Agent> iter = AgeLst.iterator();
        while (iter.hasNext()) {
            CL_Agent TmpAgt = iter.next();
            int id = TmpAgt.getID();
            int dest = TmpAgt.getNextNode();
            int src = TmpAgt.getSrcNode();
            double v = TmpAgt.getValue();

            if (dest == -1) {

                dest = nextNode(TmpAgt, src, g,maxi);
                game.chooseNextEdge(TmpAgt.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    /**
     * this method gets an agent and his current node key and needs to return where should he continue next to get to his pokemon as fast as possible.
     * we saved an hash map with key of integer (for the agents id's) and value of List<node_data>(the shortest path he needs to go to get to the wanted pokemon).
     * if the agent got to this function it means he finished his last edge so i can remove it from the list by MoveHead function which removes the first node at the list.
     * then i set the agent edge to src (the node his on right now) and the first node on the list (the node hes going to go ) then i return the dest of this edge key
     * and that is the node he is going.
     *
     * @param tmpAgt
     * @param src
     * @param g
     * @return
     */
    public static int nextNode(CL_Agent tmpAgt, int src, directed_weighted_graph g,double max) {
        arena.MoveHead(tmpAgt.getID());
        System.out.println("this is the DT : " + dt);
        System.out.println(tmpAgt.getSpeed());
        if (arena.getPathMap().get(tmpAgt.getID()).isEmpty()) {
            tmpAgt.setSpeed(tmpAgt.getSpeed() - 2);
            arena.GetBeingChased().remove(arena.getFruitMap().get(tmpAgt.getID()));
            arena.getPathMap().remove(tmpAgt.getID());
            arena.getFruitMap().remove(tmpAgt.getID());
            arena.GetBeingChased().remove(arena.getFruitMap().get(tmpAgt.getID()));//
            Iterator<edge_data> itr = g.getE(tmpAgt.getSrcNode()).iterator();
            return itr.next().getDest();
        } else {
            tmpAgt.SetEdge(g.getEdge(src, arena.getPathMap().get(tmpAgt.getID()).get(0).getKey()));

            if (max < 2){
              dt=180;
          }

            else if (max < 4) {
                dt = 120;
            }
            else if(max<5) dt=100;
            else {dt=83;}


            return arena.getPathMap().get(tmpAgt.getID()).get(0).getKey();
        }

    }

    /**
     * update id and level from login_page.
     *
     * @param level
     * @param id
     */

    public static void iupdate(int level, int id) {
        Ex2.id = id;
        Ex2.level = level;
    }


}


