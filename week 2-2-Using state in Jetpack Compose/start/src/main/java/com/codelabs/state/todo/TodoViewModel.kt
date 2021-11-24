/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {
	// use ViewModel to hoist the state from TodoScreen - to achieve "unidirectional data flow" design

	private var currentEditPosition by mutableStateOf(-1)

	// state
	var todoItems = mutableStateListOf<TodoItem>()
		private set // restricting writes to this state object to a private setter only visible in viewModel

	// state : TodoItems
	val currentEditItem: TodoItem?
		get() = todoItems.getOrNull(currentEditPosition)
	// whenever composable calls currentEditItem, it will observe changes to both todoItems & currentEditPosition to get new value

	// event : add item
	fun addItem(item: TodoItem) {
		todoItems.add(item)
	}

	// event : remove item
	fun removeItem(item: TodoItem) {
		todoItems.remove(item)
		onEditDone() // close keep editor
	}

	// event : onEditItemSelected
	fun onEditItemSelected(item: TodoItem) {
		currentEditPosition = todoItems.indexOf(item)
	}

	// event : onEditDone
	fun onEditDone() {
		currentEditPosition = -1
	}

	// event : onEditItemChange
	fun onEditItemChange(item: TodoItem) {
		val currentItem = requireNotNull(currentEditItem) // throw illegal if item is null
		require(currentItem.id == item.id) { // require(value) { message } : if value is false, throw illegal and show message
			"You can only change an item withe the same id as currentEditItem"
		}
		todoItems[currentEditPosition] = item
	}
}
