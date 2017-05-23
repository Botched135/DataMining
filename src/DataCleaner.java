
public class DataCleaner {

    public ReferenceTable TVTable;
    public ReferenceTable FilmTable;
    public ReferenceTable FavouriteGameTable;
    public ReferenceTable ProgramTable;
    public ReferenceTable StudyProgrammeTable;
    public ReferenceTable CourseChoiceTable;
    public ReferenceTable PhoneOSTable;
    public ReferenceTable PlayedGameTable;
    public ReferenceTable MovementTable;

    public void Init()
    {
        TVTable = new ReferenceTable(0);
        FilmTable = new ReferenceTable(0);
        FavouriteGameTable = new ReferenceTable(0);
        ProgramTable = new ReferenceTable(0);
        StudyProgrammeTable = new ReferenceTable(5);
        CourseChoiceTable = new ReferenceTable(3);
        PhoneOSTable = new ReferenceTable(3);
        PlayedGameTable = new ReferenceTable(0);
        MovementTable = new ReferenceTable(0);
    }
    public String[][] RemoveQuotations(String[][] _input)
    {

        String[][] res = new String[_input.length][_input[0].length];

        for(int i = 0; i < res.length;i++)
        {
            for(int j = 0; j < res[i].length;j++)
            {
                res[i][j] = _input[i][j].replace("\"","");
            }
        }

        return res;
    }

    public static char FindFirstChar(String _input)
    {
        char firstChar = 'a';
        for(int index = 0; index < _input.length(); index++)
        {
            char _char =_input.toLowerCase().charAt(index);
            if(_char != ' ' )
            {
                firstChar = _char;
                break;
            }
        }
        return firstChar;

    }
    public float[][] DataToFloat(String[][] _input, Tuple[][] tupleHolder)
    {
        float[][] res = new float[_input.length][_input[0].length];
        for(int i = 0; i < res.length;i++)
        {
            for(int j = 0; j < res[i].length;j++)
            {
                if(j == 0)
                {
                    res[i][j] = i;
                }
                else if(j == 1)
                {
                    res[i][j] = Float.parseFloat(_input[i][j]);
                }
                else if(j == 2)
                {
                    char _iChar = FindFirstChar(_input[i][j]);
                    if(_iChar == 'm' )
                        res[i][j] = 1;
                    else if(_iChar == 'f' || _iChar == 'w')
                        res[i][j] = 0;
                    else
                        res[i][j] = -1;
                }
                else if(j == 3)
                {
                    String floater;
                    floater = _input[i][j].replace(",",".");
                    floater = floater.replace(" ","");
                    res[i][j] = Float.parseFloat(floater);
                }
                else if(j == 4)
                {
                    String floater;
                    floater = _input[i][j].substring(0,_input[i][j].length() > 2 ? 3 : 2);
                    res[i][j] = Float.parseFloat(floater);
                }
                else if(j == 5)
                {
                    switch(_input[i][j])
                    {
                        case "GAMES-A":
                            res[i][j] = 0;
                            break;
                        case "GAMES-T":
                            res[i][j] = 1;
                            break;
                        case "SDT-SE":
                            res[i][j] = 2;
                            break;
                        case "SDT-DT":
                            res[i][j] = 3;
                            break;
                        case "SWU":
                            res[i][j] = 4;
                            break;
                        case "Guest student":
                            res[i][j] = 5;
                            break;
                        default:
                            res[i][j] = StudyProgrammeTable.InsertAndGet(_input[i][j].replace(" ",""));
                            break;
                    }
                }
                //Why choose the course?
                else if(j == 6)
                {
                    switch(_input[i][j])
                    {
                        case "I am interested in the subject":
                            res[i][j] = 0;
                            break;
                        case "It may help me to find a job":
                            res[i][j] = 1;
                            break;
                        case "The other optional courses were least appealing":
                            res[i][j] = 2;
                            break;
                        case "This was a mandatory course for me":
                            res[i][j] = 3;
                            break;
                        default:
                            res[i][j] = CourseChoiceTable.InsertAndGet(_input[i][j]);
                            break;
                    }
                }
                else if (j == 7)
                {
                    String replacer;
                    if(_input[i][j].contains(" ") && !_input[i][j].contains(","))
                        replacer =_input[i][j].replace(" ",",");
                    else
                        replacer = _input[i][j].replace(";",",");


                    String[] splitString = replacer.split(",");
                    StringBuilder result= new StringBuilder();
                    for (String str: splitString)
                    {
                        str = str.replace(" ","");
                        str = str.replace(";","");
                        if(str.equals(""))
                            continue;
                        result.append(Float.toString(ProgramTable.InsertAndGet(str)));
                        result.append(",");
                    }
                    result.deleteCharAt(result.length()-1);

                    String[] resultSplit = result.toString().split(",");
                    tupleHolder[i][0].initVector(resultSplit.length);
                    for(int splitIndex = 0; splitIndex < resultSplit.length;splitIndex++)
                    {
                        tupleHolder[i][0].dataVec[splitIndex] = Float.parseFloat(resultSplit[splitIndex]);
                    }

                    res[i][j] = 0;
                }
                 //Phone OS
                else if(j == 8) {
                    switch (_input[i][j]) {
                        case "Windows":
                            res[i][j] = 0;
                            break;
                        case "iOS":
                            res[i][j] = 1;
                            break;
                        case "Android":
                            res[i][j] = 2;
                            break;
                        case "Ubuntu Touch":
                            res[i][j] = 3;
                            break;
                        default:
                            res[i][j] = PhoneOSTable.InsertAndGet(_input[i][j]);
                            break;
                    }
                }
                //Topics
                else if(j >8 && j <= 19) {
                    switch (_input[i][j]) {
                        case "Not interested":
                            res[i][j] = 0;
                            break;
                        case "Meh":
                            res[i][j] = 1;
                            break;
                        case "Sounds interesting":
                            res[i][j] = 2;
                            break;
                        case "Very interesting":
                            res[i][j] = 3;
                            break;
                    }
                }
                //Games
                else if(j == 20)
                {

                    _input[i][j].replace(";",",");
                    String[] splitString = _input[i][j].split(",");
                    StringBuilder result= new StringBuilder();
                    for (String str: splitString)
                    {
                        str.replace(" ","");
                        if(str.equals(""))
                            continue;
                        result.append(Float.toString(PlayedGameTable.InsertAndGet(str)));
                        result.append(";");
                    }
                    result.deleteCharAt(result.length()-1);
                    String[] resultSplit = result.toString().split(",");
                    tupleHolder[i][1].initVector(resultSplit.length);
                    for(int splitIndex = 0; splitIndex < resultSplit.length;splitIndex++)
                    {
                        tupleHolder[i][1].dataVec[splitIndex] = Float.parseFloat(resultSplit[splitIndex]);
                    }
                    res[i][j] = 1;
                }
                //Movement how-to
                else if(j == 21)
                {
                    _input[i][j].replace(";",",");
                    String[] splitString = _input[i][j].split(",");
                    StringBuilder result= new StringBuilder();
                    for (String str: splitString)
                    {
                        str.replace(" ","");
                        if(str.equals(""))
                            continue;
                        result.append(Float.toString(MovementTable.InsertAndGet(str)));
                        result.append(";");
                    }
                    result.deleteCharAt(result.length()-1);
                    String[] resultSplit = result.toString().split(",");
                    tupleHolder[i][2].initVector(resultSplit.length);
                    for(int splitIndex = 0; splitIndex < resultSplit.length;splitIndex++)
                    {
                        tupleHolder[i][2].dataVec[splitIndex] = Float.parseFloat(resultSplit[splitIndex]);
                    }
                    res[i][j] = 2;
                }
                //Movement where
                else if(j > 21 && j <= 36)
                {
                    switch (_input[i][j]) {
                        case "Not at ITU":
                            res[i][j] = 0;
                            break;
                        case "Atrium":
                            res[i][j] = 1;
                            break;
                        case "Basement":
                            res[i][j] = 2;
                            break;
                        case "Canteen":
                            res[i][j] = 3;
                            break;
                        case "ScrollBar":
                            res[i][j] = 4;
                            break;
                        case "Analog":
                            res[i][j] = 5;
                            break;
                        case "Library":
                            res[i][j] = 6;
                            break;
                        case "Class/Auditorium":
                            res[i][j] = 7;
                            break;
                        case "Study Room":
                            res[i][j] = 8;
                            break;
                        case "Elevator":
                            res[i][j] = 9;
                            break;
                    }
                }
                //Four random numbers
                else if(j == 37)
                {
                    String[] splitString = _input[i][j].split(",");

                    tupleHolder[i][3].initVector(splitString.length);
                    //removing noise in form of number above or below that was looked for
                    for(int splitIndex = 0; splitIndex < splitString.length;splitIndex++)
                    {
                        splitString[splitIndex] = splitString[splitIndex].replace(" ","");
                        tupleHolder[i][3].dataVec[splitIndex] = Float.parseFloat(splitString[splitIndex]);
                        if(tupleHolder[i][3].dataVec[splitIndex] > 15)
                            tupleHolder[i][3].dataVec[splitIndex] = 15;
                        if(tupleHolder[i][3].dataVec[splitIndex] <0)
                            tupleHolder[i][3].dataVec[splitIndex] = 0;
                    }
                    res[i][j] = 3;
                }
                //therb fortt glaag
                else if(j == 38)
                {
                    if(_input[i][j].equals("Option 1"))
                        res[i][j] = 1;
                    else
                        res[i][j] = 0;
                }
                //Pick a number
                else if(j == 39)
                {
                    switch (_input[i][j])
                    {
                        case "7":
                            res[i][j] = 0;
                            break;
                        case "9":
                            res[i][j] = 1;
                            break;
                        case "Asparagus": //Might need to be though through more
                            res[i][j] = 2;
                            break;
                        case "13":
                            res[i][j] = 3;
                            break;
                    }

                }
                //Film
                else if(j == 40)
                {
                    String[] splitString = _input[i][j].split(",");
                    StringBuilder result= new StringBuilder();
                    for (String str: splitString)
                    {
                        str.replace(" ","");
                        if(str.equals(""))
                            continue;
                        result.append(Float.toString(FilmTable.InsertAndGet(str)));
                        result.append(",");
                    }
                    result.deleteCharAt(result.length()-1);
                    String[] resultSplit = result.toString().split(",");
                    tupleHolder[i][4].initVector(resultSplit.length);
                    for(int splitIndex = 0; splitIndex < resultSplit.length;splitIndex++)
                    {
                        tupleHolder[i][4].dataVec[splitIndex] = Float.parseFloat(resultSplit[splitIndex]);
                    }
                    res[i][j] = 4;

                }
                //TV-Shows
                else if(j == 41)
                {
                    String[] splitString = _input[i][j].split(",");
                    StringBuilder result= new StringBuilder();
                    for (String str: splitString)
                    {
                        str.replace(" ","");
                        if(str.equals(""))
                            continue;
                        result.append(Float.toString(TVTable.InsertAndGet(str)));
                        result.append(",");
                    }
                    result.deleteCharAt(result.length()-1);
                    String[] resultSplit = result.toString().split(",");
                    tupleHolder[i][5].initVector(resultSplit.length);
                    for(int splitIndex = 0; splitIndex < resultSplit.length;splitIndex++)
                    {
                        tupleHolder[i][5].dataVec[splitIndex] = Float.parseFloat(resultSplit[splitIndex]);
                    }
                    res[i][j] = 5;

                }
                //Game
                else if(j == 42)
                {
                    String[] splitString = _input[i][j].split(",");
                    StringBuilder result= new StringBuilder();
                    for (String str: splitString)
                    {
                        str.replace(" ","");
                        if(str.equals(""))
                            continue;
                        result.append(Float.toString(FavouriteGameTable.InsertAndGet(str)));
                        result.append(",");
                    }
                    result.deleteCharAt(result.length()-1);
                    String[] resultSplit = result.toString().split(",");
                    tupleHolder[i][6].initVector(resultSplit.length);
                    for(int splitIndex = 0; splitIndex < resultSplit.length;splitIndex++)
                    {
                        tupleHolder[i][6].dataVec[splitIndex] = Float.parseFloat(resultSplit[splitIndex]);
                    }
                    res[i][j] = 6;

                }
                //Row
                else if(j == 43)
                {
                    char firstChar = FindFirstChar(_input[i][j]);
                    if(_input[i][j].length() > 1 || firstChar < 'a' )
                        res[i][j] = -1;

                    else
                    {

                        int charNum = firstChar - 'a';
                        res[i][j] = charNum;
                    }
                }
                //Seat: dot need to do anything
                else if(j == 44)
                {
                    int charTest =  FindFirstChar(_input[i][j]);
                    if(_input[i][j].length() > 2 || charTest > 57 || charTest < 48)
                    {
                        res[i][j] = -1;
                        break;
                    }
                    res[i][j]=Float.parseFloat(_input[i][j]);
                }
            }
        }

        return res;
    }

    public void NormalizeTables()
    {
        TVTable.IntoNorm();
        FilmTable.IntoNorm();
        FavouriteGameTable.IntoNorm();
        ProgramTable.IntoNorm();
        StudyProgrammeTable.IntoNorm();
        CourseChoiceTable.IntoNorm();
        PhoneOSTable.IntoNorm();
        PlayedGameTable.IntoNorm();
        MovementTable.IntoNorm();
    }



}
