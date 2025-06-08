/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author godfr
 */
public class Main_dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Main_dashboard
     */
    int manage_selected = -1;
    int manage_num_row = 1;
    
    Connection conn = new dbConnect().dbcon();
    
    public Main_dashboard() {
        initComponents();
        initTableSales();
        initTableHistory();
        centerFrame();

        panel_sales.setVisible(true);
        panel_manage.setVisible(false);
        panel_history.setVisible(false);

        main_panel.requestFocusInWindow();

        initTableManage();

        // Disable pay button and hours input initially
        setReturnCarFieldsEnabled(false);

        // Add focus listener to sales_hours to prevent input if no car is selected
        sales_hours.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                // Do nothing here, remove dialog from focus event
                if (sales_id_out.getText().trim().isEmpty()) {
                    sales_hours.setText("");
                    sales_hours.setEnabled(false);
                }
            }
        });
    }
    
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
        theader.setBackground(new Color(115,12,22));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        theader.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED, new Color(115,12,22), new Color(115,12,22)));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_manage.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_manage.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        table_manage.setRowHeight(30);
        table_manage.setFont(new Font("Sarabun", Font.PLAIN, 16));
    }
    
    public void initTableSales(){
        /*int delay = 1000; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
                test_time.setText(date);
            }
        };
        new Timer(delay, taskPerformer).start(); */
        
        DefaultTableModel model = (DefaultTableModel) table_sales.getModel();
        sales_combo.removeAllItems();
        model.setRowCount(0);
        manage_num_row = 1;
        
        Statement st;
        ResultSet rs;
        
        String pList = "";
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM parking_store";
            rs = st.executeQuery(sql);
            int i = 0;
            while(rs.next()){
                // Only add license plate, car brand, and time_in
                model.addRow(new Object[]{
                    rs.getString("gen"),
                    rs.getString("regis"),
                    rs.getString("time_in")
                });
                manage_num_row++;
                if(rs.getBoolean("vailable") == true) sales_combo.addItem(rs.getInt("id") + "");
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }
        
        //Header column
        JTableHeader theader = table_sales.getTableHeader();
        theader.setBackground(new Color(115,12,22));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        theader.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED, new Color(115,12,22), new Color(115,12,22)));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_sales.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_sales.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table_sales.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table_sales.setRowHeight(30);
        table_sales.setFont(new Font("Sarabun", Font.PLAIN, 16));
    }
    
    private void initTableHistory(){
        DefaultTableModel model = (DefaultTableModel) table_history.getModel();
        model.setRowCount(0);
        
        Statement st;
        ResultSet rs;
        
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT * FROM report";
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("gen"), rs.getString("regis"), rs.getInt("totalPrice")});
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }
        
        //Header column
        JTableHeader theader = table_history.getTableHeader();
        theader.setBackground(new Color(115,12,22));
        theader.setForeground(Color.white);
        theader.setFont(new Font("Sarabun", Font.PLAIN, 18));
        theader.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED, new Color(115,12,22), new Color(115,12,22)));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table_history.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table_history.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table_history.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table_history.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        table_history.setRowHeight(30);
        table_history.setFont(new Font("Sarabun", Font.PLAIN, 16));
    }
    
    public void centerFrame() {

            Dimension windowSize = getSize();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Point centerPoint = ge.getCenterPoint();

            int dx = centerPoint.x - windowSize.width / 2;
            int dy = centerPoint.y - windowSize.height / 2;    
            setLocation(dx, dy);
            
            main_panel.requestFocusInWindow();
    }

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

        jPanel2.setBackground(new java.awt.Color(115, 12, 22));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PARKING");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 130, -1));

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Management System");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 150, -1));

        jButton1.setBackground(new java.awt.Color(152, 16, 30));
        jButton1.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_payment_history_100px.png"))); // NOI18N
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
        jButton2.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_home_64px.png"))); // NOI18N
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
        jButton3.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_google_tag_manager_100px.png"))); // NOI18N
        jButton3.setText("Parking Spot");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusPainted(false);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 190, 50));

        jLabel13.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Parking fee 50 pesos/hour");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        main_panel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 650));

        footer.setBackground(new java.awt.Color(115, 12, 22));

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        main_panel.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 900, 20));

        panel_sales.setBackground(new java.awt.Color(254, 240, 241));
        panel_sales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBorder(null);

        table_sales.setBackground(new java.awt.Color(254, 240, 241));
        table_sales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "License Plate", "Car Brand", "Time In"
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

        panel_sales.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 630));

        sales_gen.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        sales_gen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_gen, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 100, -1));

        sales_regis.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        sales_regis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_regis, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 140, 100, -1));

        jLabel4.setFont(new java.awt.Font("Sarabun", 0, 18)); // NOI18N
        jLabel4.setText("Add Car to Park");
        panel_sales.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, -1, -1));

        jLabel5.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel5.setText("Car Brand");
        panel_sales.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, -1, -1));

        jLabel6.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel6.setText("Parking Spot");
        panel_sales.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 190, -1, -1));

        jLabel8.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel8.setText("License Plate");
        panel_sales.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, -1, -1));

        sales_combo.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        panel_sales.add(sales_combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 190, 100, -1));

        sales_btnAdd.setBackground(new java.awt.Color(115, 12, 22));
        sales_btnAdd.setFont(new java.awt.Font("Sarabun", 0, 12)); // NOI18N
        sales_btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        sales_btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_add_48px_1.png"))); // NOI18N
        sales_btnAdd.setText(" Add Parked Car");
        sales_btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sales_btnAdd.setFocusPainted(false);
        sales_btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sales_btnAddMouseClicked(evt);
            }
        });
        panel_sales.add(sales_btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 250, 180, 50));

        jPanel1.setBackground(new java.awt.Color(115, 12, 22));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        panel_sales.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 330, 250, 5));

        jLabel9.setFont(new java.awt.Font("Sarabun", 0, 18)); // NOI18N
        jLabel9.setText("Return Car");
        panel_sales.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 350, -1, -1));

        jLabel10.setFont(new java.awt.Font("Sarabun", 1, 12)); // NOI18N
        jLabel10.setText("Enter Number of Hours");
        panel_sales.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 530, -1, -1));

        sales_hours.setFont(new java.awt.Font("Sarabun", 1, 14)); // NOI18N
        sales_hours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_hours, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 520, 60, 30));

        sales_btn_pay.setBackground(new java.awt.Color(0, 204, 0));
        sales_btn_pay.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        sales_btn_pay.setForeground(new java.awt.Color(255, 255, 255));
        sales_btn_pay.setText("Pay");
        sales_btn_pay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sales_btn_pay.setFocusPainted(false);
        sales_btn_pay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sales_btn_payMouseClicked(evt);
            }
        });
        panel_sales.add(sales_btn_pay, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 570, 100, 40));

        jLabel11.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel11.setText("Parking Spot");
        panel_sales.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, -1, -1));

        jLabel12.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel12.setText("License Plate");
        panel_sales.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 480, -1, -1));

        sales_regis_out.setEditable(false);
        sales_regis_out.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        sales_regis_out.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_regis_out, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 480, 100, -1));

        sales_id_out.setEditable(false);
        sales_id_out.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        sales_id_out.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_id_out, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, 100, -1));

        jLabel14.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel14.setText("Car Brand");
        panel_sales.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 440, -1, -1));

        sales_gen_out.setEditable(false);
        sales_gen_out.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        sales_gen_out.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panel_sales.add(sales_gen_out, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 440, 100, -1));

        main_panel.add(panel_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 690, 630));

        panel_history.setBackground(new java.awt.Color(254, 240, 241));
        panel_history.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBorder(null);

        table_history.setBackground(new java.awt.Color(254, 240, 241));
        table_history.setFont(new java.awt.Font("Sarabun", 0, 11)); // NOI18N
        table_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parking Spot", "Car Brand", "License Plate", "Amount Paid"
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

        panel_history.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 630));

        main_panel.add(panel_history, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 690, 630));

        panel_manage.setBackground(new java.awt.Color(254, 240, 241));
        panel_manage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        table_manage.setBackground(new java.awt.Color(254, 240, 241));
        table_manage.setFont(new java.awt.Font("Sarabun", 0, 18)); // NOI18N
        table_manage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parking Spot", "Status"
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

        panel_manage.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 630));

        manage_update.setBackground(new java.awt.Color(254, 240, 241));
        manage_update.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panel_manage.add(manage_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, 210, 220));

        btn_update.setBackground(new java.awt.Color(115, 12, 22));
        btn_update.setFont(new java.awt.Font("Sarabun", 0, 12)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_update_left_rotation_64px.png"))); // NOI18N
        btn_update.setText("Edit Parking Status");
        btn_update.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_update.setFocusPainted(false);
        btn_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_updateMouseClicked(evt);
            }
        });
        panel_manage.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 190, 50));

        jLabel3.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(115, 12, 22));
        jLabel3.setText("Status");
        panel_manage.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, -1, -1));

        combo.setBackground(new java.awt.Color(115, 12, 22));
        combo.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        combo.setForeground(new java.awt.Color(255, 255, 255));
        combo.setBorder(null);
        combo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        combo.setFocusable(false);
        combo.setLightWeightPopupEnabled(false);
        combo.setOpaque(false);
        panel_manage.add(combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 120, 40));

        btn_delete.setBackground(new java.awt.Color(115, 12, 22));
        btn_delete.setFont(new java.awt.Font("Sarabun", 0, 12)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_delete_64px.png"))); // NOI18N
        btn_delete.setText("Delete Parking Spot");
        btn_delete.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete.setFocusPainted(false);
        btn_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteMouseClicked(evt);
            }
        });
        panel_manage.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 300, 190, 50));

        btn_add.setBackground(new java.awt.Color(115, 12, 22));
        btn_add.setFont(new java.awt.Font("Sarabun", 0, 14)); // NOI18N
        btn_add.setForeground(new java.awt.Color(255, 255, 255));
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icons8_add_48px_1.png"))); // NOI18N
        btn_add.setText("Add Parking Spot");
        btn_add.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_add.setFocusPainted(false);
        btn_add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addMouseClicked(evt);
            }
        });
        panel_manage.add(btn_add, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 190, 50));

        main_panel.add(panel_manage, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 690, 630));

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

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        panel_sales.setVisible(false);
        panel_manage.setVisible(true);
        panel_history.setVisible(false);
        
        manage_update.setVisible(true);
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        panel_sales.setVisible(true);
        panel_manage.setVisible(false);
        panel_history.setVisible(false);
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        panel_sales.setVisible(false);
        panel_manage.setVisible(false);
        panel_history.setVisible(true);
    }//GEN-LAST:event_jButton1MouseClicked

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
            JOptionPane.showMessageDialog(this, "Parking spot " + manage_selected + " status updated to '" + combo.getSelectedItem() + "'.", "Edit Parking Spot", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }//GEN-LAST:event_btn_updateMouseClicked

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
                JOptionPane.showMessageDialog(this, "Parking spot " + manage_selected + " has been deleted.", "Delete Parking Spot", JOptionPane.INFORMATION_MESSAGE);
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
                            "A car is parked here. Please accomplish the payment first before editing or deleting the parking spot.", 
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

    private void btn_addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addMouseClicked
        PreparedStatement pt = null;

        try{
            String sql = "INSERT INTO parking_store (id, vailable, gen, regis) VALUES ("+ (manage_num_row) +", true, '', '')";
            pt = conn.prepareStatement(sql);
            pt.execute();
            initTableManage();
            initTableSales();
            // Show dialog for add action
            JOptionPane.showMessageDialog(this, "Parking spot " + (manage_num_row - 1) + " has been added.", "Add Parking Spot", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }//GEN-LAST:event_btn_addMouseClicked

    private void sales_btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sales_btnAddMouseClicked
    // Add validation for empty car brand or license plate
    String carBrand = sales_gen.getText();
    String licensePlate = sales_regis.getText();
    if(carBrand.trim().isEmpty() || licensePlate.trim().isEmpty()){
        JOptionPane.showMessageDialog(this, "Car Brand and License Plate cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if(!carBrand.equals("") && !licensePlate.equals("")){
        int selectCombo = Integer.parseInt(sales_combo.getSelectedItem().toString());
        // Generate unique 8-digit reference number
        String refNumber = "";
        Random rand = new Random();
        boolean unique = false;
        while (!unique) {
            int num = 10000000 + rand.nextInt(90000000); // 8-digit
            refNumber = String.valueOf(num);
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM parking_store WHERE reference_number='" + refNumber + "'");
                if (rs.next() && rs.getInt(1) == 0) {
                    unique = true;
                }
            } catch (SQLException ex) {
                System.out.print(ex);
                // fallback: break to avoid infinite loop if error
                break;
            }
        }

        // Get current time for Time In
        String timeIn = new SimpleDateFormat("hh:mm a").format(new java.util.Date());

        // Add Time In to confirmation dialog
        String confirmMsg = "Please confirm the following details:\n"
                + "Car Brand: " + carBrand + "\n"
                + "License Plate: " + licensePlate + "\n"
                + "Parking Spot: " + selectCombo + "\n"
                + "Time In: " + timeIn + "\n\n"
                + "Is this information correct?";
        int confirm = JOptionPane.showConfirmDialog(this, confirmMsg, "Confirm Car Details", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            PreparedStatement pt = null;
            try{
                // Add time_in to the update statement
                String sql = "UPDATE parking_store SET gen = '" + carBrand + "', regis = '" + licensePlate + "', vailable=false, reference_id='" + refNumber + "', time_in='" + timeIn + "' WHERE id=" + selectCombo;
                pt = conn.prepareStatement(sql);
                pt.execute();
                initTableManage();
                initTableSales();
                // Show reference number and time in the success message
                JOptionPane.showMessageDialog(this, "Car parked successfully at spot " + selectCombo + ".\nReference Number: " + refNumber + "\nTime In: " + timeIn, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch(SQLException ex){
                System.out.print(ex);
            }
        }
    }
}//GEN-LAST:event_sales_btnAddMouseClicked

    int id;
    String gen, regis;
    private void table_salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_salesMouseClicked
        if(((String) table_sales.getValueAt(table_sales.getSelectedRow(), 1)).equals("Unavailable")){
            id = (int) table_sales.getValueAt(table_sales.getSelectedRow(), 0);
            gen = (String) table_sales.getValueAt(table_sales.getSelectedRow(), 2);
            regis = (String) table_sales.getValueAt(table_sales.getSelectedRow(), 3);
            // Optionally, you can fetch/display time-in if needed:
            // String timeIn = (String) table_sales.getValueAt(table_sales.getSelectedRow(), 4);

            sales_id_out.setText(id + "");
            sales_gen_out.setText(gen);
            sales_regis_out.setText(regis);

            // Enable return car fields
            setReturnCarFieldsEnabled(true);
        }else{
            sales_id_out.setText("");
            sales_gen_out.setText("");
            sales_regis_out.setText("");

            // Disable return car fields if not occupied
            setReturnCarFieldsEnabled(false);
            // Show new dialog when user clicks on an available spot
            JOptionPane.showMessageDialog(this, "Please add a car first. To return", "No Car Selected", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_table_salesMouseClicked

    // Utility to enable/disable return car fields
    private void setReturnCarFieldsEnabled(boolean enabled) {
        sales_id_out.setEnabled(false); // always disabled (output only)
        sales_gen_out.setEnabled(false); // always disabled (output only)
        sales_regis_out.setEnabled(false); // always disabled (output only)
        sales_hours.setEnabled(enabled);
        sales_btn_pay.setEnabled(enabled); // <-- ensure pay button is also disabled
        if (!enabled) {
            sales_hours.setText("");
        }
    }

    private void sales_btn_payMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sales_btn_payMouseClicked
        // Prevent pay if no car is selected
        if (sales_id_out.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an occupied car first.", "No Car Selected", JOptionPane.WARNING_MESSAGE);
            sales_hours.setText("");
            sales_hours.setEnabled(false);
            return;
        }

        // Validate input for hours
        String hoursText = sales_hours.getText().trim();
        int hours = 0;
        try {
            hours = Integer.parseInt(hoursText);
            if (hours <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of hours (greater than 0).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of hours (numbers only).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Only open the receipt window, do not execute SQL here
        int totalPrice = hours * 50;
        receip receiptWindow = new receip(id, gen, regis, totalPrice, this);
        receiptWindow.setVisible(true);
    }//GEN-LAST:event_sales_btn_payMouseClicked

    private void table_historyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_historyMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_table_historyMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
}
