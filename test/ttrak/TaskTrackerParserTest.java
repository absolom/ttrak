package ttrak;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaskTrackerParserTest
{
    String testString = "\r\n" + 
            "    !Important info tidbit number 1\r\n" + 
            "        multiline!!\r\n" + 
            "\r\n" + 
            "    *Task1 that is finished\r\n" +                             // Line 5
            "        multiline!\r\n" + 
            "\r\n" + 
            "    /Task2 that needs to be finished as well\r\n" + 
            "        multiliner!\r\n" + 
            "\r\n" +                                                        // Line 10
            "    ?Health and safety is under corporate services.\r\n" + 
            "\r\n" + 
            "Questions for Johnny #work";                                   // Line 13

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testFindTags()
    {
        List<Tag> tags = TaskTrackerParser.getTags(testString);
        assertEquals(1, tags.size());
        
        Tag tag = tags.get(0);
        assertEquals("work", tag.getName());
        assertEquals(13, tag.getLineNum());
        assertEquals(21, tag.getIndex());
    }
    
    @Test
    public void testFindIncompleteTasks()
    {        
        List<Task> tasks = TaskTrackerParser.getIncompleteTasks(testString);
        
        assertEquals(1, tasks.size());
        
        Task t = tasks.get(0);
        assertEquals("Task2 that needs to be finished as well\r\n" + 
                "        multiliner!", t.getText());
        assertEquals(Task.State.PARTIALLY_FINISHED, t.getState());
        assertEquals(8, t.getLineNum());
    }
        
    @Test
    public void testFindQuestions()
    {
        List<Question> questions = TaskTrackerParser.getQuestions(testString);
        
        assertEquals(1, questions.size());
        
        Question q = questions.get(0);
        assertEquals("Health and safety is under corporate services.", q.getText());
        assertEquals(11, q.getLineNum());
    }
    
    @Test
    public void testGetNotes()
    {
        List<Note> notes = TaskTrackerParser.getNotes(testString);
        
        assertEquals(1, notes.size());
        
        assertEquals("Important info tidbit number 1\r\n" + 
                "        multiline!!", notes.get(0).getText());
        assertEquals(2, notes.get(0).getLineNum());
    }

}
