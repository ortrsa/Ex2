package api;

public class Edge implements edge_data{
    private int Src;
    private int Dest;
    private double Weight;
    private String Info ;
    private int Tag;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Src == edge.getSrc() &&
                Dest == edge.getDest() &&
                Weight == edge.getWeight();
    }

    @Override
    public String toString() {
        return "Edge{" +
                "Src=" + Src +
                ", Dest=" + Dest +
                ", Weight=" + Weight +
                '}';
    }
}
