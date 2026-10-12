/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;
import java.sql.Connection;
/**
 *
 * @author ikyys
 */
public class CekKoneksi {
    
 public static void main(String[] args) {
        Connection c = Koneksi.configDB();
        if (c != null) {
            System.out.println("HORE! NetBeans Berhasil Terhubung ke XAMPP!");
        } else {
            System.out.println("GAGAL TERHUBUNG!");
        }
    }
}

