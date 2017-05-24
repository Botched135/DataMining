import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * The CSVFileReader class is used to load a csv file
 *
 */
public class CSVFileReader {
	/**
	 * The read method reads in a csv file as a two dimensional string array.
	 * This method is utilizes the string.split method for splitting each line of the data file.
	 * String tokenizer bug fix provided by Martin Marcher.
	 * @param csvFile File to load
	 * @param seperationChar Character used to seperate entries
	 * @param nullValue What to insert in case of missing values
	 * @return Data file content as a 2D string array
	 * @throws IOException
	 */
	public static String[][] readDataFile(String csvFile, String seperationChar, String nullValue, boolean skipHeaderRow) throws IOException
	{
		
		List<String[]> lines = new ArrayList<String[]>();
		BufferedReader bufRdr = new BufferedReader(new FileReader(new File(csvFile)));
		
		// read the header
		String line = bufRdr.readLine();
		
		while ((line = bufRdr.readLine()) != null) {
			String[] arr = line.split(seperationChar); 
			
			for(int i = 0; i < arr.length; i++)
			{
				if(arr[i].equals(""))
				{
					arr[i] = nullValue;
				}				
			}
			if(!skipHeaderRow)
			{
				lines.add(arr);
			}			
		}
		
		String[][] ret = new String[lines.size()][];
		bufRdr.close();
		return lines.toArray(ret);
	}
	
	public static void main(String args[]) {
		DataCleaner dataClean = new DataCleaner();
		DataTransformer dataTrans = new DataTransformer();
		Apriori aprioriObj = new Apriori();
		dataClean.Init();

		try {
			String[][] data = readDataFile("Data Mining - Spring 2017.csv",";;", "-",false);
			//Make a Tuple[] for each data set, and a tuple for each data type that needs a tuple
			Tuple[][] tupHolder= new Tuple[data.length][7];
			for(int i = 0;i < tupHolder.length;i++)
			{
				for(int j =0;j < tupHolder[i].length;j++)
				{
					tupHolder[i][j] = new Tuple();
				}
			}
			data = dataClean.RemoveQuotations(data);
			float[][] FloatData = dataClean.DataToFloat(data,tupHolder);
            dataClean.NormalizeTables();

            dataTrans.SetDC(dataClean);
			//Smoothing
			float[][] ShiftArray = dataTrans.SetupTransArray(FloatData);
			for(int i = 1; i < ShiftArray.length;i++)
			{
				float[]input = dataTrans.Smoothing(ShiftArray[i]);
				for(int j = 0; j < ShiftArray[i].length;j++)
				{
					FloatData[j][i] = input[j];
				}
			}
			//Missing Value
			ShiftArray = dataTrans.SetupTransArray(FloatData);
			for(int i = 1; i < ShiftArray.length;i++)
			{
				float[]input = dataTrans.HandleMissingValue(ShiftArray[i]);
				for(int j = 0; j < ShiftArray[i].length;j++)
				{
					FloatData[j][i] = input[j];
				}
			}

			//Normalizing
            //dataTrans.NormalizeTuples(tupHolder);
			ShiftArray = dataTrans.SetupTransArray(FloatData);
			for(int i = 1; i < ShiftArray.length;i++)
			{
				if(i == 7 || i == 20 || i ==21 || i == 37 || (i >= 40 && i <= 42))
					continue;
				float[]input = dataTrans.Normalize(ShiftArray[i],i);
				for(int j = 0; j < ShiftArray[i].length;j++)
				{

					FloatData[j][i] = input[j];
				}
			}

			aprioriObj.Initiate(tupHolder,0,4);
	/*
			for (float[] line : FloatData) {
				System.out.println(Arrays.toString(line));
			}

			for (String[] line : data) {
				System.out.println(Arrays.toString(line));
			}//*/
			//System.out.println("Number of tuples loaded: "+data.length);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
