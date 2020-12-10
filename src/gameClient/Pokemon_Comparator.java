package gameClient;

import java.util.Comparator;

public class Pokemon_Comparator implements Comparator<CL_Pokemon> {


    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {
        if (o1.getValue()<o2.getValue()){return 1;}
        if(o1.getValue()>o2.getValue()){return -1;}
        return 0;
    }
}
