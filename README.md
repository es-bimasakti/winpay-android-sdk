# WinPay Payment Interface (WPI) Android SDK
***

***===SDK ini sedang dalam masa development, pemakaian sebelum rilis tidak disarankan dan resiko tidak ditanggung===***

>[WinPay](https://www.winpay.id/)  merupakan salah satu Payment Gateway Indonesia yang menyediakan sarana dan prasarana yang lengkap untuk pemilik toko/bisnis online dan kemudahan pembeli dalam melakukan pilihan pembayaran yang beragam. WinPay merupakan bagian dari [PT. Bimasakti Multi Sinergi](https://www.bm.co.id/).

SDK ini dibuat untuk memudahkan pemilik usaha atau *Merchant* dalam melakukan integrasi ke *Payment Gateway* WinPay dengan platform Android.

SDK akan melakukan *request* ke API WinPay dan menampilkan halaman WPI. Di halaman WPI terdapat berbagai macam metode pembayaran (*channel*) yang dapat pengguna/pembeli pilih.

## Cara Kerja WPI

*Channel* pembayaran di WinPay dibagi menjadi 2 jenis:

- Direct Channel

![Direct Channel](https://image.winmarket.id/img/winpay/7000/17000/2018/10/1761e626af0fe1fabb2abcaa7799178a1390797adf_0.02191200_1539743428.png  "Direct Channel")

*hasil dari Direct Channel adalah kode pembayaran, yang dibayarkan ke outlet/bank/lembaga yang dipilih.*

- Redirect Channel

![Redirect Channel](https://image.winmarket.id/img/winpay/7000/17000/2018/10/17f33935b8ca603dbe62866b7f4b0ab17532d12bf7_0.22962500_1539743572.png  "Redirect Channel")

*hasil dari Redirect Channel adalah link/URL, sehingga disarankan membuka browser/in-app-browser untuk melanjutkan transaksi.*


## Persiapan (*Preparation*)
>Untuk dapat menggunakan SDK ini pastikan Anda telah terdaftar sebagai *Merchant* WinPay, jika belum silakan daftar terlebih dahulu di [Member Area](https://member.winpay.id/) dan pastikan *Sales Agreement* sudah disetujui oleh tim Sales dan Marketing dan mendapatkan ***ID Merchant***.

1. Buka menu "Setting Integrasi" di *Member Area*

![Member Area WinPay](https://image.winmarket.id/img/winpay/7000/17000/2018/10/1766d122baa0c93d2fb15f0b73a5e3fe8a598e0745_0.20092100_1539744414.png  "Member Area WinPay")

2. Tambah Private Key

![Menu Setting Integrasi](https://image.winmarket.id/img/winpay/7000/17000/2018/10/1797f5747c06d0de445193d19abe2032c90996ef4a_0.06335800_1539744724.png  "Menu Setting Integrasi")

3. Masukkan jenis aplikasi "Android", nama *package* android Anda, dan logo (opsional).

![Form Tambah Private Key](https://image.winmarket.id/img/winpay/7000/17000/2018/10/172d83e46df39ed4413b67d641baf546bec24772a2_0.65276700_1539745463.png  "Form Tambah Private Key")

4. Simpan *Private Key* dan *Merchant Key* yang telah dibuat.

![Daftar Private Key](https://image.winmarket.id/img/winpay/7000/17000/2018/10/174f2b45625400813df7429d60925bae688c58a138_0.02233600_1539744826.png  "Daftar Private Key")

## Instalasi Singkat (*Quick Setup*)

Pasang script berikut di file **build.gradle** pada level app Anda

```gradle
implementation 'id.winpay.winpaysdk:WinPay-sdk:$wpi_last_version'
```

Buat file res dengan nama **winpay.xml** dengan konten minimum

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="wpi__private_key_1">private_key_1</string>
    <string name="wpi__private_key_2">private_key_2</string>
    <string name="wpi__merchant_key">merchant_key</string>
</resources>
```
|Variabel|Type|Nilai Awal|Deskripsi|
|---|---|---|---|
|private_key_1|String|-|Private key yang didapatkan saat generate melalui member area, digunakan sebagai username pengganti ID merchant|
|private_key_1|String|-|Private key yang didapatkan saat generate melalui member area, digunakan sebagai password|
|merchant_key|String|-|Merchant key yang didapatkan saat generate melalui member area, digunakan sebagai hash/salt untuk melakukan enkripsi signature|


## Kustomisasi Sumber Daya (*Resource Customization*)
TBD

## Kustomisasi Activity (*Activity Customization*)
TBD