package com.originalstocks.llabank.ui.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.originalstocks.llabank.R
import com.originalstocks.llabank.ui.composables.themes.Purple40
import com.originalstocks.llabank.ui.composables.themes.PurpleGrey80
import com.originalstocks.llabank.ui.screens.HomeViewModel
import com.originalstocks.llabank.data.cache.HotelEntity
import com.originalstocks.llabank.data.network.ApiState
import java.util.Locale

private const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.getAllHotelsNetworkRequest()
    }

    val bottomSheetVisible = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PurpleGrey80)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            bottomSheetVisible.value = true
                        },
                        containerColor = Purple40,
                        modifier = Modifier.padding(16.dp),
                        contentColor = Color.White
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = stringResource(R.string.for_the_vowel_details)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(LocalContext.current.getColor(R.color.off_white))) // replace with actual color
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    CollapsingAppBarWithApi(viewModel = viewModel)

                    SearchInput(viewModel = viewModel)

                    VerticalHotelList(viewModel = viewModel)
                }
            }
        }
        if (bottomSheetVisible.value) {
            ModalBottomSheet(
                containerColor = Color.White,
                onDismissRequest = { bottomSheetVisible.value = false }
            ) {
                BottomNavigationSheet(viewModel = viewModel) {
                    bottomSheetVisible.value = false
                }
            }
        }
    }
}

@Composable
fun CollapsingAppBarWithApi(viewModel: HomeViewModel) {
    val hotelResponse by viewModel.hotelResponseStateFlow.collectAsState(initial = ApiState.Loading)
    when (hotelResponse) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }

        is ApiState.Success -> {
            CollapsingAppBar(
                (hotelResponse as ApiState.Success<List<HotelEntity>>).value
            )
        }

        is ApiState.Error -> {
            Text(text = "Error: ${(hotelResponse as ApiState.Error).message}")
        }

        is ApiState.Empty -> {}
    }
}

@Composable
fun CollapsingAppBar(value: List<HotelEntity>?) {
    Column {
        LazyRow(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            items(value?.size ?: 0) { index ->
                Log.e(TAG, "CollapsingAppBar: $index")
                HorizontalImageItem(value?.get(index)?.avatar ?: "")
            }
        }
    }
}

@Composable
fun SearchInput(viewModel: HomeViewModel) {
    var query by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
    ) {
        TextField(
            value = query,
            onValueChange = { value ->
                query = value
                viewModel.updateSearchQuery(value)
            },
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.round_search_24),
                    contentDescription = null,
                    tint = Color(LocalContext.current.getColor(R.color.off_white))
                )
            },
            modifier = Modifier
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .background(Color.Transparent),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Gray,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun VerticalHotelList(viewModel: HomeViewModel) {
    val hotelResponse by viewModel.hotelResponseStateFlow.collectAsState(initial = ApiState.Loading)
    val searchQuery by viewModel.searchQuery

    when (hotelResponse) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }

        is ApiState.Success -> {
            val vList = (hotelResponse as ApiState.Success<List<HotelEntity>>).value
            val filteredList = mutableListOf<HotelEntity>()
            if (vList != null) {
                for (hotel in vList) {
                    if (hotel.name?.lowercase(Locale.ROOT)
                            ?.contains(searchQuery.lowercase(Locale.ROOT)) == true
                    ) {
                        filteredList.add(hotel)
                    }
                }
            } else {
                Log.e(TAG, "VerticalHotelList: list is null")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                items(filteredList.size) { index ->
                    HotelInfoItem(filteredList.get(index))

                }
            }
        }

        is ApiState.Error -> {
            Text(text = "Error: ${(hotelResponse as ApiState.Error).message}")
        }

        is ApiState.Empty -> {}
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HorizontalImageItem(imageLink: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .width(400.dp)
            .height(200.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp),

        ) {
        GlideImage(
            model = imageLink,
            contentDescription = stringResource(R.string.hotel_image),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HotelInfoItem(data: HotelEntity?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                GlideImage(
                    model = data?.avatar,
                    contentDescription = stringResource(R.string.hotel_image),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${data?.name}",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${data?.description}",
                    color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.6f) else Color.Black.copy(
                        alpha = 0.6f
                    ),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavigationSheet(viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val hotelResponse by viewModel.hotelResponseStateFlow.collectAsState()
    val vList = (hotelResponse as ApiState.Success<List<HotelEntity>>).value
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.DarkGray
                )
            }
        }
        Text(
            text = "Number of hotels -> ${vList?.size} \nTop 3 Characters:\n${
                viewModel.tabulatingAnalyticsForBottomSheet(
                    vList ?: mutableListOf()
                )
            }",
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
