package com.example.rss

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var rvMain: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    var sof_Data = mutableListOf<StackoverflowData>()
    private lateinit var arr: ArrayList<StackoverflowData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arr = arrayListOf()
        rvMain = findViewById<RecyclerView>(R.id.rv)
        rvAdapter = RVAdapter(arr)
        rvMain.adapter = rvAdapter
        rvMain.layoutManager = LinearLayoutManager(this)
        FetchTopSongs().execute()
    }
    private inner class FetchTopSongs : AsyncTask<Void, Void, MutableList<StackoverflowData>>() {
        val parser = RSS_Reader()
        override fun doInBackground(vararg params: Void?): MutableList<StackoverflowData> {
            val url = URL("https://stackoverflow.com/feeds/xml")
            val urlConnection = url.openConnection() as HttpURLConnection
            sof_Data =
                urlConnection.getInputStream()?.let {
                    parser.parse(it)
                }
                        as MutableList<StackoverflowData>
//            arr.add(sof_Data)
            return sof_Data
        }
        override fun onPostExecute(result: MutableList<StackoverflowData>?) {
            super.onPostExecute(result)
            val adapter =
                ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, sof_Data)
//            rvMain.adapter = adapter
        }

    }

}