package org.sniffsnirr.simplephotogalery.ui.shot

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.simplephotogalery.databinding.FragmentShotBinding
import org.sniffsnirr.simplephotogalery.ui.CommonViewModel
import org.sniffsnirr.simplephotogalery.ui.map.MapsViewModel
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor

@AndroidEntryPoint
class ShotFragment : Fragment() {

    private var _binding: FragmentShotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels()
    private var imageCapture: ImageCapture? = null
    private lateinit var executor: Executor


    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_LONG)
                    .show()
                requireActivity().finish()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShotBinding.inflate(inflater, container, false)
        val root: View = binding.root
        executor = ContextCompat.getMainExecutor(requireContext())
        checkPermissions()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            takePhoto()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermissions() {
        if (PERMISSIONS.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            startCamera()
        } else launcher.launch(PERMISSIONS)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.preview.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
            )
        }, executor)
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: run {
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_LONG).show()
            return
        }
        val fileNameAndTime = getFileName()
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileNameAndTime.first)
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    viewModel.addTile(outputFileResults.savedUri.toString(), fileNameAndTime.second)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Photo do not saved!", Toast.LENGTH_LONG)
                        .show()
                    Log.d("TakePhoto exception", exception.message.toString())
                }
            }
        )
    }

    private fun getFileName(): Pair<String, Long> {
        val fileName = StringBuilder()
        fileName.append(FILE_NAME_PREFIX)
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
        val now = Date()
        val stringDate = formatter.format(now)
        fileName.append(stringDate)
        fileName.append(FILE_EXTENSION)
        return Pair(fileName.toString(), now.time)
    }

    companion object {
        private val PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SS"
        private const val FILE_NAME_PREFIX = "my_galery_"
        private const val FILE_EXTENSION = ".jpg"
        private const val MIME_TYPE = "image/jpeg"
    }
}