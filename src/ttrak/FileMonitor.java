package ttrak;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileMonitor extends Thread
{
    File file;
    List<Tag> tags;
    List<Question> questions;
    List<Note> notes;
    List<Task> tasks;
    
    List<FileMonitorListener> listeners;
        
    public FileMonitor(File f)
    {
        this.file = f;
        tags = new ArrayList<>();
        questions = new ArrayList<>();
        notes = new ArrayList<>();
        tasks = new ArrayList<>();
        
        listeners = new ArrayList<>();
    }
    
    public void registerListener(FileMonitorListener l)
    {
        listeners.add(l);        
    }
        
    @Override
    public void run()
    {
        long lastT = 0;
        while(true)
        {
            if(lastT != file.lastModified())
            {
                try
                {
                    List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
                    
                    StringBuilder sb = new StringBuilder();
                    
                    for(String l : lines)
                    {
                        sb.append(l);
                        sb.append('\n');
                    }                   
                    
                    String fileStr = sb.toString();
                    
                    synchronized(this)
                    {
                        tags = TaskTrackerParser.getTags(fileStr);
                        questions = TaskTrackerParser.getQuestions(fileStr);
                        notes = TaskTrackerParser.getNotes(fileStr); 
                        tasks = TaskTrackerParser.getIncompleteTasks(fileStr);
                    }
                    
                    for(FileMonitorListener l : listeners)
                        l.fileChanged();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                
                lastT = file.lastModified();
            }
            
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
            }
        }
    }

    public List<Tag> getTags()
    {
        List<Tag> ret;
        synchronized(this)
        {
            ret = new ArrayList<>(tags);
        }
        return ret;
    }

    public List<Question> getQuestions()
    {
        List<Question> ret;
        synchronized(this)
        {
            ret = new ArrayList<>(questions);
        }
        return ret;
    }

    public List<Note> getNotes()
    {
        List<Note> ret;
        synchronized(this)
        {
            ret = new ArrayList<>(notes);
        }
        return ret;
    }
    
    public List<Task> getTasks()
    {
        List<Task> ret;
        synchronized(this)
        {
            ret = new ArrayList<>(tasks);                        
        }
        return ret;
    }
}
