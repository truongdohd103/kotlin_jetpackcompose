package com.frank.jetpackcomposeyoutube

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.frank.jetpackcomposeyoutube.ui.theme.JetpackComposeYoutubeTheme

@Composable
fun VideoDetailScreen(modifier: Modifier = Modifier, openCategoryScreen: () -> Unit) {
    val listVideos = fetchVideoData()
    val scrollState = rememberLazyListState()

    // Theo dõi hướng cuộn để xác định hiển thị VideoAction
    val isScrollingUp by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset == 0
        }
    }

    // Hiệu ứng mờ dần cho VideoAction
    val videoActionAlpha by animateFloatAsState(if (isScrollingUp || scrollState.firstVisibleItemIndex == 0) 1f else 0f)

    Column(modifier = modifier) {
        // Phần cố định: VideoDetail
        VideoDetail(
            videoThumb = R.drawable.video_thumbnail,
            videoTitle = "Android Jetpack Compose List and Grid",
            views = 999,
            timeAgo = "1 day ago"
        )

        // VideoAction với hiệu ứng mờ dần
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(videoActionAlpha)
        ) {
            VideoAction(
                modifier = Modifier.background(Color.White)
            )
        }

        // Phần cố định: FilterCategory
        FilterCategory(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp)
        )

        // Phần cuộn: Danh sách video
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listVideos) { video ->
                NextVideo(
                    videoTitle = video.videoTitle,
                    views = video.views,
                    timeAgo = video.timeAgo
                )
            }
        }
    }
}

fun fetchVideoData(): List<Video> {
    val list = mutableListOf<Video>()
    for (i in 1..20) {
        val video = Video(videoTitle = "Video $i", views = 1000 * i, timeAgo = "$i years ago")
        list.add(video)
    }
    return list
}

fun fetchCategoryData(): List<VideoCategory> {
    val list = mutableListOf<VideoCategory>()
    for (i in 1..20) {
        val category = VideoCategory(id = i, name = "Category $i")
        list.add(category)
    }
    return list
}

@Composable
fun FilterCategory(modifier: Modifier = Modifier) {
    val listCategory = fetchCategoryData()
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(listCategory) { category ->
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFF1F1F1),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { /* Handle category click if needed */ }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = category.name,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun VideoActionItem(modifier: Modifier = Modifier, @DrawableRes icon: Int, name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(name, style = TextStyle(fontSize = 12.sp))
    }
}

@Composable
fun VideoAction(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VideoActionItem(icon = R.drawable.ic_thumbup, name = "25.6K")
        VideoActionItem(icon = R.drawable.ic_thumbdown, name = "200K")
        VideoActionItem(icon = R.drawable.ic_share, name = "Share")
        VideoActionItem(icon = R.drawable.ic_download, name = "Download")
        VideoActionItem(icon = R.drawable.ic_save_to_playlist, name = "Save")
    }
}

@Composable
fun VideoDetailInfo(
    modifier: Modifier = Modifier,
    videoTitle: String,
    views: Int,
    timeAgo: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                videoTitle,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Text(
                "$views views",
                style = TextStyle(
                    color = Color(0xff6C6C6C),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                timeAgo,
                style = TextStyle(
                    color = Color(0xff6C6C6C),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun VideoDetail(
    modifier: Modifier = Modifier,
    @DrawableRes videoThumb: Int,
    videoTitle: String,
    views: Int,
    timeAgo: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = videoThumb),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp)
        ) {
            VideoDetailInfo(
                videoTitle = videoTitle,
                views = views,
                timeAgo = timeAgo,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun NextVideoInfo(
    videoTitle: String,
    views: Int,
    timeAgo: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (imgAvatar, tvVideoTitle, layoutInfo, imgMore) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.jetpack_compose),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .constrainAs(imgAvatar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_more),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(imgMore) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = videoTitle,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.constrainAs(tvVideoTitle) {
                start.linkTo(imgAvatar.end, margin = 4.dp)
                end.linkTo(imgMore.start, margin = 4.dp)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )

        Row(modifier = Modifier.constrainAs(layoutInfo) {
            top.linkTo(tvVideoTitle.bottom, margin = 4.dp)
            start.linkTo(tvVideoTitle.start)
        }) {
            Text(
                "$views views",
                style = TextStyle(
                    color = Color(0xff6C6C6C),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                timeAgo,
                style = TextStyle(
                    color = Color(0xff6C6C6C),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun NextVideo(
    videoTitle: String,
    views: Int,
    timeAgo: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.thumbnail_next_video),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        NextVideoInfo(
            videoTitle = videoTitle,
            views = views,
            timeAgo = timeAgo,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Composable
@Preview(name = "Video Info Item Preview", showBackground = true)
fun VideoActionItemPreview() {
    JetpackComposeYoutubeTheme {
        VideoActionItem(icon = R.drawable.ic_thumbup, name = "25.6K")
    }
}

@Composable
@Preview(name = "Video Info Preview", showBackground = true)
fun VideoActionPreview() {
    JetpackComposeYoutubeTheme {
        VideoAction()
    }
}

@Composable
@Preview(name = "video detail preview", showBackground = true)
fun VideoDetailPreview() {
    JetpackComposeYoutubeTheme {
        VideoDetail(
            videoThumb = R.drawable.video_thumbnail,
            videoTitle = "Android Jetpack Compose List and Grid",
            views = 999,
            timeAgo = "1 day ago"
        )
    }
}

@Composable
@Preview(name = "Next video preview", showBackground = true)
fun NextVideoPreview() {
    JetpackComposeYoutubeTheme {
        NextVideo(videoTitle = "Jetpack Compose Basic Layout", views = 22, timeAgo = "20 years ago")
    }
}

@Composable
@Preview(name = "Filter Category Preview", showBackground = true)
fun FilterCategoryPreview() {
    JetpackComposeYoutubeTheme {
        FilterCategory()
    }
}

@Composable
@Preview(name = "Video Detail Screen Preview", showSystemUi = true, showBackground = true)
fun VideoDetailScreenPreview() {
    JetpackComposeYoutubeTheme {
        VideoDetailScreen {}
    }
}
