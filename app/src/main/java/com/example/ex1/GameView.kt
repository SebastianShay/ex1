package com.example.ex1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class GameView(context: Context) : View(context) {

    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // רקע
        canvas.drawColor(Color.DKGRAY)

        // רכב
        paint.color = Color.RED

        canvas.drawRect(
            400f,
            1400f,
            500f,
            1600f,
            paint
        )
    }
}