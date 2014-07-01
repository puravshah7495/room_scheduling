import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

/**
 * Created by Purav Shah
 */
public class Interface extends JFrame{
    private JPanel MainPanel;
    private JComboBox rooms;
    private JPanel panel2, panel2_1, panel2_2;
    private JPanel panel3, panel3_1;
    private JButton addRoomButton, addReservation, changeReservationDate, removeSelected,
            findEmployee, removeRoom, reserveStation, clearStation;
    private JTable table1, graphicTable;
    private JTextPane textPane1;
    private JButton graphicView;
    private JPanel panel4;
    private JPanel buttonPanel;
    private DefaultTableModel tableModel, tableModel1;
    private JRadioButton name, date;
    private JButton submit;
    private ArrayRooms roomList;
    private JTextField space, city, searchField;
    private JScrollPane scrollable;
    private ButtonGroup bg;
    private JSpinner month;

    public Interface() {
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        roomList = new ArrayRooms();
        readRooms();
        readEmployees();

        //Create button group for radio buttons
        bg = new ButtonGroup();
        bg.add(name);
        bg.add(date);

        if (roomList.getSize() == 0)
            removeRoom.setEnabled(false);

        //Adding functionality to addLocationButton
        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                city = new JTextField(5);
                space = new JTextField(5);
                final JPanel dialog = new JPanel(new GridLayout(2, 2));
                dialog.add(new JLabel("Enter City: "));
                dialog.add(city);
                dialog.add(new JLabel("Enter amount of space: "));
                dialog.add(space);
                int result = JOptionPane.showConfirmDialog(null, dialog, "Enter Room and space: ", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (city.getText().equals("") || space.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            roomList.addRoom(new Room(city.getText(), Integer.parseInt(space.getText())));
                            rooms.addItem(city.getText());
                            if (!removeRoom.isEnabled())
                                removeRoom.setEnabled(true);
                            writeRoom(city.getText(), space.getText());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Room Already Exists", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        removeRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //Add functionality to submit button
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rooms.getSelectedItem().toString().equals("Select Room")) {
                    JOptionPane.showMessageDialog(null,"Select A Room","Error",JOptionPane.ERROR_MESSAGE);
                } else {
                    Room l = returnRoom(rooms);

                    panel4.setVisible(true);
                    panel2_2.setVisible(true);
                    textPane1.setVisible(false);
                    table1.setVisible(false);
                    scrollable.setVisible(false);
                    panel3_1.setVisible(false);
                    searchField.setVisible(false);
                    findEmployee.setVisible(false);
                    name.setVisible(false);
                    date.setVisible(false);

                    graphicView.setEnabled(true);

                    if (tableModel.getColumnCount() == 0) {
                        //Set table headers
                        String [] headers = {"Station","Name","Room","Date"};
                        for (String header : headers) tableModel.addColumn(header);
                    }

                    tableModel.setColumnCount(4);
                    tableModel.setRowCount(l.getTotalSpace());

                    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
                    table1.setRowSorter(sorter);

                    refreshGraphicTable();
                }
            }
        });

        ListSelectionModel rowSelectionModel = table1.getSelectionModel();
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int rowSelected = table1.getSelectedRow();
                if (rowSelected != -1 && tableModel.getValueAt(rowSelected, 0) != null) {
                    removeSelected.setEnabled(true);
                    changeReservationDate.setEnabled(true);
                } else{
                    removeSelected.setEnabled(false);
                    changeReservationDate.setEnabled(false);
                }
            }
        });

        addReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField name;
                name = new JTextField(5);
                JTextField number;
                number = new JTextField(1);
                final JPanel dialog = new JPanel(new GridLayout(2, 2));
                dialog.add(new JLabel("Enter name: "));
                dialog.add(name);
                dialog.add(new JLabel("Enter Station: "));
                dialog.add(number);

                int result = JOptionPane.showConfirmDialog(null, dialog, "Enter Employee Information", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (name.getText().equals("") || number.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Room l = returnRoom(rooms);
                        Employee employee = new Employee(Integer.parseInt(number.getText()), name.getText(), l.getName(), getDate());
                        if (!l.containsEmployee(employee)) {
                            l.addEmployee(employee);
                            writeEmployee(Integer.parseInt(number.getText()), name.getText(), l.getName(), getDate());
                            refreshTable(rooms);
                        } else {
                            JOptionPane.showMessageDialog(null, "Employee Already Exists", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        changeReservationDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: ADD SELECTION FUNCTION
                JTextField newDate = new JTextField(5);
                final JPanel dialog = new JPanel(new GridLayout(1, 2));
                dialog.add(new JLabel("Enter Date: "));
                dialog.add(newDate);

                int result = JOptionPane.showConfirmDialog(null,dialog,"Enter Employee Information",JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (newDate.getText().equals("")) {
                        JOptionPane.showMessageDialog(null,"Please Fill All Fields","Error",JOptionPane.ERROR_MESSAGE);
                    } else {
                            int row = table1.getSelectedRow();
                            String name = (String) table1.getValueAt(row,1);
                            String date = (String) table1.getValueAt(row,3);
                            returnRoom(rooms).getEmployee(name, date).setReservationDate(newDate.getText());
                            updateEmployeeDate(name, newDate.getText());
                            refreshTable(rooms);
                    }
                }
            }
        });

        removeSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    int row = table1.getSelectedRow();
                    String name = (String) table1.getValueAt(row,1);
                    String date = (String) table1.getValueAt(row,3);
                    int number = (Integer) table1.getValueAt(row,0);
                    returnRoom(rooms).removeEmployee(returnRoom(rooms).getEmployee(name,date));
                    deleteEmployee(number);
                    refreshTable(rooms);
                }
            }
        });

        graphicView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: DEFINE FUNCTION TO SWITCH TO GRAPHIC VIEW

                if (!panel4.isVisible()) {
                    textPane1.setVisible(false);
                    table1.setVisible(false);
                    scrollable.setVisible(false);
                    panel3_1.setVisible(false);
                    searchField.setVisible(false);
                    findEmployee.setVisible(false);
                    name.setVisible(false);
                    date.setVisible(false);
                    panel4.setVisible(true);
                    panel2_2.setVisible(true);
                    refreshGraphicTable();
                } else {
                    textPane1.setVisible(true);
                    table1.setVisible(true);
                    panel3.setVisible(true);
                    scrollable.setVisible(true);
                    panel3_1.setVisible(true);
                    searchField.setVisible(true);
                    findEmployee.setVisible(true);
                    name.setVisible(true);
                    date.setVisible(true);
                    panel4.setVisible(false);
                    panel2_2.setVisible(false);
                    refreshTable(rooms);
                }

            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!findEmployee.isEnabled())
                    findEmployee.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (searchField.getText().equals(""))
                    findEmployee.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        findEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = bg.getSelection().getActionCommand();
                ArrayList<Employee> arrayList;
                if (selected.equals("name")) {
                    arrayList = searchEmployeeName(searchField.getText());
                } else {
                    arrayList = searchEmployeeDate(searchField.getText());
                }

                if (arrayList.size() > 0) {
                    tableModel.setRowCount(arrayList.size());
                    //Clear table data
                    for (int i=0; i < arrayList.size(); i++) {
                        for (int j=0; j < 3; j++) {
                            tableModel.setValueAt(null, i, j);
                        }
                    }

                    //Rewrite table data
                    for (int i=0; i < arrayList.size(); i++) {
                        tableModel.setValueAt(arrayList.get(i).getNumber(),i,0);
                        tableModel.setValueAt(arrayList.get(i).getName(), i, 1);
                        tableModel.setValueAt(arrayList.get(i).getRoom(), i, 2);
                        tableModel.setValueAt(arrayList.get(i).getReservationDate(), i, 3);
                    }
                }
            }
        });

        removeRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField room = new JTextField(5);
                JPanel dialog = new JPanel(new GridLayout(1, 2));
                dialog.add(new JLabel("Enter Room: "));
                dialog.add(room);

                int result = JOptionPane.showConfirmDialog(null, dialog, "Remove Room", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (room.getText().equals(""))
                        JOptionPane.showMessageDialog(null, "Please Enter a Room", "Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        roomList.removeRoom(roomList.geRoom(room.getText()));
                        rooms.removeItem(room.getText());
                        deleteRoom(room.getText());
                    }

                }
            }
        });

        reserveStation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = graphicTable.getSelectedRow();
                int col = graphicTable.getSelectedColumn();
                JTextField name = new JTextField(5);
                JPanel dialog = new JPanel(new GridLayout(1,2));
                dialog.add(new JLabel("Enter name"));
                dialog.add(name);
                int result = JOptionPane.showConfirmDialog(null, dialog, "Reserve",JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (name.getText().equals("")) {
                        JOptionPane.showMessageDialog(null,"Enter A Name","Error",JOptionPane.ERROR_MESSAGE);
                    } else {
                        Room r = returnRoom(rooms);
                        Employee employee = new Employee(getStation(row,col), name.getText(), rooms.getSelectedItem().toString(), getDate());
                        employee.setRow(row);
                        employee.setCol(col);
                        r.addEmployee(employee);
                        writeEmployee(getStation(row,col), name.getText(), rooms.getSelectedItem().toString(), getDate());
                        refreshGraphicTable();
                    }
                }
            }
        });

        clearStation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = graphicTable.getSelectedRow();
                int col = graphicTable.getSelectedColumn();
                int result = JOptionPane.showConfirmDialog(null, "Are you sure?","Clear",JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Room r = returnRoom(rooms);
                    Employee employee = (Employee) graphicTable.getValueAt(row,col);
                    r.removeEmployee(employee);
                    deleteEmployee(employee.getNumber());
                    refreshGraphicTable();
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel2 = new JPanel();
        panel2_2 = new JPanel();
        panel2_1 = new JPanel();
        panel3_1 = new JPanel(new FlowLayout());
        panel4 = new JPanel();
        JPanel panel4_1 = new JPanel();
        buttonPanel = new JPanel();
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollable = new JScrollPane(table1);
        table1 = new JTable(tableModel);
        graphicTable = new JTable(tableModel1);
        textPane1 = new JTextPane();
        submit = new JButton("submit");
        addRoomButton = new JButton("Add Room");
        removeRoom = new JButton("Remove Room");
        graphicView = new JButton("Change View");
        addReservation = new JButton("Create Reservation");
        changeReservationDate = new JButton("Modify Reservation Date");
        removeSelected = new JButton("Remove Selected");
        findEmployee = new JButton("Search");
        reserveStation = new JButton("Reserve");
        clearStation = new JButton("clear");
        name = new JRadioButton("Name");
        name.setActionCommand("name");
        name.setSelected(true);
        date = new JRadioButton("Date");
        date.setActionCommand("date");
        final JTextArea employeeInfo = new JTextArea();
        searchField = new JTextField();

        //Initial invisible
        textPane1.setVisible(false);
        table1.setVisible(false);
        panel3_1.setVisible(false);
        panel2_2.setVisible(false);
        panel4.setVisible(false);
        searchField.setVisible(false);
        findEmployee.setVisible(false);
        name.setVisible(false);
        date.setVisible(false);

        //Initial Disables
        removeSelected.setEnabled(false);
        changeReservationDate.setEnabled(false);
        graphicView.setEnabled(false);
        findEmployee.setEnabled(false);
        reserveStation.setEnabled(false);
        clearStation.setEnabled(false);

        //Create date spinner
        month = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(month, "M/dd/yyyy");
        month.setEditor(editor);

        //Modify Text Pane
        textPane1.setEditable(false);
        Color c = new Color(0x009933);
        textPane1.setBackground(c);
        textPane1.setForeground(Color.WHITE);

        //Table1 Details
        table1.setRowHeight(30);
        table1.getTableHeader().setResizingAllowed(false);
        table1.getTableHeader().setReorderingAllowed(false);

        //GraohicTable formatting
        tableModel1.setRowCount(2);
        tableModel1.setColumnCount(5);
        graphicTable.setRowHeight(150);
        graphicTable.setCellSelectionEnabled(true);
        graphicTable.setGridColor(Color.BLACK);

        graphicTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

            }
        });

        //Make graphic table interactive
        graphicTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int row = graphicTable.rowAtPoint(p);
                int column = graphicTable.columnAtPoint(p);
                if (graphicTable.isCellSelected(row,column)) {
                    if (graphicTable.getValueAt(row, column) == null) {
                        reserveStation.setEnabled(true);
                        clearStation.setEnabled(false);
                        employeeInfo.setText("Empty");
                    } else {
                        reserveStation.setEnabled(false);
                        clearStation.setEnabled(true);
                        Employee employee = (Employee) graphicTable.getValueAt(row, column);
                        String text = "\n\nName: " + employee.getName() + "\n\nDate: " + employee.getReservationDate() + "\n\nStation number: " + employee.getNumber();
                        employeeInfo.setText(text);
                    }
                }
            }
        });

        //Center Text
        StyledDocument doc = textPane1.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        //Define Panel Layouts
        panel2_1.setBorder(BorderFactory.createTitledBorder("Set Date"));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2_1.setLayout(new BoxLayout(panel2_1, BoxLayout.Y_AXIS));
        panel2_2.setLayout(new BoxLayout(panel2_2, BoxLayout.Y_AXIS));

        BorderLayout bl = new BorderLayout();
        bl.setVgap(10);
        panel4.setLayout(bl);

        panel4_1.setLayout(new GridLayout(1, 1));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        employeeInfo.setBorder(BorderFactory.createTitledBorder("Employee Info"));
        employeeInfo.setBackground(panel2.getBackground());

        //Center Buttons
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        reserveStation.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearStation.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel4_1.setPreferredSize(new Dimension(650, 250));

        //Add content to panel
        panel2.add(panel2_1);
        panel2.add(Box.createRigidArea(new Dimension(0,10)));
        panel2.add(submit);
        panel2.add(Box.createRigidArea(new Dimension(0,10)));
        panel2_1.add(month);
        panel2_1.add(Box.createRigidArea(new Dimension(0,5)));
        panel2_2.add(employeeInfo);
        panel2_2.add(reserveStation);
        panel2_2.add(clearStation);
        panel3_1.add(addReservation);
        panel3_1.add(changeReservationDate);
        panel3_1.add(removeSelected);
        panel4.add(panel4_1, BorderLayout.CENTER);
        panel4_1.add(graphicTable);
        buttonPanel.add(addRoomButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(removeRoom);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(graphicView);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(searchField);
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(findEmployee);
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPanel.add(name);
        buttonPanel.add(date);
    }

    public Room returnRoom(JComboBox jcb) {
        //Returns room based off of selected combobox item
        String s = jcb.getSelectedItem().toString();
        return roomList.geRoom(s);
    }

    public void refreshGraphicTable() {
        Room temp = returnRoom(rooms);
        ArrayList<Employee> e = temp.getEmployees();
        ArrayList<Employee> e2 = new ArrayList<Employee>();

        for (Employee emp : e) {
            if (emp.getReservationDate().equals(getDate()))
                e2.add(emp);
        }

        for (int i=0; i<2; i++) {
            for (int j=0; j<5; j++) {
                tableModel1.setValueAt(null, i,j);
            }
        }

        for (int i=0; i<e2.size(); i++) {
            tableModel1.setValueAt(e2.get(i),e2.get(i).getRow(),e2.get(i).getCol());
        }

        for (int i=0; i<5; i++) graphicTable.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
    }

    public void refreshTable(JComboBox jcb) {
        Room temp = returnRoom(jcb);
        ArrayList<Employee> e = temp.getEmployees();
        ArrayList<Employee> e2 = new ArrayList<Employee>();

        for (Employee anE : e) {
            if (anE.getReservationDate().equals(getDate())) {
                e2.add(anE);
            }
        }

        //Clear table data
        for (int i=0; i < table1.getRowCount(); i++) {
            for (int j=0; j < 4; j++) {
                tableModel.setValueAt(null, i, j);
            }
        }

        //Rewrite table data
        for (int i=0; i < e2.size(); i++) {
                tableModel.setValueAt(e2.get(i).getNumber(), i, 0);
                tableModel.setValueAt(e2.get(i).getName(), i, 1);
                tableModel.setValueAt(e2.get(i).getRoom(), i, 2);
                tableModel.setValueAt(e2.get(i).getReservationDate(), i, 3);
        }

        //Deactivate addReservation button if table is full
        if (tableModel.getValueAt(table1.getRowCount()-1, 0) != null)
            addReservation.setEnabled(false);
        else
            addReservation.setEnabled(true);

        textPane1.setText("Total Space: " + temp.getTotalSpace() + "\t" + "Open Space: " + countEmpty());
        if (countEmpty() == 0) {
            Color filled = new Color(0xE83134);
            textPane1.setBackground(filled);
        }
    }

    public int countEmpty() {
        int counter = 0;
        for (int i = 0; i < table1.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0) == null && tableModel.getValueAt(i, 1) == null && tableModel.getValueAt(i, 2) == null)
                counter++;
        }
        return counter;
    }

    public String getDate(){
        Date date2;
        SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy");
        date2 = (Date) month.getValue();
        return format.format(date2);
    }

    public void writeRoom(String room, String space) {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "INSERT INTO ROOM (NAME,SPACE)" +
                    "VALUES ('" + room + "','"  + space + "');";
            stmt.executeUpdate(sql);
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readRooms() {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ROOM;");
            while (rs.next()) {
                String name = rs.getString("name");
                int space = rs.getInt("space");
                roomList.addRoom(new Room(name, space));
                rooms.addItem(name);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeEmployee(int number, String name, String room, String date){
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "INSERT INTO EMPLOYEE (NUMBER,NAME,ROOM,DATE) VALUES (" + number+ ",'" + name + "','" + room + "','" + date + "');";
            stmt.executeUpdate(sql);

            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readEmployees() {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EMPLOYEE;");
            while (rs.next()) {
                int number = rs.getInt("number");
                String name = rs.getString("name");
                String room = rs.getString("room");
                String date = rs.getString("date");
                Employee e = new Employee(number,name,room,date);
                roomList.geRoom(room).addEmployee(e);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeeDate(String name, String date) {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "UPDATE EMPLOYEE set DATE = "+date+" WHERE NAME= "+name+";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> searchEmployeeName(String name) {
        Connection c;
        Statement stmt;
        ArrayList<Employee> temp = new ArrayList<Employee>();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "SELECT * FROM EMPLOYEE WHERE NAME= '" + name + "';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int number = rs.getInt("number");
                String room = rs.getString("room");
                String date = rs.getString("date");
                Employee e = new Employee(number,name,room,date);
                temp.add(e);
            }
            rs.close();
            stmt.close();
            c.close();
        }catch (Exception e) {
            System.err.print(e.getClass().getName() + ": " + e.getMessage());
        }
        return temp;
    }

    public ArrayList<Employee> searchEmployeeDate(String date) {
        Connection c;
        Statement stmt;
        ArrayList<Employee> temp = new ArrayList<Employee>();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "SELECT * FROM EMPLOYEE;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int number = rs.getInt("number");
                String name = rs.getString("name");
                String room = rs.getString("room");
                Employee e = new Employee(number,name,room,date);
                temp.add(e);
            }
            rs.close();
            stmt.close();
            c.close();
        }catch (Exception e) {
            System.err.print(e.getClass().getName() + ": " + e.getMessage());
        }
        return temp;
    }

    public void deleteEmployee(int number) {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "DELETE FROM EMPLOYEE WHERE NUMBER= " + number  + ";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        }catch (Exception e) {
            System.err.print(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteRoom(String name) {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "DELETE FROM ROOM WHERE NAME= '" + name + "';";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        }catch (Exception e) {
            System.err.print(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int getStation(int row, int col) {
        if (row == 0)
            return row+col+1;
        else
            return row+col+5;
    }
}

class CellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        Component c = super.getTableCellRendererComponent(table,value,selected,focused,row,column);
        if (value instanceof Employee)  {
            c.setBackground(Color.RED);
        } else
            c.setBackground(new Color(0x57A23D));
        return c;
    }
}