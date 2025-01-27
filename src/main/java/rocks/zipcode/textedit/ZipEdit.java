package rocks.zipcode.textedit;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ZipEdit extends JFrame implements ActionListener{
    private JTextArea area;
    private JFrame frame;
    private String filename = "untitled";
    JMenuItem menuitem_mode;
    public ZipEdit() {  }


    public static void main(String[] args) {
        ZipEdit runner = new ZipEdit();
        runner.run();
    }


    public void run() {
        frame = new JFrame(frameTitle());

        // Set the look-and-feel (LNF) of the application
        // Try to default to whatever the host system prefers
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ZipEdit.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Set attributes of the app window
        area = new JTextArea();
        //Border blackline = BorderFactory.createLineBorder(Color.black);
        //area.setBorder(blackline);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        area.setText("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(area);
        frame.setLocationRelativeTo(null);
        frame.setSize(640, 480);

        // Build the menu
        JMenuBar menu_main = new JMenuBar();

        JMenu menu_file = new JMenu("File");
        JMenu menu_edit = new JMenu("Edit");

        JMenuItem menuitem_new = new JMenuItem("New");
        JMenuItem menuitem_open = new JMenuItem("Open");
        JMenuItem menuitem_save = new JMenuItem("Save");
        JMenuItem menuitem_quit = new JMenuItem("Quit");
        menuitem_mode = new JMenuItem("Dark");

        JMenuItem menuitem_copy = new JMenuItem("Copy");
        JMenuItem menuitem_paste = new JMenuItem("Paste");
        JMenuItem menuitem_find = new JMenuItem("Find");

        menuitem_new.addActionListener(this);
        menuitem_open.addActionListener(this);
        menuitem_save.addActionListener(this);
        menuitem_quit.addActionListener(this);
        menuitem_mode.addActionListener(this);
        menuitem_copy.addActionListener(this);
        menuitem_paste.addActionListener(this);
        menuitem_find.addActionListener(this);

        menu_main.add(menu_file);
        menu_main.add(menu_edit);

        menu_file.add(menuitem_new);
        menu_file.add(menuitem_open);
        menu_file.add(menuitem_save);
        menu_file.add(menuitem_quit);
        menu_file.add(menuitem_mode);

        menu_edit.add(menuitem_copy);
        menu_edit.add(menuitem_paste);
        menu_edit.add(menuitem_find);

        frame.setJMenuBar(menu_main);

        frame.setVisible(true);

    }

    public String frameTitle() {
        return "Zip Edit ("+this.filename+")";
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String ingest = "";
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setCurrentDirectory(Paths.get(".").toFile());
        jfc.setSelectedFile(new File("untitled.txt")); //took 22 minutes
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


        String ae = e.getActionCommand();
        int returnValue;
        if (ae.equals("Open")) {
            returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File f = new File(jfc.getSelectedFile().getAbsolutePath());
                this.filename = jfc.getSelectedFile().getName();
                this.frame.setTitle(this.frameTitle());
                try{
                    FileReader read = new FileReader(f);
                    Scanner scan = new Scanner(read);
                    while(scan.hasNextLine()){
                        String line = scan.nextLine() + "\n";
                        ingest = ingest + line; // come on Kris, use a stringBuilder smh you want all these floating around in the literal area
                    }
                    area.setText(ingest);
                }
                catch ( FileNotFoundException ex) { ex.printStackTrace(); }
            }
            // SAVE
        } else if (ae.equals("Save")) {
            returnValue = jfc.showSaveDialog(null);
            this.filename = jfc.getSelectedFile().getName();
            this.frame.setTitle(this.frameTitle());
            try {
                File f = new File(jfc.getSelectedFile().getAbsolutePath());
                FileWriter out = new FileWriter(f);
                out.write(area.getText());
                out.close();
            } catch (FileNotFoundException ex) {
                Component f = null;
                JOptionPane.showMessageDialog(f,"File not found.");
            } catch (IOException ex) {
                Component f = null;
                JOptionPane.showMessageDialog(f,"Error.");
            }
        } else if (ae.equals("New")) {
            area.setText("");
        } else if (ae.equals("Quit")) {
            System.exit(0);
        } else if (ae.equals("Copy")) {
            area.copy(); //lol if I could read I would have done this the first time
        } else if (ae.equals("Paste")) {
            area.paste();
        } else if (ae.equals("Find")){
            JFrame find = new JFrame();
            JTextArea inField = new JTextArea();
            JButton next = new JButton("Next");

            find.setLayout(new FlowLayout());

            find.setSize(300, 100);
            inField.setSize(250, 50);
            area.setCaretPosition(0);

            next.addActionListener((ActionEvent a) -> {
                String target = inField.getText();
                if(target.length() == 0){
                    return;
                }
                String text = area.getText();
                for (int i = area.getCaretPosition(); i < text.length()-target.length()+1; i++) {
                    if(text.startsWith(target, i)){
                        area.setCaretPosition(i);
                        area.setSelectionStart(i);
                        area.setSelectionEnd(i+target.length());
                        break;
                    }
                }
            });

            find.add(inField);
            find.add(next);

            find.setVisible(true);
        } else if(ae.equals("Dark")){
            area.setBackground(Color.BLACK);
            area.setForeground(Color.WHITE);
            area.setCaretColor(Color.WHITE);
            menuitem_mode.setText("Light");
        } else if(ae.equals("Light")){
            area.setBackground(Color.WHITE);
            area.setForeground(Color.BLACK);
            area.setCaretColor(Color.BLACK);
            menuitem_mode.setText("Dark");
        }
    }
}


