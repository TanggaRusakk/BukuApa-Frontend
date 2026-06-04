package com.example.bukuapa_frontend.ui.views.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.data.models.Review
import com.example.bukuapa_frontend.data.repositories.ReviewRepository
import com.example.bukuapa_frontend.ui.viewmodels.book.BookDetailViewModel
import com.example.bukuapa_frontend.ui.views.components.TopNavigatorBar
import com.example.bukuapa_frontend.ui.views.review.AddReviewDialog
import com.example.bukuapa_frontend.ui.views.review.EditReviewDialog
import com.example.bukuapa_frontend.ui.views.review.ReviewView
import com.example.bukuapa_frontend.utils.TokenManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailView(
    bookId: Int,
    onBackClick: () -> Unit,
    viewModel: BookDetailViewModel = viewModel()
) {
    val book by viewModel.book.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current
    val reviewService = remember { ReviewRepository(TokenManager(context)) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showReviewDialog by remember { mutableStateOf(false) }
    var editingReview by remember { mutableStateOf<Review?>(null) }
    var reviewToDelete by remember { mutableStateOf<Review?>(null) }
    var refreshKey by remember { mutableStateOf(0) }

    LaunchedEffect(bookId) {
        viewModel.fetchBookDetail(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Buku", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error!!, color = Color.Red)
            }
        } else {
            book?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .width(180.dp)
                            .aspectRatio(0.7f)
                    ) {
                        // Placeholder Cover Buku
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 2. JUDUL & PENULIS ---
                    Text(
                        text = data.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = data.author,
                        fontSize = 16.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 3. STATS (Rating, Stok, Tahun) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "Rating", value = if(data.rating > 0) "%.1f".format(data.rating) else "4.8", icon = Icons.Default.Star)
                        StatItem(label = "Stok", value = data.stock.toString())
                        StatItem(label = "Tahun", value = data.publishedYear.toString())
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- 4. DESKRIPSI ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Sinopsis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ISBN: ${data.isbn}\n\nPenerbit: ${data.publisher}\nTotal Halaman: ${data.totalPages}\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                            fontSize = 14.sp,
                            color = Color(0xFF475569),
                            lineHeight = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Ulasan Pengguna",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.height(600.dp)) {
                        key(refreshKey) {
                            ReviewView(
                                bookId = bookId,
                                service = reviewService,
                                onShowSnackbar = { message ->
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                },
                                onWriteReview = { showReviewDialog = true },
                                onEditReview = { editingReview = it },
                                onDeleteReview = { reviewToDelete = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
        
        if (showReviewDialog) {
            AddReviewDialog(
                bookId = bookId,
                service = reviewService,
                onDismiss = { showReviewDialog = false },
                onSuccess = {
                    showReviewDialog = false
                    refreshKey++
                    scope.launch { snackbarHostState.showSnackbar("Ulasan berhasil ditambahkan") }
                }
            )
        }

        if (editingReview != null) {
            EditReviewDialog(
                review = editingReview!!,
                service = reviewService,
                onDismiss = { editingReview = null },
                onSuccess = {
                    editingReview = null
                    refreshKey++
                    scope.launch { snackbarHostState.showSnackbar("Ulasan berhasil diperbarui") }
                }
            )
        }

        if (reviewToDelete != null) {
            AlertDialog(
                onDismissRequest = { reviewToDelete = null },
                title = { Text("Hapus Ulasan") },
                text = { Text("Apakah Anda yakin ingin menghapus ulasan ini?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val id = reviewToDelete!!.id
                            reviewToDelete = null
                            scope.launch {
                                val result = reviewService.deleteReview(id)
                                if (result.isSuccess) {
                                    refreshKey++
                                    snackbarHostState.showSnackbar("Ulasan berhasil dihapus")
                                } else {
                                    snackbarHostState.showSnackbar("Gagal menghapus ulasan")
                                }
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { reviewToDelete = null }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = Color(0xFFFFB900), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
        }
        Text(label, fontSize = 12.sp, color = Color(0xFF94A3B8))
    }
}
