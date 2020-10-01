package irongate.checkpot.checkpotAPI.models;

public class Suggestions {

    private Data data;

    private String unrestricted_value;

    private String value;

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public String getUnrestricted_value ()
    {
        return unrestricted_value;
    }

    public void setUnrestricted_value (String unrestricted_value)
    {
        this.unrestricted_value = unrestricted_value;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", unrestricted_value = "+unrestricted_value+", value = "+value+"]";
    }
}
