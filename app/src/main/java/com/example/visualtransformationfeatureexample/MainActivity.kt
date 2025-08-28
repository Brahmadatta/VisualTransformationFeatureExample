package com.example.visualtransformationfeatureexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.visualtransformationfeatureexample.ui.theme.VisualTransformationFeatureExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VisualTransformationFeatureExampleTheme {
                SampleVisualTransformation()
            }
        }
    }
}

object HideDigitsVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += "-"
        }

        // Custom OffsetMapping to handle hyphens
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Every 4 digits before 'offset' will add a hyphen (except after 16th digit)
                val groups = if (offset == 0) 0 else (offset - 1) / 4
                return offset + groups
            }

            override fun transformedToOriginal(offset: Int): Int {
                // For a given transformed index, count digits before that position to get the original index.
                var digitCount = 0
                var strIndex = 0
                while (strIndex < out.length && strIndex < offset) {
                    if (out[strIndex] != '-') digitCount++
                    strIndex++
                }
                return digitCount
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

@Composable
@Preview
fun SampleVisualTransformation(){
    val (number,setNumber) = remember { mutableStateOf("1234567890123456") }
    Column {
        // TextField without visual transformation, showing original input
        // Limited to 16 digits max
        TextField(
            value = number,
            onValueChange = { 
                if (it.length <= 16) {
                    setNumber(it)
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        TextField(
            value = number,
            onValueChange = setNumber,
            visualTransformation = HideDigitsVisualTransformation,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
    }
}