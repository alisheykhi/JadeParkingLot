package Objects;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LogViewer extends JFrame {

    private JPanel contentPane;
    //private JTextArea text;
    private JTable parkingTable;
    private String[] column = {"Type","Name","Log"};
    private DefaultTableModel tableModel;

    public LogViewer() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 900, 600, 250);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setTitle("Log Manager");
        tableModel = new DefaultTableModel(column, 0);
        parkingTable = new JTable(tableModel);
        //parkingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        parkingTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        //parkingTable.setBounds(5, 5, 590, 240);
        JScrollPane scrollPane = new JScrollPane(parkingTable);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);


    }

    public void add(String[] log){
        tableModel.addRow(log);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}