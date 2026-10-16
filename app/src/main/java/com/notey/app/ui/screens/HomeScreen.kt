package com.notey.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notey.app.data.Category
import com.notey.app.data.Note
import com.notey.app.data.NoteColors
import com.notey.app.ui.components.GlassCard
import com.notey.app.ui.components.GradientBackground
import com.notey.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    notes: List<Note>,
    categories: List<Category>,
    currentTheme: ThemeMode,
    onAddNote: (String, String, Int, Int?) -> Unit, // Added categoryId
    onDeleteNote: (Note) -> Unit,
    onAddCategory: (String) -> Unit,
    onDeleteCategory: (Category) -> Unit, // Optional: for long press
    onSettingsClick: () -> Unit
) {
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) } // null = "All"
    
    // Theme-based colors
    val gradientColors = when (currentTheme) {
        ThemeMode.LIGHT -> GradientColors.lightGradient
        ThemeMode.DARK -> GradientColors.darkGradient
        ThemeMode.CYBERPUNK -> GradientColors.cyberpunkGradient
    }
    val textColor = when (currentTheme) {
        ThemeMode.LIGHT -> Color.Black
        ThemeMode.DARK -> Color.White
        ThemeMode.CYBERPUNK -> CyberpunkColors.OnBackground
    }
    val secondaryTextColor = when (currentTheme) {
        ThemeMode.LIGHT -> Color.Black.copy(alpha = 0.7f)
        ThemeMode.DARK -> Color.White.copy(alpha = 0.7f)
        ThemeMode.CYBERPUNK -> CyberpunkColors.Secondary
    }
    val accentColor = when (currentTheme) {
        ThemeMode.LIGHT -> LightColors.Primary
        ThemeMode.DARK -> DarkColors.Primary
        ThemeMode.CYBERPUNK -> CyberpunkColors.Primary
    }
    
    // Filter notes based on search query AND category
    val filteredNotes = remember(notes, searchQuery, selectedCategoryId) {
        notes.filter { note ->
            val matchesSearch = if (searchQuery.isBlank()) true else {
                note.title.contains(searchQuery, ignoreCase = true) ||
                note.content.contains(searchQuery, ignoreCase = true)
            }
            val matchesCategory = if (selectedCategoryId == null) true else {
                note.categoryId == selectedCategoryId
            }
            matchesSearch && matchesCategory
        }
    }
    
    GradientBackground(gradientColors = gradientColors) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                Column(horizontalAlignment = Alignment.End) {
                    FloatingActionButton(
                        onClick = { showAddCategoryDialog = true },
                        containerColor = accentColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Text("📁", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    FloatingActionButton(
                        onClick = { showAddNoteDialog = true },
                        containerColor = accentColor
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note")
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Header with Settings - Fixed Logic
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, "Settings", tint = textColor)
                    }
                    Text(
                        text = "Notey",
                        color = textColor,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search notes...", color = secondaryTextColor) },
                    leadingIcon = { Icon(Icons.Default.Search, "Search", tint = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textColor.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                // Categories List (Horizontal)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        CategoryChip(
                            name = "All",
                            isSelected = selectedCategoryId == null,
                            accentColor = accentColor,
                            textColor = textColor,
                            onClick = { selectedCategoryId = null }
                        )
                    }
                    items(categories, key = { it.id }) { category ->
                        CategoryChip(
                            name = category.name,
                            isSelected = selectedCategoryId == category.id,
                            accentColor = accentColor,
                            textColor = textColor,
                            onClick = { selectedCategoryId = category.id }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (filteredNotes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (searchQuery.isBlank() && notes.isEmpty()) "No notes yet.\nTap + to add one!"
                            else "No notes found.",
                            color = secondaryTextColor,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredNotes, key = { it.id }) { note ->
                            NoteCard(
                                note = note, 
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                accentColor = accentColor,
                                onDelete = { onDeleteNote(note) }
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (showAddNoteDialog) {
        AddNoteDialog(
            categories = categories,
            currentCategoryId = selectedCategoryId,
            onDismiss = { showAddNoteDialog = false },
            onConfirm = { title, content, colorIndex, catId ->
                onAddNote(title, content, colorIndex, catId)
                showAddNoteDialog = false
            }
        )
    }
    
    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name ->
                onAddCategory(name)
                showAddCategoryDialog = false
            }
        )
    }
}

@Composable
fun CategoryChip(
    name: String,
    isSelected: Boolean,
    accentColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) accentColor else Color.Transparent,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, accentColor),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.White else textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
fun NoteCard(
    note: Note, 
    textColor: Color,
    secondaryTextColor: Color,
    accentColor: Color,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }
    val noteColor = Color(NoteColors.getColor(note.color))
    
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp, 50.dp)
                    .clip(CircleShape)
                    .background(noteColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, color = textColor, fontSize = 18.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    note.content,
                    color = secondaryTextColor,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    dateFormat.format(Date(note.createdAt)),
                    color = accentColor,
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddNoteDialog(
    categories: List<Category>,
    currentCategoryId: Int?,
    onDismiss: () -> Unit, 
    onConfirm: (String, String, Int, Int?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var selectedCatId by remember { mutableStateOf(currentCategoryId) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                // Category Selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(categories.find { it.id == selectedCatId }?.name ?: "No Category")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("No Category") },
                            onClick = { 
                                selectedCatId = null
                                expanded = false 
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = { 
                                    selectedCatId = category.id
                                    expanded = false 
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Color:", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(NoteColors.colors.size) { index ->
                        val color = Color(NoteColors.colors[index])
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (selectedColorIndex == index) 3.dp else 0.dp,
                                    color = if (selectedColorIndex == index) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorIndex = index }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (title.isNotBlank()) onConfirm(title, content, selectedColorIndex, selectedCatId) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddCategoryDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Folder") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Folder Name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onConfirm(name) }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
