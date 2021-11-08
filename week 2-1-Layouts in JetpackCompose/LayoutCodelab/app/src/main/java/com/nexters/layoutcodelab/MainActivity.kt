package com.nexters.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.layoutcodelab.ui.theme.LayoutCodelabTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			LayoutCodelabs()
//			PhotographerCard()
		}
	}
}

@Composable
fun LayoutCodelabs() {
	Scaffold(
		topBar = {
			TopContent()
		},
		bottomBar = {
			BottomContent()
		}
	) { innerPadding ->
		// can chain extra padding
		// Modifier.padding(innerPadding).padding(8.dp)
		BodyContent(Modifier.padding(innerPadding))
	}
}

@Composable
fun TopContent() {
	TopAppBar(
		title = {
			Text(text = "LayoutCodelab")
		},
		actions = {
			IconButton(onClick = { }) {
				// predefined material icons
				// https://fonts.google.com/icons?selected=Material+Icons
				Icon(Icons.Filled.Favorite, contentDescription = null)
			}
		}
	)
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
	// if we want to put extra padding, we can chain .padding() to parameter
	// ex) modifier.padding(8.dp)
	Column(modifier = modifier) {
		Text(text = "Hi there~!")
		Text(text = "Thanks for going through the Layout codelab")
	}
}

@Composable
fun BottomContent() {
	val menus = listOf(Pair(Icons.Filled.Coffee, "coffee"), Pair(Icons.Filled.Light, "light"), Pair(Icons.Filled.Dining, "dining"), Pair(Icons.Filled.Flatware, "flatware"))

	BottomNavigation {
		menus.forEach { (icon, title) ->
			bottomMenu(icons = icon, title = title)
		}
	}

//	BottomAppBar(
//		content = {
//			IconToggleButton(checked = true, onCheckedChange = {}) {
//				Icon(Icons.Filled.ToggleOn, contentDescription = null)
//			}
//			Text("bottom", modifier = Modifier.padding(start = 5.dp))
//		}
//	)
}

@Composable
fun RowScope.bottomMenu(icons: ImageVector, title: String) {
	Column(
		Modifier
			.padding(start = 10.dp, end = 10.dp)
			.align(CenterVertically)) {
		Icon(icons, contentDescription = null, modifier = Modifier.align(CenterHorizontally))
		Text(title)
	}
}

@Preview
@Composable
fun LayoutCodelabsPreview() {
	LayoutCodelabTheme {
		LayoutCodelabs()
	}
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) { // empty modifier do nothing
	Row(
		// order of chained modifiers matters with final result
		modifier
			.padding(8.dp)
			.clip(RoundedCornerShape(4.dp))
			.background(MaterialTheme.colors.surface)
			.clickable { }
			.padding(16.dp)) {
		Surface(
			modifier = Modifier.size(50.dp),
			shape = CircleShape,
			color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f) // copy : Copies the existing color, changing only the provided values
		) {

		}

		Column(
			// Type safety of scope-specific modifiers help you to discover and understand
			// what is available and applicable to a certain layout - if not, will cause compile-error
			modifier = Modifier
				.padding(start = 8.dp)
				.align(Alignment.CenterVertically)
		) {
			Text("Alfred Sisley", fontWeight = FontWeight.Bold)
			/** LocalContentAlpha
			 *   : define opacity level of its children
			 */
			CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
				Text("3 minutes ago", style = MaterialTheme.typography.body2)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
	LayoutCodelabTheme {
		PhotographerCard()
	}
}
