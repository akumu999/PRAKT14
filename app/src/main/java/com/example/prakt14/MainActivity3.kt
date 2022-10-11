package com.example.prakt14

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.jvm.Throws
import kotlin.random.Random

class MainActivity3 : AppCompatActivity() {
    private lateinit var button2: Button
    private lateinit var button1: Button
    private lateinit var DogsImage: ImageView
    private lateinit var AnswerText: TextView

    private var rand1 = Random(2)

    var handler = Handler(Looper.getMainLooper())
    var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        button1 = findViewById(R.id.btn)
        button2 = findViewById(R.id.btn2)
        DogsImage = findViewById(R.id.iw)
        AnswerText = findViewById(R.id.tv)


        val exec: ExecutorService = Executors.newSingleThreadExecutor()
        val e: ExecutorService = Executors.newSingleThreadExecutor()
        val e2: ExecutorService = Executors.newSingleThreadExecutor()

        button1.text = "GET YES OR NO"


        button1.setOnClickListener {

            Log.d("1", "1")

            var temp = exec.submit(Callable {
                http("https://yesno.wtf/api")
            }).get()

            Log.d("123", temp.toString())

            var answer: json3 = Gson().fromJson(temp, json3::class.java)

            e.execute {
                val imageURL = answer.image
                try {
                    val `in` = URL(imageURL).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        DogsImage.setImageBitmap(image)
                    }
                    Log.d("321", "done")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            e2.execute {
                val imageURL = answer.answer
                try {
                    AnswerText.setText(imageURL)
                    Log.d("777", "done")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    @Throws(IOException::class)
    fun http(url: String): String {
        val url1 = URL(url)
        val con = url1.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        var data: Int = con.inputStream.read()
        var str = ""
        while (data != -1) {
            str += data.toChar()
            data = con.inputStream.read()
        }
        return str
    }
}