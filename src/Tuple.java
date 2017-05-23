import java.util.Collection;

/**
 * Created by Bons on 16/04/2017.
 */
public class Tuple {

    public float[] dataVec;
    public void initVector(int size)
    {
        dataVec = new float[size];
    }
    public void Normalize(int index, DataCleaner _dc) {
        ReferenceTable rt = GetRefTab(index, _dc);
        if (rt != null) {
            Collection<Float> floats = rt.refMap.values();

            float max = 0;
            for (Float f : floats) {
                if (f > max)
                    max = f;
            }

            for (int i = 0; i < dataVec.length; i++) {
                dataVec[i] = dataVec[i] / max;
            }
        }
        else if(index == 3)
            {
                for (int i = 0; i < dataVec.length; i++)
                {
                    dataVec[i] = dataVec[i] / 15;
                }
            }
    }

    public ReferenceTable GetRefTab(int index, DataCleaner _dc)
    {
        switch (index) {
            case 0:
                return _dc.ProgramTable;
            case 1:
                return _dc.PlayedGameTable;
            case 2:
                return _dc.MovementTable;
            case 4:
                return _dc.FilmTable;
            case 5:
                return _dc.TVTable;
            case 6:
                return _dc.FavouriteGameTable;
            default:
                return null;
        }
    }
    @Override

    public boolean equals(Object obj)
    {
        if(!(obj instanceof Tuple))
            return false;

        Tuple other = (Tuple) obj;
        if(other.dataVec.length != this.dataVec.length)
            return false;

        for (int i = 0; i < dataVec.length; i++) {
            if (dataVec[i] != other.dataVec[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(float num)
    {
        for(int i = 0;i < dataVec.length;i++)
        {
            if(dataVec[i] == num)
                return true;
        }
        return false;
    }

}

