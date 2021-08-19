package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

class RoomView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val backColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private lateinit var roomCanvas: Canvas
    private lateinit var bitmap: Bitmap

    private val paint = Paint().apply{
        color=ResourcesCompat.getColor(resources, R.color.dark_green, null)
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
    }

    private val rectpaint = Paint().apply{
        color=ResourcesCompat.getColor(resources, R.color.maroon, null)
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::bitmap.isInitialized)
            bitmap.recycle()

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        roomCanvas = Canvas(bitmap)
        val skewMatrix = Matrix()
        skewMatrix.setSkew(.40f, 0f)
        roomCanvas.setMatrix(skewMatrix)

        roomCanvas.scale(0.86f, 1f)
        roomCanvas.save()

        roomCanvas.rotate(30f, width.toFloat() / 2, height.toFloat() / 2)
        roomCanvas.drawRect(width * .25f, 100f, width.toFloat() * .75f, height.toFloat() - 500f, rectpaint)
//        roomCanvas.drawLine(0f,0f,width.toFloat(), height.toFloat(),rectpaint)

//        var currheight = 0f
//        var currWidth = 0f
//
//        while (currheight <= this.height) {
//            roomCanvas.drawLine(0f, currheight, width.toFloat(), currheight, paint)
//            currheight += 100
//        }
//
//        while (currWidth <= width.toFloat()) {
//            roomCanvas.drawLine(currWidth, 0f, currWidth, height.toFloat(), paint)
//            currWidth += 100
//        }
        roomCanvas.restore()

        skewMatrix.setSkew(-.5f, 0f)
        roomCanvas.setMatrix(skewMatrix)
        roomCanvas.save()
        roomCanvas.rotate(-30f, width.toFloat() / 2, height.toFloat() / 2)
        roomCanvas.drawRect(width * .30f, 100f, width.toFloat() * .90f, height.toFloat() - 500f, rectpaint)
        roomCanvas.restore()



    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        canvas?.save()
//        canvas?.rotate(50f, width.toFloat() / 2, height.toFloat() / 2)
//        canvas?.drawRect(200f, 250f, width.toFloat() - 150f, height.toFloat() - 150f, paint)
//        canvas?.restore()
        canvas?.drawBitmap(bitmap, 0f, 0f, null)
    }
}