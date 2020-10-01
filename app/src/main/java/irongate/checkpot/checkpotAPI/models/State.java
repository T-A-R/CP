package irongate.checkpot.checkpotAPI.models;

public class State {
    private String liquidation_date;

    private String actuality_date;

    private String registration_date;

    private String status;

    public String getLiquidation_date ()
    {
        return liquidation_date;
    }

    public void setLiquidation_date (String liquidation_date)
    {
        this.liquidation_date = liquidation_date;
    }

    public String getActuality_date ()
    {
        return actuality_date;
    }

    public void setActuality_date (String actuality_date)
    {
        this.actuality_date = actuality_date;
    }

    public String getRegistration_date ()
    {
        return registration_date;
    }

    public void setRegistration_date (String registration_date)
    {
        this.registration_date = registration_date;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [liquidation_date = "+liquidation_date+", actuality_date = "+actuality_date+", registration_date = "+registration_date+", status = "+status+"]";
    }
}
