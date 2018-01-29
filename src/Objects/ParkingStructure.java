package Objects;


import org.jgraph.JGraph;
import org.jgraph.graph.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

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
        cells = new DefaultGraphCell[55];


        // Create path Vertex
        cells[0] = createVertex("Gate", 20, 20, 40, 40, Color.YELLOW, false );
        cells[1] = createVertex("", 60, 20, 40, 40,Color.white, false );
        cells[2] = createVertex("", 60, 60, 40, 40,Color.white, false );
        cells[3] = createVertex("", 60, 100, 40, 40,Color.white, false );
        cells[4] = createVertex("", 60, 140, 40, 40,Color.white, false );
        cells[5] = createVertex("", 60, 180, 40, 40,Color.white, false );
        cells[6] = createVertex("", 60, 220, 40, 40,Color.white, false );
        cells[7] = createVertex("", 60, 260, 40, 40,Color.white, false );
        cells[8] = createVertex("", 60, 300, 40, 40,Color.white, false );
        cells[9] = createVertex("", 100, 300, 40, 40,Color.white, false );
        cells[10] = createVertex("", 140, 300, 40, 40,Color.white, false );
        cells[11] = createVertex("", 180, 300, 40, 40,Color.white, false );
        cells[12] = createVertex("", 180, 260, 40, 40,Color.white, false );
        cells[13] = createVertex("", 180, 220, 40, 40,Color.white, false );
        cells[14] = createVertex("", 180, 180, 40, 40,Color.white, false );
        cells[15] = createVertex("", 180, 140, 40, 40,Color.white, false );
        cells[16] = createVertex("", 180, 100, 40, 40,Color.white, false );
        cells[17] = createVertex("", 180, 60, 40, 40,Color.white, false );
        cells[18] = createVertex("", 140, 60, 40, 40,Color.white, false );
        cells[19] = createVertex("", 100, 60, 40, 40,Color.white, false );
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
        DefaultEdge edge = new DefaultEdge("foo");
        // Fetch the ports from the new vertices, and connect them with the edge
        edge.setSource(cells[0].getChildAt(0));
        edge.setTarget(cells[1].getChildAt(0));
        cells[54] = edge;

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
