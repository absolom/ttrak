package ttrak;

public class Question
{
    String text;
    int lineNum;
    
    public Question(String text, int lineNum)
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
