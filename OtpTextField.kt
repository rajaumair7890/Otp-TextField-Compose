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
			(0..maxOtpDigits).forEach { index ->
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
