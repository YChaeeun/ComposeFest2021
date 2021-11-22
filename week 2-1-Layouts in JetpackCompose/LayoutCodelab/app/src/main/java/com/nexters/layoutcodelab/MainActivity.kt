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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nexters.layoutcodelab.ui.theme.LayoutCodelabTheme
import kotlinx.coroutines.launch
import kotlin.math.max

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			BodyContent_StaggeredGrid()
//			BodyContent_My()
//			ScrollingList()
//			LayoutCodelabs()
//			PhotographerCard()
		}
	}
}

@Preview(showBackground = true)
@Composable
fun TwoTextsPreview() {
	LayoutCodelabTheme {
		Surface {
			TwoTexts(text1 = "Hii", text2 = "Hello")
		}
	}
}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
	// 	Intrinsics lets you query children before they're actually measured
	//   ex) minIntrinsicHeight of Text - height of the Text as if the text was drawn in a single line

	// Row's minIntrinsicHeight will be the maximum minIntrinsicHeight of its children.
	// IntrinsicSize.Min - sizes children being force to be as tall as their minimum intrinsic height
	Row(modifier = modifier.height(IntrinsicSize.Min)) {
		Text(
			modifier = Modifier
				.weight(1f)
				.padding(start = 4.dp)
				.wrapContentWidth(Alignment.Start),
			text = text1
		)

		Divider(
			color = Color.Black, modifier = Modifier
				.fillMaxHeight()
				.width(1.dp))

		Text(
			modifier = Modifier
				.weight(1f)
				.padding(end = 4.dp)
				.wrapContentWidth(Alignment.End),
			text = text2
		)
	}
}

@Preview(showBackground = true)
@Composable
fun ConstraintLayoutContent_Preview() {
	LayoutCodelabTheme {
		DecoupledConstraintLayout()
//		LargeConstraintLayout()
//		ConstraintLayoutContent()
	}
}

@Composable
fun DecoupledConstraintLayout() {
	BoxWithConstraints {
		val constraints = if (maxWidth < maxHeight) {
			decoupledConstraints(margin = 16.dp) // portrait
		} else {
			decoupledConstraints(margin = 32.dp) // landscape
		}

		// pass ConstraintSet parameter & assign references using layoutId
		ConstraintLayout(constraints) {
			Button(
				onClick = {},
				modifier = Modifier.layoutId("button")
			) {
				Text("Button")
			}

			Text("text", Modifier.layoutId("text"))
		}
	}
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
	return ConstraintSet {
		val button = createRefFor("button")
		val text = createRefFor("text")

		constrain(button) {
			top.linkTo(parent.top, margin)
		}

		constrain(text) {
			top.linkTo(button.bottom, margin)
		}
	}
}

@Composable
fun LargeConstraintLayout() {
	ConstraintLayout {
		val text = createRef()

		val guideline = createGuidelineFromStart(fraction = 0.5f)
		Text(
			"This is a very very very very very very very very very long text",
			Modifier.constrainAs(text) {
				linkTo(start = guideline, end = parent.end)
				width = Dimension.preferredWrapContent.atLeast(100.dp)
				/** Dimension
				 *  - preferredWrapContent : wrap content, subject to the constraints in that dimension
				 *  - wrapContent : wrap content, even if the constraints would not allow it
				 *  - fillToConstraints : expand to fill the space defined by its constraints in that dimension
				 *  - preferredValue : fixed dp value, subject to the constraints in that dimension
				 *  - value : fixed dp value, regardless of constraints in that dimension
				 */
			}
		)
	}
}

@Composable
fun ConstraintLayoutContent() {
	ConstraintLayout {
		// Create reference for the composable to ConstraintLayout's body
		val (button1, button2, text) = createRefs()

		Button(
			onClick = {},
			modifier = Modifier.constrainAs(button1) {
				top.linkTo(parent.top, margin = 16.dp)
			}
		) {
			Text("Button 1")
		}

		// barriers (and all the other helpers) can be created in the body of ConstraintLayout, not inside constrainAs
		val barrier = createEndBarrier(button1, text)
		Button(
			onClick = {},
			modifier = Modifier.constrainAs(button2) {
				top.linkTo(parent.top, margin = 16.dp)
				start.linkTo(barrier)
			}
		) {
			Text("Button 2")
		}

		Text("Text", Modifier.constrainAs(text) {
			top.linkTo(button1.bottom, margin = 16.dp)
			centerAround(button1.end) // linkTo(anchor, anchor)
		})
	}
}

@Stable
fun Modifier.padding(all: Dp) =
	this.then(
		PaddingModifier(start = all, top = all, end = all, bottom = all, rtlAware = true)
	)

private class PaddingModifier(
	val start: Dp = 0.dp,
	val top: Dp = 0.dp,
	val end: Dp = 0.dp,
	val bottom: Dp = 0.dp,
	val rtlAware: Boolean
) : LayoutModifier {
	override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
		val horizontal = start.roundToPx() + end.roundToPx()
		val vertical = top.roundToPx() + bottom.roundToPx()

		val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

		val width = constraints.constrainWidth(placeable.width + horizontal)   // width of child + horizontal padding value
		val height = constraints.constrainHeight(placeable.height + vertical) // height of child + vertical padding value

		return layout(width, height) {
			if (rtlAware) {
				placeable.placeRelative(start.roundToPx(), top.roundToPx())
			} else {
				placeable.place(start.roundToPx(), top.roundToPx())
			}
		}
	}
}

val topics = listOf(
	"Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
	"Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
	"Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Preview
@Composable
fun BodyContent_StaggeredGrid_Preview() {
	LayoutCodelabTheme {
		BodyContent_StaggeredGrid()
	}
}

@Composable
fun BodyContent_StaggeredGrid(modifier: Modifier = Modifier) {
	/**  modifier chains,,,
	 * 	- update the constraints : from left to right ---->
	 * 	- return back the size   : from right to left <----
	 */

	/** ex) .size(200.dp).padding(16.dp)
	 * 		- return back size : Row (200 - 16 - 16) = 168
	 * 	    // width for parent : 200
	 * 		// width for child : 168.dp _ size reduced to 200, then 16 * 2 gone for padding value
	 *
	 *  ex) .padding(16.dp).size(200.dp)
	 *      - return back size : Row (16 + 16 + 200) = 232.dp
	 *      // width for parent : 232.dp
	 *      // width for child : 200.dp _ size 200 for staggered grid
	 */

	Row(
		modifier = modifier
			.background(color = Color.Gray)
			.padding(16.dp)
			.size(200.dp)
			.horizontalScroll(rememberScrollState())) {
		StaggeredGrid(modifier = modifier, rows = 5) {
			topics.forEach { topic ->
				Chip(modifier = Modifier.padding(10.dp), text = topic)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun ChipPreview() {
	LayoutCodelabTheme {
		Chip(modifier = Modifier.padding(10.dp), text = "Hi there")
	}
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
	Card(modifier = modifier, border = BorderStroke(color = Color.Black, width = Dp.Hairline), shape = RoundedCornerShape(8.dp)) {
		Row(
			modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.size(16.dp, 16.dp)
					.background(color = MaterialTheme.colors.secondary)
			)
			Spacer(modifier = Modifier.width(5.dp))
			Text(text = text)
		}
	}
}

@Composable
fun StaggeredGrid(modifier: Modifier = Modifier, rows: Int = 3, content: @Composable () -> Unit) {
	Layout(modifier = modifier, content = content) { measurables, constraints ->
		val rowWidths = IntArray(rows) { 0 }
		val rowHeights = IntArray(rows) { 0 }

		// list of measured children
		val placeables = measurables.mapIndexed { index, measurable ->
			val placeable = measurable.measure(constraints)

			// track the width & height of each row
			val row = index % rows
			rowWidths[row] += placeable.width
			rowHeights[row] = max(rowHeights[row], placeable.height)

			placeable
		}

		// Grid's width == widest row
		val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth
		// Grid's  height == sum of tallest element of each row
		val height = rowHeights.sumOf { it }.coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

		// Y of each row - accumulation of previous rows
		val rowY = IntArray(rows) { 0 }
		for (i in 1 until rows) {
			rowY[i] = rowY[i - 1] + rowHeights[i - 1]
		}

		// place children
		// set the size of parent layout
		layout(width, height) {
			val rowX = IntArray(rows) { 0 }

			placeables.forEachIndexed { i, placeable ->
				val row = i % rows
				placeable.placeRelative(
					x = rowX[row],
					y = rowY[row]
				)
				rowX[row] += placeable.width
			}
		}
	}
}

@Composable
fun MyOwnColumn(
	modifier: Modifier = Modifier,
	// add custom layout attributes
	content: @Composable () -> Unit
) {
	Layout(
		modifier = modifier,
		content = content
	) { measurables, constraints ->

		// List of measured children
		val placeables = measurables.map { measurable ->
			// measure each child
			measurable.measure(constraints)
		}

		// track the y co-ord we have placed children up to
		var positionY = 0

		// set layout as big as possible
		layout(constraints.maxWidth, constraints.maxHeight) {
			// place children
			placeables.forEach { placeable ->
				// position item on the screen
				placeable.placeRelative(x = 0, y = positionY)
				positionY += placeable.height
			}
		}
	}
}

@Preview
@Composable
fun BodyContent_My(modifier: Modifier = Modifier) {
	MyOwnColumn(modifier.padding(8.dp)) {
		Text("MyOwnColumn")
		Text("places items")
		Text("vertically.")
		Text("We've done it by hand~")
	}
}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
	LayoutCodelabTheme {
		Text("Hi - with firstBaselineToTop", modifier = Modifier.firstBaselineToTop(32.dp))
		Text("Hi - with padding", modifier = Modifier.padding(32.dp))
	}
}

/**
 * measurable : child to be measured and placed
 * constraints : minimum and maximum for the width and height of the child
 */
fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) = this.then(
	layout { measurable, constraints ->
		val placeable = measurable.measure(constraints)

		// check the composable has a first baseline
		check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
		val firstBaseline = placeable[FirstBaseline]

		// height of the composable with padding - first baseline
		val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
		val height = placeable.height + placeableY

		layout(placeable.width, height) {
			// where composable gets placed
			placeable.placeRelative(0, placeableY)
		}
	}
)

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
