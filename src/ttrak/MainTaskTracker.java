package ttrak;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MainTaskTracker
{
    static String helpStr = 
            "commands:" +
    		"  help" +
    		"  rt - List all known region tags" +
    		"  q - List all open questions" +
    		"  m - List all items to memorize" +
    		"  l - ";
    
    public static void main(String[] arg)
    {
        if(arg.length != 1)
        {
            System.out.println("Usage: ttrack <filename>");
            return;
        }
        
        FileMonitor mon = new FileMonitor(new File("test.txt"));
        mon.start();
        Scanner s = new Scanner(System.in);
        
        while(true)
        {
            String line = s.nextLine();
            System.out.println(line);
            
            line.toLowerCase();
            
            if(line.equals("quit"))
                break;
            
            synchronized(mon)
            {
                if(line.equals("q"))
                {
                    System.out.println("\n");
                    List<Question> qs = mon.getQuestions();
                    for(Question q : qs)
                    {
                        System.out.println(q.getText());
                        System.out.print('\n');
                    }
                }
            }
        }
        
        s.close();
    }
}
