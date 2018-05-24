package it.lamba.imagesadapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import it.lamba.utilslibrary.Utils.scaleSizes
import it.lamba.utilslibrary.inflate
import kotlinx.android.synthetic.main.image_layout.view.*
import java.io.File

/**
 * Allow to display images as a slideshow inside a [RecyclerView].
 * @param allowDelete set to <code>true</code> to allow deletion of an image.
 * @param context current Android context
 */
class ImagesAdapter(private val context: Context, private val allowDelete: Boolean = true): RecyclerView.Adapter<ImagesAdapter.ImageVH>()  {

    private val images = ArrayList<FileContainer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageVH(parent.inflate(R.layout.image_layout).apply {
            if(allowDelete) {
                remove_image.setOnClickListener {
                    val index = remove_image.tag as Int
                    if (!images[index].isImageNew) {
                        if (!images[index].willBeDelete) {
                            this.alpha_loading.visibility = VISIBLE
                            (it as FloatingActionButton).setImageResource(R.drawable.ic_add_black_24dp)
                            images[index].willBeDelete = true
                        } else {
                            this.alpha_loading.visibility = GONE
                            (it as FloatingActionButton).setImageResource(R.drawable.ic_close)
                            images[index].willBeDelete = false
                        }
                    } else {
                        images.removeAt(index)
                        notifyDataSetChanged()
                    }
                }
            } else remove_image.hide()
        })

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder:ImageVH, position: Int) = holder.bind(images[position], position, context)

    /**
     * Adds an image in the end of the adapter.
     * @param file the image file you need to display
     * @param isImageNew set to <code>true</code> to blur the image instead of deleting it.
     */
    fun add(file: File, isImageNew: Boolean = true){
        images.add(FileContainer(file, isImageNew))
        notifyItemInserted(images.size)
    }

    /**
     * Adds all images in the end of the adapter.
     * @param files the list of image files you need to display
     * @param isImageNew set to <code>true</code> to blur the images instead of deleting them.
     */
    fun addAll(files: List<File>, isImageNew: Boolean = true){
        files.forEach {
            add(it, isImageNew)
        }
    }

    /**
     * Returns the images in the adapter based on the type you asked. Blurred new images won't be returned.
     * @param type the image types you need to get. See [ImagesType]s
     * @return The list of files, may be empty.
     */
    fun getImages(type: ImagesType = ImagesType.NewImages): ArrayList<File> {
        val toReturn = ArrayList<File>()
        when(type){
            ImagesType.NewImages -> {
                images.forEach{
                    if(it.isImageNew) toReturn.add(it.imageFile)
                }
            }
            ImagesType.OldImages -> {
                images.forEach {
                    if(!it.isImageNew && !it.willBeDelete) toReturn.add(it.imageFile)
                }
            }
        }
        return toReturn
    }

    /**
     * Types of images available
     */
    abstract class ImagesType {

        object NewImages: ImagesType()
        object OldImages: ImagesType()
    }

    /**
     * Commodity container for an image file
     */
    class FileContainer(var imageFile: File, var isImageNew: Boolean, var willBeDelete: Boolean = false)

    class ImageVH(private val v: View): RecyclerView.ViewHolder(v){
        fun bind(fileContainer: FileContainer, position: Int, context: Context){
            v.remove_image.tag = position
            scaleSizes(fileContainer.imageFile,200f, context.resources).apply {
                val lp = v.image_content.layoutParams
                lp.width = this.x
                lp.height = this.y
                v.image_content.layoutParams = lp
                v.image_content.setImageBitmap(
                        Bitmap.createScaledBitmap(
                                BitmapFactory.decodeFile(fileContainer.imageFile.path),
                                this.x, this.y, false)
                )
            }
        }
    }
}