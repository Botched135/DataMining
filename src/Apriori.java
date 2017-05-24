/**
 * Created by Bons on 17/04/2017.
 */
import java.util.*;
public class Apriori {
    private Tuple[] data;

    private int itemCount;
    private int Object;
    private int supportThreshold;
    private float[] highestKItemset;



    public void Initiate(Tuple[][] _data,int tupleIndex, int _supportThreshold)
    {
        Tuple[] apriori = new Tuple[_data.length];
        for(int i = 0; i < apriori.length;i++)
        {
            apriori[i] = _data[i][0];
        }
        data = Arrays.copyOf(apriori,apriori.length);
        supportThreshold = _supportThreshold;
        ArrayList<float[]> _firstItemset;
        switch (tupleIndex)
        {
            case 0:
                _firstItemset = FirstFrequentItemset(DataCleaner.ProgramTable);
                break;
            case 1:
                _firstItemset = FirstFrequentItemset(DataCleaner.PlayedGameTable);
                break;
            case 2:
                _firstItemset = FirstFrequentItemset(DataCleaner.MovementTable);
                break;
            case 3:
                //Four numbers... considerations...
                _firstItemset = FirstFrequentItemset(DataCleaner.ProgramTable);
                break;
            case 4:
                _firstItemset = FirstFrequentItemset(DataCleaner.FilmTable);
                break;
            case 5:
                _firstItemset = FirstFrequentItemset(DataCleaner.TVTable);
                break;
            case 6:
                _firstItemset = FirstFrequentItemset(DataCleaner.FavouriteGameTable);
                break;
            default:
                System.out.print("Wrong index number @ Apriori:Initiate");
                return;


        }
        FrequentItemsets(_firstItemset);


    }

    private ArrayList<float[]> FirstFrequentItemset(ReferenceTable _refTab)
    {
        _refTab.refMap.size();


        //Index [0]-[length-2] and [length-1] is frequency
        System.out.print("Create first itemsets\n");
        ArrayList<float[]> res = new ArrayList<>();
        for (Map.Entry<String,Float> entry: _refTab.refMap.entrySet()
             )
        {
            float[] input = new float[2];
            input[0] = entry.getValue();
            input[1] = 0;
            res.add(input);
        }
        System.out.print("Scanning database for candidate frequency\n");
        //Scan data base for frequency
        for (int i = 0; i< res.size();i++)
        {
            int frequency =0;
            for (Tuple tuple : data)
            {
                int addition = tuple.contains(res.get(i)[0]) ? 1 : 0;
                frequency += addition;
            }
            res.get(i)[1] = frequency;
        }

        System.out.print(String.format("Discarding items with frequency below %s support \n",supportThreshold));
        //Discard candiates that are below the frequency
        for(Iterator<float[]> iter = res.listIterator();iter.hasNext();){
           if(iter.next()[1] < supportThreshold)
              iter.remove();
        }

        System.out.print(String.format("Found %s frequent candiates",res.size()));
        return res;
    }

    private ArrayList<float[]> FrequentItemsets(ArrayList<float[]> _data)
    {
        ArrayList<float[]> output = new ArrayList<>();
        int kCurrentSize = _data.size();
        int kPreviousSize = _data.size()-1;
        //Join

        return output;
    }

}
