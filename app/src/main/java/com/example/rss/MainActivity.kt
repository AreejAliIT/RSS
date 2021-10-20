package com.example.rss


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

// data
class StackoverflowData {
    var name: String = ""
    override fun toString(): String {
        return """name = $name""".trimIndent()
    }
}

class MainActivity : AppCompatActivity() {

    lateinit var rvMain: RecyclerView
    var sof_Data = mutableListOf<StackoverflowData>()
    private lateinit var arr: ArrayList<StackoverflowData>
    val feedURL = "https://stackoverflow.com/feeds"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Request API
        requestApi(feedURL)

        rvMain = findViewById(R.id.rv)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.setHasFixedSize(true)

    }


    private fun downloadXML(urlPath: String?): String {
        val xmlResult = StringBuilder()

        try {
            val url = URL(urlPath)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val response = connection.responseCode
            Log.d("MainActivity", "downloadXML: The response code was $response")

            val reader = BufferedReader(InputStreamReader(connection.inputStream))

            val inputBuffer = CharArray(500)
            var charsRead = 0
            while (charsRead >= 0) {
                charsRead = reader.read(inputBuffer)
                if (charsRead > 0) {
                    xmlResult.append(String(inputBuffer, 0, charsRead))
                }
            }
            reader.close()

            Log.d("MainActivity", "Received ${xmlResult.length} bytes")
            return xmlResult.toString()
        } catch (e: MalformedURLException) {
            Log.e("MainActivity", "downloadXML: Invalid URL ${e.message}")
        } catch (e: IOException) {
            Log.e("MainActivity", "downloadXML: IO Exception reading data: ${e.message}")
        } catch (e: SecurityException) {
            e.printStackTrace()
            Log.e("MainActivity", "downloadXML: Security exception.  Needs permissions? ${e.message}")
        } catch (e: Exception) {
            Log.e("MainActivity", "Unknown error: ${e.message}")
        }
        return ""
    }

    private fun requestApi(url:String){

        var listItems = ArrayList<StackoverflowData>()
        CoroutineScope(Dispatchers.IO).launch {
            val rssFeed = async { downloadXML(url) }.await()
            if (rssFeed.isEmpty()) {
                Log.e("MainActivity", "requestApi fun: Error downloading")
            } else {
                val parseApplications = async { RSS_Reader() }.await()
                parseApplications.parse(rssFeed)
                listItems = parseApplications.getParsedList()

                withContext(Dispatchers.Main) {
                    // RVA  class
                    rvMain.adapter = RVAdapter(listItems)
                }
            }
        }
    }

//    private inner class FetchTopSongs : AsyncTask<Void, Void, MutableList<StackoverflowData>>() {
//        val parser = RSS_Reader()
//        override fun doInBackground(vararg params: Void?): MutableList<StackoverflowData> {
//            val url = URL("https://stackoverflow.com/feeds/xml")
//            val urlConnection = url.openConnection() as HttpURLConnection
//            sof_Data =
//                urlConnection.getInputStream()?.let {
//                    parser.parse(it)
//                }
//                        as MutableList<StackoverflowData>
////            arr.add(sof_Data)
//            return sof_Data
//        }
//        override fun onPostExecute(result: MutableList<StackoverflowData>?) {
//            super.onPostExecute(result)
//            val adapter =
//                ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, sof_Data)
////            rvMain.adapter = adapter
//        }
//
//    }

}