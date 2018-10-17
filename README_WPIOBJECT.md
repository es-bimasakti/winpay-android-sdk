# WPI Object

*WPI Object* adalah obyek yang berisi parameter transaksional yang dikirim ke WPI.

Berikut daftar variable yang tersedia di WPIObject:

| Variabel                      | Type         | Nilai Awal  | Deskripsi                                                    |
| ----------------------------- | ------------ | ----------- | ------------------------------------------------------------ |
| spi_billingName               | String       | -           | Nama pembeli/yang melakukan transaksi                        |
| spi_billingPhone              | String       | -           | Nomor HP pembeli/yang melakukan transaksi                    |
| spi_billingEmail              | String       | -           | Email pembeli/yang melakukan transaksi                       |
| spi_merchant_transaction_reff | String       | -           | Nomor referensi transaksi                                    |
| spi_item                      | List WPIItem | List kosong | Daftar item transaksi, berupa list obyek WPIItem. Lihat di [Detail WPIItem](README_WPIITEM.md) |
| spi_billingDescription        | String       | -           | (Opsional) Berisi catatan transaksi atau catatan dari pembeli |

tiap variable memiliki fungsi getter dan setternya.