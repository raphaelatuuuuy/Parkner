/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parking;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Bill window for printing parking receipt
public class bill extends javax.swing.JFrame {

    // --- Color Palette for UI ---
    private static final java.awt.Color COLOR_PRIMARY_YELLOW = new java.awt.Color(230, 180, 0);
    private static final java.awt.Color COLOR_ACCENT_GOLD = new java.awt.Color(212, 163, 0);
    private static final java.awt.Color COLOR_BG_WHITE = new java.awt.Color(250, 250, 250);
    private static final java.awt.Color COLOR_TEXT_BLACK = new java.awt.Color(20, 20, 20);
    private static final java.awt.Color COLOR_TEXT_DARKGRAY = new java.awt.Color(51, 51, 51);
    private static final java.awt.Color COLOR_PANEL_LIGHTGRAY = new java.awt.Color(242, 242, 242);
    private static final java.awt.Color COLOR_SUCCESS_GREEN = new java.awt.Color(67, 160, 71);
    private static final java.awt.Color COLOR_WARNING_ORANGE = new java.awt.Color(255, 145, 0);
    private static final java.awt.Color COLOR_ERROR_RED = new java.awt.Color(211, 47, 47);
    private static final java.awt.Color COLOR_BORDER_GRAY = new java.awt.Color(189, 189, 189);

    // Database connection
    Connection conn = new dbConnect().dbcon();

    // Labels for dynamic receipt info
    private javax.swing.JLabel refLabel;
    private javax.swing.JLabel timeInLabel;
    private javax.swing.JLabel timeInValue;
    private javax.swing.JLabel timeOutLabel;
    private javax.swing.JLabel timeOutValue;
    private javax.swing.JLabel totalTimeLabel;
    private javax.swing.JLabel totalTimeValue;

    // Show receipt for given referenceId
    public bill(String referenceId) {
        initComponents();

        // Get transaction info from database
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            String sql = "SELECT id, gen, regis, totalPrice, `change`, reference_id, time_in, time_out FROM report WHERE reference_id " +
                         (referenceId == null ? "IS NULL" : ("='" + referenceId + "'")) +
                         " ORDER BY id DESC LIMIT 1";
            rs = st.executeQuery(sql);

            if (rs.next()) {
                int spotId = rs.getInt("id");
                String carBrand = rs.getString("gen");
                String licensePlate = rs.getString("regis");
                double priceValue = rs.getDouble("totalPrice");
                double vatValue = priceValue * 7 / 100;
                double totalValue = priceValue;
                double changeValue = rs.getDouble("change");
                String refNum = rs.getString("reference_id");
                String timeIn = rs.getString("time_in");
                String timeOut = rs.getString("time_out");

                // Set receipt info
                name.setText("");
                carBrandLabel.setText("Car Brand: " + carBrand);
                licenseLabel.setText("License Plate: " + licensePlate);
                refLabel.setText("Reference Number: " + (refNum == null ? "N/A" : refNum));
                timeInLabel.setText("Time In:");
                timeInValue.setText(timeIn == null ? "N/A" : timeIn);
                timeOutLabel.setText("Time Out:");
                timeOutValue.setText(timeOut == null ? "N/A" : timeOut);

                // Calculate total time parked
                String totalTimeStr = "N/A";
                if (timeIn != null && timeOut != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        java.util.Date tIn = sdf.parse(timeIn);
                        java.util.Date tOut = sdf.parse(timeOut);
                        long diffMs = tOut.getTime() - tIn.getTime();
                        if (diffMs < 0) diffMs += 24 * 60 * 60 * 1000;
                        long totalSeconds = diffMs / 1000;
                        long hrs = totalSeconds / 3600;
                        long mins = (totalSeconds % 3600) / 60;
                        totalTimeStr = String.format("%dhr %dmin", hrs, mins);
                    } catch (ParseException e) {
                        totalTimeStr = "N/A";
                    }
                }
                totalTimeLabel.setText("Total Time:");
                totalTimeValue.setText(totalTimeStr);

                // Set price, vat, total, paid, change
                price.setText(String.format("P %8.2f", priceValue));
                price.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                vat.setText(String.format("P %8.2f", vatValue));
                vat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                total.setText(String.format("P %8.2f", totalValue));
                total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                changeLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeLabel.setText("Paid:");
                changeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                changeLabel.setBounds(40, 260 + 20, 120, 20);
                changeValueLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                changeValueLabel.setText(String.format("P %8.2f", priceValue + changeValue));
                changeValueLabel.setBounds(250, 260 + 20, 120, 20);

                // Show change below paid
                javax.swing.JLabel changeLabel2 = new javax.swing.JLabel();
                changeLabel2.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeLabel2.setText("Change:");
                changeLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                changeLabel2.setName("dyn_changelabel2");
                jPanel1.add(changeLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260 + 45, 120, 20));
                javax.swing.JLabel changeValueLabel2 = new javax.swing.JLabel();
                changeValueLabel2.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeValueLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                changeValueLabel2.setText(String.format("P %8.2f", changeValue));
                changeValueLabel2.setName("dyn_changevalue2");
                jPanel1.add(changeValueLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260 + 45, 120, 20));

                // Show date
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy - h:mm a");
                LocalDateTime now = LocalDateTime.now();
                date.setText("Date: " + dtf.format(now));
            } else {
                // If no record found, show empty
                name.setText("");
                carBrandLabel.setText("");
                licenseLabel.setText("");
                price.setText("");
                vat.setText("");
                total.setText("");
                changeValueLabel.setText("");
                date.setText("");
                refLabel.setText("Reference Number: N/A");
                timeInLabel.setText("Time In:");
                timeInValue.setText("N/A");
                timeOutLabel.setText("Time Out:");
                timeOutValue.setText("N/A");
                totalTimeLabel.setText("Total Time:");
                totalTimeValue.setText("N/A");
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }

    // Default constructor
    public bill() {
        this(null);
    }

    // Build the receipt UI
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        carBrandLabel = new javax.swing.JLabel();
        licenseLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        price = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        vat = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        changeLabel = new javax.swing.JLabel();
        changeValueLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        date = new javax.swing.JLabel();

        // Dynamic labels for receipt info
        refLabel = new javax.swing.JLabel();
        refLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 13));
        refLabel.setText("Reference Number:");
        timeInLabel = new javax.swing.JLabel();
        timeInLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
        timeInLabel.setText("Time In:");
        timeInValue = new javax.swing.JLabel();
        timeInValue.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
        timeInValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        timeOutLabel = new javax.swing.JLabel();
        timeOutLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
        timeOutLabel.setText("Time Out:");
        timeOutValue = new javax.swing.JLabel();
        timeOutValue.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
        timeOutValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTimeLabel = new javax.swing.JLabel();
        totalTimeLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
        totalTimeLabel.setText("Total Time:");
        totalTimeValue = new javax.swing.JLabel();
        totalTimeValue.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
        totalTimeValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // --- UI Layout and Styling ---
        jPanel1.setBackground(COLOR_BG_WHITE);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        java.awt.Font billHeaderFont = new java.awt.Font("Courier New", java.awt.Font.BOLD, 22);
        java.awt.Font billSubHeaderFont = new java.awt.Font("Courier New", java.awt.Font.PLAIN, 13);
        java.awt.Font billLabelFont = new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12);
        java.awt.Font billBoldFont = new java.awt.Font("Courier New", java.awt.Font.BOLD, 13);

        jLabel1.setFont(billHeaderFont);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PARKING RECEIPT");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 400, 30));

        jLabel2.setFont(billLabelFont);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Thank you for using our parking service!");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 400, 20));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 340, 2));

        carBrandLabel.setFont(billLabelFont);
        carBrandLabel.setText("Car Brand:");
        jPanel1.add(carBrandLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 115, 320, 20));

        licenseLabel.setFont(billLabelFont);
        licenseLabel.setText("License Plate:");
        jPanel1.add(licenseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 135, 320, 20));

        jPanel1.add(refLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 320, 20));
        jPanel1.add(timeInLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 100, 20));
        jPanel1.add(timeInValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 120, 20));
        jPanel1.add(timeOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 100, 20));
        jPanel1.add(timeOutValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 210, 120, 20));
        jPanel1.add(totalTimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 100, 20));
        jPanel1.add(totalTimeValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, 120, 20));

        jLabel3.setFont(billLabelFont);
        jLabel3.setText("VAT 7%:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 100, 20));
        vat.setFont(billLabelFont);
        vat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        vat.setText("P 0.00");
        jPanel1.add(vat, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 270, 120, 20));

        jLabel4.setFont(billBoldFont);
        jLabel4.setText("Total:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 285, 80, 20));
        total.setFont(billBoldFont);
        total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total.setText("P 0.00");
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 285, 120, 20));

        changeLabel.setFont(billLabelFont);
        changeLabel.setText("Change:");
        jPanel1.add(changeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 80, 20));
        changeValueLabel.setFont(billLabelFont);
        changeValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        changeValueLabel.setText("P 0.00");
        jPanel1.add(changeValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 120, 20));

        date.setFont(billSubHeaderFont);
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        date.setText("Date:");
        jPanel1.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 400, 20));

        // Set window size and center
        int receiptWidth = 400;
        int receiptHeight = 420;
        setPreferredSize(new java.awt.Dimension(receiptWidth, receiptHeight));
        setMinimumSize(new java.awt.Dimension(receiptWidth, receiptHeight));
        setMaximumSize(new java.awt.Dimension(receiptWidth, receiptHeight));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, receiptWidth, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, receiptHeight, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();

        // Center the window
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
            (screenSize.width - receiptWidth) / 2,
            (screenSize.height - receiptHeight) / 2
        );
    }

    // Main method to run the bill window
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new bill().setVisible(true);
            }
        });
    }

    // --- UI Variables ---
    private javax.swing.JLabel carBrandLabel;
    private javax.swing.JLabel changeLabel;
    private javax.swing.JLabel changeValueLabel;
    private javax.swing.JLabel date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel licenseLabel;
    private javax.swing.JLabel name;
    private javax.swing.JLabel price;
    private javax.swing.JLabel total;
    private javax.swing.JLabel vat;
}
