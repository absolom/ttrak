package ttrak;

public class Note
{
    String text;
    int lineNum;
    
    public Note(String text, int lineNum)
    {
        this.text = text;
        this.lineNum = lineNum;
    }

    public String getText()
    {
        return text;
    }

    public int getLineNum()
    {
        return lineNum;
    }
}
