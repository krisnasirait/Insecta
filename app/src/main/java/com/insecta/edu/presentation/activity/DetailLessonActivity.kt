package com.insecta.edu.presentation.activity

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ar.sceneform.math.Vector3
import com.insecta.edu.R
import com.insecta.edu.adapter.ImageAdapter
import com.insecta.edu.data.Util
import com.insecta.edu.databinding.ActivityDetailLessonBinding
import com.insecta.edu.presentation.dialog.DialogFullscreenImage
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.math.Rotation
import java.io.IOException
import kotlin.math.sqrt

class DetailLessonActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener {

    private lateinit var binding: ActivityDetailLessonBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var sceneView: ArSceneView
    private lateinit var modelNode: ArModelNode

    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var initialDistance = 0f
    private var isPinching = false
    private var isBelalangModel = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = ImageAdapter(this)
        binding.rvImage.adapter = imageAdapter
        binding.rvImage.layoutManager = horizontalLayoutManager

        binding.btnChangeModel.setOnClickListener {
            changeModel()
        }

        val lessonId = intent.getStringExtra("LESSON_ID")

        val lesson = Util.dummyDetailLessonsData.find { it.lessonId == lessonId }

        lesson?.let {
            binding.tvDesc.visibility = View.VISIBLE
            binding.rvImage.visibility = View.VISIBLE

            binding.tvTitle.text = it.lessonTitle
            binding.tvDesc.text = it.lessonDescription
            imageAdapter.setData(it.lessonImages)

            mediaPlayer = MediaPlayer().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                } else {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                }

                try {
                    val assetFileDescriptor = assets.openFd("${it.lessonAudio}.m4a")
                    setDataSource(
                        assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length
                    )
                    prepareAsync()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        binding.ivPlayAudio.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
        }

        mediaPlayer.setOnCompletionListener {
            it.seekTo(0)
        }

        binding.ivAr.setOnClickListener {
            loadModel()
            binding.btnChangeModel.visibility = View.VISIBLE
            binding.sceneView.visibility = View.VISIBLE
            binding.tvDesc.visibility = View.GONE
            binding.rvImage.visibility = View.GONE
        }

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (lessonId == "4") {
            binding.ivAr.visibility = View.VISIBLE
            sceneView = findViewById(R.id.sceneView)

            modelNode = ArModelNode().apply {
                loadModelGlbAsync(
                    glbFileLocation = "models/metamorfosis_belalang.glb"
                ) {
                    loadInitialModel()
                }
            }
            sceneView.addChild(modelNode)
        } else {
            binding.ivAr.visibility = View.GONE
        }
    }

    private fun changeModel() {
        val newModelFile = if (isBelalangModel) "metamorfosis_kupu-kupu.glb" else "metamorfosis_belalang.glb"

        val newModelNode = ArModelNode()
        newModelNode.loadModelGlbAsync(glbFileLocation = newModelFile) {
            sceneView.removeChild(modelNode)
            sceneView.addChild(newModelNode)
            modelNode = newModelNode
            sceneView.planeRenderer.isVisible = true
            setTouchListenerToSceneView()
        }
        isBelalangModel = !isBelalangModel
    }

    private fun setTouchListenerToSceneView() {
        sceneView.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    initialDistance = getFingerSpacing(event)
                    isPinching = true
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    isPinching = false
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isPinching) {
                        val distance = getFingerSpacing(event)
                        val scale = distance / initialDistance
                        zoomModel(scale)
                        initialDistance = distance
                    } else {
                        val dx = event.x - lastX
                        val dy = event.y - lastY
                        rotateModel(dx, dy)
                    }
                    lastX = event.x
                    lastY = event.y
                }
            }
            true
        }
    }

    private fun loadInitialModel() {
        modelNode.loadModelGlbAsync(
            glbFileLocation = "models/metamorfosis_belalang.glb"
        ) {
            sceneView.planeRenderer.isVisible = true
            setTouchListenerToSceneView()
        }
    }

    private fun getFingerSpacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    private fun zoomModel(scale: Float) {
        val currentScale = modelNode.scale

        val newScale = currentScale * scale

        modelNode.scale = newScale
    }

    private fun loadModel() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false
    }

    private fun rotateModel(dx: Float, dy: Float) {
        val rotationSpeed = 0.5f

        val currentRotation = modelNode.modelRotation
        val xRotation = currentRotation.x + dy * rotationSpeed
        val yRotation = currentRotation.y + dx * rotationSpeed

        modelNode.modelRotation = Rotation(xRotation, yRotation, currentRotation.z)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onItemClick(position: Int, imageUrl: String) {
        val dialog = DialogFullscreenImage.newInstance(imageUrl)
        dialog.show(supportFragmentManager, "fullscreenDialog")
    }
}