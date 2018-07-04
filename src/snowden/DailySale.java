/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snowden;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kasun
 */
public class DailySale extends javax.swing.JFrame {

    static Date yourDate;
    static Object ob;

    /**
     * Creates new form DailySale
     */
    public DailySale() {
        initComponents();
        loadtime();
        load_table();
        calculateTootalAmount();
        calculatecashAmount();
        calculateBalanceAmount();
        loadtb2();
        loadmonth();
        loadmonth2table();
    }

    public void loadmonth2table() {

        try {
            Calendar ca1 = Calendar.getInstance();
            int iMonth = (ca1.get(Calendar.MONTH) + 1);
            Connection com = DB.conect();

            DefaultTableModel dtm1 = (DefaultTableModel) jTable3.getModel();
            int i = dtm1.getRowCount();
            while (i > 0) {
                dtm1.removeRow(i - 1);
                i--;

            }
            ResultSet r = com.createStatement().executeQuery("select * from order where EXTRACT(MONTH FROM Date)='" + iMonth + "'");

            while (r.next()) {

                String a1 = r.getString("order_id");

                ResultSet r1 = com.createStatement().executeQuery("select * from order where order_id='" + a1 + "'");
                while (r1.next()) {
                    Vector v = new Vector();
                    String a2 = r1.getString("cusid");
                    String a3 = r1.getString("invototal");
                    String a4 = r1.getString("cash");
                    String a5 = r1.getString("cheque");
                    String a6 = r1.getString("balance");

                    v.add(a1);
                    v.add(a2);
                    v.add(a3);
                    v.add(a4);
                    v.add(a5);
                    v.add(a6);
                    dtm1.addRow(v);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadmonth() {
        try {
            Calendar ca1 = Calendar.getInstance();
            int iMonth = (ca1.get(Calendar.MONTH) + 1);
            Connection com = DB.conect();

            double inv = 0.0;
            double inv1 = 0.0;

            ResultSet r = com.createStatement().executeQuery("select * from invoicesummery where EXTRACT(MONTH FROM invodate)='" + iMonth + "'");
            Vector v = new Vector();
            double caltot = 0.0;
            double totm = 0.0;
            double credit = 0.0;
            double creditm = 0.0;
            double totrec = 0.0;
            double investm = 0.0;
            double profitm = 0.0;
            while (r.next()) {

                String a1 = r.getString("invoiceno");
                String a2 = r.getString("cusid");
                String a3 = r.getString("invototal");
                String a4 = r.getString("cash");
                String a5 = r.getString("cheque");
                String a6 = r.getString("balance");
//                String a7 = r.getString("qty");
//                String a8 = r.getString("tot");

                caltot = Double.parseDouble(a3);
                totm = totm + caltot;
                jTextField7.setText(totm + "");
                credit = Double.parseDouble(a6);
                creditm = creditm + credit;
                jTextField8.setText(creditm + "");
                totrec = totm - creditm;
                jTextField9.setText(totrec + "");

                String aa = "";

//               double inv+=0.0;
                ResultSet r1 = com.createStatement().executeQuery("select * from invoice where invoiceno='" + a1 + "'");

                while (r1.next()) {

                    String b3 = r1.getString("itemid");
                    String b7 = r1.getString("qty");

                    ResultSet r2 = com.createStatement().executeQuery("select * from item where itemid='" + b3 + "'");

                    while (r2.next()) {

                        aa = r2.getString("pup");
                        // String aa1 = r1.getString("qty");
                    }
                    inv = Double.parseDouble(aa) * Double.parseDouble(b7);
                   

                }
                 inv1 = inv1+inv;

                    System.out.println(inv1 + "");
            }

            System.out.println(inv + "");

            jTextField10.setText(inv1 + "");
            profitm = totm - inv1;
            jTextField11.setText(profitm + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadtb2() {
        try {
            Connection com = DB.conect();
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            int i = dtm.getRowCount();
            while (i > 0) {
                dtm.removeRow(i - 1);
                i--;

            }

            ResultSet r = com.createStatement().executeQuery("select * from invoice where invoiceno='" + ob + "'");
            Vector v = new Vector();
            while (r.next()) {
//               
//                String a1 = r.getString("invoiceno");
//                String a2 = r.getString("cusid");
                String a3 = r.getString("itemid");
                String a4 = r.getString("itemname");
                String a5 = r.getString("category");
                String a6 = r.getString("sup");
                String a7 = r.getString("qty");
                String a8 = r.getString("tot");
//                
//                v.add(a1);
//                v.add(a2);
                v.add(a3);
                v.add(a4);
                v.add(a5);
                v.add(a6);
                v.add(a7);
                v.add(a8);
                dtm.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadtime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //   get current date time with Date()
        Date currentdate = new Date();
        //System.out.println(dateFormat.format(date)); don't print it, but save it!
        String yourDate = dateFormat.format(currentdate);
        jLabel1.setText(yourDate);

    }

    public void calculateTootalAmount() {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        int i = dtm.getRowCount();
        double tot = 0.0;
        for (int a = 0; a < i; a++) {
            tot += Double.parseDouble(dtm.getValueAt(a, 2).toString());
        }

        jTextField1.setText(tot + "");
//        jTextField11.setText(tot + "");
        double pr = Double.parseDouble(jTextField5.getText());
        double profit = tot - pr;
        jTextField6.setText(profit + "");

    }

    public void calculatecashAmount() {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        int i = dtm.getRowCount();
        double tot = 0.0;
        for (int a = 0; a < i; a++) {
            tot += Double.parseDouble(dtm.getValueAt(a, 3).toString());
        }

        jTextField2.setText(tot + "");
//        jTextField11.setText(tot + "");
    }

    public void calculateBalanceAmount() {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        int i = dtm.getRowCount();
        double tot = 0.0;
        for (int a = 0; a < i; a++) {
            tot += Double.parseDouble(dtm.getValueAt(a, 5).toString());
        }

        jTextField3.setText(tot + "");
//        jTextField11.setText(tot + "");
    }

    void load_table() {
        try {

            Connection com = DB.conect();
//            ResultSet r = com.createStatement().executeQuery("select * from customerpayment where invodate='" + yourDate + "'");
//            String cusid = null;
//            while (r.next()) {
//                cusid = r.getString("cusid");
//            }
//            System.out.println(cusid);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            int i = dtm.getRowCount();
            while (i > 0) {
                dtm.removeRow(i - 1);
                i--;

            }
            String d = jLabel1.getText();
            String d1[] = d.split("/");
            String datein = d1[0] + "-" + d1[1] + "-" + d1[2];
            ResultSet p = com.createStatement().executeQuery("select * from invoicesummery where invodate='" + datein + "'");
            String aa = "";
            double inv = 0.0;
            double inv1 = 0.0;
            while (p.next()) {
                Vector v = new Vector();

                String a = p.getString("invoiceno");
                v.add(a);
                v.add(p.getString("cusid"));
                v.add(p.getString("invototal"));
                v.add(p.getString("cash"));
                v.add(p.getString("cheque"));
                v.add(p.getString("balance"));

                dtm.addRow(v);

                ResultSet r = com.createStatement().executeQuery("select * from invoice where invoiceno='" + a + "'");

                while (r.next()) {

                    String a3 = r.getString("itemid");
                    String a7 = r.getString("qty");

                    ResultSet r1 = com.createStatement().executeQuery("select * from item where itemid='" + a3 + "'");

                    while (r1.next()) {

                        aa = r1.getString("pup");
                        // String aa1 = r1.getString("qty");
                    }
                    inv += Double.parseDouble(aa) * Double.parseDouble(a7);
                    inv1 = Double.parseDouble(aa) * Double.parseDouble(a7);

                    System.out.println(inv1 + "");

                }

                System.out.println(inv + "");

            }
            jTextField5.setText(inv + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1270, 680));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(0), "Daily Sales & income", 0, 0, new java.awt.Font("Adobe Arabic", 1, 36), new java.awt.Color(0, 165, 255))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 130, 23));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice ID", "Customer ID", "Total", "Cash", "Discount", "Balance"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 83, 466, 180));

        jLabel2.setText("Total");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 126, -1));

        jLabel3.setText("Total Cash Received");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, -1, -1));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 126, -1));

        jLabel4.setText("Credit");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 48, -1));
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 126, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Invested");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, -1, 25));
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 100, 25));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Profit");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 350, 44, 29));

        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(0, 165, 255));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 350, 100, 29));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "Category", "SUP", "Qty", "Total"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel7.setText("Invoice No");

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(578, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(152, 152, 152))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Monthly Sales & Income", 0, 0, new java.awt.Font("Adobe Arabic", 0, 36), new java.awt.Color(0, 165, 255))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 120, -1));

        jLabel8.setText("Monthly total ");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice NO", "Customer ID", "Total", "Cash", "Discount", "Balance"
            }
        ));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 466, 152));

        jLabel9.setText("credit");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, -1));

        jLabel10.setText("Total cash Received");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));
        jPanel3.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 120, -1));
        jPanel3.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 310, 84, -1));

        jLabel11.setText("Invested");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 240, -1, -1));
        jPanel3.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 240, 68, -1));

        jLabel12.setText("Profit");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, -1, -1));
        jPanel3.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 270, 81, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            Object ob = dtm.getValueAt(jTable1.getSelectedRow(), 0);
            System.out.println(ob);
            validate();

            jTextField4.setText(ob + "");
            try {
                Connection com = DB.conect();
                DefaultTableModel dtmm = (DefaultTableModel) jTable2.getModel();
                int i = dtmm.getRowCount();
                while (i > 0) {
                    dtmm.removeRow(i - 1);
                    i--;

                }

                ResultSet r = com.createStatement().executeQuery("select * from invoice where invoiceno='" + ob + "'");

                while (r.next()) {

//                String a1 = r.getString("invoiceno");
//                String a2 = r.getString("cusid");
                    String a3 = r.getString("itemid");
                    String a4 = r.getString("itemname");
                    String a5 = r.getString("category");
                    String a6 = r.getString("sup");
                    String a7 = r.getString("qty");
                    String a8 = r.getString("tot");
                    Vector v = new Vector();

//                v.add(a1);
//                v.add(a2);
                    v.add(a3);
                    v.add(a4);
                    v.add(a5);
                    v.add(a6);
                    v.add(a7);
                    v.add(a8);
                    dtmm.addRow(v);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } //            jList1.setListData(v);
        ////            jTextField4.setText(ob+"");
        ////            jTextField1.setText(ob+"");
        catch (Exception e) {
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased

        try {
            Connection com = DB.conect();
            DefaultTableModel dt = (DefaultTableModel) jTable2.getModel();
            int i = dt.getRowCount();
            while (i > 0) {
                dt.removeRow(i - 1);
                i--;

            }

            ResultSet r = com.createStatement().executeQuery("select * from invoice where invoiceno='" + jTextField4.getText() + "'");
            Vector v = new Vector();
            while (r.next()) {
//               
//                String a1 = r.getString("invoiceno");
//                String a2 = r.getString("cusid");
                String a3 = r.getString("itemid");
                String a4 = r.getString("itemname");
                String a5 = r.getString("category");
                String a6 = r.getString("sup");
                String a7 = r.getString("qty");
                String a8 = r.getString("tot");
//                
//                v.add(a1);
//                v.add(a2);
                v.add(a3);
                v.add(a4);
                v.add(a5);
                v.add(a6);
                v.add(a7);
                v.add(a8);
                dt.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        loadtb2();  // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        try {
            DefaultTableModel dtmm = (DefaultTableModel) jTable3.getModel();
            Object ob = dtmm.getValueAt(jTable3.getSelectedRow(), 0);
            System.out.println(ob);
            jTextField4.setText(ob + "");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();

            Connection com = DB.conect();

            int i = dtm.getRowCount();
            while (i > 0) {
                dtm.removeRow(i - 1);
                i--;

            }

            ResultSet r = com.createStatement().executeQuery("select * from invoice where invoiceno='" + ob + "'");

            while (r.next()) {

//                String a1 = r.getString("invoiceno");
//                String a2 = r.getString("cusid");
                String a3 = r.getString("itemid");
                String a4 = r.getString("itemname");
                String a5 = r.getString("category");
                String a6 = r.getString("sup");
                String a7 = r.getString("qty");
                String a8 = r.getString("tot");
                Vector v = new Vector();

//                v.add(a1);
//                v.add(a2);
                v.add(a3);
                v.add(a4);
                v.add(a5);
                v.add(a6);
                v.add(a7);
                v.add(a8);
                dtm.addRow(v);

            }
        } catch (Exception e) {
            e.printStackTrace();

        } //            jList1.setListData(v);
        ////            jTextField4.setText(ob+"");
        ////            jTextField1.setText(ob+"");

    }//GEN-LAST:event_jTable3MouseClicked

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
            java.util.logging.Logger.getLogger(DailySale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailySale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailySale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailySale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DailySale().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
