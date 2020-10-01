package irongate.checkpot.checkpotAPI.models;

public class Management {
    private String post;

    private String name;

    public String getPost ()
    {
        return post;
    }

    public void setPost (String post)
    {
        this.post = post;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [post = "+post+", name = "+name+"]";
    }
}
