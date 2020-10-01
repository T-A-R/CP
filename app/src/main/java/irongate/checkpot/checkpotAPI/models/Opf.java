package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.SerializedName;

public class Opf {
    private String code;

    @SerializedName("short")
    private String shortName;

    private String type;

    private String full;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getShortName ()
    {
        return shortName;
    }

    public void setShort (String shortName)
    {
        this.shortName = shortName;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getFull ()
    {
        return full;
    }

    public void setFull (String full)
    {
        this.full = full;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", short = "+shortName+", type = "+type+", full = "+full+"]";
    }
}
