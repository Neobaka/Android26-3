package com.example.players.ui.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.players.data.Player
import com.example.players.ui.components.Shirt
import com.example.players.ui.list.AppOrange
import com.example.players.ui.list.Avatar

private val LinkColor = Color(0xFF00C2C7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailsScreen(
    onBack: () -> Unit,
    viewModel: PlayerDetailsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val player = state.player

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Карточка", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppOrange,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (player == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Игрок не найден")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Header(player)
                Spacer(Modifier.height(16.dp))
                InfoSection(player)
                Spacer(Modifier.height(16.dp))
                StatsSection(player)
                Spacer(Modifier.height(24.dp))
                FootballField(player)
                Spacer(Modifier.height(24.dp))
                CommentsSection(
                    comments = player.comments,
                    expanded = state.commentsExpanded,
                    onToggle = viewModel::toggleComments
                )
            }
        }
    }
}

@Composable
private fun Header(player: Player) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (avatar, name, shirt) = createRefs()

        Box(Modifier.constrainAs(avatar) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }) { Avatar(size = 48) }

        Text(
            text = player.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(avatar.end, margin = 8.dp)
                end.linkTo(shirt.start, margin = 8.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
            }
        )

        Shirt(
            number = player.number,
            size = 48.dp,
            modifier = Modifier.constrainAs(shirt) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
private fun InfoSection(player: Player) {
    val uriHandler = LocalUriHandler.current
    Text("Информация", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
    InfoRow("Возраст", "${player.age} лет")
    InfoRow("Позиция", player.position)
    InfoRow("Команда", player.team)
    Spacer(Modifier.height(8.dp))
    Row {
        Text(
            "Страница",
            color = LinkColor,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { uriHandler.openUri(player.socialUrl) }
        )
        Text(" в социальной сети")
    }
    Spacer(Modifier.height(8.dp))
    Text(
        "Резюме",
        color = LinkColor,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { uriHandler.openUri(player.resumeUrl) }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.padding(vertical = 2.dp)) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(72.dp))
        Text(value)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatsSection(player: Player) {
    val s = player.stats
    Text("Статистика выступлений", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatChip("${s.games} игр", Color(0xFF757575))
        StatChip("${s.goals} гол", Color(0xFF00D6C6))
        StatChip("${s.assists} передача", Color(0xFFFFCC80), textColor = Color.Black)
        StatChip("${s.yellow} желтая", Color(0xFFD4B106))
        StatChip("${s.red} красных", Color(0xFFD71E0A))
    }
}

@Composable
private fun StatChip(text: String, color: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun FootballField(player: Player) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        val fieldWidth = 240.dp
        val fieldHeight = 170.dp
        val shirtSize = 24.dp

        Box(Modifier.size(fieldWidth, fieldHeight)) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke = Stroke(width = 6.dp.toPx())
                val w = size.width
                val h = size.height
                // внешняя рамка и центральная линия
                drawRect(Color.Black, style = stroke)
                drawLine(Color.Black, Offset(w / 2, 0f), Offset(w / 2, h), strokeWidth = 6.dp.toPx())
                // центральный круг
                drawCircle(Color.Black, radius = h * 0.2f, center = Offset(w / 2, h / 2), style = stroke)
                // штрафные площадки
                val boxW = w * 0.14f
                val boxH = h * 0.4f
                drawRect(Color.Black, topLeft = Offset(0f, h / 2 - boxH / 2), size = Size(boxW, boxH), style = stroke)
                drawRect(Color.Black, topLeft = Offset(w - boxW, h / 2 - boxH / 2), size = Size(boxW, boxH), style = stroke)
            }
            player.fieldSpots.forEach { spot ->
                Shirt(
                    size = shirtSize,
                    color = if (spot.isCurrent) AppOrange else Color.Black,
                    modifier = Modifier.offset(
                        x = fieldWidth * spot.x - shirtSize / 2 + 24.dp,
                        y = fieldHeight * spot.y - shirtSize / 2 - 8.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun CommentsSection(
    comments: List<String>,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Комментарии", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                comments.forEach { Text("• $it") }
            }
        }
    }
}
