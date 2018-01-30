package Objects;


import org.jgraph.JGraph;
import org.jgraph.graph.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ParkingStructure {
    JGraph graph;
    DefaultGraphCell[] cells;
    JFrame frame;
    Boolean [] slot;
    private JTable parkingTable;
    private String[] column = {"Capacity","Available","reserved"};
    private DefaultTableModel tableModel;

    public ParkingStructure(String title) {
        slot = new Boolean[31];
        Arrays.fill(slot, Boolean.FALSE);
        GraphModel model = new DefaultGraphModel();
        graph = new JGraph(model);
        graph.setCloneable(true);
        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);
        cells = new DefaultGraphCell[110];


        // Create path Vertex
        cells[0] = createVertex("Gate", 20, 20, 40, 40, Color.YELLOW, false );
        cells[1] = createVertex("101", 60, 20, 40, 40,Color.white, false );
        cells[2] = createVertex("102", 60, 60, 40, 40,Color.white, false );
        cells[3] = createVertex("103", 60, 100, 40, 40,Color.white, false );
        cells[4] = createVertex("104", 60, 140, 40, 40,Color.white, false );
        cells[5] = createVertex("105", 60, 180, 40, 40,Color.white, false );
        cells[6] = createVertex("106", 60, 220, 40, 40,Color.white, false );
        cells[7] = createVertex("107", 60, 260, 40, 40,Color.white, false );
        cells[8] = createVertex("108", 60, 300, 40, 40,Color.white, false );
        cells[9] = createVertex("109", 100, 300, 40, 40,Color.white, false );
        cells[10] = createVertex("110", 140, 300, 40, 40,Color.white, false );
        cells[11] = createVertex("111", 180, 300, 40, 40,Color.white, false );
        cells[12] = createVertex("112", 180, 260, 40, 40,Color.white, false );
        cells[13] = createVertex("113", 180, 220, 40, 40,Color.white, false );
        cells[14] = createVertex("114", 180, 180, 40, 40,Color.white, false );
        cells[15] = createVertex("115", 180, 140, 40, 40,Color.white, false );
        cells[16] = createVertex("116", 180, 100, 40, 40,Color.white, false );
        cells[17] = createVertex("117", 180, 60, 40, 40,Color.white, false );
        cells[18] = createVertex("118", 140, 60, 40, 40,Color.white, false );
        cells[19] = createVertex("119", 100, 60, 40, 40,Color.white, false );
        // Create Parking lot Vertex
        cells[20] = createVertex("1", 20, 60, 40, 40,Color.GREEN, false );
        cells[21] = createVertex("2", 20, 100, 40, 40,Color.GREEN, false );
        cells[22] = createVertex("3", 20, 140, 40, 40,Color.GREEN, false );
        cells[23] = createVertex("4", 20, 180, 40, 40,Color.GREEN, false );
        cells[24] = createVertex("5", 20, 220, 40, 40,Color.GREEN, false );
        cells[25] = createVertex("6", 20, 260, 40, 40,Color.GREEN, false );
        cells[26] = createVertex("7", 20, 300, 40, 40,Color.GREEN, false );
        cells[27] = createVertex("8", 60, 340, 40, 40,Color.GREEN, false );
        cells[28] = createVertex("9", 100, 340, 40, 40,Color.GREEN, false );
        cells[29] = createVertex("10", 140, 340, 40, 40,Color.GREEN, false );
        cells[30] = createVertex("11", 180, 340, 40, 40,Color.GREEN, false );
        cells[31] = createVertex("12", 220, 300, 40, 40,Color.GREEN, false );
        cells[32] = createVertex("13", 220, 260, 40, 40,Color.GREEN, false );
        cells[33] = createVertex("14", 220, 220, 40, 40,Color.GREEN, false );
        cells[34] = createVertex("15", 220, 180, 40, 40,Color.GREEN, false );
        cells[35] = createVertex("16", 220, 140, 40, 40,Color.GREEN, false );
        cells[36] = createVertex("17", 220, 100, 40, 40,Color.GREEN, false );
        cells[37] = createVertex("18", 220, 60, 40, 40,Color.GREEN, false );
        cells[38] = createVertex("19", 180, 20, 40, 40,Color.GREEN, false );
        cells[39] = createVertex("20", 140, 20, 40, 40,Color.GREEN, false );
        cells[40] = createVertex("21", 100, 20, 40, 40,Color.GREEN, false );
        cells[41] = createVertex("22", 100, 100, 40, 40,Color.GREEN, false );
        cells[42] = createVertex("23", 100, 140, 40, 40,Color.GREEN, false );
        cells[43] = createVertex("24", 100, 180, 40, 40,Color.GREEN, false );
        cells[44] = createVertex("25", 100, 220, 40, 40,Color.GREEN, false );
        cells[45] = createVertex("26", 100, 260, 40, 40,Color.GREEN, false );
        cells[46] = createVertex("27", 140, 100, 40, 40,Color.GREEN, false );
        cells[47] = createVertex("28", 140, 140, 40, 40,Color.GREEN, false );
        cells[48] = createVertex("29", 140, 180, 40, 40,Color.GREEN, false );
        cells[49] = createVertex("30", 140, 220, 40, 40,Color.GREEN, false );
        cells[50] = createVertex("31", 140, 260, 40, 40,Color.GREEN, false );
        // create blank slot
        cells[51] = createVertex("Blank", 20, 340, 40, 40,Color.BLACK, false );
        cells[52] = createVertex("Blank", 220, 340, 40, 40,Color.BLACK, false );
        cells[53] = createVertex("Blank", 220, 20, 40, 40,Color.BLACK, false );

        // Create Edge
//        DefaultEdge edge1 = new DefaultEdge("Gate-101");
//        // Fetch the ports from the new vertices, and connect them with the edge
//        edge1.setSource(cells[0].getChildAt(0));
//        edge1.setTarget(cells[1].getChildAt(0));
        cells[54] = setEdge(0,1);
        cells[55] = setEdge(1,2);
        cells[56] = setEdge(2,3);
        cells[57] = setEdge(3,4);
        cells[58] = setEdge(4,5);
        cells[59] = setEdge(5,6);
        cells[60] = setEdge(6,7);
        cells[61] = setEdge(7,8);
        cells[62] = setEdge(8,9);
        cells[63] = setEdge(9,10);
        cells[64] = setEdge(10,11);
        cells[65] = setEdge(11,12);
        cells[66] = setEdge(12,13);
        cells[67] = setEdge(13,14);
        cells[68] = setEdge(14,15);
        cells[69] = setEdge(15,16);
        cells[70] = setEdge(16,17);
        cells[71] = setEdge(17,18);
        cells[72] = setEdge(18,19);
        cells[73] = setEdge(19,2);
        cells[74] = setEdge(2,20);
        cells[75] = setEdge(3,21);
        cells[76] = setEdge(4,22);
        cells[77] = setEdge(5,23);
        cells[78] = setEdge(6,24);
        cells[79] = setEdge(7,25);
        cells[80] = setEdge(8,26);

        cells[81] = setEdge(3,22+19);
        cells[82] = setEdge(4,23+19);
        cells[83] = setEdge(5,24+19);
        cells[84] = setEdge(6,25+19);
        cells[85] = setEdge(7,26+19);

        cells[86] = setEdge(8,8+19);
        cells[87] = setEdge(9,9+19);
        cells[88] = setEdge(9,26+19);
        cells[89] = setEdge(10,10+19);
        cells[90] = setEdge(10,31+19);
        cells[91] = setEdge(11,11+19);
        cells[92] = setEdge(11,12+19);
        cells[93] = setEdge(12,13+19);
        cells[94] = setEdge(12,31+19);
        cells[95] = setEdge(13,14+19);
        cells[96] = setEdge(13,30+19);
        cells[97] = setEdge(14,29+19);
        cells[98] = setEdge(14,15+19);
        cells[99] = setEdge(15,28+19);
        cells[100] = setEdge(15,16+19);
        cells[101] = setEdge(16,17+19);
        cells[102] = setEdge(16,27+19);
        cells[103] = setEdge(17,18+19);
        cells[104] = setEdge(17,19+19);
        cells[105] = setEdge(18,20+19);
        cells[106] = setEdge(18,27+19);
        cells[107] = setEdge(19,21+19);
        cells[108] = setEdge(19,22+19);
        cells[109] = setEdge(1,21+19);




        // Set Arrow Style for edge
//        int arrow = GraphConstants.ARROW_CLASSIC;
//        GraphConstants.setLineEnd(edge.getAttributes(), arrow);
//        GraphConstants.setEndFill(edge.getAttributes(), true);

        // Insert the cells via the cache, so they get selected
        graph.getGraphLayoutCache().insert(cells);

        tableModel = new DefaultTableModel(column, 0);
        parkingTable = new JTable(tableModel);
        parkingTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        parkingTable.setBounds(50, 360, 230, 100);
//        frame.getContentPane().add(new JScrollPane(parkingTable));

        // Show in Frame
        frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(graph));
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);





//        frame.add(parkingTable, BorderLayout.CENTER);
//        frame.add(parkingTable.getTableHeader(), BorderLayout.NORTH);

    }
    public DefaultEdge setEdge(int source,int target ){
        DefaultEdge edge = new DefaultEdge(Integer.toString(source)+"-"+Integer.toString(target));
        edge.setSource(cells[source].getChildAt(0));
        edge.setTarget(cells[target].getChildAt(0));
        return edge;
    }

    public int getFreeLot(){
        int random ;
        do {
           random = ThreadLocalRandom.current().nextInt(20, 50 + 1);
        }while (GraphConstants.getGradientColor(cells[random].getAttributes()) != Color.GREEN );
        GraphConstants.setGradientColor(cells[random].getAttributes(),Color.RED);
//        graph.refresh();
//        graph.updateUI();
        graph.getGraphLayoutCache().update();
        frame.repaint();
        return random;
    }

    public void setRows(String[] list){
        tableModel.addRow(list);
    }

    private static DefaultGraphCell createVertex(String name, double x,
                                                double y, double w, double h, Color bg, boolean raised) {

        // Create vertex with the given name
        DefaultGraphCell cell = new DefaultGraphCell(name);
        // Set bounds
        GraphConstants.setBounds(cell.getAttributes(),
                new Rectangle2D.Double(x, y, w, h));
        // Set fill color
        if (bg != null) {
            GraphConstants.setGradientColor(cell.getAttributes(), bg);
            GraphConstants.setOpaque(cell.getAttributes(), true);
        }

        // Set raised border
        if (raised) {
            GraphConstants.setBorder(cell.getAttributes(),
                    BorderFactory.createRaisedBevelBorder());
        } else // Set black border
        {
            GraphConstants.setBorderColor(cell.getAttributes(),
                    Color.black);
        }
        // Add a Floating Port
        cell.addPort();

        return cell;
    }
}
