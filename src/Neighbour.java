/**
 * Created by Bons on 26/05/2017.
 */
public class Neighbour {
    public float label;
    public float distance;
    public int index;
    Neighbour(float label, float distance, int index){
        this.label = label;
        this.distance = distance;
        this.index = index;
    }
    Neighbour(){
        label = -1;
        distance = -1;
        index = -1;
    }
}
