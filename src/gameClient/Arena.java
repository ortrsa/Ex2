package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 *
 * @author boaz.benmoshe
 */
public class Arena {
    public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1, EPS = EPS2;
    private directed_weighted_graph _gg;
    private dw_graph_algorithms ga;
    private List<CL_Agent> _agents;
    private List<CL_Pokemon> _pokemons;
    private List<CL_Pokemon> _pokemons2;
    private List<CL_Pokemon> BeingChased = new ArrayList<>();
    private List<String> _info;
    private static Point3D MIN = new Point3D(0, 100, 0);
    private static Point3D MAX = new Point3D(0, 100, 0);
  //  private static comparator c = new comparator();
    private PriorityQueue<CL_Pokemon> PriorPoke;// = new PriorityQueue<>(c);
    private HashMap<Integer, CL_Pokemon> fruitMap = new HashMap<>();
    private HashMap<Integer, List<node_data>> pathMap = new HashMap<>();
//    private List<node_data> CrntPath;


    public Arena() {
        _info = new ArrayList<String>();
    }

    private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
        _gg = g;
        this.setAgents(r);
        this.setPokemons(p);
    }

    public void setPokemons(List<CL_Pokemon> f) {
        this._pokemons = f;
    }

    public void setAgents(List<CL_Agent> f) {
        this._agents = f;
    }

    public void setWGraph(dw_graph_algorithms g) {
        this.ga = g;
    }

    public void setGraph(directed_weighted_graph g) {
        this._gg = g;
    }

    private void init() {
        MIN = null;
        MAX = null;
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        Iterator<node_data> iter = _gg.getV().iterator();
        while (iter.hasNext()) {
            geo_location c = iter.next().getLocation();
            if (MIN == null) {
                x0 = c.x();
                y0 = c.y();
                x1 = x0;
                y1 = y0;
                MIN = new Point3D(x0, y0);
            }
            if (c.x() < x0) {
                x0 = c.x();
            }
            if (c.y() < y0) {
                y0 = c.y();
            }
            if (c.x() > x1) {
                x1 = c.x();
            }
            if (c.y() > y1) {
                y1 = c.y();
            }
        }
        double dx = x1 - x0, dy = y1 - y0;
        MIN = new Point3D(x0 - dx / 10, y0 - dy / 10);
        MAX = new Point3D(x1 + dx / 10, y1 + dy / 10);

    }

    public List<CL_Agent> getAgents() {
        return _agents;
    }

    public List<CL_Pokemon> getPokemons() {
        return _pokemons;
    }


    public directed_weighted_graph getGraph() {
        return _gg;
    }

    public List<String> get_info() {
        return _info;
    }

    public void set_info(List<String> _info) {
        this._info = _info;
    }

    ////////////////////////////////////////////////////
    public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
        ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for (int i = 0; i < ags.length(); i++) {
                CL_Agent c = new CL_Agent(gg, 0);
                c.update(ags.get(i).toString());
                ans.add(c);
            }
            //= getJSONArray("Agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  c.update(ans);
        return ans;
    }

    public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
        ArrayList<CL_Pokemon> ans = new ArrayList<CL_Pokemon>();
        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray ags = ttt.getJSONArray("Pokemons");
            for (int i = 0; i < ags.length(); i++) {
                JSONObject pp = ags.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                //double s = 0;//pk.getDouble("speed");
                String p = pk.getString("pos");
                CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
                ans.add(f);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
        //	oop_edge_data ans = null;
        Iterator<node_data> itr = g.getV().iterator();
        while (itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while (iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
                if (f) {
                    fr.set_edge(e);
                }
            }
        }
    }

    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {

        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS2) {
            ans = true;
        }
        return ans;
    }

    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if (type < 0 && dest > src) {
            return false;
        }
        if (type > 0 && src > dest) {
            return false;
        }

        return isOnEdge(p, g.getNode(src).getLocation(), g.getNode(dest).getLocation());
    }

    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        while (itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) {
                    x0 = p.x();
                }
                if (p.x() > x1) {
                    x1 = p.x();
                }
                if (p.y() < y0) {
                    y0 = p.y();
                }
                if (p.y() > y1) {
                    y1 = p.y();
                }
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }

    public PriorityQueue<CL_Pokemon> GetQueue() {
        return this.PriorPoke;
    }

    public HashMap<Integer, List<node_data>> getPathMap() {
        return pathMap;
    }
    public void SetTmpPokeda(){
        _pokemons2 = new ArrayList<>();
        _pokemons2.addAll(_pokemons);
    }
    public List<CL_Pokemon> GetPokeda (){
        return _pokemons2;
    }

    public void SetQueue(PriorityQueue<CL_Pokemon> queue) {
        this.PriorPoke = queue;
    }

    /**
     * this Function returns true only if one of the agents on agents List isn't "chasing" a pokemon at hte moment .
     * were checking every agent if there is an agent that his id isnt on "fruitmap" HashMap if so it means hes free.
     * @return
     */
    public boolean isFree() {
        if (fruitMap.isEmpty()) return true;
        Iterator<CL_Agent> itr = _agents.iterator();
        while (itr.hasNext()) {
            if (!fruitMap.containsKey(itr.next().getID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * this methos gets a pokemon and sets the Agent that will get the fastest to the pokemon from the free agents.
     * we are checking all the free agents if there are on a node (their get next node returns -1) we just check their distance to the pokemon src node and divide it by agent speed.
     * if the agent is on a middle of an edge were checking how far is the dest of that edge from him plus from that edge dest how far to the pokemon src then we divide it by his speed.
     * then we take the minimum of all the results we got and we set the agent in the hashmap of fruitmap so well know he isnt available no more.
     * and we check the shortest path he needs to go with SetCrntpath function and we save the List at the PthMap hashmap we the agent id.
     * @param Picka
     */
    public void setFastest(CL_Pokemon Picka) {
        Iterator<CL_Agent> itr = _agents.iterator();
       double dis3;
        double dis = -1;
        CL_Agent FastestAge = null;
        while (itr.hasNext()) {
            CL_Agent TmpAgent = itr.next();
            if (!fruitMap.containsKey(TmpAgent.getID())) {
                if(TmpAgent.getNextNode()==-1){dis3 =ga.shortestPathDist(TmpAgent.getSrcNode(),Picka.get_edge().getSrc())/TmpAgent.getSpeed();}
               else{ if (TmpAgent.getSrcNode() == Picka.get_edge().getSrc()) {
                    FastestAge = TmpAgent;

                    break;
                }

                double dis2 = _gg.getNode(TmpAgent.get_curr_edge().getDest()).getLocation().distance(TmpAgent.getLocation());
                if (dis2 == _gg.getEdge(TmpAgent.getSrcNode(), TmpAgent.getNextNode()).getWeight())
                    dis2 = 0;
                double dis1 = ga.shortestPathDist(TmpAgent.get_curr_edge().getDest(), Picka.get_edge().getSrc());
                dis3 = (dis2 + dis1) / TmpAgent.getSpeed();}
                if (dis == -1 || dis3 < dis) {
                    FastestAge = TmpAgent;
                    dis = dis3;
                }
            }
        }
        fruitMap.put(FastestAge.getID(), Picka);
        if(FastestAge.getNextNode()==-1)
            pathMap.put(FastestAge.getID(),SetNewPath(FastestAge,Picka.get_edge().getSrc(), Picka.get_edge().getDest()));
        else
        pathMap.put(FastestAge.getID(), SetCrntPath(FastestAge, Picka.get_edge().getSrc(), Picka.get_edge().getDest()));

    }


     public void SetPokeCatch(CL_Agent Ash){
        Iterator<CL_Pokemon>itr = _pokemons.iterator();
        double dis =-1;
        double dis1,dis2;
        double MinDis=-1;
        CL_Pokemon Squirtel=null;
        while(itr.hasNext()){
            CL_Pokemon PickaTmp = itr.next();
        if(!BeingChased.contains(PickaTmp)){
            if(Ash.getNextNode()==-1){
                dis = ga.shortestPathDist(Ash.getSrcNode(),PickaTmp.get_edge().getSrc());
            }
            else{
                dis1 = ga.getGraph().getNode(Ash.get_curr_edge().getDest()).getLocation().distance(Ash.getLocation());
                dis2 = ga.shortestPathDist(Ash.get_curr_edge().getDest(),PickaTmp.get_edge().getSrc());
                dis =dis1+dis2;
            }
            if(MinDis==-1||dis<MinDis){
                MinDis=dis;
                Squirtel = PickaTmp;
            }
         }}
        fruitMap.put(Ash.getID(),Squirtel);
        BeingChased.add(Squirtel);
         if(Ash.getNextNode()==-1)
             pathMap.put(Ash.getID(),SetNewPath(Ash,Squirtel.get_edge().getSrc(), Squirtel.get_edge().getDest()));
         else
             pathMap.put(Ash.getID(), SetCrntPath(Ash, Squirtel.get_edge().getSrc(), Squirtel.get_edge().getDest()));     }

    /**
     * This function returns a list of nodes the shortest path between an agent to dest node but he has to go throw src node.
     * we send the aggent location node and src to shortestPath function and then we add dest node and we have the shortest path we need to eat the wanted pokemon.
     * @param Age
     * @param src
     * @param dest
     * @return
     */
    public List<node_data> SetCrntPath(CL_Agent Age,int src,int dest){
        List<node_data> CrntPath= ga.shortestPath(Age.get_curr_edge().getDest(),src);
        CrntPath.add(ga.getGraph().getNode(dest));
        return CrntPath;
    }

    /**
     * This method gets an agent and checks if the pokemon he is chasing is on the updated list of pokemons
     * if not it means he ate it and we need to clear the hashmaps so well know the agent is free to catch other pokemons.
     * @param TmpAgent
     * @return
     */
    public void DealWithEaten(CL_Agent TmpAgent) {
        if (!_pokemons.contains(fruitMap.get(TmpAgent.getID()))) {
            fruitMap.remove(TmpAgent.getID());
            pathMap.remove(TmpAgent.getID());

        } else {
            _pokemons2.remove(fruitMap.get(TmpAgent.getID()));
        }
    }

    public void DealWithEaten2(CL_Agent TmpAgent) {
        if (!_pokemons.contains(fruitMap.get(TmpAgent.getID()))) {
            BeingChased.remove(fruitMap.get(TmpAgent.getID()));
            fruitMap.remove(TmpAgent.getID());
            pathMap.remove(TmpAgent.getID());

        }
//        else {
//            _pokemons2.remove(fruitMap.get(TmpAgent.getID()));
//        }
    }

    /**
     * Removes the first node on a list.
     * @param tmpAgt
     */
    public void MoveHead(int tmpAgt) {
        pathMap.get(tmpAgt).remove(0);
    }

    public HashMap<Integer, CL_Pokemon> getFruitMap() {
        return fruitMap;
    }
    /**
     * This function returns a list of nodes the shortest path between an agent to dest node but he has to go throw src node.
     * we send the aggent location node and src to shortestPath function and then we add dest node and we have the shortest path we need to eat the wanted pokemon.
     * @param Age
     * @param src
     * @param dest
     * @return
     */
    public List<node_data> SetNewPath (CL_Agent Age,int src ,int dest){
        List<node_data> Crnt = ga.shortestPath(Age.getSrcNode(),src);
        Crnt.add(ga.getGraph().getNode(dest));
        return Crnt;
    }
public List<CL_Pokemon> GetBeingChased(){
        return BeingChased;
}
    public boolean hasPoke() {
        if(_pokemons.size()==BeingChased.size()){
            return false;
        }
        return true;
    }
}
