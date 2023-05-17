package coreui.compose

import androidx.compose.runtime.*
import coreui.compose.base.Alignment
import coreui.compose.base.Arrangement
import coreui.compose.base.Box
import coreui.compose.base.Column
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.util.UiMessage
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun UiMessageHost(
    message: UiMessage?,
    duration: UiMessageDuration = UiMessageDuration.LONG
) {
    var isMessageVisible by remember { mutableStateOf(false) }

    if (message != null) {
        LaunchedEffect(message.id) {
            isMessageVisible = true
            delay(duration.duration)
            isMessageVisible = false
        }
    }

    Box(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
                backgroundColor(Color.transparent)
            }
        },
        overlapElevation = 1000,
        contentAlignment = Alignment.Box.TopEnd
    ) {
        Box(
            attrs = {
                style {
                    if (isMessageVisible) {
                        opacity(1)
                        transform {
                            translateY(0.percent)
                        }
                    } else {
                        opacity(0)
                        transform {
                            translateY((-100).percent)
                        }
                    }

                    transitions {
                        "opacity" {
                            this.duration = 0.15.s
                            timingFunction = AnimationTimingFunction.EaseOut
                        }

                        "transform" {
                            this.duration = 0.15.s
                            timingFunction = AnimationTimingFunction.EaseOut
                        }
                    }
                }
            }
        ) innerBox@{
            message ?: return@innerBox
            Message(message = message, duration = duration)
        }
    }
}

@Composable
private fun Message(
    message: UiMessage,
    duration: UiMessageDuration
) {
    val loadingProgressWidth by AnimateFloatAsState(
        key = message.id,
        duration = duration.duration,
        from = 0f,
        to = 100f
    )

    Column(
        attrs = {
            style {
                width(400.px)
                margin(10.px)
                borderRadius(Shape.medium)
                color(AppTheme.colors.inverseOnSurface)
                backgroundColor(AppTheme.colors.inverseSurface)
                overflow(Overflow.Clip)
                pointerEvents(PointerEvents.Auto)
            }
        },
        verticalArrangement = Arrangement.Vertical.SpaceBetween
    ) {
        Text(
            attrs = {
                style {
                    width(LengthKeyword.Auto)
                    margin(16.px)
                }
            },
            text = message.message,
            overflowWrap = OverflowWrap.BreakWord,
        )

        Box(
            attrs = {
                style {
                    width(loadingProgressWidth.percent)
                    height(4.px)
                    backgroundColor(AppTheme.colors.inversePrimary)
                }
            }
        )
    }
}

enum class UiMessageDuration(val duration: Duration) {
    LONG(duration = 3500.toDuration(DurationUnit.MILLISECONDS)),
    SHORT(duration = 2000.toDuration(DurationUnit.MILLISECONDS))
}