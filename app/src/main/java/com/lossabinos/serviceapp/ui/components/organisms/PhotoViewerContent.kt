package com.lossabinos.serviceapp.ui.components.organisms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lossabinos.serviceapp.ui.components.molecules.PhotoImageWithZoom

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoViewerContent(
    photoPaths: List<String>,
    initialIndex: Int,
    modifier: Modifier = Modifier,
    onIndexChange: (Int) -> Unit = {},
    zoomState: Float,
    onZoomChange: (Float) -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }

    Box(modifier = modifier.fillMaxSize()) {
        if (photoPaths.isNotEmpty()) {
            HorizontalPager(
                state = rememberPagerState(initialPage = initialIndex) { photoPaths.size },
                modifier = Modifier.fillMaxSize()
            ) { page ->
                currentIndex = page
                onIndexChange(page)
                PhotoImageWithZoom(
                    imagePath = photoPaths[page],
                    zoomState = zoomState,
                    onZoomChange = onZoomChange
                )
            }
        }
    }
}
