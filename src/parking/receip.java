package parking;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

// Payment window for handling cash and QR payments
public class receip extends javax.swing.JFrame {

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

    Connection conn = new dbConnect().dbcon();
    private String carBrand;
    private String licensePlate;
    private int totalPrice;
    private javax.swing.JFrame parentDashboard;
    private String referenceId = null;
    private String lastReferenceId = null;
    private javax.swing.JButton btn_qrpay; // QR Pay button
    private boolean qrPaid = false; // Track if QR payment was made
    private String qrPaymentInfo = null; // Store QR payment info

    // Store the last generated QR image for receipt printing
    private BufferedImage lastQrImage = null;
    private String lastPaymentMethod = "Cash"; // Track payment method

    // Constructor for payment window with details
    public receip(String referenceId, String carBrand, String licensePlate, int totalPrice, javax.swing.JFrame parentDashboard) {
        initComponents();
        btn_print.setVisible(false);
        btn_qrpay.setVisible(true); // Show QR Pay button
        centerFrame();
        this.referenceId = referenceId;
        this.carBrand = carBrand;
        this.licensePlate = licensePlate;
        this.totalPrice = totalPrice;
        this.parentDashboard = parentDashboard;
        price.setText(totalPrice + " pesos");
    }

    // Default constructor for testing
    public receip() {
        initComponents();
        btn_print.setVisible(false);
        btn_qrpay.setVisible(true);
        centerFrame();
        Statement st;
        ResultSet rs;
        try{
            st = (Statement) conn.createStatement();
            String sql = "SELECT price FROM totalprice";
            rs = st.executeQuery(sql);
            while(rs.next()){
                price.setText(rs.getInt("price") + "pesos");
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }

    // Center the payment window on screen
    private void centerFrame() {
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;    
        setLocation(dx, dy);
    }

    // UI setup for the payment window
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pay = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        receip_show = new javax.swing.JLabel();
        btn_print = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        price = new javax.swing.JLabel();
        btn_qrpay = new javax.swing.JButton(); // QR Pay button

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 400));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(115, 12, 22));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Enter Amount Paid");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        pay.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        pay.setText("  ");
        pay.setBorder(null);
        jPanel1.add(pay, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 120, 30));

        jButton1.setBackground(new java.awt.Color(152, 16, 30));
        jButton1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Pay Cash"); // Changed label
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        // Widen and center the button
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 260, 40));

        jLabel2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Change");
        // Widen and center the Change label
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 120, 30));

        receip_show.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        receip_show.setForeground(new java.awt.Color(51, 204, 0));
        // Widen and center the Change value
        receip_show.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Widened from 190 to 260, and moved left to 100 for better centering
        jPanel1.add(receip_show, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 260, 30));

        btn_print.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        btn_print.setText("Print Receipt");
        btn_print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_printMouseClicked(evt);
            }
        });
        // Widen and center the button
        jPanel1.add(btn_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 260, 40));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 400, 40));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Amount Due");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        price.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        price.setForeground(new java.awt.Color(255, 0, 51));
        jPanel1.add(price, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 120, 30));

        // --- Apply new color palette ---
        jPanel1.setBackground(COLOR_BG_WHITE);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(COLOR_BORDER_GRAY, 2));
        jLabel1.setForeground(COLOR_TEXT_BLACK);
        jLabel2.setForeground(COLOR_ACCENT_GOLD);
        jLabel3.setForeground(COLOR_TEXT_BLACK);
        price.setForeground(COLOR_ERROR_RED);
        pay.setBackground(COLOR_PANEL_LIGHTGRAY);
        pay.setForeground(COLOR_TEXT_BLACK);
        jButton1.setBackground(COLOR_PRIMARY_YELLOW);
        jButton1.setForeground(COLOR_TEXT_BLACK);
        btn_print.setBackground(COLOR_ACCENT_GOLD);
        btn_print.setForeground(COLOR_TEXT_BLACK);
        receip_show.setForeground(COLOR_SUCCESS_GREEN);

        // Add QR Pay button
        btn_qrpay.setFont(new java.awt.Font("Inter", 0, 18));
        btn_qrpay.setText("Pay via QR");
        btn_qrpay.setBackground(COLOR_SUCCESS_GREEN);
        btn_qrpay.setForeground(COLOR_BG_WHITE);
        btn_qrpay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_qrpay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_qrpayMouseClicked(evt);
            }
        });
        // Widen and center the button
        jPanel1.add(btn_qrpay, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 260, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    // Handle Pay Cash button click for payment
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        String payText = pay.getText().trim();
        double paidAmount = 0;
        boolean paidByCash = false;
        try {
            paidAmount = Double.parseDouble(payText);
            if (paidAmount < totalPrice) {
                receip_show.setText("Insufficient Amount");
                btn_print.setVisible(false);
                return;
            }
            paidByCash = true;
        } catch (NumberFormatException e) {
            receip_show.setText("Invalid Amount");
            btn_print.setVisible(false);
            return;
        }
        // Always validate amount, even for QR
        if (paidAmount < totalPrice) {
            receip_show.setText("Insufficient Amount");
            btn_print.setVisible(false);
            return;
        }
        double change = paidAmount - totalPrice;
        receip_show.setText(change > 0 ? (change + " pesos") : "0 pesos");
        btn_print.setVisible(true);

        // --- Ensure QR image is generated for Pay Cash ---
        if (lastQrImage == null) {
            // Generate QR content if not already set
            if (qrPaymentInfo == null) {
                qrPaymentInfo = "PARKNER PAY\nReference: " + (referenceId != null ? referenceId : "N/A")
                    + "\nCar: " + (carBrand != null ? carBrand : "")
                    + "\nPlate: " + (licensePlate != null ? licensePlate : "")
                    + "\nAmount: " + totalPrice + " pesos";
            }
            try {
                QRCodeWriter qrWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrWriter.encode(qrPaymentInfo, BarcodeFormat.QR_CODE, 200, 200);
                BufferedImage qrImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < 200; x++) {
                    for (int y = 0; y < 200; y++) {
                        qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                lastQrImage = qrImage;
            } catch (WriterException e) {}
        }
        // --- end ensure QR image ---

        lastPaymentMethod = "Cash";
        processPayment(change, true); // true = show confirm dialog
    }//GEN-LAST:event_jButton1MouseClicked

    // --- QR Pay Button Handler ---
    private void btn_qrpayMouseClicked(java.awt.event.MouseEvent evt) {
        String qrContent = "PARKNER PAY\nReference: " + (referenceId != null ? referenceId : "N/A")
                + "\nCar: " + (carBrand != null ? carBrand : "")
                + "\nPlate: " + (licensePlate != null ? licensePlate : "")
                + "\nAmount: " + totalPrice + " pesos";
        qrPaymentInfo = qrContent;

        BufferedImage qrImage = null;
        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
            qrImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            // Fill white background first
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    qrImage.setRGB(x, y, 0xFFFFFFFF);
                }
            }
            // Draw QR code
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    if (bitMatrix.get(x, y)) {
                        qrImage.setRGB(x, y, 0xFF000000);
                    }
                }
            }
        } catch (WriterException e) {}

        lastQrImage = qrImage; // Save for printing
        lastPaymentMethod = "Digital";

        javax.swing.JDialog qrDialog = new javax.swing.JDialog(this, "Scan QR to Pay", true);
        qrDialog.setSize(350, 500); // More height for QR
        qrDialog.setResizable(false);
        qrDialog.setLayout(null);

        javax.swing.JLabel qrLabel = new javax.swing.JLabel();
        qrLabel.setBounds(75, 30, 200, 200);
        if (qrImage != null) {
            qrLabel.setIcon(new ImageIcon(qrImage));
        } else {
            qrLabel.setText("QR Code Error");
            qrLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            qrLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        }
        qrDialog.add(qrLabel);

        javax.swing.JLabel info = new javax.swing.JLabel("<html><center>Scan this QR code with your banking app to pay.<br>Amount: " + totalPrice + " pesos</center></html>");
        info.setBounds(20, 240, 310, 40);
        info.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        qrDialog.add(info);

        javax.swing.JButton btnSimulate = new javax.swing.JButton("Simulate Payment");
        btnSimulate.setBounds(105, 300, 140, 40);
        btnSimulate.setBackground(COLOR_SUCCESS_GREEN);
        btnSimulate.setForeground(COLOR_BG_WHITE);
        btnSimulate.addActionListener(e -> {
            qrPaid = true;
            pay.setText(String.valueOf(totalPrice));
            // Validate amount before processing
            double paidAmount = 0;
            try {
                paidAmount = Double.parseDouble(pay.getText().trim());
            } catch (NumberFormatException ex) {
                receip_show.setText("Invalid Amount");
                btn_print.setVisible(false);
                qrDialog.dispose();
                return;
            }
            if (paidAmount < totalPrice) {
                receip_show.setText("Insufficient Amount");
                btn_print.setVisible(false);
                qrDialog.dispose();
                return;
            }
            receip_show.setText("QR Payment Received");
            btn_print.setVisible(true);
            qrDialog.dispose();
            lastPaymentMethod = "Digital";
            // Process payment immediately, no confirm dialog
            processPayment(0.0, false); // false = no confirm dialog
        });
        qrDialog.add(btnSimulate);

        qrDialog.setLocationRelativeTo(this);
        qrDialog.setVisible(true);
    }

    // --- Payment processing logic for both cash and QR ---
    private void processPayment(double change, boolean showConfirmDialog) {
        // Prevent double processing
        if (btn_print.isVisible() && !btn_print.isEnabled()) return;
        btn_print.setEnabled(false);

        if (showConfirmDialog) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Confirm payment?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                btn_print.setVisible(false);
                btn_print.setEnabled(true);
                return;
            }
        }
        Statement st;
        ResultSet rs;
        try {
            // Always fetch car details from parking_store before clearing
            String refId = referenceId;
            String timeIn = "";
            String dateIn = "";
            String carBrandDb = carBrand;
            String licensePlateDb = licensePlate;
            String refSql = "SELECT gen, regis, reference_id, time_in, date_in FROM parking_store WHERE reference_id " +
                    ((refId == null || refId.trim().isEmpty()) ? "IS NULL" : ("='" + refId + "'"));
            st = conn.createStatement();
            rs = st.executeQuery(refSql);
            if (rs.next()) {
                if (rs.getString("gen") != null && !rs.getString("gen").trim().isEmpty())
                    carBrandDb = rs.getString("gen");
                if (rs.getString("regis") != null && !rs.getString("regis").trim().isEmpty())
                    licensePlateDb = rs.getString("regis");
                refId = rs.getString("reference_id");
                timeIn = rs.getString("time_in");
                dateIn = rs.getString("date_in");
            }
            // Fallback: if timeIn or dateIn is empty, try to get from report or use current
            if (timeIn == null || timeIn.trim().isEmpty()) {
                ResultSet rs2 = st.executeQuery("SELECT time_in FROM report WHERE reference_id " +
                    ((refId == null || refId.trim().isEmpty()) ? "IS NULL" : ("='" + refId + "'")) +
                    " AND time_in IS NOT NULL AND time_in <> '' ORDER BY id DESC LIMIT 1");
                if (rs2.next()) {
                    timeIn = rs2.getString("time_in");
                }
                if (timeIn == null || timeIn.trim().isEmpty()) {
                    timeIn = new SimpleDateFormat("hh:mm a").format(new java.util.Date());
                }
            }
            if (dateIn == null || dateIn.trim().isEmpty()) {
                ResultSet rs2 = st.executeQuery("SELECT date_in FROM report WHERE reference_id " +
                    ((refId == null || refId.trim().isEmpty()) ? "IS NULL" : ("='" + refId + "'")) +
                    " AND date_in IS NOT NULL AND date_in <> '' ORDER BY id DESC LIMIT 1");
                if (rs2.next()) {
                    dateIn = rs2.getString("date_in");
                }
                if (dateIn == null || dateIn.trim().isEmpty()) {
                    dateIn = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                }
            }
            lastReferenceId = refId;
            String timeOut = new SimpleDateFormat("hh:mm a").format(new java.util.Date());

            // Get next id for report table
            int nextId = 1;
            rs = st.executeQuery("SELECT MAX(id) FROM report");
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }

            // Insert payment record into report
            String refIdValue = (refId == null) ? "NULL" : ("'" + refId + "'");
            String timeInValue = (timeIn == null) ? "''" : ("'" + timeIn + "'");
            String timeOutValue = (timeOut == null) ? "''" : ("'" + timeOut + "'");
            String dateInValue = (dateIn == null) ? "''" : ("'" + dateIn + "'");
            String qrInfoValue = (qrPaymentInfo == null) ? "NULL" : ("'" + qrPaymentInfo.replace("'", "''") + "'");
            String paymentMethodValue = (lastPaymentMethod == null) ? "'Cash'" : ("'" + lastPaymentMethod + "'");
            String sql = "INSERT INTO report (id, gen, regis, totalPrice, `change`, reference_id, time_in, time_out, date_in, qr_info, payment_method) VALUES (" +
                    nextId + ", '" + carBrandDb + "', '" + licensePlateDb + "', " + totalPrice + ", " + change + ", " +
                    refIdValue + ", " +
                    timeInValue + ", " +
                    timeOutValue + ", " +
                    dateInValue + ", " +
                    qrInfoValue + ", " +
                    paymentMethodValue + ")";
            st.executeUpdate(sql);

            // Mark parking slot as available again (clear car details)
            if (refId != null && !refId.trim().isEmpty()) {
                sql = "UPDATE parking_store SET gen='', regis='', time_in='', date_in='', reference_id=NULL WHERE reference_id='" + refId + "'";
            } else {
                sql = "UPDATE parking_store SET gen='', regis='', time_in='', date_in='', reference_id=NULL WHERE reference_id IS NULL";
            }
            st.executeUpdate(sql);

            // --- Update occupied count in parking_slot table ---
            Statement st2 = conn.createStatement();
            ResultSet rsSlot = st2.executeQuery("SELECT occupied FROM parking_slot WHERE id=1");
            if (rsSlot.next()) {
                int occ = rsSlot.getInt(1);
                if (occ > 0) {
                    occ = occ - 1;
                    if (occ < 0) occ = 0;
                    st2.executeUpdate("UPDATE parking_slot SET occupied=" + occ + " WHERE id=1");
                }
            }
            // --- end update occupied ---

            // Refresh dashboard after payment
            if (parentDashboard instanceof Main_dashboard) {
                Main_dashboard dash = (Main_dashboard) parentDashboard;
                dash.sales_id_out.setText("");
                dash.sales_gen_out.setText("");
                dash.sales_regis_out.setText("");
                dash.sales_hours.setText("");
                dash.initTableManage(); // refresh manage panel
                dash.initTableSales();
                dash.refreshTotalRevenue();
                dash.refreshAvailableSlots(); // update available slots
            }
        } catch (SQLException ex) {
            System.out.print(ex);
        }
        btn_print.setEnabled(true);
    }

    // Handle Print Receipt button click
    // This function prints the receipt after payment
    private void btn_printMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseClicked
        // Always pass the lastQrImage and payment method to the bill window
        bill billWindow = new bill(lastReferenceId, qrPaymentInfo, lastQrImage, lastPaymentMethod); // Pass payment method
        billWindow.pack();
        billWindow.setLocationRelativeTo(null);

        // Print the bill panel directly
        java.awt.print.PrinterJob pj = java.awt.print.PrinterJob.getPrinterJob();
        pj.setJobName("Parking Receipt");
        pj.setPrintable(new java.awt.print.Printable() {
            public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) return java.awt.print.Printable.NO_SUCH_PAGE;
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                billWindow.getContentPane().printAll(g2d);
                return java.awt.print.Printable.PAGE_EXISTS;
            }
        });
        if (pj.printDialog()) {
            try {
                pj.print();
            } catch (java.awt.print.PrinterException exc) {
                javax.swing.JOptionPane.showMessageDialog(
                    receip.this,
                    "Unable to print or save receipt.\n" + exc.getMessage(),
                    "Print Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        }

        // Optionally close the payment window after printing
        dispose();
    }//GEN-LAST:event_btn_printMouseClicked

    // Main method for testing
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
            java.util.logging.Logger.getLogger(receip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(receip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(receip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(receip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new receip().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btn_print;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField pay;
    private javax.swing.JLabel price;
    private javax.swing.JLabel receip_show;
}
