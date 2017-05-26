import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Bons on 26/05/2017.
 */
public class kNearestNeighbours
{
    //Boolean array is used to check whether the attributes should be calced nominal
    private boolean[] isNominal;
    private float[][] trainingData;
    private float[][] testData;
    private int k;
    private int nonLabelAttributes;

    //Remember to insert normalized data
    //Last index is the class label
    public void Initiate(float[][] data, boolean[] isNominal,int k, float trainingPercent)
    {
        this.isNominal = isNominal;
        this.k = k;
        trainingData = Arrays.copyOfRange(data,0,(int)(data.length*trainingPercent));
        testData = Arrays.copyOfRange(data,(int)(data.length*trainingPercent),data.length);
        nonLabelAttributes = trainingData[0].length-1;

        System.out.println(String.format("Initiate %s-NN, with training set of %s tuples and test data of %s tuples",k,trainingData.length,testData.length));
        float[] testDataLabels = new float[testData.length];
        for(int i = 0; i<testData.length; i++)
        {
            testDataLabels[i] = isNominal[nonLabelAttributes] ? ClassifyDiscrete(FindNearestNeighbours(i)) : ClassifyContinuous(FindNearestNeighbours(i));
        }
        System.out.println(String.format("%s-NN Done",k));


    }
    //Returns majority vote.
    private float ClassifyDiscrete(Neighbour[] neighbours)
    {
        HashMap<Float, Float> labelMap = new HashMap<>();
        float label;
        //Calculating every  weight for each label
        for(int i= 0; i< neighbours.length;i++)
        {
            label = neighbours[i].label;

            if(labelMap.containsKey(label))
            {
                float val = labelMap.get(label);
                val += 1/neighbours[i].distance;
                labelMap.put(label, val);
            }
            else
                labelMap.put(label,1/neighbours[i].distance);

        }

        //Best fit
        float bestFit = 0;
        label = -1.0f;


        for(Iterator<Float> iter = labelMap.keySet().iterator();iter.hasNext();)
        {
            float currentLabel = iter.next();
            float weight = labelMap.get(currentLabel);
            if(weight > bestFit) {
                bestFit = weight;
                label = currentLabel;
            }
        }
        return label;
    }
    //Returns weighted average
    private float ClassifyContinuous(Neighbour[] neighbours)
    {
        HashMap<Float, Float> labelMap = new HashMap<>();
        float averageLabel;
        for(int i= 0; i< neighbours.length;i++)
        {
            averageLabel = neighbours[i].label;

            if(labelMap.containsKey(averageLabel))
            {
                float val = labelMap.get(averageLabel);
                val += 1/neighbours[i].distance;
                labelMap.put(averageLabel, val);
            }
            else
                labelMap.put(averageLabel,1/neighbours[i].distance);

        }

        float totalLabelWeighted = 0;
        for(Iterator<Float> iter = labelMap.keySet().iterator();iter.hasNext();)
        {
            totalLabelWeighted +=labelMap.get(iter.next());
        }
        averageLabel = totalLabelWeighted/(neighbours.length);

        return averageLabel;
    }


    private float Distance(float[] tuple1, float[]tuple2)
    {
        if(tuple1.length != tuple2.length)
        {
            System.out.println("ERROR! Tuples in kNN are not equal");
            return -1;
        }
        //Avoid sqrt to improve performance, therefore all distances are calculated squared
        float squaredDistance=0;

        for(int i = 0; i< nonLabelAttributes;i++)
        {
            //Evaluate nominal
            if(isNominal[i])
                squaredDistance+= tuple1[i] == tuple2[i] ? 0 : 1;
            //Evaluate numeric
            else
                squaredDistance += Math.pow(tuple1[i]-tuple2[i],2.0);

        }
        if(squaredDistance < 0)
        {
            System.out.println("ERROR! Distance calculation went wrong");
            return squaredDistance;
        }
        return squaredDistance;
    }


    //returns index of the K nearest neightsbors and their weight
    private Neighbour[] FindNearestNeighbours(int indexOfTuple)
    {
        Neighbour[] nearestNeighbours = new Neighbour[k];
        for(int i=0;i<k;i++)
        {
            nearestNeighbours[i] = new Neighbour();
        }
        int size = trainingData.length;
        float[] distance = new float[size];
        for(int i= 0; i< size;i++)
        {
            distance[i] = Distance(testData[indexOfTuple],trainingData[i]);
        }
        float[][] sortedDistance = BubbleSort(distance);
        for(int i = 0; i<k;i++)
        {
            nearestNeighbours[i].index = (int)sortedDistance[i][0];
            nearestNeighbours[i].distance = sortedDistance[i][1];
            nearestNeighbours[i].label = sortedDistance[i][2];

        }
        return nearestNeighbours;

    }

    private float[][] BubbleSort(float[] input)
    {
        int size =  input.length;
        float[][] res = new float[size][];
        for(int i =0;i<size;i++)
        {
            res[i] = new float[3];
            //index
            res[i][0] = i;
            //distance
            res[i][1] = input[i];
            //class label
            res[i][2] = trainingData[i][nonLabelAttributes];
        }
        Arrays.copyOf(input,size);

        boolean swapped = true;
        while(swapped)
        {
            swapped = false;
            for(int i = 1; i < size;i++)
            {
                if(res[i-1][1] >res[i][1])
                {


                    float temp = res[i][0];
                    res[i][0] = res[i-1][0];
                    res[i-1][0] = temp;

                    temp = res[i][1];
                    res[i][1] = res[i-1][1];
                    res[i-1][1] = temp;

                    temp = res[i][2];
                    res[i][2] = res[i-1][2];
                    res[i-1][2] = temp;

                    swapped = true;

                }
            }
        }
        return res;
    }

}

