package com.example.bukuapa_frontend.ui.views.review

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukuapa_frontend.data.models.Review
import com.example.bukuapa_frontend.domain.protocols.ReviewServiceProtocol
import com.example.bukuapa_frontend.ui.viewmodels.review.ReviewViewModel

@Composable
fun ReviewView(
    bookId: Int,
    service: ReviewServiceProtocol,
    onShowSnackbar: (String) -> Unit = {},
    onWriteReview: () -> Unit = {}
) {
    val viewModel: ReviewViewModel = viewModel {
        ReviewViewModel(service)
    }

    val uiState by viewModel.uiState.collectAsState()
    val canReview by viewModel.canReview.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.loadReviews(bookId)
        viewModel.checkCanReview(bookId)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        when (val state = uiState) {
            is ReviewViewModel.ReviewUiState.Success -> {
                ReviewHeader(
                    averageRating = state.data.averageRating,
                    totalReviews = state.data.total,
                    canReview = canReview,
                    onWriteReview = onWriteReview
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.data.reviews, key = { it.id }) { review ->
                        ReviewItem(
                            review = review,
                            formattedDate = viewModel.formatDate(review.createdAt),
                            isOwner = false
                        )
                    }
                    if (state.data.pagination.page < state.data.pagination.totalPages) {
                        item {
                            Button(
                                onClick = {
                                    viewModel.loadReviews(
                                        bookId,
                                        state.data.pagination.page + 1,
                                        20
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Muat Lebih Banyak")
                            }
                        }
                    }
                }
            }
            is ReviewViewModel.ReviewUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ReviewViewModel.ReviewUiState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Belum ada ulasan untuk buku ini", fontSize = 16.sp)
                    if (canReview) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onWriteReview) {
                            Text("Tulis Ulasan Pertama")
                        }
                    }
                }
            }
            is ReviewViewModel.ReviewUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadReviews(bookId) }) {
                        Text("Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewHeader(
    averageRating: Double?,
    totalReviews: Int,
    canReview: Boolean,
    onWriteReview: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = averageRating?.let { "%.1f".format(it) } ?: "N/A",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$totalReviews ulasan",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (canReview) {
            ElevatedButton(onClick = onWriteReview) {
                Text("Tulis Ulasan")
            }
        }
    }
}

@Composable
private fun ReviewItem(
    review: Review,
    formattedDate: String,
    isOwner: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = review.user.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (index < review.rating)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.comment,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}