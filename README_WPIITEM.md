# WPI Item

*WPI Item* adalah obyek yang berisi data barang pembelian/transaksi.

Berikut daftar variable yang tersedia di WPIItem:

| Variabel | Type    | Nilai Awal | Deskripsi                                                    |
| -------- | ------- | ---------- | ------------------------------------------------------------ |
| name     | String  | -          | Nama barang                                                  |
| price    | Integer | 0          | Harga satuan barang                                          |
| qty      | Integer | 0          | Jumlah satuan barang                                         |
| weight   | Double  | 0          | (Opsional) Bobot total barang (harga x satuan) dalam **gram** |
| sku      | String  | -          | (Opsional) Stock Keeping Unit (SKU)                          |
| desc     | String  | -          | (Opsional) Catatan singkat barang                            |

tiap variable memiliki fungsi getter dan setternya.