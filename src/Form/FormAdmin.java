package Form;

import koneksi.Koneksi;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormAdmin extends javax.swing.JFrame {
    DefaultTableModel model;

    public FormAdmin() {
        initComponents();
        setLocationRelativeTo(null);
        loadDataPendaftar();
    }

    public void loadDataPendaftar() {
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Siswa");
        model.addColumn("Mata Lomba");
        model.addColumn("Status Tahapan");
        tblPendaftar.setModel(model);
        
        try {
            Connection c = Koneksi.configDB();
            Statement s = c.createStatement();
            String sql = "SELECT pendaftaran.id, users.username, lomba.nama_lomba, pendaftaran.status " +
                         "FROM pendaftaran " +
                         "JOIN users ON pendaftaran.user_id = users.id " +
                         "JOIN lomba ON pendaftaran.lomba_id = lomba.id";
            ResultSet r = s.executeQuery(sql);
            while (r.next()) {
                model.addRow(new Object[]{
                    r.getString("id"),
                    r.getString("username"),
                    r.getString("nama_lomba"),
                    r.getString("status")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNamaLomba = new javax.swing.JTextField();
        btnUbahStatus = new javax.swing.JButton();
        btnSimpanLomba = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPendaftar = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("PANEL ADMIN - BI GOT TALENT");

        btnUbahStatus.setText("Status");
        btnUbahStatus.addActionListener(this::btnUbahStatusActionPerformed);

        btnSimpanLomba.setText("Simpan Lomba");
        btnSimpanLomba.addActionListener(this::btnSimpanLombaActionPerformed);

        tblPendaftar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblPendaftar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnUbahStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSimpanLomba))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNamaLomba))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(113, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtNamaLomba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUbahStatus)
                    .addComponent(btnSimpanLomba))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUbahStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahStatusActionPerformed
                int baris = tblPendaftar.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data siswa di tabel terlebih dahulu!");
            return;
        }
        String idPendaftaran = model.getValueAt(baris, 0).toString();
        String statusBaru = JOptionPane.showInputDialog(this, "Masukkan Status Baru (Contoh: Tahap Briefing / Pelatihan / Selesai):");
        
        if (statusBaru != null && !statusBaru.trim().isEmpty()) {
            try {
                Connection c = Koneksi.configDB();
                Statement s = c.createStatement();
                String sql = "UPDATE pendaftaran SET status='" + statusBaru + "' WHERE id='" + idPendaftaran + "'";
                s.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Status berhasil diperbarui!");
                loadDataPendaftar();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal update status: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnUbahStatusActionPerformed

    private void btnSimpanLombaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanLombaActionPerformed
                try {
            Connection c = Koneksi.configDB();
            Statement s = c.createStatement();
            String sql = "INSERT INTO lomba (nama_lomba) VALUES ('" + txtNamaLomba.getText() + "')";
            s.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Mata Lomba berhasil ditambahkan!");
            txtNamaLomba.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tambah lomba: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanLombaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormAdmin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpanLomba;
    private javax.swing.JButton btnUbahStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPendaftar;
    private javax.swing.JTextField txtNamaLomba;
    // End of variables declaration//GEN-END:variables
}
