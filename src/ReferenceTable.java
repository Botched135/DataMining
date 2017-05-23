import java.util.*;
/**
 * Created by Bons on 15/04/2017.
 */
public class ReferenceTable
{

    ReferenceTable(float startPos)
    {
        this.startPoint = startPos-1;

    }

    public float startPoint;

    public HashMap<String,Float> refMap = new HashMap<String, Float>();
    public HashMap<String,Float> normMap= new HashMap<String, Float>();

    float InsertAndGet(String _input)
    {
        String loweredInput = _input.toLowerCase();
        Boolean testKey =refMap.containsKey(loweredInput);
        if(testKey != null && testKey)
        {
            return refMap.get(loweredInput);
        }
        startPoint++;
        refMap.put(loweredInput,startPoint);

        return refMap.get(loweredInput);
    }
    public void IntoNorm()
    {
        Set<String> _keys = refMap.keySet();
        float maxVal =0;
        for (String str:_keys)
        {
            float input = refMap.get(str);
            if(input > maxVal)
                maxVal = input;
        }
        for (String str:_keys)
        {
            float input = refMap.get(str);
            normMap.put(str, input/maxVal);
        }

    }
}
