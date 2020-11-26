package api;

public class Edge implements edge_data{
    private int Src;
    private int Dest;
    double Weight;
    String Info ;
    int Tag;

    public Edge(int src, int dest , double weight){
        this.Src = src;
        this.Dest = dest;
        this.Weight = weight;
        this.Tag = -1;
    }

    @Override
    public int getSrc() {

        return Src;
    }

    @Override
    public int getDest() {
        return Dest;
    }

    @Override
    public double getWeight() {
        return Weight;
    }

    @Override
    public String getInfo() {
        return Info;
    }

    @Override
    public void setInfo(String s) {
        this.Info = s;

    }

    @Override
    public int getTag() {
        return Tag;
    }

    @Override
    public void setTag(int t) {
        this.Tag = t;

    }
}
