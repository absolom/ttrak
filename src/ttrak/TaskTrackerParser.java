package ttrak;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskTrackerParser
{
    static Pattern pTag;
    static Pattern pTaskIncomplete;
    static Pattern pQuestion;
    static Pattern pNote;
    static Pattern pDoubleBreak;
    
    static 
    {
        pTag = Pattern.compile("#\\w+");
        pTaskIncomplete = Pattern.compile("\\s(-|\\/)(\\w.*)(\\n|\\r)");
        pQuestion = Pattern.compile("\\s\\?(\\w.*)(\\n|\\r)");
        pNote = Pattern.compile("\\s!(\\w.*)(\\n|\\r)");
        pDoubleBreak = Pattern.compile("^\\W*?(\\n|\\r)", Pattern.MULTILINE);
    }

    public static List<Tag> getTags(String tst)
    {
        List<Tag> ret = new ArrayList<>();
        Matcher m = pTag.matcher(tst);
        int ind = 0;
        while(m.find(ind))
        {
            ind = m.end();
            char end = tst.charAt(ind-1);
            if(end == '<' || end == '>')
                continue;
            ret.add( new Tag( m.group().substring(1) , findLineNumber(tst, m.start()) , m.start() - findLineStart(tst, m.start()) ) );
        }
                
        return ret;
    }

    private static int findLineStart(String tst, int start)
    {
        for(int i = start; i > 0; i--)
            if(tst.charAt(i) == '\n')
                return i+1;
        
        return 0;
    }
    
    private static int findGroupEnd(String tst, int start, boolean multiLine)
    {
        if(!multiLine)
            return tst.indexOf('\n', start);
        else
        {
            Matcher m = pDoubleBreak.matcher(tst);
            
            if(!m.find(start))
                return tst.length()-1;
            
            return m.start();
        }
    }

    public static List<Task> getIncompleteTasks(String tst)
    {
        List<Task> ret = new ArrayList<>();
        Matcher m = pTaskIncomplete.matcher(tst);
        int ind = 0;
        
        while(m.find(ind))
        {
            int textStartInd = m.start(2);
            int textStopInd = findGroupEnd( tst, m.start(), true );
            String text = tst.substring(textStartInd, textStopInd).trim();
            Task.State state = m.group(1).charAt(0) == '/' ? Task.State.PARTIALLY_FINISHED : Task.State.UNFINISHED;
            int lineNum = findLineNumber( tst, m.start() );
            
            ret.add( new Task(text, state, lineNum) );
            
            ind = m.end();
        }

        return ret;
    }

    public static List<Question> getQuestions(String tst)
    {
        List<Question> ret = new ArrayList<>();
        Matcher m = pQuestion.matcher(tst);
        int ind = 0;
        while(m.find(ind))
        {
            int textStartInd = m.start()+2;
            int textStopInd = findGroupEnd( tst, m.start(), true );
            String text = tst.substring(textStartInd, textStopInd).trim();
            int lineNum = findLineNumber( tst, m.start() );
            
            ret.add( new Question(text, lineNum ) );
            
            ind = m.end();
        }
                
        return ret;
    }

    public static List<Note> getNotes(String tst)
    {

        List<Note> ret = new ArrayList<>();
        Matcher m = pNote.matcher(tst);
        int ind = 0;
        
        while(m.find(ind))
        {
            int textStartInd = m.start()+2;
            int textStopInd = findGroupEnd( tst, m.start(), true );
            String text = tst.substring(textStartInd, textStopInd).trim();
            int lineNum = findLineNumber( tst, m.start() );
            
            ret.add( new Note(text, lineNum) );
            
            ind = m.end();
        }

        return ret;
    }

    private static int findLineNumber(String tst, int mark)
    {
        int ind = 0;
        int cnt = 0;
        
        if(mark >= tst.length())
            throw new IllegalArgumentException();
        
        do
        {
            ind = tst.indexOf('\n', ind);
            cnt++;
            
            if(ind == -1)
                break;
            
        } while(mark >= ind++);
        
        return cnt;
    }
        
}
