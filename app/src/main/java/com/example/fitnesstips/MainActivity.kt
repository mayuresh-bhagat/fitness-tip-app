package com.example.fitnesstips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.FitnessTipsTheme
import com.example.fitnesstips.data.FitnessTipDataSource
import com.example.fitnesstips.model.FitnessTip
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessTipsTheme {
                Surface {
                    FitnessApp()
                }
            }
        }
    }
}

@Composable
fun FitnessApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() }
    ) {
        FitnessTipList(
            FitnessTipDataSource.fitnessTipsObject.fitnessTips,
            contentPaddding = it
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displaySmall
            )
        },
        modifier = modifier
    )
}

@Composable
fun FitnessTipList(
    tips: List<FitnessTip>,
    modifier: Modifier = Modifier,
    contentPaddding: PaddingValues = PaddingValues(0.dp)
) {
    val visibility = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = visibility,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
        modifier = modifier
    ) {
        LazyColumn(contentPadding = contentPaddding) {
            itemsIndexed(tips) { index, item ->
                FitnessTipItem(
                    fitnessTip = item,
                    day = (index + 1),
                    modifier = Modifier
                        .padding(
                            8.dp
                        )
                )
            }
        }
    }
}

@Composable
fun FitnessTipItem(
    fitnessTip: FitnessTip,
    day: Int,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = stringResource(fitnessTip.title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Day $day",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }


                Spacer(modifier = Modifier.weight(1f))

                if(!expanded) {
                    Box(
                        modifier = Modifier
                            .clip(shape = MaterialTheme.shapes.small)

                    ) {
                        Image(
                            painter = painterResource(fitnessTip.image),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .size(64.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }



                Spacer(modifier = Modifier.weight(0.1f))

                MoreItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                )

            }

            if(expanded) {
                FitnessTipDescription(fitnessTip.description, fitnessTip.image)
            }

        }
    }
}

@Composable
private fun MoreItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun FitnessTipDescription(
    @StringRes description:Int,
    @DrawableRes image: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.small)
                .size(150.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.large)

            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify

        )
    }
}




@Preview
@Composable
fun FitnessTipItemPreview() {
    FitnessTipsTheme {
        FitnessTipItem(
            FitnessTip(
                R.string.title_1,
                R.string.description_1,
                R.drawable.image_1
            ),
            day = 1
        )
    }
}


@Preview
@Composable
fun FitnessTipListPreview() {
    FitnessTipsTheme {
        FitnessTipList(FitnessTipDataSource.fitnessTipsObject.fitnessTips)
    }
}

