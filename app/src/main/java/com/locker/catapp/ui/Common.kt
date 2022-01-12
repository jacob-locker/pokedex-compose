package com.locker.catapp.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp

@Composable
fun <T> GridView(
    cols: Int = 1,
    list: List<T>,
    rowModifier: Modifier = Modifier,
    colModifier: Modifier = Modifier,
    child: @Composable (dataModal: T) -> Unit
) {

    val rows = (list.size / cols) + (if (list.size % cols > 0) 1 else 0)

    Column(modifier = colModifier) {

        for (r in 0 until rows) {
            Row(modifier = rowModifier, horizontalArrangement = Arrangement.SpaceAround) {
                for (cell in 0 until cols) {
                    val i = (r * cols) + cell
                    if (i < list.size) { child(list[i]) } else { break }
                }
            }
        }
    }

}
@Composable
fun GradientCard(
    text: String,
    textColor: Color,
    gradient: Brush,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        onClick = { onClick() })
    {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = textColor)
        }
    }
}

//@Composable
//fun <T> LazyGridFor(
//    items: List<T>,
//    rowSize: Int = 1,
//    itemContent: @Composable BoxScope.(T) -> Unit,
//) {
//    val rows = items.chunked(rowSize)
//    LazyColumnFor(rows) { row ->
//        Row(Modifier.fillParentMaxWidth()) {
//            for ((index, item) in row.withIndex()) {
//                Box(Modifier.fillMaxWidth(1f / (rowSize - index))) {
//                    itemContent(item)
//                }
//            }
//        }
//    }
//}