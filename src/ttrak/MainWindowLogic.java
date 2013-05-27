package ttrak;

import java.io.File;
import java.util.List;

public class MainWindowLogic implements FileMonitorListener
{
    public enum ViewType {QUESTIONS, TAGS, NOTES, TASKS};
    MainWindow win;
    FileMonitor mon;
    private ViewType view;
    String content;
    
    public MainWindowLogic(MainWindow win)
    {
        this.win = win;   
        view = ViewType.NOTES;
        content = "";
    }
    
    public void initialize(File file)
    {
        mon = new FileMonitor(file);
        mon.registerListener(this);
        mon.start();
    }

    public void setView(ViewType view)
    {
        this.view = view;
        regenerateContent();
        win.refresh();
    }
    
    public String getContent()
    {
        return content;
    }

    private void regenerateContent()
    {
        StringBuilder sb = new StringBuilder();
        switch(view)
        {
            case NOTES:
            {
                List<Note> notes = mon.getNotes();
                content = "";
                for(Note n : notes)
                {
                    sb.append(n.getText());
                    sb.append('\n');
                    sb.append('\n');
                }
                content = sb.toString();
                break;
            }
            case QUESTIONS:
            {
                List<Question> questions = mon.getQuestions();
                content = "";
                for(Question n : questions)
                {
                    sb.append(n.getText());
                    sb.append('\n');
                    sb.append('\n');
                }
                content = sb.toString();
                break;
            }
            case TAGS:
            {
                List<Tag> tags = mon.getTags();
                content = "";
                for(Tag n : tags)
                {
                    sb.append(n.getName());
                    sb.append('\n');
                    sb.append('\n');
                }
                content = sb.toString();
                break;
            }
            case TASKS:
            {
                List<Task> tags = mon.getTasks();
                content = "";
                for(Task t : tags)
                {
                    sb.append(t.getText());
                    sb.append('\n');
                    sb.append('\n');
                }
                content = sb.toString();
                break;
            }
            default:
            {
                break;
            }
        }
    }

    @Override
    public void fileChanged()
    {
        regenerateContent();
        win.refresh();
    }
}
