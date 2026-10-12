/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Form;

/**
 *
 * @author ikyys
 */
import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class FormJuri extends javax.swing.JFrame {

    private final int idJuri;
    private DefaultTableModel model;

    private JTable tblPendaftar;
    private JTextArea txtCatatan;
    private JLabel lblDipilih;
    private JButton btnLolos;
    private JButton btnTidakLolos;
    private JButton btnRefresh;
    private JButton btnLogout;

    public FormJuri(int idUser) {
        this.idJuri = idUser;
        initUI();
        setLocationRelativeTo(null);
        loadDataPendaftar();
    }

    private void initUI() {
        setTitle("Dashboard Juri - BI GOT TALENT");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JLabel lblJudul = new JLabel("PANEL JURI - BI GOT TALENT", JLabel.CENTER);
        lblJudul.setFont(lblJudul.getFont().deriveFont(Font.BOLD, 16f));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));

        model = new DefaultTableModel(
                new Object[]{"ID", "Nama Siswa", "Mata Lomba", "Status Tahapan", "Hasil Juri", "Catatan Juri"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPendaftar = new JTable(model);
        tblPendaftar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPendaftar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tampilkanDetailTerpilih();
            }
        });
        JScrollPane spTabel = new JScrollPane(tblPendaftar);
        spTabel.setPreferredSize(new Dimension(760, 230));

        lblDipilih = new JLabel("Pilih pendaftar di tabel untuk dinilai.");
        txtCatatan = new JTextArea(5, 40);
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        JScrollPane spCatatan = new JScrollPane(txtCatatan);
        JLabel lblCatatan = new JLabel("Catatan Juri (wajib diisi jika Tidak Lolos):");

        JPanel panelInput = new JPanel(new BorderLayout(0, 6));
        panelInput.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));
        JPanel atas = new JPanel(new BorderLayout(0, 4));
        atas.add(lblDipilih, BorderLayout.NORTH);
        atas.add(lblCatatan, BorderLayout.SOUTH);
        panelInput.add(atas, BorderLayout.NORTH);
        panelInput.add(spCatatan, BorderLayout.CENTER);

        btnLolos = new JButton("Lolos");
        btnTidakLolos = new JButton("Tidak Lolos");
        btnRefresh = new JButton("Refresh");
        btnLogout = new JButton("Logout");
        btnLolos.addActionListener(e -> beriNilai("Lolos"));
        btnTidakLolos.addActionListener(e -> beriNilai("Tidak Lolos"));
        btnRefresh.addActionListener(e -> loadDataPendaftar());
        btnLogout.addActionListener(e -> {
            new FormLogin().setVisible(true);
            dispose();
        });

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelTombol.add(btnLolos);
        panelTombol.add(btnTidakLolos);
        panelTombol.add(btnRefresh);
        panelTombol.add(btnLogout);

        JPanel tengah = new JPanel(new BorderLayout());
        tengah.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        tengah.add(spTabel, BorderLayout.CENTER);

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(panelInput, BorderLayout.CENTER);
        bawah.add(panelTombol, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(lblJudul, BorderLayout.NORTH);
        getContentPane().add(tengah, BorderLayout.CENTER);
        getContentPane().add(bawah, BorderLayout.SOUTH);
        pack();
    }

    public void loadDataPendaftar() {
        model.setRowCount(0);
        String sql = "SELECT p.id, u.username, l.nama_lomba, p.status, p.hasil_juri, p.catatan_juri "
                + "FROM pendaftaran p "
                + "JOIN users u ON p.user_id = u.id "
                + "JOIN lomba l ON p.lomba_id = l.id "
                + "ORDER BY p.id";
        try (Connection c = Koneksi.configDB();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {
            while (r.next()) {
                model.addRow(new Object[]{
                    r.getString("id"),
                    r.getString("username"),
                    r.getString("nama_lomba"),
                    r.getString("status"),
                    r.getString("hasil_juri"),
                    r.getString("catatan_juri")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
        txtCatatan.setText("");
        lblDipilih.setText("Pilih pendaftar di tabel untuk dinilai.");
    }

    private void tampilkanDetailTerpilih() {
        int baris = tblPendaftar.getSelectedRow();
        if (baris == -1) {
            return;
        }
        String nama = String.valueOf(model.getValueAt(baris, 1));
        String lomba = String.valueOf(model.getValueAt(baris, 2));
        Object catatan = model.getValueAt(baris, 5);
        lblDipilih.setText("Dinilai: " + nama + " (" + lomba + ")");
        txtCatatan.setText(catatan == null ? "" : catatan.toString());
    }

    private void beriNilai(String hasil) {
        int baris = tblPendaftar.getSelectedRow();
        if (baris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pendaftar di tabel terlebih dahulu!");
            return;
        }

        String catatan = txtCatatan.getText().trim();
        if (hasil.equals("Tidak Lolos") && catatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan wajib diisi untuk menjelaskan alasan Tidak Lolos!");
            txtCatatan.requestFocus();
            return;
        }

        String idPendaftaran = model.getValueAt(baris, 0).toString();
        String nama = String.valueOf(model.getValueAt(baris, 1));

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Beri nilai \"" + hasil + "\" untuk " + nama + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE pendaftaran SET hasil_juri=?, catatan_juri=?, juri_id=?, "
                + "tgl_penilaian=NOW() WHERE id=?";
        try (Connection c = Koneksi.configDB();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hasil);
            ps.setString(2, catatan.isEmpty() ? null : catatan);
            ps.setInt(3, idJuri);
            ps.setInt(4, Integer.parseInt(idPendaftaran));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Penilaian berhasil disimpan!");
            loadDataPendaftar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan penilaian: " + e.getMessage());
        }
    }
}