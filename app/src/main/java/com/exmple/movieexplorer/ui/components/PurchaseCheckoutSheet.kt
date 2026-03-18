package com.exmple.movieexplorer.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.getRentalPricePerDay
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.RatingGold
import com.exmple.movieexplorer.ui.theme.SuccessGreen
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlinx.coroutines.launch

private enum class CheckoutState { CONFIRM, PROCESSING, SUCCESS }

@Composable
fun PurchaseCheckoutSheet(
    movie: Movie,
    onConfirm: (days: Int) -> Unit,
    onDismiss: () -> Unit,
    onViewRentals: () -> Unit
) {
    var state by remember { mutableStateOf(CheckoutState.CONFIRM) }
    var days by remember { mutableIntStateOf(1) }
    val pricePerDay = getRentalPricePerDay(movie.rating)

    var sheetVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { sheetVisible = true }

    // Processing -> Success transition
    LaunchedEffect(state) {
        if (state == CheckoutState.PROCESSING) {
            delay(1800)
            state = CheckoutState.SUCCESS
        }
    }

    // Auto-dismiss after success
    LaunchedEffect(state) {
        if (state == CheckoutState.SUCCESS) {
            delay(4000)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC0D0D1A))
            .clickable(enabled = state == CheckoutState.CONFIRM, onClick = onDismiss)
    ) {
        AnimatedVisibility(
            visible = sheetVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ) + fadeIn(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = false) {}
                    .border(0.5.dp, GlassBorder, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                AnimatedContent(
                    targetState = state,
                    transitionSpec = {
                        (fadeIn(tween(400)) + scaleIn(tween(400), initialScale = 0.92f))
                            .togetherWith(fadeOut(tween(200)))
                    },
                    label = "checkoutStates"
                ) { currentState ->
                    when (currentState) {
                        CheckoutState.CONFIRM -> ConfirmContent(
                            movie = movie,
                            days = days,
                            pricePerDay = pricePerDay,
                            onDaysChange = { days = it },
                            onSlideConfirm = {
                                onConfirm(days)
                                state = CheckoutState.PROCESSING
                            },
                            onDismiss = onDismiss
                        )
                        CheckoutState.PROCESSING -> ProcessingContent(movie = movie)
                        CheckoutState.SUCCESS -> SuccessContent(
                            movie = movie,
                            days = days,
                            totalPrice = pricePerDay * days,
                            onViewRentals = onViewRentals
                        )
                    }
                }
            }
        }
    }
}

// ───── State 1: Confirmation ─────

@Composable
private fun ConfirmContent(
    movie: Movie,
    days: Int,
    pricePerDay: Int,
    onDaysChange: (Int) -> Unit,
    onSlideConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(TextMuted.copy(alpha = 0.4f), RoundedCornerShape(50))
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Close button
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
            }
        }

        Text(
            text = "Checkout",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Movie info row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphism(
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = CardSurface.copy(alpha = 0.6f),
                    borderColor = GlassBorder
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(65.dp)
                    .height(95.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = RatingGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${movie.rating}", style = MaterialTheme.typography.labelMedium, color = RatingGold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Day selector
        Text("Rental Duration", style = MaterialTheme.typography.titleSmall, color = TextSecondary)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { if (days > 1) onDaysChange(days - 1) },
                modifier = Modifier
                    .size(44.dp)
                    .background(CardSurface, CircleShape)
                    .border(0.5.dp, GlassBorder, CircleShape)
            ) {
                Icon(Icons.Default.Remove, "Decrease", tint = PurpleLight)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 28.dp)
            ) {
                Text(
                    text = "$days",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = if (days == 1) "day" else "days",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            IconButton(
                onClick = { if (days < 30) onDaysChange(days + 1) },
                modifier = Modifier
                    .size(44.dp)
                    .background(CardSurface, CircleShape)
                    .border(0.5.dp, GlassBorder, CircleShape)
            ) {
                Icon(Icons.Default.Add, "Increase", tint = PurpleLight)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Price breakdown
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                PriceRow("Rate", "\u20B9$pricePerDay/day")
                PriceRow("Duration", "$days day${if (days > 1) "s" else ""}")
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(GlassBorder)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        text = "\u20B9${pricePerDay * days}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = PurpleLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Slide to confirm
        SlideToConfirmButton(onConfirmed = onSlideConfirm)

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun PriceRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
    }
}

@Composable
private fun SlideToConfirmButton(onConfirmed: () -> Unit) {
    val pillSizePx = with(LocalDensity.current) { 58.dp.toPx() }
    var trackWidthPx by remember { mutableFloatStateOf(1000f) }
    val maxDragPx = (trackWidthPx - pillSizePx).coerceAtLeast(1f)

    val dragOffset = remember { Animatable(0f) }
    val progress = (dragOffset.value / maxDragPx).coerceIn(0f, 1f)
    var confirmed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val bgAlpha by animateFloatAsState(
        targetValue = if (confirmed) 1f else 0.3f + progress * 0.7f,
        label = "bgAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .onSizeChanged { trackWidthPx = it.width.toFloat() }
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PurplePrimary.copy(alpha = bgAlpha * 0.5f),
                        PurplePrimary.copy(alpha = bgAlpha)
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .border(0.5.dp, PurpleLight.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .then(
                if (!confirmed) {
                    Modifier.pointerInput(maxDragPx) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                val currentProgress = (dragOffset.value / maxDragPx).coerceIn(0f, 1f)
                                if (currentProgress >= 0.7f) {
                                    confirmed = true
                                    onConfirmed()
                                } else {
                                    scope.launch {
                                        dragOffset.animateTo(0f, tween(300))
                                    }
                                }
                            },
                            onHorizontalDrag = { change, delta ->
                                change.consume()
                                scope.launch {
                                    dragOffset.snapTo(
                                        (dragOffset.value + delta).coerceIn(0f, maxDragPx)
                                    )
                                }
                            }
                        )
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Background text
        Text(
            text = if (confirmed) "Confirmed!" else "Slide to Confirm",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary.copy(alpha = 1f - progress * 0.5f)
        )

        // Draggable pill
        if (!confirmed) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(dragOffset.value.roundToInt(), 0) }
                    .padding(4.dp)
                    .size(50.dp)
                    .background(
                        Brush.linearGradient(listOf(PurplePrimary, PurpleLight)),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Slide",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ───── State 2: Processing ─────

@Composable
private fun ProcessingContent(movie: Movie) {
    val pulseAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            pulseAnim.animateTo(1f, tween(1000, easing = LinearEasing))
            pulseAnim.animateTo(0f, tween(1000, easing = LinearEasing))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pulsing ring around poster
        Box(contentAlignment = Alignment.Center) {
            val strokeAlpha = 0.3f + pulseAnim.value * 0.7f
            val ringScale = 1f + pulseAnim.value * 0.06f

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(ringScale)
                    .drawBehind {
                        drawCircle(
                            color = PurplePrimary.copy(alpha = strokeAlpha),
                            radius = size.minDimension / 2,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
            )

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .alpha(0.85f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Processing your rental...",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please wait",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}

// ───── State 3: Success ─────

@Composable
private fun SuccessContent(
    movie: Movie,
    days: Int,
    totalPrice: Int,
    onViewRentals: () -> Unit
) {
    var showCheck by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showConfetti = true
        delay(200)
        showCheck = true
        delay(400)
        showText = true
        delay(300)
        showDetails = true
        delay(300)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Confetti particles
        if (showConfetti) {
            ConfettiOverlay()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Animated checkmark
            AnimatedVisibility(
                visible = showCheck,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(SuccessGreen.copy(alpha = 0.3f), Color.Transparent)
                            ),
                            CircleShape
                        )
                        .border(2.dp, SuccessGreen.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\u2713",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            AnimatedVisibility(
                visible = showText,
                enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()
            ) {
                Text(
                    text = "Rental Confirmed!",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Details card
            AnimatedVisibility(
                visible = showDetails,
                enter = slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(400)
                ) + fadeIn(tween(400))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.7f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(55.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$days day${if (days > 1) "s" else ""} rental",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Text(
                            text = "\u20B9$totalPrice",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = PurpleLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // View Rentals button
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(tween(500)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(500)
                )
            ) {
                Button(
                    onClick = onViewRentals,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "View My Rentals",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

// ───── Confetti ─────

@Composable
private fun ConfettiOverlay() {
    val particles = remember {
        val colors = listOf(PurplePrimary, PurpleLight, RatingGold, SuccessGreen, Color(0xFFE040FB))
        List(15) {
            ConfettiParticle(
                color = colors[it % colors.size],
                startX = Random.nextFloat(),
                startY = Random.nextFloat() * 0.4f + 0.1f,
                size = Random.nextFloat() * 8f + 4f,
                delay = (it * 60L)
            )
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        particles.forEach { particle ->
            AnimatedConfettiDot(particle)
        }
    }
}

private data class ConfettiParticle(
    val color: Color,
    val startX: Float,
    val startY: Float,
    val size: Float,
    val delay: Long
)

@Composable
private fun AnimatedConfettiDot(particle: ConfettiParticle) {
    var visible by remember { mutableStateOf(false) }
    val yDrift = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(particle.delay)
        visible = true
        alphaAnim.animateTo(1f, tween(300))
        yDrift.animateTo(
            targetValue = -60f,
            animationSpec = tween(2000, easing = FastOutSlowInEasing)
        )
        alphaAnim.animateTo(0f, tween(800))
    }

    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = (particle.startX * 800).roundToInt(),
                        y = ((particle.startY * 300) + yDrift.value).roundToInt()
                    )
                }
                .alpha(alphaAnim.value)
        ) {
            Box(
                modifier = Modifier
                    .size(particle.size.dp)
                    .background(particle.color, CircleShape)
            )
        }
    }
}
