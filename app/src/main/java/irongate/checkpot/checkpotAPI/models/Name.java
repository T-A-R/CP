package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.SerializedName;

public class Name {
    private String short_with_opf;

    private String full_with_opf;

    @SerializedName("short")
    private String shortName;

    private Object latin;

    private String full;

    public String getShort_with_opf ()
    {
        return short_with_opf;
    }

    public void setShort_with_opf (String short_with_opf)
    {
        this.short_with_opf = short_with_opf;
    }

    public String getFull_with_opf ()
    {
        return full_with_opf;
    }

    public void setFull_with_opf (String full_with_opf)
    {
        this.full_with_opf = full_with_opf;
    }

    public String getShortName ()
    {
        return shortName;
    }

    public void setShortName (String shortName)
    {
        this.shortName = shortName;
    }

    public Object getLatin ()
    {
        return latin;
    }

    public void setLatin (Object latin)
    {
        this.latin = latin;
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
        return "ClassPojo [short_with_opf = "+short_with_opf+", full_with_opf = "+full_with_opf+", short = "+shortName+", latin = "+latin+", full = "+full+"]";
    }
}
