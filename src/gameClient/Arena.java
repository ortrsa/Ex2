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
    private List<String> _info;
    private static Point3D MIN = new Point3D(0, 100, 0);
    private static Point3D MAX = new Point3D(0, 100, 0);
    private PriorityQueue<CL_Pokemon> PriorPoke = new PriorityQueue<>();
    private HashMap<Integer, CL_Pokemon> fruitMap = new HashMap<>();
    private HashMap<Integer, List<node_data>> pathMap = new HashMap<>();
//    private List<node_data> CrntPath;


    public Arena() {
        ;
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
    }//init();}

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

    //	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
//		geo_location src = g.getNode(s).getLocation();
//		geo_location dest = g.getNode(d).getLocation();
//		return isOnEdge(p,src,dest);
//	}
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

    public void SetQueue(PriorityQueue<CL_Pokemon> queue) {
        this.PriorPoke = queue;
    }

    public boolean isFree() {
        if (fruitMap.isEmpty()) return true;
        Iterator<Integer> itr = fruitMap.keySet().iterator();
        while (itr.hasNext()) {
            if (fruitMap.get(itr.next()) == null) {
                return true;
            }
        }
        return false;
    }

    public void setFastest(CL_Pokemon Picka) {
        boolean flag = true;
        Iterator<CL_Agent> itr = _agents.iterator();
       double dis3;
        double dis = -1;
        CL_Agent FastestAge = null;
        while (itr.hasNext()) {
            CL_Agent TmpAgent = itr.next();
            if (!fruitMap.containsKey(TmpAgent.getID())) {
                if(TmpAgent.getNextNode()==-1){System.out.println("hay");dis3 =ga.shortestPathDist(TmpAgent.getSrcNode(),Picka.get_edge().getSrc());}
               else{ if (TmpAgent.getSrcNode() == Picka.get_edge().getSrc()) {
                    FastestAge = TmpAgent;

                    break;
                }
                System.out.println(TmpAgent.get_curr_edge() + "  Agentedge");
                System.out.println(TmpAgent.getValue());
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
System.out.println("hay hay");
        fruitMap.put(FastestAge.getID(), Picka);
        System.out.println("bin za lza");
        if(FastestAge.getNextNode()==-1)
            pathMap.put(FastestAge.getID(),SetNewPath(FastestAge,Picka.get_edge().getSrc(), Picka.get_edge().getDest()));
        else
        pathMap.put(FastestAge.getID(), SetCrntPath(FastestAge, Picka.get_edge().getSrc(), Picka.get_edge().getDest()));
//        System.out.println("asdasdasd");
//System.out.println(ga.getGraph().getEdge(FastestAge.getSrcNode(),pathMap.get(FastestAge.getID()).get(0).getKey()));

    }

    public List<node_data> SetCrntPath(CL_Agent Age,int src,int dest){
        List<node_data> CrntPath= ga.shortestPath(Age.get_curr_edge().getDest(),src);
        CrntPath.add(ga.getGraph().getNode(dest));
        return CrntPath;
    }

    public boolean DealWithEaten(CL_Agent TmpAgent) {
        if (!_pokemons.contains(fruitMap.get(TmpAgent.getID()))) {
            fruitMap.remove(TmpAgent.getID());
            pathMap.remove(TmpAgent.getID());
            return true;

        } else {
            _pokemons.remove(fruitMap.get(TmpAgent.getID()));
            return false;
        }
    }

    public void MoveHead(int tmpAgt) {
        pathMap.get(tmpAgt).remove(0);
    }

    public HashMap<Integer, CL_Pokemon> getFruitMap() {
        return fruitMap;
    }
    public List<node_data> SetNewPath (CL_Agent Age,int src ,int dest){
        List<node_data> Crnt = ga.shortestPath(Age.getSrcNode(),src);
        Crnt.add(ga.getGraph().getNode(dest));
        return Crnt;
    }
}
