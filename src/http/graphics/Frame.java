/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.graphics;

import http.client.core.Client;
import http.client.core.Connection;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author p1609594
 */
public class Frame extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form Frame
     */
    public Frame() {
        initComponents();
        jTabbedPane1.setTitleAt(0, "Texte");
        Client c = new Client();
        c.addObserver(this);
        new Thread(c).start();
        buttonOk.addActionListener((ActionEvent e) -> {
            cookiesArea.setText("");
            String url = addressBar.getText();
            try {
                String address = url.split("/")[0];
                String file = "/" + url.split("/", 2)[1];
                c.addConnection(new Connection(address, 80, file));
            } catch (java.lang.ArrayIndexOutOfBoundsException zezeze) {
                c.addConnection(new Connection(url, 80, "/"));
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Connection c = (Connection) arg;
        if (c.getHeader() == null) {
            JOptionPane.showMessageDialog(this, "La connexion a échoué",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //new File("cookies/" + addressBar.getText().split("/")[0]).delete();
        c.getHeader().getCookies().forEach(cookie -> {
            try {
                cookiesArea.setText(cookiesArea.getText() + cookie + "\n");
                BufferedWriter output = new BufferedWriter(new FileWriter("cookies/" + addressBar.getText().split("/")[0], true));
                output.write(cookie + "\n");
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        if (c.getHeader().getContentType().startsWith("image")) {
            jTabbedPane1.setSelectedIndex(1);

            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(c.getContent().getContent()));
                Image dimg = img.getScaledInstance(jTabbedPane1.getWidth() - 10, jTabbedPane1.getHeight() - 20, Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(dimg));
            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            jTabbedPane1.setSelectedIndex(0);
            contentArea.setText(new String(c.getContent().getContent()));
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

        addressBar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        buttonOk = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cookiesArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addressBar.setText("134.214.117.134/index.html");

        jLabel1.setText("Address : ");

        buttonOk.setText("OK");

        contentArea.setColumns(20);
        contentArea.setRows(5);
        jScrollPane1.setViewportView(contentArea);
        contentArea.getAccessibleContext().setAccessibleName("");
        contentArea.getAccessibleContext().setAccessibleDescription("");

        jTabbedPane1.addTab("tab1", jScrollPane1);
        jTabbedPane1.addTab("Image", jLabel2);

        cookiesArea.setColumns(20);
        cookiesArea.setRows(5);
        jScrollPane2.setViewportView(cookiesArea);

        jTabbedPane1.addTab("Cookies", jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonOk)
                .addContainerGap(254, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(buttonOk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressBar;
    private javax.swing.JButton buttonOk;
    private javax.swing.JTextArea contentArea;
    private javax.swing.JTextArea cookiesArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
