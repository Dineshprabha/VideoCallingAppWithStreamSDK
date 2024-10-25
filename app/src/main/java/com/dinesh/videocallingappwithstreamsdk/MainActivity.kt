package com.dinesh.videocallingappwithstreamsdk

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dinesh.videocallingappwithstreamsdk.ui.theme.VideoCallingAppWithStreamSDKTheme
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VideoCallingAppWithStreamSDKTheme {
                val apiKey = "mmhfdzb5evj2"
                val userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL3Byb250by5nZXRzdHJlYW0uaW8iLCJzdWIiOiJ1c2VyL1p1Y2t1c3MiLCJ1c2VyX2lkIjoiWnVja3VzcyIsInZhbGlkaXR5X2luX3NlY29uZHMiOjYwNDgwMCwiaWF0IjoxNzI5NTk0NTcyLCJleHAiOjE3MzAxOTkzNzJ9.MJoYQWtm7qQiqZ277TuKe6SqkGtpmirSf5hOIdK5ojg"
                val userId = "Zuckuss"
                val callId = "EXigM0djeUZ7"

                // Create a user.
                val user = User(
                    id = userId, // any string
                    name = "Tutorial", // name and image are used in the UI
                    image = "https://bit.ly/2TIt8NR",
                )

                // Initialize StreamVideo. For a production app, we recommend adding the client to your Application class or di module.
                val client = StreamVideoBuilder(
                    context = applicationContext,
                    apiKey = apiKey,
                    geo = GEO.GlobalEdgeNetwork,
                    user = user,
                    token = userToken,
                ).build()

                setContent {
                    // Request permissions and join a call, which type is `default` and id is `123`.
                    val call = client.call(type = "default", id = callId)
                    LaunchCallPermissions(
                        call = call,
                        onAllPermissionsGranted = {
                            // All permissions are granted so that we can join the call.
                            val result = call.join(create = true)
                            result.onError {
                                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    )

                    // Apply VideTheme
                    VideoTheme {
                        // Define required properties.
                        val participants by call.state.participants.collectAsState()
                        val connection by call.state.connection.collectAsState()

                        // Render local and remote videos.
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (connection != RealtimeConnection.Connected) {
                                Text("Loading...", fontSize = 30.sp)
                            } else {
                                Text("Call ${call.id} has ${participants.size} participants", fontSize = 30.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

