/**
 * Created by Bons on 17/04/2017.
 */
import java.util.*;
public class Apriori  {
    private Tuple[] data;

    private int itemCount;
    private int Object;
    private int supportThreshold;
    private float[] highestKItemset;



    public ArrayList<float[]> Initiate(Tuple[][] _data,int tupleIndex, int _supportThreshold)
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
                return null;


        }
        return FrequentItemsets(_firstItemset);


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
        //Sort the items to make later pattern mining easier.
        BubbleSort(res);
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


        System.out.print(String.format("Found %s frequent candiates\n",res.size()));
        return res;
    }

    private ArrayList<float[]> FrequentItemsets(ArrayList<float[]> _data)
    {
        ArrayList<float[]> output = new ArrayList<>();
        int kCurrentSize = _data.get(0).length+1;
        int kPreviousSize = _data.get(0).length;
        //Join
        System.out.print(String.format("Joining %s-Itemsets\n",kPreviousSize));
        for(int i = 0;i<_data.size();i++)
        {
            for(int j = i+1;j<_data.size();j++)
            {
                //Testing if k-2 items are equal for both itemsets
                float[]input = new float[kCurrentSize];
                boolean joinTest = true;
                for(int k = 0; k < kPreviousSize-2;k++)
                {
                    if(_data.get(i)[k] != _data.get(j)[k])
                    {
                        joinTest = false;
                    }
                }
                if(joinTest) {
                    for(int k = 0; k<kPreviousSize-1;k++)
                    {
                        input[k] =_data.get(i)[k];
                    }
                    input[kPreviousSize-1] = _data.get(j)[kPreviousSize-2];
                    output.add(input);
                }
            }

        }
        System.out.print(String.format("Joining resulted in %s Candidate Itemsets\n",output.size()));
        System.out.print(String.format("Prune with %s support \n",supportThreshold));
        //Scan database for frequency
        for(int i = 0; i< output.size();i++)
        {
            float[] temp =Arrays.copyOf(output.get(i),output.get(i).length-1);
            output.get(i)[kPreviousSize] = DatabaseScan(temp);
        }
        //Prune based on support
        for(Iterator<float[]> iter = output.listIterator();iter.hasNext();)
        {
            if(iter.next()[kPreviousSize] < supportThreshold)
                iter.remove();
        }
        //If the pruned list of frequent itemsets is not empty, search for k+1 items set.
        //else return k-1 frequent itemssets
        if(output.size() != 0)
            return FrequentItemsets(output);

        return _data;
    }

    private int DatabaseScan(float[] numbers)
    {
       int frequency =0;
       for (Tuple tuple : data)
       {
           int addition = tuple.contains(numbers) ? 1 : 0;
           frequency += addition;
       }
       return frequency;
    }

    private void BubbleSort(ArrayList<float[]> data)
    {
        int size = data.size();
        boolean swapped = true;
        while(swapped)
        {
            swapped = false;
            for(int i = 1; i<size-1;i++)
            {
                if(data.get(i-1)[0] > data.get(i)[0]) {
                    Collections.swap(data, i - 1, i);
                    swapped = true;
                }
            }
        }
    }

}
