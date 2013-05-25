package ttrak;

public class Tag
{
    String name;
    int index;
    int lineNum;
    
    public Tag(String name, int lineNum, int index)
    {
        this.name = name;
        this.lineNum = lineNum;
        this.index = index;
    }

    public String getName()
    {
        return name;
    }

    public int getIndex()
    {
        return index;
    }

    public int getLineNum()
    {
        return lineNum;
    }
}
