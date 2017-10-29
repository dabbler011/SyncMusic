package com.example.hacks17

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.lang.Math.toIntExact

/**
 * Created by akshat on 29/10/17.
 */
 class SyncInit {

   fun send(timestamp: Long,name: String,userName: String,context: Context)
    {
        doAsync {
            val JSON: MediaType = MediaType.parse("application/json; charset=utf-8");

            val client = OkHttpClient()

            val bodyString = "{\"name\":\""+name +"\",\"timestamp\":"+timestamp+"}"

            val body = RequestBody.create(JSON,bodyString)
            try {
                val request = Request.Builder()
                        .url("https://syncit-33de3.firebaseio.com/"+userName+".json")
                        .post(body)
                        .build()
                val response = client.newCall(request).execute()
            } catch (e : Exception) {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun retrieve (name: String, context: Context, typedList: List<AudioModel>) : SyncModel{
        var model = SyncModel()

        doAsync {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("https://syncit-33de3.firebaseio.com/"+name+".json")
                    .build()
            val response = client.newCall(request).execute()
            try {
                if (response.isSuccessful) {
                    val body = JSONObject(response.body()?.string())
                    val keys = body.keys()

                    while (keys.hasNext()) {

                        val key = keys.next().toString()
                        val childObj = body.getJSONObject(key)
                        if (childObj != null) {
                            model.name = childObj.getString("name")
                            model.timestamp = childObj.getLong("timestamp")
                        }
                    }
                    uiThread {
                        val song = model.name
                        var required: AudioModel? = null
                        Toast.makeText(context, song + "gv " + model.timestamp, Toast.LENGTH_SHORT).show()
                        val it = typedList.iterator()
                        while (it.hasNext()) {
                            required = it.next()
                            if (required!!.getaArtist() == song) {
                                break
                            }
                            required = null
                        }
                        if (required != null) {
                            val mp = MediaPlayer()
                            mp.setDataSource(required.getaAlbum())
                            mp.prepare()
                            mp.seekTo(toIntExact(System.currentTimeMillis()-model.timestamp-15));
                            mp.start();
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }

        return model
    }
}