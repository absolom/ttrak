package ttrak;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import ttrak.MainWindowLogic.ViewType;

public class MainWindow 
{
    static File file;
    private JFrame frmTaskTracker;
    private JTextArea taMain;
    private MainWindowLogic logic;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.out.println("Please provide a file to monitor.");
            System.out.flush();
            return;
        }
        
        file = new File(args[0]);
        if(!file.exists())
        {
            System.out.println("File not found.");
            System.out.flush();
            return;
        }
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    MainWindow window = new MainWindow();
                    window.frmTaskTracker.setVisible(true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainWindow()
    {
        initialize();
        logic = new MainWindowLogic(this);
        logic.initialize(file);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frmTaskTracker = new JFrame();
        frmTaskTracker.setTitle("Task Tracker");
        frmTaskTracker.setBounds(100, 100, 736, 461);
        frmTaskTracker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComboBox cbFilter = new JComboBox();
        cbFilter.setEditable(true);
        
        JLabel lblFilter = new JLabel("Filter");
        
        JComboBox cbView = new JComboBox();
        cbView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                viewChanged(((JComboBox)arg0.getSource()).getSelectedIndex());
            }
        });
        cbView.setModel(new DefaultComboBoxModel(new String[] {"Note", "Task", "Question", "Tag"}));
        
        JLabel lblView = new JLabel("View");
        
        JScrollPane scrollPane = new JScrollPane();
        GroupLayout groupLayout = new GroupLayout(frmTaskTracker.getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblView)
                    .addGap(4)
                    .addComponent(cbView, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                    .addGap(18)
                    .addComponent(lblFilter)
                    .addGap(10)
                    .addComponent(cbFilter, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(418, Short.MAX_VALUE))
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(4)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(3)
                            .addComponent(lblView))
                        .addComponent(cbView, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(3)
                            .addComponent(lblFilter))
                        .addComponent(cbFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
        );
        
        taMain = new JTextArea();
        taMain.setEditable(false);
        scrollPane.setViewportView(taMain);
        frmTaskTracker.getContentPane().setLayout(groupLayout);
    }

    protected void viewChanged(int selectedIndex)
    {
        ViewType view;
        switch(selectedIndex)
        {
            case 0:
                view = ViewType.NOTES;
                break;
            case 1:
                view = ViewType.TASKS;
                break;
            case 2:
                view = ViewType.QUESTIONS;
                break;
            default:
            case 3:
                view = ViewType.TAGS;
                break;
        }
        logic.setView(view);
    }

    public void refresh()
    {
        EventQueue.invokeLater(new Runnable()
        { 
            @Override
            public void run()
            {
//                logic.getTagSections();  // TODO: Implement this.
                taMain.setText(logic.getContent());                
            }
        });
    }
}
