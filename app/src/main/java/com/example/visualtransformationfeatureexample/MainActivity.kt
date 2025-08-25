package com.example.visualtransformationfeatureexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    private val regex = Regex("[0-9]")
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.replace(regex,"*")
        return TransformedText(AnnotatedString(trimmed), offsetMapping = OffsetMapping.Identity)
    }
}

@Composable
@Preview
fun SampleVisualTransformation(){
    val (number,setNumber) = remember { mutableStateOf("1234-5678-9012-3456") }
    Column {
        TextField(
            value = number,
            onValueChange = setNumber,
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        )

        TextField(
            value = number,
            onValueChange = setNumber,
            visualTransformation = HideDigitsVisualTransformation,
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        )
    }
}