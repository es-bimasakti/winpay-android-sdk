# WinPay Payment Interface (WPI) Android SDK

[![GPL Licence](https://member.winpay.id/assets/img/sdk-android/license.svg)](https://opensource.org/licenses/GPL-3.0/) [![Maintenance](https://member.winpay.id/assets/img/sdk-android/maintained.svg)](https://github.com/es-bimasakti/winpay-android-sdk/graphs/commit-activity)  [![Release](https://member.winpay.id/assets/img/sdk-android/release.svg)](https://github.com/es-bimasakti/winpay-android-sdk/releases) 

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
|Variabel|Type|Deskripsi|
|---|---|---|
|private_key_1|String|Private key yang didapatkan saat generate melalui member area, digunakan sebagai username pengganti ID merchant|
|private_key_1|String|Private key yang didapatkan saat generate melalui member area, digunakan sebagai password|
|merchant_key|String|Merchant key yang didapatkan saat generate melalui member area, digunakan sebagai hash/salt untuk melakukan enkripsi signature|

Panggil Activity WPI

```java
WPIToolbarImplActivity.start(activity, request_code, wpi_object, url_listener);
```

| Variabel     | Type      | Deskripsi                                                    |
| ------------ | --------- | ------------------------------------------------------------ |
| activity     | Activity  | Activity untuk memanggil intent activity WPI                 |
| request_code | Integer   | Private key yang didapatkan saat generate melalui member area, digunakan sebagai password |
| wpi_object   | WPIObject | Obyek yang berisi parameter basis request ke WPI, lihat di [Detail WPI Object](README_WPIOBJECT.md) |
| url_listener | String    | (Opsional) URL listener untuk system WinPay melakukan notifikasi ketika ada pembayaran sukses |

Pasang ***onActivityResult*** pada Activity Anda

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == REQUEST_SPI_TOOLBAR && data != null) {
        WPIResponse response = AffinityHelper.intentToResponse(intent);
        if (response != null) {
            //tampilkan response.getResponse_desc();
        } else {
            //tampilkan warning/error lain
        }
    }
}
```

[WPIResponse](README_WPIRESPONSE.md) adalah obyek yang dikembalikan saat ***onActivityResult*** melalui intent. Gunakan ***AffinityHelper.intentToResponse(intent)*** untuk konversi intent ke WPIResponse.

**Catatan:**

Aplikasi diwajibkan untuk melakukan rekapitulasi hasil pembayaran secara internal karena WinPay hanya memberikan pelaporan berupa transaksi yang telah berhasil.

## Kustomisasi Sumber Daya (*Resource Customization*)

Berikut xml daftar semua sumber daya yang dapat Anda lakukan kustomisasi di file **winpay.xml** proyek Anda:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--TIPE BOOLEAN-->
    <bool name="wpi__sandbox_mode">false</bool>
    <bool name="wpi__is_loggable">false</bool>
    <bool name="wpi__show_direct_label">@bool/wpi__sandbox_mode</bool>
    <bool name="wpi__show_redirect_url">true</bool>

    <!--TIPE WARNA-->
    <color name="wpi__colorPrimary">#1B75BC</color>
    <color name="wpi__colorPrimaryDark">#004F92</color>
    <color name="wpi__colorAccent">#F58600</color>
    <color name="wpi__colorBackground">#FFFFFF</color>
    <color name="wpi__colorBackgroundDark">#000000</color>
    <color name="wpi__colorTransparent">#77000000</color>
    <color name="wpi__colorGridBackground">#11F58600</color>

    <!--TIPE DIMENSI-->
    <dimen name="wpi__icon_medium">24dp</dimen>
    <dimen name="wpi__icon_large">48dp</dimen>
    <dimen name="wpi__padding_small">8dp</dimen>
    <dimen name="wpi__padding_medium">16dp</dimen>
    <dimen name="wpi__text_size_small">12sp</dimen>
    <dimen name="wpi__text_size_medium">16sp</dimen>
    <dimen name="wpi__text_size_large">24sp</dimen>
    <dimen name="wpi__border_radius">8dp</dimen>
    <dimen name="wpi__border_width">2dp</dimen>
    <dimen name="wpi__dialog_margin">32dp</dimen>
    <dimen name="wpi__card_elevation">4dp</dimen>
    <dimen name="wpi__toolbar_image_initial_height">81dp</dimen>

    <!--TIPE ANGKA-->
    <integer name="wpi__toolbar_grid">3</integer>
    <integer name="wpi__toolbar_image_width">512</integer>
    <integer name="wpi__toolbar_image_height">214</integer>

    <!--TIPE TEKS-->
    <string name="wpi__button_ok">OK</string>
    <string name="wpi__button_yes">Ya</string>
    <string name="wpi__button_cancel">Batal</string>
    <string name="wpi__button_visit">Lihat Cara Pembayaran</string>
    <string name="wpi__confirm_exit_web">Transaksi yang dibatalkan tidak dapat diulang, apakah Anda yakin akan membatalkan proses transaksi ini?</string>
    <string name="wpi__confirm_exit_toolbar">Apakah Anda yakin akan kembali ke halaman sebelumnya?</string>
    <string name="wpi__info_process_waiting">mohon tunggu…</string>
    <string name="wpi__info_process_loading">kami sedang memproses permintaan Anda</string>
    <string name="wpi__info_response_cancelled">Transaksi dibatalkan oleh pelanggan</string>
    <string name="wpi__info_response_unknown">Kesalahan Umum</string>
    <string name="wpi__info_text_copied">Teks telah tersalin</string>
    <string name="wpi__info_toolbar">Silakan pilih media pembayaran favorit Anda untuk melanjutkan pembayaran</string>
    <string name="wpi__info_title_format">Pembayaran %1$s</string>
    <string name="wpi__warning_connection_failure">Tidak dapat tersambung ke Server</string>
    <string name="wpi__warning_connection_denied">Ditolak Server dengan kode %1$d</string>
    <string name="wpi__warning_connection_canceled">Koneksi ke Server di batalkan</string>
    <string name="wpi__warning_connection_unknown">Kesalahan tidak diketahui</string>
    <string name="wpi__warning_connection_response">Respon dari server tidak dikenali oleh Aplikasi</string>
    <string name="wpi__warning_connection_offline">Tidak ada koneksi internet</string>
    <string name="wpi__warning_connection_timeout">Waktu tempuh koneksi ke server telah habis</string>
    <string name="wpi__title_confirm">Konfirmasi</string>
    <string name="wpi__title_info">Informasi</string>
    <string name="wpi__title_warning">Perhatian</string>
    <string name="wpi__title_direct">Direct</string>
</resources>
```

Keterangan sumber daya:

| Variabel                          | Type    | Deskripsi                                                    |
| --------------------------------- | ------- | ------------------------------------------------------------ |
| wpi__sandbox_mode                 | boolean | Jika Anda membutuhkan akses sandbox untuk melakukan testing/percobaan dengan pembayaran dummy ubah nilainya menjadi ***true*** |
| wpi__is_loggable                  | boolean | Menampilkan log pada beberapa event (tidak direkomendasikan untuk production) |
| wpi__show_direct_label            | boolean | Menampilkan label "wpi__title_direct" pada daftar toolbar WPI |
| wpi__show_redirect_url            | boolean | Jika nilainya ***true*** maka winpay akan secara otomatis membuka halaman status pembayaran setelah melakukan transaksi Redirect, ubah menjadi ***false*** jika Anda ingin mengelola sendiri tampilan halaman status |
|                                   |         |                                                              |
| wpi__colorPrimary                 | color   | Warna utama aplikasi, mempengaruhi bilah aplikasi, teks, dan tombol |
| wpi__colorPrimaryDark             | color   | Warna yang sedikit lebih gelap dari warna utama, mempengaruhi bilah notifikasi |
| wpi__colorAccent                  | color   | Warna aksen, mempengaruhi teks, tombol, dan warna latar untuk peringatan |
| wpi__colorBackground              | color   | Warna latar belakang (terang)                                |
| wpi__colorBackgroundDark          | color   | Warna latar belakang (gelap)                                 |
| wpi__colorTransparent             | color   | Warna latar belakang (transparan)                            |
| wpi__colorGridBackground          | color   | Warna latar belakang *grid channel* pembayaran WPI.          |
|                                   |         |                                                              |
| wpi__icon_medium                  | dimen   | Ukuran ikon sedang, digunakan untuk ikon tombol              |
| wpi__icon_large                   | dimen   | Ukuran ikon besar, digunakan gambar animasi, notifikasi dialog, dll |
| wpi__padding_small                | dimen   | Lebar pad kecil, digunakan untuk teks                        |
| wpi__padding_medium               | dimen   | Lebar pad sedang, digunakan untuk pemisah antar elemen       |
| wpi__text_size_small              | dimen   | Ukuran teks kecil, digunakan untuk subjudul                  |
| wpi__text_size_medium             | dimen   | Ukuran teks sedang, digunakan untuk teks umum                |
| wpi__text_size_large              | dimen   | Ukuran teks besar, digunakan untuk judul                     |
| wpi__border_radius                | dimen   | Ukuran pembulatan pinggiran elemen                           |
| wpi__border_width                 | dimen   | Lebar pinggiran                                              |
| wpi__dialog_margin                | dimen   | Sela pembatas layar dan dialog                               |
| wpi__card_elevation               | dimen   | Tinggi level elevasi bayangan (khusus API 21 ke atas)        |
| wpi__toolbar_image_initial_height | dimen   | Tinggi awal gambar WPI toolbar (tinggi sesungguhnya menyesuaikan rasio lebar dan tinggi layar) |
|                                   |         |                                                              |
| wpi__toolbar_grid                 | integer | Jumlah kolom per baris                                       |
| wpi__toolbar_image_width          | integer | Rasio lebar gambar WPI toolbar                               |
| wpi__toolbar_image_height         | integer | Rasio tinggi gambar WPI toolbar                              |
|                                   |         |                                                              |

## Kustomisasi Activity (*Activity Customization*)
Anda dapat membangun sendiri model *Activity* sesuai dengan kaidah/aturan aplikasi Anda, dan tetap berada dalam *Work Flow* WPI dengan cara inherit **WPIToolbarActivity**.

```java
package my.awesome.app;

import java.util.List;

import id.winpay.winpaysdk.main.item.ChannelGroup;
import id.winpay.winpaysdk.main.item.WPIResponse;

import my.awesome.app.R;

public class MyWPIActivity extends WPIToolbarActivity {
    @Override
    protected boolean isToolbarGrouped() {
        //Ubah jadi false jika Anda tidak menginginkan data tanpa pengelompokan
        return true;
    }

    @Override
    protected void processToolbar(List<ChannelGroup> groups) {
		//Fungsi ini digunakan untuk memproses/menampilkan daftar channel toolbar (misal menggunakan ListView atau RecyclerView)
        //Jika isToolbarGrouped adalah false maka "groups" hanya berisi 1 ChannelGroup
    }

    @Override
    protected void processPaymentDirect(WPIResponse response) {
		//Fungsi ini digunakan untuk memproses transaksi pembayaran jenis Direct
    }

    @Override
    protected void processPaymentRedirect(WPIResponse response) {
		//Fungsi ini digunakan untuk memproses transaksi pembayaran jenis Redirect
    }

    @Override
    public int getLayoutResource() {
        //XML layout activity ini
        return R.layout.my_wpi_activity_layout;
    }

    @Override
    public void onRequestToolbarProcess() {
		//Dipanggil saat SDK akan melakukan request ke server meminta data toolbar, dapat digunakan untuk menampilkan animasi/progress bar
    }

    @Override
    public void onRequestToolbarFailed(String rc, String msg) {
		//Dipanggil saat SDK gagal melakukan request ke server meminta data toolbar, dapat digunakan untuk menampilkan alert/warning
    }

    @Override
    public void onRequestInquiryProcess() {
		//Dipanggil saat SDK akan melakukan request ke server meminta data pembayaran, dapat digunakan untuk menampilkan animasi/progress bar
    }

    @Override
    public void onRequestInquiryFailed(String rc, String msg) {
		//Dipanggil saat SDK gagal melakukan request ke server meminta data pembayaran, dapat digunakan untuk menampilkan alert/warning
    }
}
```

Panggil *Activity* Anda

```java
WPIToolbarActivity.start(activity, MyWPIActivity.class, request_code, wpi_object, url_listener);
```

## Author

SDK ini dikerjakan oleh tim developer WinPay unit E-Payment Solution divisi IT & Business Solution, PT. Bimasakti Multi Sinergi.

Kontribusi ditutup hanya untuk tim internal. Jika ada laporan isu, galat, atau saran dapat dilayangkan ke halaman [Issue Tracker](https://github.com/es-bimasakti/winpay-android-sdk/issues) github ini.

## Lisensi

WinPay merupakan merek terdaftar milik PT. Bimasakti Multi Sinergi. SDK ini merupakan alat bantu untuk memudahkan merchant melakukan integrasi via aplikasi Android. Semua proses legal dan operasional tetap mengikuti aturan yang berlaku di WinPay.

- [Syarat dan Ketentuan](https://winpay.id/syarat-dan-ketentuan/)
- [Kebijakan Privasi](https://winpay.id/privasi-policy/)
- [Kontak Kami](https://winpay.id/kontak/)

## Pustaka

SDK ini dibuat dengan pustaka berikut:

- Klien HTTP+HTTP/2 [OkHttp](https://github.com/square/okhttp), untuk melakukan permintaan ke server.
- Pengelolaan penampilan dan penyimpanan gambar [Glide](https://github.com/bumptech/glide).
- WebView dari [Android Advanced WebView](https://github.com/delight-im/Android-AdvancedWebView) untuk menampilkan panduan transaksi WPI dan pembayaran versi *Redirect*.