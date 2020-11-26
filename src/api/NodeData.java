package api;

public class NodeData implements node_data  {
    private int key;
    private geo_location geo;
    private String Info;
    private int tag;
    private double Weight;
    private int Ck;

    public NodeData(double X, double Y, double Z){
//        geo = new DWGraph_DS.Geo_Location(X,Y,Z);
        this.key=Ck;
        Ck++;
        this.Info="";
        this.tag=-1;
        this.Weight=-1;
    }

    public NodeData(){
        this.key=Ck;
        Ck++;
        this.Info="";
        this.tag=-1;
        this.Weight=0;
    }
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {

        return geo;
    }

    @Override
    public void setLocation(geo_location p) {
//        this.geo=new DWGraph_DS.Geo_Location(p.x(),p.y(),p.z());
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
}
