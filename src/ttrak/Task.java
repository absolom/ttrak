package ttrak;

public class Task
{
    public enum State {FINISHED, UNFINISHED, PARTIALLY_FINISHED};
    State state;
    String text;
    int lineNum;
    
    public Task(String text, State state, int lineNum)
    {
        this.text = text;
        this.state = state;
        this.lineNum = lineNum;
    }

    public State getState()
    {
        return state;
    }

    public String getText()
    {
        return text;
    }

    public Object getLineNum()
    {
        return lineNum;
    }
}
