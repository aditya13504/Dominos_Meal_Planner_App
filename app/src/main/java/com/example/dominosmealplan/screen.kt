package com.example.dominosmealplan

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.roundToInt

val GST_RATE = 0.18 // 18% GST
val DominoRed = Color(0xFFE31837) // Domino's Red
val DominoBlue = Color(0xFF0055A5) // Domino's Blue
val DominoWhite = Color(0xFFFFFFFF) // White
val DominoBlack = Color(0xFF000000) // Black
val DominoDarkGray = Color(0xFF333333) // Dark Gray
val LightGrey = Color(0xFFEBEBEB) // Lightgreycolor


data class MenuItem(
    val name: String,
    val price: Int,
    val category: String // "veg" or "non-veg"
)

val dominoMenu = listOf(
    // Pizzas
    MenuItem("Margherita", 200, "veg"),
    MenuItem("Pepperoni", 250, "non-veg"),
    MenuItem("Veggie Paradise", 220, "veg"),
    MenuItem("Cheese Burst", 300, "veg"),
    MenuItem("Farmhouse", 280, "veg"),
    MenuItem("Chicken Dominator", 350, "non-veg"),
    MenuItem("Paneer Makhani", 270, "veg"),
    MenuItem("Spicy Chicken", 320, "non-veg"),
    MenuItem("Tandoori Veg", 240, "veg"),
    MenuItem("Tandoori Chicken", 330, "non-veg"),
    MenuItem("Double Cheese Margherita", 310, "veg"),
    MenuItem("Mexican Green Wave", 290, "veg"),
    MenuItem("Chicken Golden Delight", 249, "non-veg"),
    MenuItem("Non-Veg Supreme", 319, "non-veg"),
    MenuItem("Veg Extravaganza", 260, "veg"),
    MenuItem("Pepper Barbecue Chicken & Onion", 229, "non-veg"),
    MenuItem("Chicken Sausage", 189, "non-veg"),
    MenuItem("Chicken Pepperoni", 319, "non-veg"),
    MenuItem("Chicken Fiesta", 249, "non-veg"),
    MenuItem("Indi Chicken Tikka", 319, "non-veg"),
    MenuItem("Keema Do Pyaza", 189, "non-veg"),

    // Sides
    MenuItem("Garlic Breadsticks", 100, "veg"),
    MenuItem("Stuffed Garlic Bread", 150, "veg"),
    MenuItem("Paneer Zingy Parcel", 120, "veg"),
    MenuItem("Chicken Wings", 180, "non-veg"),
    MenuItem("Potato Wedges", 90, "veg"),
    MenuItem("Chicken Pepperoni Stuffed Garlic Bread", 200, "non-veg"),
    MenuItem("Veg Pasta Italiano White", 130, "veg"),
    MenuItem("Non-Veg Pasta Italiano White", 160, "non-veg"),
    MenuItem("Veg Pasta Italiano Red", 130, "veg"),
    MenuItem("Non-Veg Pasta Italiano Red", 160, "non-veg"),

    // Desserts (all veg)
    MenuItem("Choco Lava Cake", 110, "veg"),
    MenuItem("Butterscotch Mousse Cake", 140, "veg"),
    MenuItem("New York Cheesecake", 170, "veg"),
    MenuItem("Dark Fantasy", 120, "veg"),
    MenuItem("Chocolate Brownie", 100, "veg"),
    MenuItem("Vanilla Ice Cream", 80, "veg"),
    MenuItem("Strawberry Ice Cream", 80, "veg"),
    MenuItem("Chocolate Ice Cream", 80, "veg"),

    // Beverages (all veg)
    MenuItem("Pepsi 500ml", 60, "veg"),
    MenuItem("Mirinda 500ml", 60, "veg"),
    MenuItem("7Up 500ml", 60, "veg"),
    MenuItem("Mountain Dew 500ml", 60, "veg"),
    MenuItem("Water Bottle 1L", 40, "veg"),
    MenuItem("Iced Tea", 70, "veg"),
    MenuItem("Cold Coffee", 90, "veg"),
    MenuItem("Orange Juice", 80, "veg"),
    MenuItem("Mango Juice", 80, "veg")
)

@Composable
fun DominosApp() {
    var budget by remember { mutableStateOf("") }
    var isVegOnly by remember { mutableStateOf(false) }
    val filteredMenu = if (isVegOnly) {
        dominoMenu.filter { it.category == "veg" }
    } else {
        dominoMenu
    }
    val combinations = remember(budget, isVegOnly) {
        calculateCombinations(
            userBudget = budget.toIntOrNull() ?: 0,
            menu = filteredMenu
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        Column(
            modifier = Modifier
                .padding(25.dp),
            horizontalAlignment = Alignment.End
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            // Header Row with Filter Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Veg Filter Button
                FilterChip(
                    selected = isVegOnly,
                    onClick = { isVegOnly = !isVegOnly },
                    label = { Text("Veg Only") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DominoBlue,
                        selectedLabelColor = Color.LightGray
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(35.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Domino's Meal Planner",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DominoBlue
            )
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Domino's Logo",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Enter your budget", color = DominoDarkGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DominoWhite,
                    unfocusedContainerColor = DominoWhite,
                    focusedTextColor = DominoBlack,
                    unfocusedTextColor = DominoBlack,
                    focusedIndicatorColor = DominoBlue,
                    unfocusedIndicatorColor = DominoRed
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                items(combinations) { combination ->
                    CombinationCard(combination)
                }
            }
        }
    }
}

fun calculateCombinations(
    userBudget: Int,
    menu: List<MenuItem>
): List<List<MenuItem>> {
    val validCombinations = mutableListOf<List<MenuItem>>()
    if (userBudget <= 0) return validCombinations

    val effectiveSubtotal = (userBudget / (1 + GST_RATE)).roundToInt()

    menu.forEachIndexed { i, _ ->
        val combination = mutableListOf<MenuItem>()
        var currentTotal = 0

        for (j in i until menu.size) {
            val item = menu[j]
            if (currentTotal + item.price <= effectiveSubtotal) {
                combination.add(item)
                currentTotal += item.price
                validCombinations.add(combination.toList())
            } else {
                break
            }
        }
    }

    return validCombinations.distinct()
}

@Composable
fun CombinationCard(combination: List<MenuItem>) {
    val subtotal = combination.sumOf { it.price }
    val gst = (subtotal * GST_RATE).roundToInt()
    val grandTotal = subtotal + gst

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = DominoWhite),
        border = BorderStroke(1.dp, DominoBlue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                combination.forEach { item ->
                    Text(
                        text = "${item.name} - ₹${item.price}",
                        fontSize = 16.sp,
                        color = DominoDarkGray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Subtotal: ₹$subtotal",
                    fontSize = 16.sp,
                    color = DominoDarkGray
                )
                Text(
                    text = "GST (${(GST_RATE * 100).toInt()}%): ₹$gst",
                    fontSize = 16.sp,
                    color = DominoDarkGray
                )
                Text(
                    text = "Total: ₹$grandTotal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DominoBlue
                )
            }
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Domino's Logo",
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}