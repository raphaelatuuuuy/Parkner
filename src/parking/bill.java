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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author godfr
 */
public class bill extends javax.swing.JFrame {

    Connection conn = new dbConnect().dbcon();

    // Add a constructor that accepts referenceId
    public bill(String referenceId) {
        initComponents();

        Statement st;
        ResultSet rs;

        try {
            // Get the latest report for this referenceId
            st = conn.createStatement();
            String sql = "SELECT id, gen, regis, totalPrice, `change`, reference_id FROM report WHERE reference_id " +
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

                name.setText("Parking Spot: " + spotId);
                carBrandLabel.setText("Car Brand: " + carBrand);
                licenseLabel.setText("License Plate: " + licensePlate);

                // Remove any previous dynamic reference number label
                for (java.awt.Component comp : jPanel1.getComponents()) {
                    if (comp instanceof javax.swing.JLabel) {
                        String txt = ((javax.swing.JLabel) comp).getText();
                        if (txt != null && txt.startsWith("Reference Number:")) {
                            jPanel1.remove(comp);
                            break;
                        }
                    }
                }
                // Reference Number
                javax.swing.JLabel refLabel = new javax.swing.JLabel();
                refLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 13));
                refLabel.setText("Reference Number: " + (refNum == null ? "N/A" : refNum));
                jPanel1.add(refLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 320, 20));

                // Move divider further down (after ref num)
                jPanel2.setBounds(30, 175, 340, 1);

                // Set all values, align right
                price.setText(String.format("P %.2f", priceValue));
                price.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                vat.setText(String.format("P %.2f", vatValue));
                vat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                total.setText(String.format("P %.2f", totalValue));
                total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

                // Amount Paid (reuse changeLabel/changeValueLabel)
                changeLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeLabel.setText("Paid:");
                changeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                changeLabel.setBounds(40, 255, 120, 20);

                changeValueLabel.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                changeValueLabel.setText(String.format("P %.2f", priceValue + changeValue));
                changeValueLabel.setBounds(250, 255, 120, 20);

                // Add a static label for "Change" below Amount Paid
                javax.swing.JLabel changeLabel2 = new javax.swing.JLabel();
                changeLabel2.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeLabel2.setText("Change:");
                changeLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jPanel1.add(changeLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 120, 20));

                javax.swing.JLabel changeValueLabel2 = new javax.swing.JLabel();
                changeValueLabel2.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12));
                changeValueLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                changeValueLabel2.setText(String.format("P %.2f", changeValue));
                jPanel1.add(changeValueLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 120, 20));

                // Date
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy - h:mm a");
                LocalDateTime now = LocalDateTime.now();
                date.setText("Date: " + dtf.format(now));
            } else {
                // Show error if nothing found
                name.setText("No bill found.");
                carBrandLabel.setText("");
                licenseLabel.setText("");
                price.setText("");
                vat.setText("");
                total.setText("");
                changeValueLabel.setText("");
                date.setText("");
            }
        } catch(SQLException ex){
            System.out.print(ex);
        }
    }

    // Default constructor for compatibility
    public bill() {
        this(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // Use a classic, readable font for receipts
        java.awt.Font billHeaderFont = new java.awt.Font("Courier New", java.awt.Font.BOLD, 22);
        java.awt.Font billSubHeaderFont = new java.awt.Font("Courier New", java.awt.Font.PLAIN, 13);
        java.awt.Font billLabelFont = new java.awt.Font("Courier New", java.awt.Font.PLAIN, 12);
        java.awt.Font billBoldFont = new java.awt.Font("Courier New", java.awt.Font.BOLD, 13);

        // Header
        jLabel1.setFont(billHeaderFont);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PARKING RECEIPT");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 400, 30));

        jLabel2.setFont(billLabelFont);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Thank you for using our parking service!");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 400, 20));

        // Divider (move lower, after car details and reference number)
        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 340, 2));

        // Parking Spot
        name.setFont(billBoldFont);
        name.setText("Parking Spot:");
        jPanel1.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 320, 20));

        // Car Brand
        carBrandLabel.setFont(billLabelFont);
        carBrandLabel.setText("Car Brand:");
        jPanel1.add(carBrandLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 115, 320, 20));

        // License Plate
        licenseLabel.setFont(billLabelFont);
        licenseLabel.setText("License Plate:");
        jPanel1.add(licenseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 135, 320, 20));

        // VAT
        jLabel3.setFont(billLabelFont);
        jLabel3.setText("VAT 7%:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 80, 20));

        vat.setFont(billLabelFont);
        vat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        vat.setText("P 0.00");
        jPanel1.add(vat, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 120, 20));

        // Total
        jLabel4.setFont(billBoldFont);
        jLabel4.setText("Total:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 215, 80, 20));

        total.setFont(billBoldFont);
        total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total.setText("P 0.00");
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 215, 120, 20));

        // Change
        changeLabel.setFont(billLabelFont);
        changeLabel.setText("Change:");
        jPanel1.add(changeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 80, 20));

        changeValueLabel.setFont(billLabelFont);
        changeValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        changeValueLabel.setText("P 0.00");
        jPanel1.add(changeValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 120, 20));

        // Date
        date.setFont(billSubHeaderFont);
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        date.setText("Date:");
        jPanel1.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 400, 20));

        // Receipt border
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180), 2));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new bill().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
}
