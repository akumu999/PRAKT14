package com.example.prakt14

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var tvMain: TextView
    private lateinit var button: Button
    private lateinit var button2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMain = findViewById(R.id.tvMain)
        button = findViewById(R.id.btn)
        button2 = findViewById(R.id.btn2)
        /*Инициальизация исполнителя для потока*/
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        button.text = "Get random joke"
        button.setOnClickListener {
            /*получение данных из потока для отображения в textView*/
            var temp = executorService.submit(Callable {
                httpRequest("https://geek-jokes.sameerkumar.website/api?format=json")
            }).get()
            executorService.execute {
                var jokes: json1 = Gson().fromJson(temp, json1::class.java)
                val imageURL = jokes.joke
                try {
                    tvMain.setText(imageURL)
                    Log.d("777", "done")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
            button2.setOnClickListener{
            val intent = Intent(this@MainActivity,MainActivity2::class.java);
            startActivity(intent);
        }
    }

    /*Функция для работы в отдельном потоке*/
    @Throws(IOException::class)
    fun httpRequest(urlString: String):String {
        /*Созддание экземпляра URL*/
        val url = URL(urlString)
        /*Создание экзепляра класса для соедниения по протоколу HTTP*/
        val connection = url.openConnection() as HttpURLConnection
        /*Установка метода запроса*/
        connection.requestMethod = "GET"
        /*Отправка запроса и чтение полученых данных*/
        var data: Int = connection.inputStream.read()
        var str = ""
        while (data != -1){
            str += data.toChar()
            data = connection.inputStream.read()
        }
        Log.d("API_QWE", str)
        return str
    }
}