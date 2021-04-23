package com.example.dangeroussituationrecognition


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.robotemi.sdk.Robot
import com.robotemi.sdk.exception.OnSdkExceptionListener
import com.robotemi.sdk.face.OnFaceRecognizedListener
import com.robotemi.sdk.listeners.*
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener
import com.robotemi.sdk.permission.OnRequestPermissionResultListener
import com.robotemi.sdk.sequence.OnSequencePlayStatusChangedListener

import com.example.dangeroussituationrecognition.customview.OverlayView
import com.example.dangeroussituationrecognition.customview.OverlayView.DrawCallback
import com.example.dangeroussituationrecognition.env.BorderedText
import com.example.dangeroussituationrecognition.env.ImageUtils
import com.example.dangeroussituationrecognition.env.Logger
import org.tensorflow.lite.examples.detection.tflite.Detector
import org.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel
import com.example.dangeroussituationrecognition.tracking.MultiBoxTracker


// This is an arbitrary number using to keep tab of the permission
private const val REQUEST_CODE_PERMISSIONS = 10
// This is an array of all the permission specified in the manifest
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"


class MainActivity : AppCompatActivity(){

//public abstract class MainActivity: AppCompatActivity(),
//Robot.NlpListener,
//OnRobotReadyListener,
//Robot.ConversationViewAttachesListener,
//Robot.WakeupWordListener,
//Robot.ActivityStreamPublishListener,
//Robot.TtsListener,
//OnBeWithMeStatusChangedListener,
//OnGoToLocationStatusChangedListener,
//OnLocationsUpdatedListener,
//OnConstraintBeWithStatusChangedListener,
//OnDetectionStateChangedListener,
//Robot.AsrListener,
//OnTelepresenceEventChangedListener,
//OnRequestPermissionResultListener,
//OnDistanceToLocationChangedListener,
//OnCurrentPositionChangedListener,
//OnSequencePlayStatusChangedListener,
//OnRobotLiftedListener,
//OnDetectionDataChangedListener,
//OnUserInteractionChangedListener,
//OnFaceRecognizedListener,
//OnSdkExceptionListener{

    val TAG = "myTag"

    private var robot: Robot? = null

//    /**
//     * Setting up all the event listeners
//     */
//    override fun onStart() {
//        super.onStart()
//        robot!!.addOnRobotReadyListener(this)
//        robot!!.addNlpListener(this)
//        robot!!.addOnGoToLocationStatusChangedListener(this)
//        robot!!.addConversationViewAttachesListenerListener(this)
//        robot!!.addWakeupWordListener(this)
//        robot!!.addTtsListener(this)
//        robot!!.addOnLocationsUpdatedListener(this)
//        robot!!.addOnConstraintBeWithStatusChangedListener(this)
//        robot!!.addOnDetectionStateChangedListener(this)
//        robot!!.addAsrListener(this)
//        robot!!.addOnDistanceToLocationChangedListener(this)
//        robot!!.addOnCurrentPositionChangedListener(this)
//        robot!!.addOnSequencePlayStatusChangedListener(this)
//        robot!!.addOnRobotLiftedListener(this)
//        robot!!.addOnDetectionDataChangedListener(this)
//        robot!!.addOnUserInteractionChangedListener(this)
//        robot!!.showTopBar()
//    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        robot = Robot.getInstance() // get an instance of the robot in order to begin using its features.
//        robot!!.addOnRequestPermissionResultListener(this)
//        robot!!.addOnTelepresenceEventChangedListener(this)
//        robot!!.addOnFaceRecognizedListener(this)
//        robot!!.addOnSdkExceptionListener(this)



//        // Preview
//        viewFinder = findViewById(R.id.view_finder)
//
        // Request camera permissions
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
            Log.d(TAG, "onCreate: Start Camera")
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }





    }


    /**
     * tiltAngle controls temi's head by specifying which angle you want
     * to tilt to and at which speed.
     */
    fun tiltAngle(view: View?) {
        robot!!.tiltAngle(-15)
    }








    /**Camera
     */
    private lateinit var viewFinder: TextureView




    private fun startCamera() {
        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 640))
        }.build()

        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            //viewFinder.surfaceTexture = it.surfaceTexture
            viewFinder.setSurfaceTexture(it.surfaceTexture)
            updateTransform()
        }

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(this, preview)



    }


    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
        viewFinder.scaleX = -1f
    }




    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }











//    /** Called when the user taps the Send button */
//    fun sendMessage(view: View) {
//        val editText = findViewById<EditText>(R.id.editText)
//        val message = editText.text.toString()
//        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
//        }
//        startActivity(intent)
//    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Stop the Activity")
    }

}

