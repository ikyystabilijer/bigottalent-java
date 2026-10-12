A. Alur Fitur Utama Aplikasi
1. Sisi Admin (Pengelola)
Kelola Akun Siswa (CRUD User): Admin dapat membuatkan akun siswa, mengedit data siswa, atau menghapus akun siswa yang terdaftar.
Kelola Mata Lomba (CRUD Lomba): Admin bisa memasukkan jenis mata lomba (contoh: Solo Vocal, Dance Modern, Stand Up Comedy).
Manajemen Status Peserta: Admin dapat mengubah status progres pendaftaran siswa (misal: Pendaftaran Diterima, Dokumen dalam Tinjauan, Jadwal Briefing, Tahap Pelatihan, hingga Final).
2. Sisi Siswa (Peserta)
Login & Pendaftaran: Siswa login menggunakan akun yang dibuatkan admin (atau register sendiri), lalu mengisi form pendaftaran lomba (memilih kategori lomba).
Tracking Status (Pemberitahuan Tahap): Siswa memiliki halaman Dashboard Siswa untuk melihat riwayat lombanya dan melihat status progres terkini (misalnya: bar progres atau badge status: "Dokumen sedang ditinjau oleh juri" atau "Undangan Briefing tanggal X").
3. Sisi Juri (Penilai)

- **Login Juri:** Juri login menggunakan akun yang dibuatkan di database (role `juri`). Setelah login berhasil, juri langsung diarahkan ke Dashboard Juri.
- **Melihat Daftar Pendaftar:** Dashboard Juri menampilkan semua pendaftar dalam bentuk tabel, berisi nama siswa, mata lomba yang dipilih, status tahapan, hasil penilaian juri, dan catatan juri.
- **Memberi Penilaian (Lolos / Tidak Lolos):** Juri memilih satu pendaftar di tabel, lalu menentukan hasil penilaian: **Lolos** atau **Tidak Lolos**. Sebelum disimpan, sistem menampilkan dialog konfirmasi.
- **Memberi Catatan Penilaian:** Juri dapat menuliskan catatan untuk peserta.
  - Jika **Lolos**, catatan bersifat opsional.
  - Jika **Tidak Lolos**, catatan **wajib diisi** sebagai alasan kenapa peserta tidak lolos.
- **Pemberitahuan ke Siswa:** Hasil penilaian dan catatan juri tersimpan di database. Saat siswa login, pop-up pemberitahuan muncul (*"Selamat, Anda LOLOS"* atau *"Mohon maaf, Anda TIDAK LOLOS"* beserta catatan juri). Hasil juga tampil di Dashboard Siswa.
- **Riwayat Penilaian:** Sistem mencatat juri yang menilai dan waktu penilaiannya (`juri_id` dan `tgl_penilaian`). Penilaian yang sudah ada bisa diperbarui oleh juri bila diperlukan.

TAHAP 1: Menyiapkan Database di XAMPP
Buka aplikasi XAMPP Control Panel, lalu klik Start pada Apache dan MySQL.
Buka browser, ketik: http://localhost/phpmyadmin
Buat database baru:
Klik tab New di sebelah kiri.
Masukkan nama database: db_bigottalent
Klik Create.
Masuk ke database db_bigottalent, klik tab SQL, lalu copy-paste kode SQL di bawah ini dan klik Go:

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin', 'siswa') NOT NULL
);

CREATE TABLE lomba (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_lomba VARCHAR(100) NOT NULL,
    deskripsi TEXT
);

CREATE TABLE pendaftaran (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    lomba_id INT NOT NULL,
    status VARCHAR(100) DEFAULT 'Dokumen dalam Tinjauan'
);


-- Masukkan akun Admin default
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
-- Masukkan contoh akun Siswa default
INSERT INTO users (username, password, role) VALUES ('budi', '123', 'siswa');
-- Masukkan contoh data Lomba
INSERT INTO lomba (nama_lomba, deskripsi) VALUES ('Solo Vocal', 'Lomba menyanyi solo lagu pop/daerah');
INSERT INTO lomba (nama_lomba, deskripsi) VALUES ('Dance Modern', 'Lomba tari kreasi modern berkelompok');

-- Masukin akun Juri
-- Pastikan role bisa berisi 'juri'
ALTER TABLE users MODIFY role VARCHAR(20) NOT NULL;

-- Kolom baru di tabel pendaftaran
ALTER TABLE pendaftaran
  ADD COLUMN hasil_juri    VARCHAR(20) NOT NULL DEFAULT 'Belum Dinilai',
  ADD COLUMN catatan_juri  TEXT        NULL,
  ADD COLUMN juri_id       INT         NULL,
  ADD COLUMN tgl_penilaian DATETIME    NULL;

-- Akun juri buat testing
INSERT INTO users (username, password, role) VALUES ('juri1', 'juri123', 'juri');



