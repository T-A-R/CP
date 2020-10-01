package irongate.checkpot.checkpotAPI.models;

public class Address {
    private String unrestricted_value;

    private String value;

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
        return "ClassPojo [unrestricted_value = "+unrestricted_value+", value = "+value+"]";
    }
}
