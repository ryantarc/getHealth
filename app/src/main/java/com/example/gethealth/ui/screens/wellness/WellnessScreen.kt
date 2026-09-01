package com.example.gethealth.ui.screens.wellness

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gethealth.data.UserRepository
import com.example.gethealth.data.WellbeingRepository
import com.example.gethealth.model.MoodEntry
import com.example.gethealth.ui.components.GetHealthTopBar
import com.example.gethealth.ui.util.WindowWidthSize
import com.example.gethealth.ui.util.rememberWindowWidthSize
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WellnessScreen() {
    val userEmail = UserRepository.currentUserEmail.value ?: "guest@example.com"
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Mood Tracker", "History", "Statistics")

    // Using the Central Draft State from Repository to ensure 100% persistence
    val selectedMood by WellbeingRepository.draftMood
    val note by WellbeingRepository.draftNote
    val selectedDate = LocalDate.ofEpochDay(WellbeingRepository.draftDateEpoch.longValue)
    val saveMessage by WellbeingRepository.draftMessage

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = { GetHealthTopBar(title = "Wellness Advisor") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Simple intro text at the top of content, similar to Fitness module
            if (!isLandscape) {
                Text(
                    text = "Track your mood and explore your emotional health.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when (selectedTab) {
                0 -> MoodSelectionScreen(
                    userEmail = userEmail,
                    selectedMood = selectedMood,
                    note = note,
                    selectedDate = selectedDate,
                    message = saveMessage
                )
                1 -> MoodHistoryScreen(userEmail)
                2 -> MoodStatisticsScreen(userEmail)
            }
        }
    }
}

@Composable
fun MoodSelectionScreen(
    userEmail: String,
    selectedMood: String?,
    note: String,
    selectedDate: LocalDate,
    message: String?
) {
    val scope = rememberCoroutineScope()
    var showDatePicker by remember { mutableStateOf(value = false) }
    val windowWidthSize = rememberWindowWidthSize()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val moods = listOf(
        "😄" to "Great",
        "🙂" to "Good",
        "😐" to "Okay",
        "😔" to "Low",
        "😣" to "Stressed"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(if (isLandscape) 12.dp else 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How are you feeling?",
            style = if (isLandscape) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 24.dp))

        if ((windowWidthSize == WindowWidthSize.EXPANDED) || isLandscape) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mood Rating",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                    ) {
                        Box(modifier = Modifier.padding(vertical = 8.dp)) {
                            MoodGrid(moods, selectedMood, compact = true) { WellbeingRepository.draftMood.value = it }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    WellnessTips(selectedMood, isLandscape)
                }
                Column(modifier = Modifier.weight(1f)) {
                    MoodForm(
                        note = note,
                        onNoteChange = { WellbeingRepository.draftNote.value = it },
                        selectedDate = selectedDate,
                        onDateClick = { showDatePicker = true },
                    ) {
                        if (selectedMood != null) {
                            scope.launch {
                                try {
                                    val entry = MoodEntry(
                                        email = userEmail,
                                        mood = selectedMood,
                                        note = note,
                                        date = selectedDate.toString()
                                    )
                                    WellbeingRepository.addMoodEntry(entry)
                                    WellbeingRepository.draftMessage.value = "Mood saved for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd"))}!"
                                    WellbeingRepository.resetDraft()
                                } catch (e: Exception) {
                                    WellbeingRepository.draftMessage.value = "Error: ${e.message}"
                                }
                            }
                        } else {
                            WellbeingRepository.draftMessage.value = "Please select a mood."
                        }
                    }
                }
            }
        } else {
            // Standard Portrait Layout
            Text(
                text = "Mood Rating",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start).padding(start = 4.dp, bottom = 4.dp)
            )
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.padding(vertical = 12.dp)) {
                    MoodGrid(moods, selectedMood) { WellbeingRepository.draftMood.value = it }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            MoodForm(
                note = note,
                onNoteChange = { WellbeingRepository.draftNote.value = it },
                selectedDate = selectedDate,
                onDateClick = { showDatePicker = true },
            ) {
                if (selectedMood != null) {
                    scope.launch {
                        try {
                            val entry = MoodEntry(
                                email = userEmail,
                                mood = selectedMood,
                                note = note,
                                date = selectedDate.toString()
                            )
                            WellbeingRepository.addMoodEntry(entry)
                            WellbeingRepository.draftMessage.value = "Mood saved for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}!"
                            WellbeingRepository.resetDraft()
                        } catch (e: Exception) {
                            WellbeingRepository.draftMessage.value = "Error: ${e.message}"
                        }
                    }
                } else {
                    WellbeingRepository.draftMessage.value = "Please select a mood."
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            WellnessTips(selectedMood, false)
        }

        if (showDatePicker) {
            MoodDatePickerDialog(
                initialDate = selectedDate,
                onDateSelected = { 
                    WellbeingRepository.draftDateEpoch.longValue = it.toEpochDay()
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
fun WellnessTips(selectedMood: String?, compact: Boolean) {
    val tip = when (selectedMood) {
        "Great" -> "Ride the wave! Share your joy with someone today."
        "Good" -> "Keep it up! A quick stretch will maintain this energy."
        "Okay" -> "Neutral days are perfect for reflection. Try journaling."
        "Low" -> "Be gentle. A warm tea or short walk can help lift spirits."
        "Stressed" -> "Take a break. Try 4-7-8 breathing: inhale 4s, hold 7s, exhale 8s."
        else -> null
    }

    AnimatedVisibility(
        visible = tip != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        tip?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(if (compact) 8.dp else 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(if (compact) 18.dp else 24.dp)
                    )
                    Spacer(modifier = Modifier.width(if (compact) 8.dp else 12.dp))
                    Text(
                        text = it,
                        style = if (compact) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(date)
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun MoodForm(
    note: String,
    onNoteChange: (String) -> Unit,
    selectedDate: LocalDate,
    onDateClick: () -> Unit,
    onSave: () -> Unit
) {
    Column {
        OutlinedCard(
            onClick = onDateClick,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
        ) {
            Row(
                modifier = Modifier.padding(if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 8.dp else 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Date", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text("Add a note (optional)") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            minLines = 2
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Mood")
        }
    }
}

@Composable
fun MoodGrid(
    moods: List<Pair<String, String>>, 
    selectedMood: String?, 
    compact: Boolean = false,
    onMoodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        moods.forEach { (emoji, label) ->
            val isSelected = selectedMood == label
            val moodColor = when (label) {
                "Great" -> Color(0xFF4CAF50)
                "Good" -> Color(0xFF8BC34A)
                "Okay" -> Color(0xFF9E9E9E)
                "Low" -> Color(0xFFFF8A80)
                "Stressed" -> Color(0xFFD32F2F)
                else -> MaterialTheme.colorScheme.primary
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = if (isSelected) moodColor.copy(alpha = 0.2f) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) moodColor else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onMoodSelected(label) }
                    .padding(vertical = if (compact) 8.dp else 12.dp, horizontal = 4.dp)
            ) {
                Text(text = emoji, fontSize = if (compact) 26.sp else 36.sp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) moodColor else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun MoodStatisticsScreen(userEmail: String) {
    var history by remember { mutableStateOf<List<MoodEntry>>(emptyList()) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        history = WellbeingRepository.getMoodHistory(userEmail)
    }

    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No data to show statistics.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val moodCounts = history.groupingBy { it.mood }.eachCount()
    val total = history.size

    val stats = listOf(
        Triple("Great", "😄", Color(0xFF4CAF50)),      // Green
        Triple("Good", "🙂", Color(0xFF8BC34A)),       // Light Green
        Triple("Okay", "😐", Color(0xFF9E9E9E)),       // Grey
        Triple("Low", "😔", Color(0xFFFF8A80)),        // Light Red
        Triple("Stressed", "😣", Color(0xFFD32F2F))    // Red
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Mood Summary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 24.dp))

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
                    MoodPieChart(moodCounts, total, stats, sizeDp = 140)
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    stats.forEach { (label, emoji, color) ->
                        MoodStatItem(label, emoji, moodCounts[label] ?: 0, total, color, compact = true)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
                MoodPieChart(moodCounts, total, stats)
            }
            Spacer(modifier = Modifier.height(32.dp))
            stats.forEach { (label, emoji, color) ->
                MoodStatItem(label, emoji, moodCounts[label] ?: 0, total, color)
            }
        }
    }
}

@Composable
fun MoodPieChart(
    moodCounts: Map<String, Int>,
    total: Int,
    stats: List<Triple<String, String, Color>>,
    sizeDp: Int = 200
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.2f),
                radius = size.width / 2,
                style = Stroke(width = (sizeDp/7).dp.toPx())
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            stats.forEach { (label, _, color) ->
                val count = moodCounts[label] ?: 0
                if (count > 0) {
                    val sweepAngle = (count.toFloat() / total) * 360f
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        size = Size(size.width, size.height),
                        style = Stroke(width = (sizeDp/7).dp.toPx(), cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngle
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = total.toString(),
                style = if (sizeDp < 180) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Logs",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MoodStatItem(label: String, emoji: String, count: Int, total: Int, color: Color, compact: Boolean = false) {
    val progress = if (total > 0) count.toFloat() / total else 0f
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (compact) 2.dp else 4.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(if (compact) 6.dp else 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = if (compact) 16.sp else 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, style = if (compact) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.weight(1f))
                Text(count.toString(), style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(if (compact) 2.dp else 8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(if (compact) 3.dp else 8.dp),
                color = color,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun MoodHistoryScreen(userEmail: String) {
    val scope = rememberCoroutineScope()
    var history by remember { mutableStateOf<List<MoodEntry>>(emptyList()) }
    var editingEntry by remember { mutableStateOf<MoodEntry?>(null) }
    
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedMoodFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        history = WellbeingRepository.getMoodHistory(userEmail)
    }

    val filteredHistory = history.filter { entry ->
        val noteMatches = entry.note?.contains(searchQuery, ignoreCase = true) ?: false
        val dateMatches = entry.date.contains(searchQuery, ignoreCase = true)
        val matchesSearch = noteMatches || dateMatches
        val matchesMood = selectedMoodFilter == null || entry.mood == selectedMoodFilter
        matchesSearch && matchesMood
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search notes or dates...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Box {
                IconButton(onClick = { showFilterMenu = true }) {
                    if (selectedMoodFilter != null) {
                        BadgedBox(badge = { Badge { Text("!") } }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                    } else {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
                
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All Moods") },
                        onClick = {
                            selectedMoodFilter = null
                            showFilterMenu = false
                        }
                    )
                    val moods = listOf("Great", "Good", "Okay", "Low", "Stressed")
                    moods.forEach { mood ->
                        DropdownMenuItem(
                            text = { Text(mood) },
                            onClick = {
                                selectedMoodFilter = mood
                                showFilterMenu = false
                            }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (filteredHistory.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (history.isEmpty()) "No history yet." else "No matches found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredHistory) { entry ->
                        MoodItem(
                            entry = entry,
                            onEdit = { editingEntry = entry },
                            onDelete = {
                                scope.launch {
                                    WellbeingRepository.deleteMoodEntry(entry.id ?: return@launch)
                                    history = WellbeingRepository.getMoodHistory(userEmail)
                                }
                            }
                        )
                    }
                }
            }

            if (editingEntry != null) {
                EditMoodDialog(
                    entry = editingEntry!!,
                    onDismiss = { editingEntry = null },
                    onConfirm = { updatedEntry ->
                        scope.launch {
                            WellbeingRepository.updateMoodEntry(updatedEntry)
                            history = WellbeingRepository.getMoodHistory(userEmail)
                            editingEntry = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MoodItem(entry: MoodEntry, onEdit: () -> Unit, onDelete: () -> Unit) {
    val emoji = when (entry.mood) {
        "Great" -> "😄"
        "Good" -> "🙂"
        "Okay" -> "😐"
        "Low" -> "😔"
        "Stressed" -> "😣"
        else -> "❓"
    }
    val moodColor = when (entry.mood) {
        "Great" -> Color(0xFF4CAF50)
        "Good" -> Color(0xFF8BC34A)
        "Okay" -> Color(0xFF9E9E9E)
        "Low" -> Color(0xFFFF8A80)
        "Stressed" -> Color(0xFFD32F2F)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = moodColor.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.mood, fontWeight = FontWeight.Bold)
                Text(text = entry.date, style = MaterialTheme.typography.bodySmall)
                if (!entry.note.isNullOrEmpty()) {
                    Text(text = entry.note, style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun EditMoodDialog(
    entry: MoodEntry,
    onDismiss: () -> Unit,
    onConfirm: (MoodEntry) -> Unit
) {
    var note by remember { mutableStateOf(entry.note ?: "") }
    var selectedMood by remember { mutableStateOf(entry.mood) }
    val moods = listOf("Great", "Good", "Okay", "Low", "Stressed")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Mood Entry") },
        text = {
            Column {
                Text("Mood:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    moods.forEach { mood ->
                        val isSelected = selectedMood == mood
                        val emoji = when (mood) {
                            "Great" -> "😄"
                            "Good" -> "🙂"
                            "Okay" -> "😐"
                            "Low" -> "😔"
                            "Stressed" -> "😣"
                            else -> "❓"
                        }
                        val moodColor = when (mood) {
                            "Great" -> Color(0xFF4CAF50)
                            "Good" -> Color(0xFF8BC34A)
                            "Okay" -> Color(0xFF9E9E9E)
                            "Low" -> Color(0xFFFF8A80)
                            "Stressed" -> Color(0xFFD32F2F)
                            else -> MaterialTheme.colorScheme.primary
                        }
                        Text(
                            text = emoji,
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) moodColor.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) moodColor else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMood = mood }
                                .padding(8.dp),
                            fontSize = 24.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(entry.copy(mood = selectedMood, note = note)) }) {
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
