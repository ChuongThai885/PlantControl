package com.PBL5.plantcontrol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.PBL5.plantcontrol.ui.theme.PlantControlTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantControlTheme {
                val database= Firebase.database

                val humidity= remember {
                    mutableStateOf(50f)
                }
                val humidityRef= database.getReference("Humidity")
                val temperature= remember {
                    mutableStateOf(25f)
                }
                val temperatureRef= database.getReference("Temperature")
                val manual= remember {
                    mutableStateOf(false)
                }
                val manualRef= database.getReference("manual")
                humidityRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<Float>()
                        if (value != null) {
                            humidity.value= value
                        }
                        Log.e("DEBUG", "Value is: " + value)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("ERROR", "Failed to read value.", error.toException())
                    }
                })
                temperatureRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<Float>()
                        if (value != null) {
                            temperature.value= value
                        }
                        Log.d("DEBUG", "Value is: " + value)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("ERROR", "Failed to read value.", error.toException())
                    }
                })
                manualRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<Boolean>()
                        if (value != null) {
                            manual.value= value
                        }
                        Log.d("DEBUG", "Value is: " + value)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("ERROR", "Failed to read value.", error.toException())
                    }
                })
                val waterpump= remember {
                    mutableStateOf(false)
                }
                val waterpumpRef = database.getReference("water_pump")
                waterpumpRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<Boolean>()
                        if (value != null) {
                            waterpump.value= value
                        }
                        Log.d("DEBUG", "Value is: " + value)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("ERROR", "Failed to read value.", error.toException())
                    }
                })
                val light= remember {
                    mutableStateOf(false)
                }
                val lightRef= database.getReference("led")
                lightRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<Boolean>()
                        if (value != null) {
                            light.value= value
                        }
                        Log.d("DEBUG", "Value is: " + value)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("ERROR", "Failed to read value.", error.toException())
                    }
                })
                val imgSrc= remember {
                    mutableStateOf("")
                }
                val imgSrcRef= database.getReference("img")
                imgSrcRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<String>()
                        if (value != null) {
                            imgSrc.value= value
                        }
                        Log.d("DEBUG", "Value is: " + value)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("ERROR", "Failed to read value.", error.toException())
                    }
                })
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(107,107,107)
                ) {
                    Column {
                        TopHeader(Temperature = temperature.value, Humidity = humidity.value)
                        Surface(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .height(190.dp),
                            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.Top,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = "Tự động: ",
                                        style = MaterialTheme.typography.h5
                                    )
                                    Column(
                                        modifier = Modifier.padding(end=20.dp)
                                    ) {
                                        Switch2(manual = !manual.value) {
                                                returnvalue-> manual.value= !returnvalue
                                                manualRef.setValue(manual.value)
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = "Bơm nước: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Column(
                                        modifier = Modifier.padding(end=20.dp)
                                    ) {
                                        Switch(
                                            checked = waterpump.value,
                                            enabled = manual.value,
                                            onCheckedChange = {
                                                waterpump.value=!waterpump.value
                                                waterpumpRef.setValue(waterpump.value)
                                            }
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = "Đèn: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Column(
                                        modifier = Modifier.padding(end=20.dp)
                                    ) {
                                        Switch(
                                            checked = light.value,
                                            enabled = manual.value,
                                            onCheckedChange = {
                                                light.value=!light.value
                                                lightRef.setValue(light.value)
                                            }
                                        )
                                    }
                                }
                            }

                        }
                        val context = LocalContext.current
                        Button(
                            content = {
                                Text("Open")
                            },
                            onClick = {
                                val intent = Intent(context, DetectActivity::class.java)
                                startActivity(intent)
                            }
                        )
                        abc(imgSrc.value)
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeader(
    Humidity: Float,
    Temperature: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .padding(10.dp),
        backgroundColor= Color(95,195,39),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                createLogo()
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h4
                )
            }
            Row(
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nhiệt độ:",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = String.format("%.1f°C",Temperature),
                    style = MaterialTheme.typography.h6
                )
            }
            Row(
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Độ ẩm:",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = String.format("%.1f",Humidity)+"%",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}


@Composable
fun  createLogo() {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .padding(10.dp),
        shape = CircleShape
    ) {
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo image",
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop)
    }
}

@Composable
fun abc(
    src: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(100.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data( src)
                    .crossfade(false)
                    .build()
            )
            , contentDescription = "camera image"
        )
    }
}

@Composable
fun Switch2(
    scale: Float = 2f,
    width: Dp = 36.dp,
    height: Dp = 20.dp,
    strokeWidth: Dp = 2.dp,
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color(0xFFe0e0e0),
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
    manual: Boolean,
    returnvalue: (Boolean) -> Unit
) {
    val switchON= remember {
        mutableStateOf(manual)
    }

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // This is called when the user taps on the canvas
                        switchON.value = !switchON.value
                        returnvalue(switchON.value)
                    }
                )
            }
    ) {
        // Track
        drawRoundRect(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )

        // Thumb
        drawCircle(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }

//    Spacer(modifier = Modifier.padding(10.dp))
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlantControlTheme {
        Greeting("Android")
    }
}