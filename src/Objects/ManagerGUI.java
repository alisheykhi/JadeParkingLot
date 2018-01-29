package Objects;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class ManagerGUI extends JFrame {

    private JPanel contentPane;
    private JTable parkingTable;
    private String[] column = {"Name","Capacity","Available","reserved"};
    private DefaultTableModel tableModel;

    public ManagerGUI() {

        setTitle("Parking Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(1050, 100, 357, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tableModel = new DefaultTableModel(column, 0);
        parkingTable = new JTable(tableModel);
        parkingTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
//        parkingTable.setBounds(50, 100, 230, 100);
//        contentPane.add(parkingTable, BorderLayout.CENTER);
//        contentPane.add(parkingTable.getTableHeader(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(parkingTable);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
//
//        JLabel lblLogo = new JLabel("Parking Manager");
//        lblLogo.setFont(new Font("Tahoma", Font.BOLD, 24));
//        lblLogo.setBounds(70, 11, 300, 53);
//        contentPane.add(lblLogo);
//
//        JLabel lblIAvPa = new JLabel("available Parking :");
//        lblIAvPa.setFont(new Font("Tahoma", Font.PLAIN, 15));
//        lblIAvPa.setBounds(20, 70, 300, 25);
//        contentPane.add(lblIAvPa);
    }

    public void setRows(String[] list){
        tableModel.addRow(list);
    }
    public void updateReseved(int row, String count){
        tableModel.setValueAt(count, row, 3);
    }
    public void updateCapacity(int row, String count){
        tableModel.setValueAt(count, row, 2);
    }
}