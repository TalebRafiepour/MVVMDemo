package com.taleb.mvvmdemo.demo1.networking.mock

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit.client.Client
import retrofit.client.Header
import retrofit.client.Request
import retrofit.client.Response
import retrofit.mime.TypedByteArray
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class MockHttpClient(private val context: Context) : Client {
    private val headerList: MutableList<Header>

    init {
        headerList = ArrayList()
    }

    @Throws(IOException::class)
    override fun execute(request: Request): Response =

        try {
            val url = request.url

            var mockResponse: MockResponse? = null

            when {
                url.contains("registration") -> mockResponse = getResponse("registrationResponse.txt")
                url.contains("login") -> mockResponse = getResponse("loginResponse.txt")
                url.contains("account") -> mockResponse = getResponse("accountResponse.txt")
                url.contains("games") -> mockResponse = getResponse("gamesResponse.txt")
            }

            val responseBody =
                TypedByteArray("application/json", mockResponse!!.responseBody.toByteArray())

            Thread.sleep(2000)

            Response(
                request.url,
                mockResponse.responseCode,
                mockResponse.responseMessage,
                headerList,
                responseBody
            )

        } catch (e: Exception) {

            val responseBody = TypedByteArray("application/json", "".toByteArray())

            Response(
                request.url,
                500,
                "File not found",
                Collections.emptyList(),
                responseBody
            )
        }


    @Throws(Exception::class)
    fun getResponse(path: String): MockResponse {

        val inputStream = context.assets.open(path)
        val result = convertStreamToString(inputStream)

        Log.d("TEST", result)
        val responseBody = getResponseBody(result)

        Log.d("TEST", responseBody)
        val responseHeader = getResponseHeader(result)
        processResponseHeader(result)
        val responseCode = getResponseCode(responseHeader)
        val responseMessage = getResponseMessage(responseHeader)

        return MockResponse(responseCode, responseMessage, responseBody)
    }

    @Throws(JSONException::class)
    private fun getResponseBody(responseJson: String): String {

        val topLevelJsonObject = JSONObject(responseJson)
        return topLevelJsonObject.getJSONObject("Body").toString()
    }

    @Throws(JSONException::class)
    private fun getResponseCode(responseHeaderJson: JSONObject): String {

        return responseHeaderJson.getString("Code")
    }

    @Throws(JSONException::class)
    private fun getResponseMessage(responseHeaderJson: JSONObject): String {

        return responseHeaderJson.getString("Message")
    }

    @Throws(JSONException::class)
    private fun getResponseHeader(responseJson: String): JSONObject {

        val topLevelObject = JSONObject(responseJson)
        return topLevelObject.getJSONObject("Header")
    }

    @Throws(JSONException::class)
    private fun processResponseHeader(responseHeader: String) {

        headerList.clear()
        val topLevelObject = JSONObject(responseHeader)

        val responseHeaderJson = topLevelObject.getJSONObject("Header")
        val keys = responseHeaderJson.keys()

        while (keys.hasNext()) {
            val key = keys.next() as String
            val header = Header(key, responseHeaderJson.getString(key))
            headerList.add(header)
        }
    }

    @Throws(IOException::class)
    private fun convertStreamToString(inputStream: InputStream): String {
        val writer = StringWriter()
        val buffer = CharArray(2048)
        inputStream.use { inputStream ->
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

            var n: Int = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer, 0, n)
                n = reader.read(buffer)
            }
        }
        return writer.toString()
    }


    class MockResponse(
        private val responseCodeString: String,
        val responseMessage: String,
        val responseBody: String
    ) {

        val responseCode: Int
            get() = Integer.valueOf(responseCodeString)
    }
}