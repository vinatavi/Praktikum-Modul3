package com.example.pemesanantiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = Color(0xFFF3F4F6)) {
                    PemesananTiketScreen()
                }
            }
        }
    }
}

/**
 * Composable utama halaman Pemesanan Tiket.
 *
 * State yang digunakan:
 * - hargaTiket   : harga per tiket (tetap / tidak berubah oleh user)
 * - jumlahTiket  : jumlah tiket yang dipesan, diubah lewat tombol - dan +
 * - totalBayar   : diturunkan (derived) dari hargaTiket * jumlahTiket setiap recomposition,
 *                  sehingga selalu otomatis mengikuti perubahan jumlahTiket.
 */
@Composable
fun PemesananTiketScreen() {
    val hargaTiket = 25000

    // State utama: setiap kali diubah, Compose otomatis melakukan recomposition
    var jumlahTiket by remember { mutableStateOf(1) }

    // Total dihitung ulang otomatis setiap kali jumlahTiket berubah (derived state)
    val totalBayar = hargaTiket * jumlahTiket

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ---------- Header ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF3B82F6), Color(0xFF2563EB))
                    )
                )
                .padding(top = 48.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎫", fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Pemesanan Tiket",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Pesan tiket dengan mudah!",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ---------- Card: Harga Tiket ----------
            InfoCard {
                Text("Harga Tiket", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = formatRupiah(hargaTiket),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                )
                Text("per tiket", fontSize = 12.sp, color = Color.Gray)
            }

            // ---------- Card: Jumlah Tiket ----------
            InfoCard {
                Text("Jumlah Tiket", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BulatIconButton(symbol = "-") {
                        // Event: kurangi jumlah tiket, minimal 1
                        if (jumlahTiket > 1) jumlahTiket--
                    }
                    Text(
                        text = "$jumlahTiket",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(60.dp)
                    )
                    BulatIconButton(symbol = "+") {
                        // Event: tambah jumlah tiket
                        jumlahTiket++
                    }
                }
            }

            // ---------- Card: Total ----------
            InfoCard {
                Text("Total", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = formatRupiah(totalBayar),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF15803D)
                )
            }

            // ---------- Tombol Reset ----------
            Button(
                onClick = {
                    // Event: kembalikan state jumlahTiket ke kondisi awal
                    jumlahTiket = 1
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
            ) {
                Text("↺  RESET", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun BulatIconButton(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF3B82F6))
            .then(Modifier),
        contentAlignment = Alignment.Center
    ) {
        IconTextButton(symbol = symbol, onClick = onClick)
    }
}

@Composable
private fun IconTextButton(symbol: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
        Text(symbol, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

private fun formatRupiah(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return "Rp${formatter.format(value)}"
}

@Preview(showBackground = true)
@Composable
fun PemesananTiketPreview() {
    MaterialTheme {
        PemesananTiketScreen()
    }
}
