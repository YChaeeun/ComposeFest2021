package com.nexters.basiccodelab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.basiccodelab2.ui.theme.BasicCodelab2Theme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MyApp()
		}
	}
}

@Composable
fun OnboardingScreen(onClick: () -> Unit) {
	Surface {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text("Welcome to the Basics Codelab!!")
			Button(
				modifier = Modifier.padding(vertical = 24.dp),
				onClick = onClick
			) {
				Text("Continue")
			}
		}
	}
}

@Composable
private fun MyApp() {
	// remember : preserve state across recompositions
	// by : property delegate saves from writing `.value` every time
	var shouldShowOnboarding by remember { mutableStateOf(true) }

	if (shouldShowOnboarding) {
		OnboardingScreen {
			shouldShowOnboarding = false // passing callbacks down
		}
	} else {
		Greetings()
	}
}

@Composable
fun Greetings(names: List<String> = listOf("World", "Compose")) {
	Column {
		for (name in names) {
			Greeting(name = name)
		}
	}
}

@Composable
fun Greeting(name: String) {
	val expanded = remember { mutableStateOf(false) }
	val extraPadding = if (expanded.value) 48.dp else 0.dp

	Surface(
		color = MaterialTheme.colors.primary,
		modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
	) {
		Row(modifier = Modifier.padding(24.dp)) {
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.padding(bottom = extraPadding)) {
				Text(text = "Hello, ")
				Text(text = name)
			}
			OutlinedButton(
				onClick = { expanded.value = !expanded.value }
			) {
				Text(if (expanded.value) "Show less" else "Show more")
			}
		}
	}
}

// multiple Previews are allowed
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardPreview() {
	BasicCodelab2Theme {
		OnboardingScreen {} // empty - Do Nothing
	}
}

@Preview(showBackground = true, name = "preview", widthDp = 320, heightDp = 320)
@Composable
fun DefaultPreview() {
	BasicCodelab2Theme {
		Greetings()
	}
}