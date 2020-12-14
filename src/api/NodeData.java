package api;

/**
 * This class implements node_data interface and has an inner class
 * named Geo_Location that implements geo_location interface.
 */
public class NodeData implements node_data, Comparable<node_data>  {
    private int key;
    private geo_location geo;
    private String Info;
    private int tag;
    private double Weight;
    private static int Ck;

    /**
     * Counstructors for NodeData thats gets every constructor
     * some of the thing required for a node and initialize the node by it.
     * @param X
     * @param Y
     * @param Z
     * @param key
     * @param Info
     * @param tag
     * @param Weight
     */
    public NodeData(double X, double Y, double Z,int key,String Info,int tag,double Weight){
        geo = new Geo_Location(X,Y,Z);
        this.key=key;
        this.Info=Info;
        this.tag=tag;
        this.Weight=Weight;
    }

    public NodeData(double X, double Y, double Z,int key){
    this.key=key;
    this.geo = new Geo_Location(X,Y,Z);
    this.Weight=-1;
    this.Info="";
    this.tag=-1;
}

    public NodeData(node_data n){
        this.key=n.getKey();
        this.Info=n.getInfo();
        this.tag=n.getTag();
        this.Weight=n.getWeight();
        this.geo=new Geo_Location(n.getLocation().x(),n.getLocation().y(),n.getLocation().z());
    }

    public NodeData(){
        this.key=Ck;
        Ck++;
        this.Info="";
        this.tag=-1;
        this.Weight=-1;
    }
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {

        return this.geo;
    }

    @Override
    public void setLocation(geo_location p) {
        this.geo=new Geo_Location(p.x(),p.y(),p.z());
    }

    @Override
    public double getWeight() {
        return Weight;
    }

    @Override
    public void setWeight(double w) {
        this.Weight=w;
    }

    @Override
    public String getInfo() {
        return Info;
    }

    @Override
    public void setInfo(String s) {
        this.Info=s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;
    }

    /**
     * Compares a node by his weight for a Priority Queue
     * lower weights will by at the peek of the queue.
     * @param o
     * @return
     */
    @Override
    public int compareTo(node_data o) {
        if (this.Weight < o.getWeight()) return -1;
        if (this.Weight > o.getWeight()) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "key=" + key +
                ", Weight=" + Weight +
                '}';
    }

    /**
     * Resets the counter for unique keys.
     */
    public void resetCk(){
        Ck=0;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inner class that implements geo_location interface.
     */
    public class Geo_Location implements geo_location{
        private double X,Y,Z;

        /**
         * Contractor for Geo location that gets X Y Z nad initialize the location.
         * @param X
         * @param Y
         * @param Z
         */
        public Geo_Location(double X,double Y,double Z){
            this.X=X;
            this.Y=Y;
            this.Z=Z;
        }
    @Override
    public double x() {
        return X;
    }

    @Override
    public double y() {
        return Y;
    }

    @Override
    public double z() {
        return Z;
    }

        /**
         * Returns the distance of a line between two dotes at R3 world.
         * @param g
         * @return
         */
    @Override
    public double distance(geo_location g) {
double dis =Math.sqrt((Math.pow(this.X-g.x(),2)+Math.pow(this.Y-g.y(),2)+Math.pow(this.Z-g.z(),2)));
        return dis;
    }
}
}
