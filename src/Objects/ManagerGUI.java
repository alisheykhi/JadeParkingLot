package Objects;

import Agents.Parking;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Vector;

public class ManagerGUI extends JFrame {

    private JPanel contentPane;
    private JTable parkingTable;
    private String[] column = {"Name","Capacity","Available"};
    Vector colHdrs;


    public ManagerGUI() {
        DefaultTableModel tableModel = new DefaultTableModel(column, 0);
        colHdrs = new Vector(3);
        colHdrs.addElement(new String("Name"));
        colHdrs.addElement(new String("Capacity"));
        colHdrs.addElement(new String("Available"));
        tableModel.setColumnIdentifiers(colHdrs);
        parkingTable = new JTable(tableModel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 357, 391);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblLogo = new JLabel("Parking Manager");
        lblLogo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblLogo.setBounds(70, 11, 300, 53);
        contentPane.add(lblLogo);

        JLabel lblIAvPa = new JLabel("available Parking :");
        lblIAvPa.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblIAvPa.setBounds(20, 70, 300, 25);
        contentPane.add(lblIAvPa);


//        txtBookName = new JTextField();
//        txtBookName.setHorizontalAlignment(SwingConstants.CENTER);
//        txtBookName.setText("CS");
//        txtBookName.setFont(new Font("Tahoma", Font.BOLD, 16));
//        txtBookName.setBounds(51, 163, 232, 39);
//        contentPane.add(txtBookName);
//        txtBookName.setColumns(10);
//
//        JButton btnBuy = new JButton("Buy");
//        btnBuy.setFont(new Font("Tahoma", Font.BOLD, 19));
//        btnBuy.setBounds(120, 237, 113, 47);
//        contentPane.add(btnBuy);
    }
    public void updateList(String[][] list){
        parkingTable = new JTable(list,column);
        parkingTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
        parkingTable.setBounds(50, 100, 230, 100);
        contentPane.add(parkingTable ,BorderLayout.CENTER);
        contentPane.add(parkingTable.getTableHeader(), BorderLayout.NORTH);
        parkingTable.updateUI();
    }

}