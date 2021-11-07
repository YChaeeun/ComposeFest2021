package com.nexters.basiccodelab2

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
			Text("Welcome to the Basics Codelab!!", style = MaterialTheme.typography.h4)
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
	/** remember
	 * 	: preserve state across recompositions
	 * 	: only as long as composable is kept in the Composition (lost state when rotate - when activity restarts, all state is lost)
	 *
	 ** rememberSaveable
	 *   : state surviving configuration changes (ex. rotate, switch to Dark Theme)
	 * 				   and process death (ex. kill process)
	 * */

	// by : property delegate saves from writing `.value` every time
	var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

	if (shouldShowOnboarding) {
		OnboardingScreen {
			shouldShowOnboarding = false // passing callbacks down
		}
	} else {
		Greetings()
	}
}

@Composable
fun Greetings(names: List<String> = List(1000) { "$it" }) {
	// like RecyclerView
	LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
		// import androidx.compose.foundation.lazy.items
		items(items = names) { name ->
			Greeting(name) // emits new Composable as you scroll (vs RecyclerView recycles children)
			// emitting is cheaper than instantiating Android view
		}
	}
}

@Composable
fun Greeting(name: String) {
	val expanded = remember { mutableStateOf(false) }

	/** animate*AsState
	 *   : able to interrupted - if target value changes, restart animation & points to new value
	 * */
	val extraPadding by animateDpAsState(
		targetValue = if (expanded.value) 48.dp else 0.dp,
		animationSpec = spring( // make animation natural
			dampingRatio = Spring.DampingRatioMediumBouncy,
			stiffness = Spring.StiffnessLow
		)
	)

	Surface(
		color = MaterialTheme.colors.primary,
		modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
	) {
		Row(modifier = Modifier.padding(24.dp)) {
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.padding(bottom = extraPadding.coerceAtLeast(0.dp))) {
				Text(text = "Hello, ")
				Text(
					text = name,
					style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.ExtraBold))
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

// multiple preview for themes
@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "DefaultPreviewDark")
@Preview(showBackground = true, name = "preview", widthDp = 320, heightDp = 320)
@Composable
fun DefaultPreview() {
	BasicCodelab2Theme {
		Greetings()
	}
}