package com.nexters.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nexters.layoutcodelab.ui.theme.LayoutCodelabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ScrollingList()
//			LayoutCodelabs()
//			PhotographerCard()
		}
	}
}

@Composable
fun SimpleList() {
	// Column list renders all items, even not visible on the screen --> BAD performance
	// Instead, use LazyColumn
	val scrollState = rememberScrollState()

	Column(Modifier.verticalScroll(scrollState)) {
		repeat(100) {
			Text("Item #$it")
		}
	}
}

@Composable
fun LazyList() {
	val scrollState = rememberLazyListState()

	LazyColumn(state = scrollState, modifier = Modifier.width(320.dp)) {
		items(100) {
			Text("Item #$it")
		}
	}
}

@ExperimentalCoilApi
@Composable
fun ImageList() {
	val scrollState = rememberLazyListState()

	LazyColumn(state = scrollState) {
		items(100) {
			ImageListItem(index = it)
		}
	}
}

@ExperimentalCoilApi
@Composable
fun ImageListItem(index: Int) {
	Row(verticalAlignment = CenterVertically) {
		Image(
			painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
			contentDescription = "Android Logo",
			modifier = Modifier.size(50.dp)
		)
		Spacer(modifier = Modifier.width(10.dp))
		Text("Item #$index", style = MaterialTheme.typography.subtitle1)
	}
}

@ExperimentalCoilApi
@Composable
fun ScrollingList() {
	val listSize = 100
	val scrollState = rememberLazyListState()
	val coroutineScope = rememberCoroutineScope()

	Column {
		Row {
			Button(onClick = {
				coroutineScope.launch {
					scrollState.animateScrollToItem(0)
				}
			}) {
				Text("Scroll To Top")
			}

			Button(onClick = {
				coroutineScope.launch {
					scrollState.animateScrollToItem(listSize - 1)
				}
			}) {
				Text("Scroll To Bottom")
			}
		}

		LazyColumn(state = scrollState) {
			items(listSize) {
				ImageListItem(index = it)
			}
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
		LazyList()
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
