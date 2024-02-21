package com.sdv.kit.pexelsapp.data.manager

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.sdv.kit.pexelsapp.domain.manager.ImageManager
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Locale
import javax.inject.Inject

class ImageManagerImpl @Inject constructor(
    private val context: Context
) : ImageManager {

    override fun downloadImage(
        imageUrl: String,
        imageName: String,
        onError: (Exception) -> Unit
    ) {
        try {
            val inputStream = java.net.URL(imageUrl).openStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)

            saveImageToGallery(
                bitmap = bitmap,
                imageName = imageName
            )
        } catch (e: Exception) {
            onError(e)
        }
    }

    override fun saveImageToGallery(bitmap: Bitmap, imageName: String) {
        val contentResolver: ContentResolver = context.contentResolver
        val outputStream: OutputStream?
        val filename = "${imageName}_${System.currentTimeMillis()}.jpg"

        outputStream = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                getImageOutputStreamQ(
                    contentResolver = contentResolver,
                    filename = filename
                )
            }

            else -> {
                getImageOutputStreamDefault(
                    contentResolver = contentResolver,
                    filename = filename
                )
            }
        }

        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    override fun getImagesFromPicturesFolder(prefix: String?): List<Bitmap> {
        val picturesDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        if (!picturesDirectory.exists() || !picturesDirectory.isDirectory) {
            return emptyList()
        }

        val imageFiles = picturesDirectory.listFiles() ?: return emptyList()

        return imageFiles
            .filter { file ->
                file.isFile && isImageFile(file) && hasPrefix(file, prefix)
            }
            .map { file ->
                BitmapFactory.decodeFile(file.absolutePath)
            }
    }

    private fun hasPrefix(file: File, prefix: String?): Boolean {
        prefix ?: return true
        val fileName = file.name.lowercase(Locale.getDefault())
        return fileName.startsWith(prefix)
    }

    private fun isImageFile(file: File): Boolean {
        val fileName = file.name.lowercase(Locale.getDefault())

        for (extension in supportedExtensions) {
            if (fileName.endsWith(".$extension")) {
                return true
            }
        }

        return false
    }

    private fun getImageOutputStreamQ(
        contentResolver: ContentResolver,
        filename: String
    ): OutputStream? {
        contentResolver.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            return imageUri?.let { resolver.openOutputStream(it) }
        }
    }

    private fun getImageOutputStreamDefault(
        contentResolver: ContentResolver,
        filename: String
    ): FileOutputStream {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, filename)

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
        }

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(imageFile.absolutePath),
            arrayOf("image/jpeg"),
            null
        )

        return FileOutputStream(imageFile)
    }

    companion object {
        private val supportedExtensions = arrayOf("jpg", "jpeg", "png", "gif", "bmp")
    }
}