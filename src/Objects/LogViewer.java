package Objects;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LogViewer extends JFrame {

    private JPanel contentPane;
    private JTextArea text;

    public LogViewer() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 900, 500, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        text = new JTextArea();
        text.setFont(new Font("Tahoma", Font.PLAIN, 12));
        text.setBounds(5, 5, 490, 290);
        contentPane.add(text,BorderLayout.CENTER);
    }

    public void add(String log){
        text.append(log+"\n");
    }

}