package com.codingWithUmair.otpTextField

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpTextField(
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	@IntRange(from = 4, to = 6)
	maxOtpDigits: Int = 6,
	accentColor: Color = MaterialTheme.colorScheme.primary,
	textColor: Color = MaterialTheme.colorScheme.onSurface,
	style: OtpTextFieldStyle = OtpTextFieldStyle.BORDERED_BOX,
	keyboardOptions: KeyboardOptions = KeyboardOptions(
		keyboardType = KeyboardType.NumberPassword
	),
	keyboardActions: KeyboardActions = KeyboardActions.Default
) {
	val customTextSelectionColors = remember {
		TextSelectionColors(
			handleColor = Color.Transparent,
			backgroundColor = Color.Transparent
		)
	}
	CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
		BasicTextField(
			value = value,
			onValueChange = { newValue ->
				if (newValue.all { char -> char.isDigit() }) {
					onValueChange(newValue.take(maxOtpDigits))
				}
			},
			decorationBox = { innerTextField ->
				OtpTextFieldDecorationBox(
					value = value,
					innerTextField = innerTextField,
					maxOtpDigits = maxOtpDigits,
					style = style,
					accentColor = accentColor,
					textColor = textColor
				)
			},
			textStyle = TextStyle(color = Color.Transparent),
			cursorBrush = SolidColor(Color.Unspecified),
			keyboardOptions = keyboardOptions,
			keyboardActions = keyboardActions,
			modifier = modifier
		)
	}
}

@Composable
private fun OtpTextFieldDecorationBox(
	value: String,
	style: OtpTextFieldStyle,
	maxOtpDigits: Int,
	accentColor: Color,
	textColor: Color,
	modifier: Modifier = Modifier,
	innerTextField: @Composable () -> Unit,
) {
	Box(modifier = modifier) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
		){
			(0..< maxOtpDigits).forEach { index ->
				OtpTextFieldItem(
					value = (value.getOrNull(index) ?: "").toString(),
					style = style,
					accentColor = accentColor,
					textColor = textColor
				)
			}
		}
		innerTextField()
	}
}

@Composable
private fun OtpTextFieldItem(
	value: String,
	style: OtpTextFieldStyle,
	accentColor: Color,
	textColor: Color,
	modifier: Modifier = Modifier
) {
	val content = @Composable {
		Text(
			text = value,
			fontSize = 24.sp,
			fontWeight = FontWeight.Bold,
			color = textColor
		)
	}
	when(style){
		OtpTextFieldStyle.BORDERED_BOX -> BoxStyleItem(color = accentColor, modifier = modifier, content = content)
		OtpTextFieldStyle.UNDERLINE -> UnderlineStyleItem(color = accentColor, modifier = modifier, content = content)
	}
}

@Composable
private fun BoxStyleItem(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
){
	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.size(48.dp)
			.clip(RoundedCornerShape(8.dp))
			.border(2.dp, color, RoundedCornerShape(8.dp))
	){ content() }
}

@Composable
private fun UnderlineStyleItem(
	color: Color,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.size(48.dp)
	){
		content()
		Box(
			modifier = Modifier
				.width(48.dp)
				.height(2.dp)
				.background(color)
				.align(Alignment.BottomCenter)
		)
	}
}

enum class OtpTextFieldStyle{
	BORDERED_BOX, UNDERLINE
}
