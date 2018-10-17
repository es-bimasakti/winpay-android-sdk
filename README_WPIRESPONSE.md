# WPI Response

*WPI Response adalah obyek yang dikembalikan oleh WPI saat ***onActivityResult*** melalui intent. Obyek ini bersifat *read-only*. 

Berikut daftar variable yang tersedia di WPIResponse:

| Variabel            | Type    | Deskripsi                                                    |
| ------------------- | ------- | ------------------------------------------------------------ |
| reff_id             | String  | Nomor referensi pembayaran dari WPI                          |
| order_id            | String  | Nomor referensi inquiry dari WPI                             |
| response_code       | String  | 2 atau 3 digit kode respon, 2 digit untuk kode eksternal (dari WPI), 3 digit untuk internal (dari aplikasi) |
| response_desc       | Integer | Deskripsi dari respon                                        |
| merchant_reff       | String  | Nomor referensi yang sebelumnya dikirim melalui [WPIObject](README_WPIOBJECT.md) |
| spi_status_url      | String  | URL status transaksi, berisi petunjuk cara pembayaran (untuk channel redirect hanya muncul setelah eksekusi ***spi_payment_url***) |
| spi_payment_url     | String  | URL pembayaran, **khusus untuk channel redirect**            |
| payment_code        | String  | Kode pembayaran, **khusus untuk channel direct**             |
| payment_method      | String  | Nama channel/media pembayaran                                |
| payment_method_code | String  | Kode channel/media pembayaran                                |
| fee_admin           | double  | Biaya admin                                                  |
| total_amount        | double  | Total nominal transaksi                                      |

tiap variable memiliki fungsi getter.

## Response Code

Berikut daftar kode respon yang terdaftar di sistem WPI:

| Kode Respon | Deskripsi                     | Keterangan                                                   |
| ----------- | ----------------------------- | ------------------------------------------------------------ |
| 01          | Access Denied! not authorized | Respon ini muncul jika merchant tidak terdaftar atau private key tidak valid |
| 04          | Data not found                | Data tidak ditemukan, muncul jika merchant tidak memiliki satu pun akses ke *channel* pembayaran |
| 05          | General error                 | Terjadi kesalahan di WPI, silakan hubungi tim teknis jika terjadi kesalahan demikian |
| 99          | Parameters not valid          | Parameter yang dikirim oleh aplikasi tidak valid, silakan cek [WPIObject](README_WPIOBJECT.md) yang dikirim |
|             |                               |                                                              |

Untuk kode respon 3 digit mengacu pada [kode status HTTP](https://id.wikipedia.org/wiki/Daftar_kode_status_HTTP).