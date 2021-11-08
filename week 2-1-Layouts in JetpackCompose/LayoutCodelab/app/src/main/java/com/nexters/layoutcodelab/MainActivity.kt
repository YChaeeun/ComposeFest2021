package com.nexters.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.layoutcodelab.ui.theme.LayoutCodelabTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PhotographerCard()
		}
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
