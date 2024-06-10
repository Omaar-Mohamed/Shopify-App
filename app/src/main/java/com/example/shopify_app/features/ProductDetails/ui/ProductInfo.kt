package com.example.shopify_app.features.ProductDetails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify_app.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    sliderList: MutableList<Int>,
    backwardIcon: ImageVector = Icons.Default.KeyboardArrowLeft,
    forwardIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    dotsActiveColor: Color = Color.DarkGray,
    dotsInActiveColor: Color = Color.LightGray,
    dotsSize: Dp = 10.dp,
    pagerPaddingValues: PaddingValues = PaddingValues(horizontal = 65.dp),
    imageCornerRadius: Dp = 16.dp,
    imageHeight: Dp = 250.dp,
) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(enabled = pagerState.canScrollBackward, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }) {
                Icon(imageVector = backwardIcon, contentDescription = "back")
            }

            HorizontalPager(
                count = sliderList.size,
                state = pagerState,
                contentPadding = pagerPaddingValues,
                modifier = modifier.weight(1f)
            ) { page ->
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffset

                val scaleFactor = 0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)


                Box(modifier = modifier
                    .graphicsLayer {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    .alpha(
                        scaleFactor.coerceIn(0f, 1f)
                    )
                    .padding(10.dp)
                    .clip(RoundedCornerShape(imageCornerRadius))) {
                    Image(
                        painter = painterResource(sliderList[page]),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight),
                        contentScale = ContentScale.FillHeight
                    )
                }
            }

            IconButton(enabled = pagerState.currentPage != sliderList.size - 1, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }) {
                Icon(imageVector = forwardIcon, contentDescription = "forward")
            }
        }
        Row(
            modifier
                .height(50.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            repeat(sliderList.size) {
                val color = if (pagerState.currentPage == it) dotsActiveColor else dotsInActiveColor
                Box(modifier = modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .size(dotsSize)
                    .background(color)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    })
            }
        }
    }
}

@Composable
fun SliderShow() {
    val sliderList = remember {
        mutableListOf(
            R.drawable.tshirt, R.drawable.splash, R.drawable.background
        )
    }
    CustomSlider(sliderList = sliderList)
}

@Composable
fun ProductInfo(){
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Roller Rabbit",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        )
        Text(
            text = "Vado Odelle Dress",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rating
            Text(
                text = "⭐ 4.5",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFFFC107), // Yellow color for stars
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "(4 Reviews)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Size Options
        SingleSelectChips()
        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = "Description",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Get a little lift from these Sam Edelman sandals featuring ruched straps and leather lace-up ties, while a braided jute sole makes a fresh statement for summer.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}