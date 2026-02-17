package com.example.bild_atr_vieta

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent


class AudioActivity : AppCompatActivity() {

    private lateinit var recorder: MediaRecorder
    private lateinit var outputFile: String
    private lateinit var listView: ListView
    private val recordings = mutableListOf<String>()
    private var isRecording = false
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                200
            )
        }
        analytics = Firebase.analytics
        val btnRecord = findViewById<Button>(R.id.btnRecord)
        listView = findViewById(R.id.listView)

//        btnRecord.setOnClickListener {
//            startRecording()
//        }
        btnRecord.setOnClickListener {

            if (!isRecording) {
                startRecording()
                btnRecord.text = "Stop"
                isRecording = true
            } else {
                stopRecording()
                btnRecord.text = "Record"
                isRecording = false
            }
        }

    }

    private fun startRecording() {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "start");
            param(FirebaseAnalytics.Param.ITEM_NAME, "stop");
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        }
        val dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        outputFile = "${dir?.absolutePath}/record_${System.currentTimeMillis()}.3gp"

        recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.setOutputFile(outputFile)

        recorder.prepare()
        recorder.start()

        Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show()
    }
    private fun stopRecording() {

        recorder.stop()
        recorder.release()

        recordings.add(outputFile)

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            recordings
        )

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.audio_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "ok");
            param(FirebaseAnalytics.Param.ITEM_NAME, "bet");
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        }
        when (item.itemId) {

            R.id.menu_image -> {
                finish() // atgriežas uz MainActivity
            }

            R.id.menu_delete -> {
                deleteAllRecordings()
            }
        }

        return true
    }
    private fun deleteAllRecordings() {

        val dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)

        dir?.listFiles()?.forEach { file ->
            if (file.extension == "3gp") {
                file.delete()
            }
        }

        recordings.clear()
        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            recordings
        )

        Toast.makeText(this, "All recordings deleted", Toast.LENGTH_SHORT).show()
    }

}
