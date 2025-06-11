package parking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// Main dashboard for parking management system
public class Main_dashboard extends javax.swing.JFrame {

    // Color palette for UI
    private static final java.awt.Color COLOR_PRIMARY_YELLOW = new java.awt.Color(230, 180, 0);
    private static final java.awt.Color COLOR_ACCENT_GOLD = new java.awt.Color(212, 163, 0);
    private static final java.awt.Color COLOR_BG_WHITE = new java.awt.Color(250, 250, 250);
    private static final java.awt.Color COLOR_TEXT_BLACK = new java.awt.Color(20, 20, 20);
    private static final java.awt.Color COLOR_TEXT_DARKGRAY = new java.awt.Color(51, 51, 51);
    private static final java.awt.Color COLOR_PANEL_LIGHTGRAY = new java.awt.Color(242, 242, 242);
    private static final java.awt.Color COLOR_SUCCESS_GREEN = new java.awt.Color(67, 160, 71);
    private static final java.awt.Color COLOR_WARNING_ORANGE = new java.awt.Color(255, 145, 0);
    private static final java.awt.Color COLOR_ERROR_RED = new java.awt.Color(229, 115, 115);
    private static final java.awt.Color COLOR_BORDER_GRAY = new java.awt.Color(189, 189, 189);
    private static final java.awt.Color COLOR_TABLE_SELECTION = new java.awt.Color(255, 236, 179);
    private static final java.awt.Color COLOR_TABLE_ROW_ALT = new java.awt.Color(252, 243, 207);

    // Variables for managing state and UI
    int manage_selected = -1;
    int manage_num_row = 1;
    Connection conn = new dbConnect().dbcon();
    private Map<Integer, java.util.Date> runningTimeMap = new HashMap<>();
    private Timer runningTimeTimer;
    private javax.swing.JLabel totalRevenueLabel = new javax.swing.JLabel();
    private javax.swing.JPanel revenuePanel = new javax.swing.JPanel();
    private int computedHours = 1;
    private javax.swing.JLabel availableSlotsLabel = new javax.swing.JLabel();
    private javax.swing.JTextField historySearchField = new javax.swing.JTextField();
    private javax.swing.JLabel historySearchIcon = new javax.swing.JLabel();
    private javax.swing.table.TableRowSorter<DefaultTableModel> historyRowSorter;
    private javax.swing.JLabel dateTimeLabel = new javax.swing.JLabel();
    private Timer dateTimeTimer;

    // Constructor: sets up the dashboard and UI
    public Main_dashboard() {
        // Setup UI and tables
        initComponents();
        initTableSales();
        initTableHistory();
        centerFrame();
        panel_sales.setVisible(true);
        panel_manage.setVisible(false);
        panel_history.setVisible(false);
        main_panel.requestFocusInWindow();
        initTableManage();
        setReturnCarFieldsEnabled(false);

        sales_hours.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (sales_id_out.getText().trim().isEmpty()) {
                    sales_hours.setText("");
                    sales_hours.setEnabled(false);
                }
            }
        });

        // Setup revenue label and search field
        totalRevenueLabel.setText("Total Revenue: 0 pesos");
        totalRevenueLabel.setFont(new Font("Inter", Font.BOLD, 18));
        totalRevenueLabel.setForeground(COLOR_PRIMARY_YELLOW);
        totalRevenueLabel.setOpaque(true);
        totalRevenueLabel.setBackground(new Color(254, 240, 241));
        totalRevenueLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY_YELLOW, 2, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        historySearchField.setFont(new Font("Inter", Font.PLAIN, 16));
        historySearchField.setToolTipText("Search a record here");
        historySearchField.setColumns(16);
        historySearchField.setPreferredSize(new java.awt.Dimension(200, 32));
        historySearchField.setMaximumSize(new java.awt.Dimension(200, 32));
        historySearchField.setMinimumSize(new java.awt.Dimension(120, 32));
        historySearchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 1, true),
            BorderFactory.createEmptyBorder(4, 36, 4, 8)
        ));
        historySearchField.setText("");
        historySearchField.putClientProperty("JTextField.placeholderText", "Search a record here");

        java.net.URL searchIconUrl = getClass().getResource("/asset/icons/search.png");
        if (searchIconUrl != null) {
            ImageIcon icon = new ImageIcon(new ImageIcon(searchIconUrl).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
            historySearchIcon.setIcon(icon);
        }
        historySearchIcon.setOpaque(false);
        historySearchIcon.setBounds(8, 8, 20, 20);

        javax.swing.JLayeredPane searchLayeredPane = new javax.swing.JLayeredPane();
        searchLayeredPane.setPreferredSize(new java.awt.Dimension(220, 32));
        searchLayeredPane.setLayout(null);
        historySearchField.setBounds(0, 0, 200, 32);
        historySearchIcon.setBounds(8, 6, 20, 20);
        searchLayeredPane.add(historySearchField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        searchLayeredPane.add(historySearchIcon, javax.swing.JLayeredPane.PALETTE_LAYER);

        historySearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterHistoryTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterHistoryTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterHistoryTable(); }
        });

        javax.swing.JPanel revenueSearchPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 0));
        revenueSearchPanel.setBackground(new Color(254, 240, 241));
        revenueSearchPanel.add(totalRevenueLabel, java.awt.BorderLayout.WEST);
        revenueSearchPanel.add(searchLayeredPane, java.awt.BorderLayout.EAST);
        revenueSearchPanel.setBounds(0, 590, 890, 40);

        panel_history.remove(totalRevenueLabel);
        panel_history.add(revenueSearchPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 890, 40));
        panel_history.setComponentZOrder(revenueSearchPanel, 0);

        jScrollPane3.setBounds(0, 0, 890, 590);
        panel_history.setLayout(null);
        panel_history.add(jScrollPane3);

        updateTotalRevenue();

        try {
            UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Inter", Font.PLAIN, 14)));
            UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Inter", Font.PLAIN, 13)));
        } catch (Exception e) {}

        // Set icons for buttons
        java.net.URL url;
        url = getClass().getResource("/asset/icons/payment_history.png");
        if (url != null) {
            jButton1.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }
        url = getClass().getResource("/asset/icons/home.png");
        if (url != null) {
            jButton2.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }
        url = getClass().getResource("/asset/icons/parking_slot.png");
        if (url != null) {
            jButton3.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }
        url = getClass().getResource("/asset/icons/add-car.png");
        if (url != null) {
            sales_btnAdd.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }
        url = getClass().getResource("/asset/icons/update.png");
        if (url != null) {
            btn_update.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }
        url = getClass().getResource("/asset/icons/delete.png");
        if (url != null) {
            btn_delete.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }
        url = getClass().getResource("/asset/icons/add-parkingslot.png");
        if (url != null) {
            btn_add.setIcon(new ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        }

        // Add logo and credit label to sidebar
        try {
            url = getClass().getResource("/asset/icons/parkner-logo.png");
            if (url != null) {
                ImageIcon logoIcon = new ImageIcon(
                    new javax.swing.ImageIcon(url)
                        .getImage().getScaledInstance(220, 120, java.awt.Image.SCALE_SMOOTH)
                );
                JLabel logoLabel = new JLabel(logoIcon);
                logoLabel.setHorizontalAlignment(JLabel.CENTER);
                jPanel2.add(logoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 480, 220, 220));
                JLabel creditLabel = new JLabel("@Parkner 2025");
                creditLabel.setFont(new Font("Inter", Font.ITALIC, 10));
                creditLabel.setHorizontalAlignment(JLabel.CENTER);
                jPanel2.add(creditLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, 210, 20));
            }
        } catch (Exception e) {}

        // Set colors for panels and buttons
        main_panel.setBackground(COLOR_BG_WHITE);
        panel_sales.setBackground(COLOR_PANEL_LIGHTGRAY);
        panel_manage.setBackground(COLOR_PANEL_LIGHTGRAY);
        panel_history.setBackground(COLOR_PANEL_LIGHTGRAY);
        jPanel2.setBackground(COLOR_BORDER_GRAY);
        footer.setBackground(COLOR_BORDER_GRAY);
        jPanel2.setBackground(COLOR_BORDER_GRAY);
        jLabel1.setForeground(COLOR_TEXT_BLACK);
        jLabel2.setForeground(COLOR_TEXT_DARKGRAY);
        jLabel13.setForeground(COLOR_TEXT_DARKGRAY);
        jButton1.setBackground(COLOR_PRIMARY_YELLOW);
        jButton1.setForeground(COLOR_TEXT_BLACK);
        jButton2.setBackground(COLOR_PRIMARY_YELLOW);
        jButton2.setForeground(COLOR_TEXT_BLACK);
        jButton3.setBackground(COLOR_PRIMARY_YELLOW);
        jButton3.setForeground(COLOR_TEXT_BLACK);
        sales_btnAdd.setBackground(COLOR_PRIMARY_YELLOW);
        sales_btnAdd.setForeground(COLOR_TEXT_BLACK);
        btn_update.setBackground(COLOR_ACCENT_GOLD);
        btn_update.setForeground(COLOR_TEXT_BLACK);
        btn_delete.setBackground(COLOR_ERROR_RED);
        btn_delete.setForeground(COLOR_BG_WHITE);
        btn_add.setBackground(COLOR_SUCCESS_GREEN);
        btn_add.setForeground(COLOR_BG_WHITE);
        sales_btn_pay.setBackground(COLOR_SUCCESS_GREEN);
        sales_btn_pay.setForeground(COLOR_BG_WHITE);

        // Table header and row color setup
        JTableHeader salesHeader = table_sales.getTableHeader();
        salesHeader.setBackground(COLOR_PRIMARY_YELLOW);
        salesHeader.setForeground(COLOR_TEXT_BLACK);
        salesHeader.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT_GOLD, 2, true));
        JTableHeader manageHeader = table_manage.getTableHeader();
        manageHeader.setBackground(COLOR_PRIMARY_YELLOW);
        manageHeader.setForeground(COLOR_TEXT_BLACK);
        manageHeader.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT_GOLD, 2, true));
        JTableHeader historyHeader = table_history.getTableHeader();
        historyHeader.setBackground(COLOR_PRIMARY_YELLOW);
        historyHeader.setForeground(COLOR_TEXT_BLACK);
        historyHeader.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT_GOLD, 2, true));
        table_sales.setBackground(COLOR_PANEL_LIGHTGRAY);
        table_sales.setForeground(COLOR_TEXT_BLACK);
        table_sales.setSelectionBackground(COLOR_TABLE_SELECTION);
        table_sales.setSelectionForeground(COLOR_TEXT_BLACK);
        table_manage.setBackground(COLOR_PANEL_LIGHTGRAY);
        table_manage.setForeground(COLOR_TEXT_BLACK);
        table_manage.setSelectionBackground(COLOR_TABLE_SELECTION);
        table_manage.setSelectionForeground(COLOR_TEXT_BLACK);
        table_history.setBackground(COLOR_PANEL_LIGHTGRAY);
        table_history.setForeground(COLOR_TEXT_BLACK);
        table_history.setSelectionBackground(COLOR_TABLE_SELECTION);
        table_history.setSelectionForeground(COLOR_TEXT_BLACK);

        // Hide Parking Slot column in some tables
        if (table_sales.getColumnModel().getColumnCount() > 0) {
            table_sales.getColumnModel().getColumn(0).setMinWidth(0);
            table_sales.getColumnModel().getColumn(0).setMaxWidth(0);
            table_sales.getColumnModel().getColumn(0).setWidth(0);
        }
        if (table_history.getColumnModel().getColumnCount() > 0) {
            table_history.getColumnModel().getColumn(0).setMinWidth(0);
            table_history.getColumnModel().getColumn(0).setMaxWidth(0);
            table_history.getColumnModel().getColumn(0).setWidth(0);
        }

        // Alternate row color for tables
        DefaultTableCellRenderer altRowRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_PANEL_LIGHTGRAY : COLOR_TABLE_ROW_ALT);
                } else {
                    c.setBackground(COLOR_TABLE_SELECTION);
                }
                c.setForeground(COLOR_TEXT_BLACK);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };
        for (int i = 0; i < table_sales.getColumnCount(); i++) {
            table_sales.getColumnModel().getColumn(i).setCellRenderer(altRowRenderer);
        }
        for (int i = 0; i < table_manage.getColumnCount(); i++) {
            table_manage.getColumnModel().getColumn(i).setCellRenderer(altRowRenderer);
        }
        for (int i = 0; i < table_history.getColumnCount(); i++) {
            table_history.getColumnModel().getColumn(i).setCellRenderer(altRowRenderer);
        }
        totalRevenueLabel.setBackground(COLOR_ACCENT_GOLD);
        totalRevenueLabel.setForeground(COLOR_TEXT_BLACK);
        revenuePanel.setBackground(COLOR_PANEL_LIGHTGRAY);
        revenuePanel.setBorder(javax.swing.BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 2));
        historyRowSorter = new javax.swing.table.TableRowSorter<>((DefaultTableModel) table_history.getModel());
        table_history.setRowSorter(historyRowSorter);
    }

    // Loads the table with cars currently parked
    public void initTableSales(){
        DefaultTableModel model = (DefaultTableModel) table_sales.getModel();
        // Remove combo box for slot selection
        sales_combo.setVisible(false);
        model.setRowCount(0);
        manage_num_row = 1;
        runningTimeMap.clear();

        Statement st;
        ResultSet rs;

        int availableSlotCount = 0;
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM parking_store";
            rs = st.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("id");
                String regis = rs.getString("regis");
                String gen = rs.getString("gen");
                String timeInStr = rs.getString("time_in");
                boolean vailable = rs.getBoolean("vailable");
                // Only display rows where slot is NOT available (i.e., has a car parked)
                // And only if car details are present (not empty)
                if (!vailable && gen != null && !gen.trim().isEmpty() && regis != null && !regis.trim().isEmpty()) {
                    String runningTime = "";
                    java.util.Date timeInDate = null;
                    if (timeInStr != null && !timeInStr.trim().isEmpty()) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            java.util.Date parsedTime = sdf.parse(timeInStr);
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            java.util.Calendar now = java.util.Calendar.getInstance();
                            cal.setTime(parsedTime);
                            // Set the date part to today
                            cal.set(now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH), now.get(java.util.Calendar.DAY_OF_MONTH));
                            timeInDate = cal.getTime();
                            long diff = System.currentTimeMillis() - timeInDate.getTime();
                            if (diff < 0) {
                                diff += 24 * 60 * 60 * 1000;
                            }
                            long totalMinutes = diff / (60 * 1000);
                            long hrs = totalMinutes / 60;
                            long mins = totalMinutes % 60;
                            runningTime = String.format("%02d:%02d", hrs, mins);
                            runningTimeMap.put(id, timeInDate);
                        } catch (Exception e) {
                            runningTime = "";
                        }
                    }
                    model.addRow(new Object[]{
                        id,
                        regis,
                        gen,
                        timeInStr,
                        runningTime
                    });
                    // Do not increment manage_num_row here, as it's not used for table_sales display
                }
                if(vailable) {
                    availableSlotCount++;
                }
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }

        // Remove any rows that have empty car details (safety, in case)
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            String regis = (String) model.getValueAt(i, 1);
            String gen = (String) model.getValueAt(i, 2);
            if (regis == null || regis.trim().isEmpty() || gen == null || gen.trim().isEmpty()) {
                model.removeRow(i);
            }
        }

        // Update available slots label
        availableSlotsLabel.setText("Available Slots: " + availableSlotCount);

        // Only disable the button here, do not show dialog
        if (availableSlotCount == 0) {
            sales_btnAdd.setEnabled(false);
        } else {
            sales_btnAdd.setEnabled(true);
        }

        //Header column
        JTableHeader theader = table_sales.getTableHeader();
        theader.setBackground(COLOR_PRIMARY_YELLOW);
        theader.setForeground(COLOR_TEXT_BLACK);
        theader.setFont(new Font("Inter", Font.PLAIN, 18));
        theader.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT_GOLD, 2, true));
        // Center header text
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) theader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for (int i = 0; i < table_sales.getColumnCount(); i++) {
            table_sales.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table_sales.setRowHeight(30);
        table_sales.setFont(new Font("Inter", Font.PLAIN, 16));
        // Apply alternate row renderer after updating model
        applyAlternateRowRenderer(table_sales);
    }

    // Loads the table with parking slot status
    public void initTableManage(){
        DefaultTableModel model = (DefaultTableModel) table_manage.getModel();
        model.setRowCount(0);
        manage_num_row = 1;

        Statement st;
        ResultSet rs;

        String pList = "";
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT id, vailable FROM parking_store";
            rs = st.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                String status = rs.getBoolean("vailable") == true ? "Available" : "Unavailable";
                model.addRow(new Object[]{rs.getInt("id"), status});
                manage_num_row++;
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }

        //Header column
        JTableHeader theader = table_manage.getTableHeader();
        theader.setBackground(COLOR_PRIMARY_YELLOW);
        theader.setForeground(COLOR_TEXT_BLACK);
        theader.setFont(new Font("Inter", Font.PLAIN, 18));
        theader.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT_GOLD, 2, true));
        // Center header text
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) theader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Change column header to "Parking Slot"
        table_manage.getColumnModel().getColumn(0).setHeaderValue("Parking Slot");
        table_manage.getColumnModel().getColumn(1).setHeaderValue("Status");

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_manage.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_manage.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table_manage.setRowHeight(30);
        table_manage.setFont(new Font("Inter", Font.PLAIN, 16));
        // Apply alternate row renderer after updating model
        applyAlternateRowRenderer(table_manage);
    }

    // Loads the service history table
    private void initTableHistory(){
        DefaultTableModel model = (DefaultTableModel) table_history.getModel();
        model.setRowCount(0);

        Statement st;
        ResultSet rs;

        try{
            st = (Statement) conn.createStatement();
            // Fetch date_in from report
            String sql = "SELECT * FROM report";
            rs = st.executeQuery(sql);

            while(rs.next()){
                // Format date_in as "MMMM dd, yyyy"
                String dateInRaw = rs.getString("date_in");
                String formattedDate = dateInRaw;
                if (dateInRaw != null && !dateInRaw.trim().isEmpty()) {
                    try {
                        java.text.SimpleDateFormat dbFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        java.text.SimpleDateFormat displayFormat = new java.text.SimpleDateFormat("MMMM dd, yyyy");
                        java.util.Date date = dbFormat.parse(dateInRaw);
                        formattedDate = displayFormat.format(date);
                    } catch (Exception e) {
                        // fallback to raw if parsing fails
                        formattedDate = dateInRaw;
                    }
                }
                // Add time_in, time_out, reference_id, and formatted date_in to the row
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("gen"),
                    rs.getString("regis"),
                    rs.getInt("totalPrice"),
                    formattedDate, // <-- formatted date_in here
                    rs.getString("time_in"),
                    rs.getString("time_out"),
                    rs.getString("reference_id")
                });
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }

        // Update table headers to include Date
        JTableHeader theader = table_history.getTableHeader();
        theader.setBackground(COLOR_PRIMARY_YELLOW);
        theader.setForeground(COLOR_TEXT_BLACK);
        theader.setFont(new Font("Inter", Font.PLAIN, 18));
        theader.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT_GOLD, 2, true));
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) theader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for (int i = 0; i < table_history.getColumnCount(); i++) {
            table_history.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table_history.setRowHeight(30);
        table_history.setFont(new Font("Inter", Font.PLAIN, 16));
        applyAlternateRowRenderer(table_history);
        updateTotalRevenue();
    }

    // Centers the dashboard window on the screen
    public void centerFrame() {

            Dimension windowSize = getSize();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Point centerPoint = ge.getCenterPoint();

            int dx = centerPoint.x - windowSize.width / 2;
            int dy = centerPoint.y - windowSize.height / 2;    
            setLocation(dx, dy);
            
            main_panel.requestFocusInWindow();
    }

    // UI setup (auto-generated)
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        main_panel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        footer = new javax.swing.JPanel();
        panel_sales = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_sales = new javax.swing.JTable();
        sales_gen = new javax.swing.JTextField();
        sales_regis = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        sales_combo = new javax.swing.JComboBox<>();
        sales_btnAdd = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        sales_hours = new javax.swing.JTextField();
        sales_btn_pay = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        sales_regis_out = new javax.swing.JTextField();
        sales_id_out = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        sales_gen_out = new javax.swing.JTextField();
        panel_history = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_history = new javax.swing.JTable();
        panel_manage = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_manage = new javax.swing.JTable();
        manage_update = new javax.swing.JPanel();
        btn_update = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        combo = new javax.swing.JComboBox<>();
        btn_delete = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();

        jLabel7.setText("jLabel7");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        main_panel.setBackground(new java.awt.Color(255, 255, 255));
        main_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // Set sidebar panel to gray
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // Eye-catching application name
        jLabel1.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 32)); // Larger, bold
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PARKNER");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Padding left
        // Optionally, add a subtle shadow effect (simulate with a second label if desired)
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 200, 40));

        // Tagline styling
        jLabel2.setFont(new java.awt.Font("Inter", java.awt.Font.ITALIC, 14)); // Italic, smaller
        jLabel2.setForeground(new java.awt.Color(220, 220, 220)); // Lighter gray
        jLabel2.setText("Your Partner in Parking");
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 0)); // Padding left
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 200, 20));

        jButton1.setBackground(new java.awt.Color(152, 16, 30));
        jButton1.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/payment_history.png"))); // NOI18N
        jButton1.setText("Service History");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusPainted(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 190, 50));

        jButton2.setBackground(new java.awt.Color(152, 16, 30));
        jButton2.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/home.png"))); // NOI18N
        jButton2.setText("Dashboard");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 190, 50));

        jButton3.setBackground(new java.awt.Color(152, 16, 30));
        jButton3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/parking_slot.png"))); // NOI18N
        jButton3.setText("Parking Slot");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusPainted(false);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 190, 50));

        // Move and style the Parking Fee label to the leftmost side
        jLabel13.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 13));
        jLabel13.setText("Parking fee 50 pesos/hour");
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 180, 20));

        // --- Date and Time label above Parkner logo in sidebar ---
        dateTimeLabel.setFont(new Font("Inter", Font.BOLD, 10));
        dateTimeLabel.setForeground(COLOR_TEXT_BLACK);
        dateTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Lower the label to below the Service History button and above the logo
        // Service History button y=360, height=50, logo y=410, so use y=420
        dateTimeLabel.setBounds(0, 420, 210, 30);
        updateDateTimeLabel();
        // Timer to update every second for real running time
        dateTimeTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDateTimeLabel();
                // Animate the label horizontally (simple left-right movement)
                int baseX = 0;
                int amplitude = 20; // pixels to move left/right
                long time = System.currentTimeMillis() / 1000;
                int offset = (int) (Math.sin(time % 60 / 60.0 * 2 * Math.PI) * amplitude);
                dateTimeLabel.setLocation(baseX + offset, 420);
            }
        });
        dateTimeTimer.setInitialDelay(0);
        dateTimeTimer.start();
        jPanel2.add(dateTimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 210, 30));

        // --- Add logo.png at the lowermost part of the sidebar ---
        try {
            ImageIcon logoIcon = new ImageIcon(
                new javax.swing.ImageIcon(getClass().getResource("/asset/icons/logo.png"))
                    .getImage().getScaledInstance(220, 220, java.awt.Image.SCALE_SMOOTH)
            );
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            // Center horizontally, place at the bottom (x= -5 to center in 210px panel, y=410 for 220px logo)
            jPanel2.add(logoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(-5, 410, 220, 220));

            // Add credit text just below the logo, centered and a bit higher
            JLabel creditLabel = new JLabel("@Parkner 2025");
            creditLabel.setFont(new Font("Inter", Font.PLAIN, 13));
            creditLabel.setForeground(new Color(255, 255, 255));
            creditLabel.setHorizontalAlignment(JLabel.CENTER);
            jPanel2.add(creditLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 210, 20));
        } catch (Exception e) {
            // If logo not found, do nothing
        }

        // Set icon-text gap and alignment for all main buttons (sidebar and right panel)
        int sidebarIconTextGap = 16; // consistent gap between icon and text
        Dimension sidebarBtnSize = new Dimension(190, 50); // consistent button size
        int sidebarIconLeftMargin = 12; // margin in pixels

        javax.swing.border.EmptyBorder iconLeftMargin = new javax.swing.border.EmptyBorder(0, sidebarIconLeftMargin, 0, 0);

        // Sidebar buttons
        jButton1.setIconTextGap(sidebarIconTextGap);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setPreferredSize(sidebarBtnSize);
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            jButton1.getBorder(), iconLeftMargin));

        jButton2.setIconTextGap(sidebarIconTextGap);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setPreferredSize(sidebarBtnSize);
        jButton2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            jButton2.getBorder(), iconLeftMargin));

        jButton3.setIconTextGap(sidebarIconTextGap);
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton3.setPreferredSize(sidebarBtnSize);
        jButton3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            jButton3.getBorder(), iconLeftMargin));

        // Right panel/manage buttons
        btn_add.setIconTextGap(sidebarIconTextGap);
        btn_add.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_add.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_add.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            btn_add.getBorder(), iconLeftMargin));

        btn_update.setIconTextGap(sidebarIconTextGap);
        btn_update.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_update.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_update.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            btn_update.getBorder(), iconLeftMargin));

        btn_delete.setIconTextGap(sidebarIconTextGap);
        btn_delete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_delete.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_delete.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            btn_delete.getBorder(), iconLeftMargin));

        // Sales panel buttons
        sales_btnAdd.setIconTextGap(sidebarIconTextGap);
        sales_btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sales_btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        sales_btnAdd.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            sales_btnAdd.getBorder(), iconLeftMargin));

        sales_btn_pay.setIconTextGap(sidebarIconTextGap);
        sales_btn_pay.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sales_btn_pay.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        sales_btn_pay.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            sales_btn_pay.getBorder(), iconLeftMargin));

        main_panel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 650));

        footer.setBackground(new java.awt.Color(115, 12, 22));

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE) // widened from 900 to 1100
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        main_panel.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 1100, 20)); // widened

        panel_sales.setBackground(new java.awt.Color(254, 240, 241));
        panel_sales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBorder(null);

        table_sales.setBackground(new java.awt.Color(254, 240, 241));
        table_sales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parking Slot", "License Plate", "Car Brand", "Time In", "Running Time"
            }
        ));
        table_sales.setAlignmentX(0.0F);
        table_sales.setAlignmentY(0.0F);
        table_sales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table_sales.setFocusable(false);
        table_sales.setGridColor(new java.awt.Color(255, 255, 255));
        table_sales.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table_sales.setOpaque(false);
        table_sales.setRequestFocusEnabled(false);
        table_sales.setSelectionBackground(new java.awt.Color(193, 16, 22));
        table_sales.setShowHorizontalLines(false);
        table_sales.setShowVerticalLines(false);
        table_sales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_salesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_sales);

        panel_sales.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 630)); // widened from 440 to 650

        // --- BEGIN FIXED RIGHT PANEL LAYOUT FOR SALES PANEL ---
        // Set right panel width and starting x position
        int rightPanelWidth = 220;
        int rightPanelX = 650; // 650 (table width) + 0 (no extra padding)

        // Use vertical spacing for each component
        int y = 30;
        int spacing = 40;
        int labelHeight = 22;
        int fieldHeight = 28;
        int btnHeight = 40;
        int fieldWidth = 120;
        int labelX = rightPanelX + 10;
        int fieldX = rightPanelX + 100;

        // Add Car to Park label
        jLabel4.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 18)); // Bold
        jLabel4.setText("Add Car to Park");
        panel_sales.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(labelX + 40, y, rightPanelWidth, labelHeight));
        y += spacing;

        // Car Brand
        jLabel5.setFont(new java.awt.Font("Inter", 0, 14));
        jLabel5.setText("Car Brand");
        panel_sales.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(labelX, y, 80, labelHeight));
        sales_gen.setFont(new java.awt.Font("Inter", 0, 14));
        sales_gen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_gen, new org.netbeans.lib.awtextra.AbsoluteConstraints(fieldX, y, fieldWidth, fieldHeight));
        y += spacing;

        // License Plate
        jLabel8.setFont(new java.awt.Font("Inter", 0, 14));
        jLabel8.setText("License Plate");
        panel_sales.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(labelX, y, 90, labelHeight));
        sales_regis.setFont(new java.awt.Font("Inter", 0, 14));
        sales_regis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_regis, new org.netbeans.lib.awtextra.AbsoluteConstraints(fieldX, y, fieldWidth, fieldHeight));
        y += spacing;

        // Show available slots label
        availableSlotsLabel.setFont(new Font("Inter", Font.BOLD, 14));
        availableSlotsLabel.setForeground(COLOR_SUCCESS_GREEN);
        availableSlotsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        availableSlotsLabel.setText("Available Slots: 0");
        // Add to panel_sales in the Add Car section (just above Add Car button)
        panel_sales.add(availableSlotsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(rightPanelX + 20, y, rightPanelWidth - 40, 25));
        y += 30;

        // Add Parked Car Button
        sales_btnAdd.setBackground(new java.awt.Color(115, 12, 22));
        sales_btnAdd.setFont(new java.awt.Font("Inter", 0, 12));
        sales_btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        sales_btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/add-car.png")));
        sales_btnAdd.setText("Add a Car");
        sales_btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sales_btnAdd.setFocusPainted(false);
        sales_btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sales_btnAddMouseClicked(evt);
            }
        });
        panel_sales.add(sales_btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(rightPanelX + 20, y, rightPanelWidth - 40, btnHeight));
        y += btnHeight + 20;

        // --- Center and widen the divider, and move it lower ---
        int dividerWidth = rightPanelWidth + 40; // wider than right panel
        int dividerX = rightPanelX + (rightPanelWidth - dividerWidth) / 2 + 20; // center relative to right panel
        int dividerY = 310; // move divider higher so Pay & Exit is visible
        jPanel1.setBackground(COLOR_BORDER_GRAY);
        panel_sales.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(dividerX, dividerY, dividerWidth, 5));

        // --- Move Pay & Exit section higher so it's visible ---
        int paySectionY = dividerY + 25; // place just below the divider
        int payLabelY = paySectionY;
        int payFieldSpacing = 40;
        int payLabelHeight = 22;
        int payFieldHeight = 28;
        int payBtnHeight = 40;
        int payLabelX = rightPanelX + 10;
        int payFieldX = rightPanelX + 100;

        // Pay & Exit label
        jLabel9.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 18));
        jLabel9.setText("Pay & Exit");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panel_sales.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(rightPanelX + 10, payLabelY, rightPanelWidth - 20, payLabelHeight+20));
        payLabelY += payFieldSpacing;

        // --- HIDE Parking Slot (output) in Pay & Exit section ---
        // jLabel11.setFont(new java.awt.Font("Inter", 0, 14));
        // jLabel11.setText("Parking Slot");
        // panel_sales.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(payLabelX, payLabelY, 80, payLabelHeight));
        // sales_id_out.setEditable(false);
        // sales_id_out.setFont(new java.awt.Font("Inter", 0, 14));
        // sales_id_out.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        // panel_sales.add(sales_id_out, new org.netbeans.lib.awtextra.AbsoluteConstraints(payFieldX, payLabelY, fieldWidth, payFieldHeight));
        // payLabelY += payFieldSpacing;
        // Instead, just increment payLabelY to keep spacing
        payLabelY += payFieldSpacing;

        // Car Brand (output)
        jLabel14.setFont(new java.awt.Font("Inter", 0, 14));
        jLabel14.setText("Car Brand");
        panel_sales.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(payLabelX, payLabelY, 80, payLabelHeight));
        sales_gen_out.setEditable(false);
        sales_gen_out.setFont(new java.awt.Font("Inter", 0, 14));
        sales_gen_out.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_gen_out, new org.netbeans.lib.awtextra.AbsoluteConstraints(payFieldX, payLabelY, fieldWidth, payFieldHeight));
        payLabelY += payFieldSpacing;

        // License Plate (output)
        jLabel12.setFont(new java.awt.Font("Inter", 0, 14));
        jLabel12.setText("License Plate");
        panel_sales.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(payLabelX, payLabelY, 90, payLabelHeight));
        sales_regis_out.setEditable(false);
        sales_regis_out.setFont(new java.awt.Font("Inter", 0, 14));
        sales_regis_out.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_regis_out, new org.netbeans.lib.awtextra.AbsoluteConstraints(payFieldX, payLabelY, fieldWidth, payFieldHeight));
        payLabelY += payFieldSpacing;

        // Enter Number of Hours
        jLabel10.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
        jLabel10.setText("Estimated No. of Hours");
        panel_sales.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(payLabelX, payLabelY, 150, payLabelHeight));
        sales_hours.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 14));
        sales_hours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_hours, new org.netbeans.lib.awtextra.AbsoluteConstraints(payFieldX + 60, payLabelY - 5, 60, 30));
        payLabelY += payFieldSpacing;

        // Pay Button
        sales_btn_pay.setBackground(new java.awt.Color(0, 204, 0));
        sales_btn_pay.setFont(new java.awt.Font("Inter", 0, 14));
        sales_btn_pay.setForeground(new java.awt.Color(255, 255, 255));
        sales_btn_pay.setText("Pay");
        sales_btn_pay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sales_btn_pay.setFocusPainted(false);
        sales_btn_pay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sales_btn_payMouseClicked(evt);
            }
        });
        // Widened button: increase width from (rightPanelWidth - 100) to (rightPanelWidth - 60)
        panel_sales.add(sales_btn_pay, new org.netbeans.lib.awtextra.AbsoluteConstraints(rightPanelX + 35, payLabelY, rightPanelWidth - 60, payBtnHeight));

        // --- END FIXED RIGHT PANEL LAYOUT FOR SALES PANEL ---

        main_panel.add(panel_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 890, 630)); // widened

        // --- History Panel ---
        panel_history.setBackground(new java.awt.Color(254, 240, 241));
        panel_history.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBorder(null);

        table_history.setBackground(new java.awt.Color(254, 240, 241));
        table_history.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N
        table_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parking Slot", "Car Brand", "License Plate", "Amount Paid", "Date", "Time In", "Time Out", "Reference ID"
            }
        ));
        table_history.setAlignmentX(0.0F);
        table_history.setAlignmentY(0.0F);
        table_history.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table_history.setFocusable(false);
        table_history.setGridColor(new java.awt.Color(255, 255, 255));
        table_history.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table_history.setOpaque(false);
        table_history.setRequestFocusEnabled(false);
        table_history.setSelectionBackground(new java.awt.Color(193, 16, 22));
        table_history.setShowHorizontalLines(false);
        table_history.setShowVerticalLines(false);
        table_history.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_historyMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(table_history);

        panel_history.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 590)); // widened from 690 to 890, height 590 for revenue panel

        // Adjust revenue panel width
        revenuePanel.setBounds(0, 590, 890, 40); // widened

        main_panel.add(panel_history, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 890, 630)); // widened

        // --- Manage Panel ---
        panel_manage.setBackground(new java.awt.Color(254, 240, 241));
        panel_manage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        // Make the table_manage less wide so the buttons fit properly at the right
        table_manage.setBackground(new java.awt.Color(254, 240, 241));
        table_manage.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        table_manage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parking Slot", "Status"
            }
        ));
        table_manage.setToolTipText("");
        table_manage.setAlignmentX(0.0F);
        table_manage.setAlignmentY(0.0F);
        table_manage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table_manage.setGridColor(new java.awt.Color(255, 255, 255));
        table_manage.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table_manage.setOpaque(false);
        table_manage.setRequestFocusEnabled(false);
        table_manage.setSelectionBackground(new java.awt.Color(193, 16, 22));
        table_manage.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table_manage.setShowHorizontalLines(false);
        table_manage.setShowVerticalLines(false);
        table_manage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_manageMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_manage);

        // Make the table 600px wide so the buttons have more space at the right
        panel_manage.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 630)); // table less wide

        // Move manage panel controls (buttons) to the rightmost side, vertically stacked, and make them wider
        int managePanelX = 640; // right after the table (600px table + 20px gap)
        int buttonWidth = 200;
        panel_manage.add(manage_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(managePanelX, 100, buttonWidth, 300)); // more vertical space

        btn_add.setBackground(new java.awt.Color(115, 12, 22));
        btn_add.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        btn_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/add-parkingslot.png"))); // NOI18N
        btn_add.setText("Add Slot");
        btn_add.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_add.setFocusPainted(false);
        btn_add.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addMouseClicked(evt);
            }
        });
        panel_manage.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(managePanelX, 110, buttonWidth, 50));

        // Status label (black) beside combo box
        int statusY = 180;
        int statusLabelWidth = 60;
        int comboWidth = buttonWidth - statusLabelWidth - 10; // 10px gap
        jLabel3.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        jLabel3.setForeground(java.awt.Color.BLACK);
        jLabel3.setText("Status");
        panel_manage.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(managePanelX, statusY, statusLabelWidth, 30));

        combo.setBackground(Color.WHITE); // no red
        combo.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        combo.setForeground(java.awt.Color.BLACK);
        combo.setBorder(null);
        combo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        combo.setFocusable(false);
        combo.setLightWeightPopupEnabled(false);
        combo.setOpaque(true);
        panel_manage.add(combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(managePanelX + statusLabelWidth + 10, statusY, comboWidth, 30));
        statusY += spacing;

        btn_update.setBackground(new java.awt.Color(115, 12, 22));
        btn_update.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/update.png"))); // NOI18N
        btn_update.setText("Edit Status");
        btn_update.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_update.setFocusPainted(false);
        btn_update.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_updateMouseClicked(evt);
            }
        });
        panel_manage.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(managePanelX, 230, buttonWidth, 50));

        btn_delete.setBackground(new java.awt.Color(115, 12, 22));
        btn_delete.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons/delete.png"))); // NOI18N
        btn_delete.setText("Delete Slot");
        btn_delete.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete.setFocusPainted(false);
        btn_delete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteMouseClicked(evt);
            }
        });
        panel_manage.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(managePanelX, 290, buttonWidth, 50));

        main_panel.add(panel_manage, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 890, 630)); // widened

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Handles Parking Slot sidebar button click
    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        panel_sales.setVisible(false);
        panel_manage.setVisible(true);
        panel_history.setVisible(false);
        
        manage_update.setVisible(true);

        // Only enable Add Slot button, disable Edit and Delete until a row is selected
        btn_add.setEnabled(true);
        btn_update.setEnabled(false);
        btn_delete.setEnabled(false);
        combo.setEnabled(false);
    }//GEN-LAST:event_jButton3MouseClicked

    // Handles Dashboard sidebar button click
    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        panel_sales.setVisible(true);
        panel_manage.setVisible(false);
        panel_history.setVisible(false);
    }//GEN-LAST:event_jButton2MouseClicked

    // Handles Service History sidebar button click
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        panel_sales.setVisible(false);
        panel_manage.setVisible(false);
        panel_history.setVisible(true);
        // Always refresh the service history table when Service History is clicked
        initTableHistory();
        updateTotalRevenue(); // Ensure revenue is updated when switching to history
    }//GEN-LAST:event_jButton1MouseClicked

    // Handles Edit Status button click for parking slots
    private void btn_updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateMouseClicked
        // Prevent action if button is disabled
        if (!btn_update.isEnabled()) {
            return;
        }
        // Show confirmation dialog before editing
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to edit the status of this parking spot?",
            "Confirm Edit",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        PreparedStatement pt = null;

        try{
            Boolean status = combo.getSelectedItem().toString().equals("Available") ? true : false;
            String sql = "UPDATE parking_store SET vailable = " + status + " WHERE id=" + manage_selected;    
            if(status == true){
                // Also reset time_in when making available
                sql = "UPDATE parking_store SET vailable = true, gen='', regis='', time_in='' WHERE id=" + manage_selected;   
            }
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            initTableSales();
            // Show dialog for edit action
            JOptionPane.showMessageDialog(this, "Parking slot " + manage_selected + " status updated to '" + combo.getSelectedItem() + "'.", "Edit Parking Slot", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }//GEN-LAST:event_btn_updateMouseClicked

    // Handles Delete Slot button click for parking slots
    private void btn_deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseClicked
        // Prevent action if button is disabled
        if (!btn_delete.isEnabled()) {
            return;
        }
        // Show confirmation dialog before deleting
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this parking spot?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        if(manage_selected != 0){
            Statement st;
            ResultSet rs;
            PreparedStatement pt = null;
            
            try{
                String sql = "DELETE FROM parking_store WHERE id=" + manage_selected;
                pt = conn.prepareStatement(sql);
                pt.execute();
                // Show dialog for delete action
                JOptionPane.showMessageDialog(this, "Parking slot " + manage_selected + " has been deleted.", "Delete Parking Slot", JOptionPane.INFORMATION_MESSAGE);
            } catch(SQLException ex){
                System.out.print(ex);
            }

            try{
                for(int i=manage_selected;i<=manage_num_row;i++){
                    int id = i+1;
                    
                    String sql = "UPDATE parking_store SET id="+i+" WHERE id=" + id;
                    pt = conn.prepareStatement(sql);
                    pt.execute();
                }
                
                Boolean vailable_store = true;
                String gen_store = "", regis_store = "", time_in_store = "";
                st = (Statement) conn.createStatement();
                String sql = "SELECT * FROM parking_store WHERE id=" + manage_selected;
                rs = st.executeQuery(sql);
                while(rs.next()){
                    vailable_store = rs.getBoolean("vailable");
                    gen_store = rs.getString("gen");
                    regis_store = rs.getString("regis");
                    time_in_store = rs.getString("time_in");
                }
                
                for(int i=manage_selected;i<=manage_num_row;++i){                    
                    st = (Statement) conn.createStatement();
                    sql = "SELECT * FROM parking_store WHERE id=" + (i+1);
                    rs = st.executeQuery(sql);
                    
                    while(rs.next()){
                        sql = "UPDATE parking_store SET vailable=" + vailable_store + ", gen='" + gen_store + "', regis='" + regis_store + "', time_in='" + time_in_store + "' WHERE id=" + (i+1);
                        pt = conn.prepareStatement(sql);
                        pt.execute();
                        vailable_store = rs.getBoolean("vailable");
                        gen_store = rs.getString("gen");
                        regis_store = rs.getString("regis");
                        time_in_store = rs.getString("time_in");
                    }
                    
                    sql = "UPDATE parking_store SET vailable=true, gen='', regis='', time_in='' WHERE id=" + manage_selected;
                    pt = conn.prepareStatement(sql);
                    pt.execute();
                }
                
                initTableSales();
                initTableManage();
                manage_selected = 0;
            } catch(SQLException ex){
                System.out.print(ex);
            }
        }
    }//GEN-LAST:event_btn_deleteMouseClicked

    // Handles click on a row in the manage table
    private void table_manageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_manageMouseClicked
        int selected = (int) table_manage.getValueAt(table_manage.getSelectedRow(), 0);

        manage_selected = selected;

        Statement st;
        ResultSet rs;

        combo.removeAllItems();
        combo.addItem("Available");
        combo.addItem("Unavailable");

        // Always disable first, will enable only if available or empty unavailable
        btn_update.setEnabled(false);
        btn_delete.setEnabled(false);
        combo.setEnabled(false);

        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT id, vailable, gen, regis FROM parking_store WHERE id=" + selected;
            rs = st.executeQuery(sql);

            while(rs.next()){
                String status = rs.getBoolean("vailable") == true ? "Available" : "Unavailable";
                String gen = rs.getString("gen");
                String regis = rs.getString("regis");
                combo.setSelectedItem(status);
                if ("Unavailable".equals(status)) {
                    if ((gen == null || gen.trim().isEmpty()) && (regis == null || regis.trim().isEmpty())) {
                        // Unavailable but no car/brand/license, allow edit/delete with confirmation
                        btn_update.setEnabled(true);
                        btn_delete.setEnabled(true);
                        combo.setEnabled(true);
                    } else {
                        // Unavailable and has car/brand/license, block edit/delete
                        btn_update.setEnabled(false);
                        btn_delete.setEnabled(false);
                        combo.setEnabled(false);
                        JOptionPane.showMessageDialog(this, 
                            "A car is parked here. Please accomplish the payment first before editing or deleting the parking slot.",
                            "Edit/Delete Not Allowed",
                            JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    // Enable only if available
                    btn_update.setEnabled(true);
                    btn_delete.setEnabled(true);
                    combo.setEnabled(true);
                }
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }

        manage_update.setVisible(false);
    }//GEN-LAST:event_table_manageMouseClicked

    // Handles Add Slot button click
    private void btn_addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addMouseClicked
        PreparedStatement pt = null;

        try{
            // Get the current maximum id from parking_store
            int nextId = 1;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(id) FROM parking_store");
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }
            String sql = "INSERT INTO parking_store (id, vailable, gen, regis) VALUES ("+ nextId +", true, '', '')";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            initTableSales();
            // Show dialog for add action with correct slot number
            JOptionPane.showMessageDialog(this, "Parking slot " + nextId + " has been added.", "Add Parking Slot", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }//GEN-LAST:event_btn_addMouseClicked

    // Handles Add Car button click
    private void sales_btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sales_btnAddMouseClicked
    // If button is disabled, show dialog and return
    if (!sales_btnAdd.isEnabled()) {
        JOptionPane.showMessageDialog(this, "No parking slots available. Please add more slots or wait for a slot to be vacated.", "No Parking Slot Available", JOptionPane.WARNING_MESSAGE);
        return;
    }
    // Add validation for empty car brand or license plate
    String carBrand = sales_gen.getText();
    String licensePlate = sales_regis.getText();
    if(carBrand.trim().isEmpty() || licensePlate.trim().isEmpty()){
        JOptionPane.showMessageDialog(this, "Car Brand and License Plate cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Find the lowest-numbered available slot
    int selectSlot = -1;
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT id FROM parking_store WHERE vailable=true ORDER BY id ASC LIMIT 1");
        if (rs.next()) {
            selectSlot = rs.getInt("id");
        }
    } catch (SQLException ex) {
        System.out.print(ex);
    }
    if (selectSlot == -1) {
        JOptionPane.showMessageDialog(this, "No parking slots available. Please add more slots or wait for a slot to be vacated.", "No Parking Slot Available", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Generate unique 8-digit reference number
    String refNumber = "";
    Random rand = new Random();
    boolean unique = false;
    while (!unique) {
        int num = 10000000 + rand.nextInt(90000000); // 8-digit
        refNumber = String.valueOf(num);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM parking_store WHERE reference_id='" + refNumber + "'");
            if (rs.next() && rs.getInt(1) == 0) {
                unique = true;
            }
        } catch (SQLException ex) {
            System.out.print(ex);
            break;
        }
    }

    // Get current time for Time In
    String timeIn = new SimpleDateFormat("hh:mm a").format(new java.util.Date());
    // Get current date for Date In
    String dateIn = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

    // Add Time In and assigned slot to confirmation dialog
    String confirmMsg = "Please confirm the following details:\n"
            + "Car Brand: " + carBrand + "\n"
            + "License Plate: " + licensePlate + "\n\n"
            + "Is this information correct?";
    int confirm = JOptionPane.showConfirmDialog(this, confirmMsg, "Confirm Car Details", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        PreparedStatement pt = null;
        try{
            // Save date_in as well
            String sql = "UPDATE parking_store SET gen = '" + carBrand + "', regis = '" + licensePlate + "', vailable=false, reference_id='" + refNumber + "', time_in='" + timeIn + "', date_in='" + dateIn + "' WHERE id=" + selectSlot;
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            // Instead of initTableSales(), call the following to ensure only valid rows are shown:
            initTableSales();
            // Remove any rows that have empty car details (safety)
            DefaultTableModel model = (DefaultTableModel) table_sales.getModel();
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                String regisVal = (String) model.getValueAt(i, 1);
                String genVal = (String) model.getValueAt(i, 2);
                if (regisVal == null || regisVal.trim().isEmpty() || genVal == null || genVal.trim().isEmpty()) {
                    model.removeRow(i);
                }
            }
            JOptionPane.showMessageDialog(this, "Car parked successfully." + ".\n\nReference Number: " + refNumber + "\nTime In: " + timeIn, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }
}//GEN-LAST:event_sales_btnAddMouseClicked

    // Handles click on a row in the sales table
    int id;
    String gen, regis;
    private void table_salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_salesMouseClicked
        // Adjusted for new column order (with Running Time column)
        id = (int) table_sales.getValueAt(table_sales.getSelectedRow(), 0);
        regis = (String) table_sales.getValueAt(table_sales.getSelectedRow(), 1);
        gen = (String) table_sales.getValueAt(table_sales.getSelectedRow(), 2);

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT vailable, time_in FROM parking_store WHERE id=" + id);
            if (rs.next() && !rs.getBoolean("vailable")) {
                // Additional validation: check if car brand or license plate is empty
                if (gen == null || gen.trim().isEmpty() || regis == null || regis.trim().isEmpty()) {
                    sales_id_out.setText("");
                    sales_gen_out.setText("");
                    sales_regis_out.setText("");
                    setReturnCarFieldsEnabled(false);
                    JOptionPane.showMessageDialog(this, "Please select occupied car.", "No Car Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                sales_id_out.setText(id + "");
                sales_gen_out.setText(gen);
                sales_regis_out.setText(regis);

                // --- Compute running time and hours for payment ---
                String timeInStr = rs.getString("time_in");
                int hoursToPay = 1;
                if (timeInStr != null && !timeInStr.trim().isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        java.util.Date parsedTime = sdf.parse(timeInStr);
                        java.util.Calendar cal = java.util.Calendar.getInstance();

                        java.util.Calendar now = java.util.Calendar.getInstance();
                        cal.setTime(parsedTime);
                        // Set the date part to today
                        cal.set(now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH), now.get(java.util.Calendar.DAY_OF_MONTH));
                        long diffMillis = System.currentTimeMillis() - cal.getTimeInMillis();
                        if (diffMillis < 0) {
                            diffMillis += 24 * 60 * 60 * 1000;
                        }
                        long totalMinutes = diffMillis / (60 * 1000);
                        hoursToPay = (int) (totalMinutes / 60);
                        if (totalMinutes % 60 != 0) {
                            hoursToPay += 1; // round up to next hour if any minutes
                        }
                        if (hoursToPay < 1) hoursToPay = 1;
                    } catch (Exception e) {
                        hoursToPay = 1;
                    }
                }
                computedHours = hoursToPay;
                // Show computed hours in the UI
                sales_hours.setText(String.valueOf(computedHours));
                sales_hours.setEnabled(false); // always disabled, just for display

                // Restore the Pay button icon and text (no value)
                java.net.URL url = getClass().getResource("/asset/icons/payment.png");
                if (url != null) {
                    sales_btn_pay.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
                } else {
                    sales_btn_pay.setIcon(null);
                }
                sales_btn_pay.setText("Pay");

                // Enable return car fields
                setReturnCarFieldsEnabled(true);
            } else {
                sales_id_out.setText("");
                sales_gen_out.setText("");
                sales_regis_out.setText("");
                sales_hours.setText("");
                sales_btn_pay.setText("Pay");
                setReturnCarFieldsEnabled(false);
                // Show dialog when user clicks on an available slot
                JOptionPane.showMessageDialog(this, "Please select occupied car.", "No Car Selected", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            System.out.print(ex);
        }
    }//GEN-LAST:event_table_salesMouseClicked

    // Enables or disables the Pay & Exit fields
    private void setReturnCarFieldsEnabled(boolean enabled) {
        sales_id_out.setEnabled(false); // always disabled (output only)
        sales_gen_out.setEnabled(false); // always disabled (output only)
        sales_regis_out.setEnabled(false); // always disabled (output only)
        sales_hours.setEnabled(false); // always disabled, just for display
        sales_btn_pay.setEnabled(enabled); // <-- ensure pay button is also disabled
        if (!enabled) {
            sales_hours.setText("");
            java.net.URL url = getClass().getResource("/asset/icons/payment.png");
            if (url != null) {
                sales_btn_pay.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(url).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } else {
                sales_btn_pay.setIcon(null);
            }
            sales_btn_pay.setText("Pay");
        }
    }

    // Handles Pay button click
    private void sales_btn_payMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sales_btn_payMouseClicked
        // Prevent pay if no car is selected
        if (sales_id_out.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an occupied car first.", "No Car Selected", JOptionPane.WARNING_MESSAGE);
            sales_hours.setText("");
            sales_hours.setEnabled(false);
            return;
        }

        // Use computedHours for payment
        int hours = computedHours;
        if (hours <= 0) hours = 1;
        int totalPrice = hours * 50;

        // Only open the receipt window, do not execute SQL here
        receip receiptWindow = new receip(id, gen, regis, totalPrice, this);
        receiptWindow.setVisible(true);

        // --- Clear time_in, running time, and reference_id after payment ---
        try {
            // This will clear time_in, running time (by removing time_in), and reference_id
            String sql = "UPDATE parking_store SET vailable=true, gen='', regis='', time_in='', reference_id='' WHERE id=" + id;
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.execute();
            // Remove running time entry for this id
            runningTimeMap.remove(id);

            // --- Remove all rows from the dashboard table (table_sales) ---
            DefaultTableModel model = (DefaultTableModel) table_sales.getModel();
            model.setRowCount(0);

            initTableManage();
            // Do NOT call initTableSales() here, as it would reload all rows
            // Optionally clear the output fields
            sales_id_out.setText("");
            sales_gen_out.setText("");
            sales_regis_out.setText("");
            sales_hours.setText("");
            sales_btn_pay.setText("Pay");
            setReturnCarFieldsEnabled(false);
        } catch (SQLException ex) {
            System.out.print(ex);
        }
    }//GEN-LAST:event_sales_btn_payMouseClicked

    // Handles click on a row in the history table
    private void table_historyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_historyMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_table_historyMouseClicked

    // Main method to run the dashboard
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JPanel footer;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel main_panel;
    private javax.swing.JPanel manage_update;
       private javax.swing.JPanel panel_history;
    private javax.swing.JPanel panel_manage;
    private javax.swing.JPanel panel_sales;
    private javax.swing.JButton sales_btnAdd;
    private javax.swing.JButton sales_btn_pay;
    private javax.swing.JComboBox<String> sales_combo;
    private javax.swing.JTextField sales_gen;
    public javax.swing.JTextField sales_gen_out;
    public javax.swing.JTextField sales_hours;
    public javax.swing.JTextField sales_id_out;
    private javax.swing.JTextField sales_regis;
    public javax.swing.JTextField sales_regis_out;
    private javax.swing.JTable table_history;
    private javax.swing.JTable table_manage;
    private javax.swing.JTable table_sales;
    // End of variables declaration//GEN-END:variables

    // Updates the total revenue label
    private void updateTotalRevenue() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM(totalPrice) FROM report");
            int total = 0;
            if (rs.next()) {
                total = rs.getInt(1);
            }
            totalRevenueLabel.setText("Total Revenue: " + total + " pesos");
        } catch (SQLException ex) {
            totalRevenueLabel.setText("Total Revenue: Error");
        }
    }
    
    // Public method to refresh revenue (used by receipt window)
    public void refreshTotalRevenue() {
        updateTotalRevenue();
    }

    // Applies alternate row coloring to a table
    private void applyAlternateRowRenderer(javax.swing.JTable table) {
        DefaultTableCellRenderer altRowRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_PANEL_LIGHTGRAY : COLOR_TABLE_ROW_ALT);
                } else {
                    c.setBackground(COLOR_TABLE_SELECTION);
                }
                c.setForeground(COLOR_TEXT_BLACK);
                // Center header text
                setHorizontalAlignment(JLabel.CENTER);
            return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(altRowRenderer);
        }
        // Center header text as well
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }

    // Starts timer to update running time for parked cars
    private void startRunningTimeTimer() {
        runningTimeTimer = new Timer(60000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update running time for each occupied slot
                for (Map.Entry<Integer, java.util.Date> entry : runningTimeMap.entrySet()) {
                    int id = entry.getKey();
                    java.util.Date timeIn = entry.getValue();
                    long diff = System.currentTimeMillis() - timeIn.getTime();
                    if (diff < 0) {
                        diff += 24 * 60 * 60 * 1000;
                    }
                    long totalMinutes = diff / (60 * 1000);
                    long hrs = totalMinutes / 60;
                    long mins = totalMinutes % 60;
                    String runningTime = String.format("%02d:%02d", hrs, mins);

                    // Update the running time in the table
                    DefaultTableModel model = (DefaultTableModel) table_sales.getModel();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        Object spotId = model.getValueAt(row, 0);
                        if (spotId instanceof Integer && ((Integer) spotId) == id) {
                            model.setValueAt(runningTime, row, 4); // Update "Running Time" column
                            break;
                        }
                    }
                }
            }
        });
        runningTimeTimer.start();
    }

    // Filters the history table based on search input
    private void filterHistoryTable() {
        String text = historySearchField.getText();
        if (historyRowSorter == null) return;
        if (text == null || text.trim().isEmpty()) {
            historyRowSorter.setRowFilter(null);
        } else {
            String expr = "(?i)" + java.util.regex.Pattern.quote(text.trim());
            historyRowSorter.setRowFilter(javax.swing.RowFilter.regexFilter(expr));
        }
    }

    // Updates the date and time label in the sidebar
    private void updateDateTimeLabel() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("'Today is' MMMM d, yyyy, h:mm:ss a");
        String now = sdf.format(new java.util.Date());
        dateTimeLabel.setText(now);
    }
}
