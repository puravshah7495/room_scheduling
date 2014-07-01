import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

/**
 * Created by Purav Shah
 */
public class GraphicCalendar{
    static JLabel labelMonth, labelYear;
    static JButton btnPrev, btnNext;
    static JTable tableCalendar;
    static JComboBox comboYear;
    static JFrame main;
    static Container pane;
    static DefaultTableModel mdlCalendar;
    static JScrollPane scroll;
    static JPanel panel;
    static int day, month, year, currentMonth, currentYear;

    public GraphicCalendar() {


        //Define look of calendar
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}

        //Create calendar controls
        labelMonth = new JLabel("January");
        labelYear = new JLabel("Change year: ");
        comboYear = new JComboBox();
        btnNext = new JButton(">>");
        btnPrev = new JButton("<<");
        mdlCalendar = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCalendar = new JTable(mdlCalendar);
        scroll = new JScrollPane(tableCalendar);
        panel = new JPanel(null);

        //Prepare calendar
        panel.setBorder(BorderFactory.createTitledBorder("Calendar"));

        //Add components to container
        panel.add(labelMonth);
        panel.add(labelYear);
        panel.add(comboYear);
        panel.add(btnNext);
        panel.add(btnPrev);
        panel.add(scroll);

        //Set bounds
        panel.setBounds(0,0,320,335);
        labelMonth.setBounds(160-labelMonth.getPreferredSize().width/2,25,100,25);
        labelYear.setBounds(10, 305, 80, 20);
        comboYear.setBounds(230, 305, 80, 20);
        btnPrev.setBounds(10, 25, 50, 25);
        btnNext.setBounds(260, 25, 50, 25);
        scroll.setBounds(10, 50, 300, 250);

        //Create a calendar
        GregorianCalendar calendar = new GregorianCalendar();
        day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        month = calendar.get(GregorianCalendar.MONTH);
        year = calendar.get(GregorianCalendar.YEAR);
        currentMonth = month;
        currentYear = year;

        //Add years to the combobox
        for (int i = year - 10; i <= year+5; i++) {
            comboYear.addItem(String.valueOf(i));
        }

        //Label days of the week
        String [] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int j = 0; j<7; j++) {
            mdlCalendar.addColumn(headers[j]);
        }

        tableCalendar.getParent().setBackground(tableCalendar.getBackground()); // Set background color

        //Prevent resizing calendar
        tableCalendar.getTableHeader().setResizingAllowed(false);
        tableCalendar.getTableHeader().setReorderingAllowed(false);

        //Only allow one cell selection
        tableCalendar.setColumnSelectionAllowed(true);
        tableCalendar.setRowSelectionAllowed(true);
        tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Set a 6x7 calendar
        tableCalendar.setRowHeight(38);
        mdlCalendar.setColumnCount(7);
        mdlCalendar.setRowCount(6);

        //Add actions to buttons
        btnPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMonth == 0) {
                    currentMonth = 11;
                    currentYear--;
                } else {
                    currentMonth--;
                }
                refreshDisplay(currentMonth,currentYear);
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMonth == 11) {
                    currentMonth = 0;
                    currentYear++;
                } else
                    currentMonth++;
                refreshDisplay(currentMonth,currentYear);
            }
        });
        comboYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboYear.getSelectedItem() != null) {
                    String b = comboYear.getSelectedItem().toString();
                    currentYear = Integer.parseInt(b);
                    refreshDisplay(currentMonth,currentYear);
                }
            }
        });

        refreshDisplay(month,year);
    }

    public static void refreshDisplay(int month, int yr) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som; //Number Of Days, Start Of Month

        //Prepare buttons
        btnPrev.setEnabled(true); //Enable buttons at first
        btnNext.setEnabled(true);
        if (month == 0 && yr <= year-10){btnPrev.setEnabled(false);} //Too early
        if (month == 11 && yr >= year+100){btnNext.setEnabled(false);} //Too late
        labelMonth.setText(months[month]); //Refresh the month label (at the top)
        labelMonth.setBounds(160-labelMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
        comboYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box

        //Get number of days and start of month
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);

        //Clear table
        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                mdlCalendar.setValueAt(null, i, j);
            }
        }

        for (int i=1; i<=nod; i++){
            int row = new Integer((i+som-2)/7);
            int column  =  (i+som-2)%7;
            mdlCalendar.setValueAt(i, row, column);
        }
    }

    public static JFrame getMain() {
        return main;
    }

    public static JPanel getPanel() {
        return panel;
    }

    public static JTable getTableCalendar() {
        return tableCalendar;
    }
}
