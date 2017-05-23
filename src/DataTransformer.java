import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Bons on 16/04/2017.
 */
public class DataTransformer {

    private DataCleaner cleanRef;
    private float[] sortingArray;

    public void SetDC(DataCleaner _dc)
    {
        cleanRef = _dc;
    }
    private float[] Sort(float[] _input)
    {
        sortingArray = Arrays.copyOf(_input,_input.length);
        QuickSort(0,sortingArray.length-1);
        return sortingArray;
    }
    private void QuickSort(int startIndex, int endIndex) {
        int i = startIndex;
        int j = endIndex;
        // calculate pivot number, I am taking pivot as middle index number
        float pivot = sortingArray[startIndex + (endIndex - startIndex) / 2];
        // Divide into two arrays
        while (i <= j) {

            while (sortingArray[i] < pivot) {
                i++;
            }
            while (sortingArray[j] > pivot) {
                j--;
            }
            if (i <= j) {
                float temp = sortingArray[i];
                sortingArray[i] = sortingArray[j];
                sortingArray[j] = temp;
                i++;
                j--;
            }
        }
        if (startIndex < j)
            QuickSort(startIndex, j);
        if (i < endIndex)
            QuickSort(i, endIndex);
    }
    public float[] Normalize(float[] _input, int index)
    {
        float[] res = _input;
        float[] sortRes = Sort(res);

        if(index == 5 || index == 6 || index == 8)
        {
            float normVal = NormalValForOther(GetCorrectRef(index));
            for(int i = 0;i< res.length;i++)
            {
                res[i] = res[i]/normVal;
            }
            return res;
        }
        for(int i = 0;i< res.length;i++)
        {
            res[i] = (res[i]-sortRes[0])/(sortRes[sortRes.length-1]-sortRes[0]);
        }

        return res;
    }
    private ReferenceTable GetCorrectRef(int index)
    {
        if(index == 5)
            return cleanRef.StudyProgrammeTable;
        if(index == 6)
            return cleanRef.CourseChoiceTable;
        return cleanRef.PhoneOSTable;
    }
    private float NormalValForOther(ReferenceTable ref)
    {
        float max = ref.startPoint+1;
        Collection<Float> floats = ref.refMap.values();
        for (Float f : floats) {
            if (f > max)
                max = f;
        }

        return max;
    }

    public float[] Smoothing(float[] _input)
    {
        float[] res = new float[_input.length];
        float[] sortRes = Sort(_input);
        float[] fivePointSum = FivePointSum(sortRes,0);
        float IQR = IQR(sortRes);
        float[][] bins = new float[sortRes.length/5][5];
        int[] sortOrder = revertSortArray(_input,sortRes);
        for (int i = 0; i < bins.length;i++)
        {
            bins[i][0] = sortRes[(i*5)];
            bins[i][1] = sortRes[(i*5)+1];
            bins[i][2] = sortRes[(i*5)+2];
            bins[i][3] = sortRes[(i*5)+3];
            bins[i][4] = sortRes[(i*5)+4];
        }
        for (int i = 0; i < bins.length;i++)
        {
            float median = Median(bins[i]);

            for(int j = 0; j < bins[i].length;j++)
            {
                if(bins[i][j] > sortRes[(int)fivePointSum[3]]+(IQR*1.5f) || bins[i][j] < sortRes[(int)fivePointSum[1]]-(IQR*1.5f))
                    bins[i][j] = median;
            }
        }
        for(int i = 0; i< sortRes.length;i++)
            sortRes[i]=bins[i/5][i%5];

        for(int i = 0;i < res.length;i++)
            res[sortOrder[i]] = sortRes[i];


        return res;
    }

    private float Median(float[] input)
    {
        float res;

        if(input.length%2 == 1)
        {
            int index = input.length / 2;
            res = input[index];
        }
        else
        {
            int index = input.length/2;
            res = (input[index]+input[index-1])/2;
        }
        return res;

    }
    private float IQR(float[] input)
    {
        return Quantile(input,false)-Quantile(input,true);
    }
    private float[] FivePointSum(float[] input, float index)
    {
        //MUST BE SORTED
        float[] res = new float[6]; //[5] is index number
        res[5] = index;
        res[0] = input[0];//Min
        res[1] = Quantile(input, true); //Q1
        res[2] = Median(input);
        res[3] = Quantile(input, false);// Q3
        res[4] = input[input.length-1];

        return res;
    }
    private int Quantile(float[] input, boolean QuantileOne)
    {
        int res;
        int roundUpTest;
        float quantPoint = input.length;
        if(QuantileOne)
        {
            quantPoint = quantPoint/4.0f;
            roundUpTest = (int)quantPoint;
            if(quantPoint > roundUpTest)
                res = roundUpTest +1;
            else
                res = roundUpTest;
        }
        else
        {
            quantPoint = (quantPoint * (3.0f / 4.0f));
            roundUpTest = (int) quantPoint;
            if (quantPoint > roundUpTest)
                res = roundUpTest + 1;
            else
                res = roundUpTest;
        }

        return res;
    }

    private int[] revertSortArray(float[] input, float[] sortedInput) {
        int[] reverse = new int[input.length];
        boolean[] visited = new boolean[input.length];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
        for (int i = 0; i < reverse.length; i++) {
            for (int j = 0; j < reverse.length; j++) {
                if (input[i] == sortedInput[j] && !visited[j]) {
                    reverse[j] = i;
                    visited[j] = true;
                    break;
                }
            }
        }
        return reverse;
    }

    public float[][] SetupTransArray(float[][] _input)
    {
        float[][] res = new float[_input[0].length][_input.length];

        for (int i = 0; i < res.length; i++)
        {
            for (int j = 0; j < res[0].length; j++)
            {
                res[i][j] = _input[j][i];
            }
        }

        return res;
    }
    public float[] HandleMissingValue(float[] input)
    {
        float[] res = new float[input.length];
        float[] sortedRes = Sort(Arrays.copyOf(input,input.length));
        float Median = Median(sortedRes);
        for(int i = 0; i< res.length;i++)
        {
            if(input[i] == -1)
                res[i] = Median;
            else
                res[i] = input[i];

        }

        return res;
    }
    public void NormalizeTuples(Tuple[][] input)
    {
        for (int i =0;i<input.length;i++)
        {
            for(int j=0;j<input[i].length;j++)
            {
                if(j == 7 || j == 20 || j ==21 || j == 37 || (j >= 40 && j <= 42))
                    input[i][j].Normalize(j, cleanRef);

            }
        }
    }
}
