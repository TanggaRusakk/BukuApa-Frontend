# Spesifikasi Frontend - Aplikasi BukuApa

## 1. Spesifikasi & Dokumentasi Arsitektur
- **Pola Arsitektur:** Model-View-ViewModel (MVVM)
- **Bahasa Pemrograman:** Kotlin (Android)
- **Standar Penamaan (Naming Conventions):**
  - **Class, Interface, Protocol:** `PascalCase` (contoh: `ManageBookViewModel`)
  - **Function & Variable:** `camelCase` (contoh: `fetchBookDetail()`, `bookDetails`)
  - **Constant:** `UPPER_SNAKE_CASE` (contoh: `MAX_BORROW_DAYS`)
- **Format & Indentasi:** 4 spasi (standar gaya Kotlin), batas baris 100-120 karakter.
- **Bahasa Kode:** Comment dan View App dalam Bahasa Indonesia, sedangkan Code menggunakan Bahasa Inggris.
- **Dokumentasi Header:** Setiap class utama wajib memiliki komentar singkat di bagian atas yang menjelaskan fungsinya.

## 2. Struktur Folder dan File
Struktur layer untuk Frontend (MVVM) dipisahkan menjadi:
- **Views**: Khusus berisi logika UI dan deteksi aksi pengguna. (Contoh file: `ManageBookView.kt`, `LoginView.kt`)
- **ViewModels**: Pengelola state layar dan jembatan antara `View` dan sumber data eksternal. (Contoh file: `ManageBookViewModel.kt`)
- **Models**: Berisi struktur data class atau DTO (Data Transfer Object). (Contoh file: `BookModel.kt`, `ReviewModel.kt`)
- **Repositories**: Tempat implementasi dari protokol/abstraksi yang bertugas melakukan panggilan (request) API ke backend. (Contoh file: `BookRepository.kt`)

## 3. Functional Requirements (Kebutuhan Fungsional)
- **FR-01 Register**: Menyediakan fitur pendaftaran akun (nama, email, sandi) untuk menjadi Member. Validasi minimum sandi 8 karakter.
- **FR-02 Login**: Memverifikasi kredensial dan mengarahkan pengguna ke dasbor yang sesuai (Member / Staff).
- **FR-03 Manage Books**: Fitur untuk Staff guna mengelola inventaris katalog (Menambah, Mengubah, Menghapus buku).
- **FR-04 Manage Borrowing**:
  - Untuk Member: Memperpanjang durasi peminjaman dan melihat riwayat pinjaman.
  - Untuk Staff: Membuat peminjaman baru, memperbarui status, dan melihat daftar peminjaman.
- **FR-05 Manage Book Reviews**: Fitur untuk Member guna menambah, mengubah, atau menghapus teks ulasan pada buku yang telah dipinjam.
- **FR-06 Browse Books**: Menampilkan katalog koleksi buku pada antarmuka utama yang dilengkapi fitur pencarian teks yang interaktif.

## 4. Non-Functional Requirements (Kebutuhan Non-Fungsional)
- **Lingkungan Teknologi (NFR-01):** Dibangun untuk sistem operasi mobile Android menggunakan komponen standar Kotlin dan Jetpack.
- **Kinerja (NFR-02):** MVVM digunakan agar UI tidak mengalami kondisi terkunci (freeze) saat pengambilan data asinkron dari API, menjaga waktu respons UI di bawah 2 detik.
- **Kebergunaan (NFR-04):** Antarmuka pengguna dirancang berdasarkan Material Design untuk Android yang intuitif. Pemisahan View dan ViewModel memungkinkan antarmuka yang tersinkronisasi mulus menggunakan stateflow.

## 5. Flow Sequence Diagram (Alur Eksekusi)
**Alur Pemanggilan File:**
`View` -> `ViewModel` -> `Repository` -> `Model`

**Penjelasan Alur Eksekusi:**
1. **View** mendeteksi aksi dari pengguna (misal: tombol cari buku diklik).
2. Aksi diteruskan ke **ViewModel**.
3. **ViewModel** memerintahkan **Repository** untuk memproses data.
4. **Repository** melakukan HTTP request ke API Backend.
5. Respons JSON dari server diubah oleh **Repository** menjadi objek **Model** (misal: `BookModel`).
6. Objek mentah ini dikembalikan ke **ViewModel**.
7. **ViewModel** mengolah data tersebut lalu memperbarui *state UI*.
8. **View** menangkap perubahan *state* secara reaktif dan menampilkannya ke layar.

**Batasan Arsitektur:**
- `View` hanya diizinkan berkomunikasi dengan `ViewModel`.
- `ViewModel` dilarang keras menghubungi API secara langsung dan wajib melewati `Repository`.
