/**
 * Created by Bons on 17/04/2017.
 */
import java.util.*;
public class Apriori {
    private float[][] data;

    private int itemCount;
    private int Object;
    private int supportThreshold;



 /*   public List<float[]> Initiate(float[][] _data, int _supportThreshold)
    {
        data = Arrays.copyOf(_data,_data.length);
        supportThreshold = _supportThreshold;
    }
*/
    private List<Float> FirstItemset(ReferenceTable _refTab)
    {
        _refTab.refMap.size();
        List<Float> res = new ArrayList<Float>();
        for (Map.Entry<String,Float> entry: _refTab.refMap.entrySet()
             )
        {
            res.add(entry.getValue());
        }
        return res;
    }
    /*
    private List<float[]> FrequentItemsets()
    {

    }

*/
}
