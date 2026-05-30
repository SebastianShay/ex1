package com.example.roadevadergame

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var car: View
    private lateinit var enemy: View
    private lateinit var leftBtn: Button
    private lateinit var rightBtn: Button
    private lateinit var restartBtn: Button
    private lateinit var livesText: TextView

    private var lives = 3
    private var carX = 300f
    private var gameOver = false

    private val handler = Handler()

    private val gameLoop = object : Runnable {
        override fun run() {
            if (!gameOver) {
                moveEnemy()
                checkCollision()
                handler.postDelayed(this, 30)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        car = findViewById(R.id.car)
        enemy = findViewById(R.id.enemy)

        leftBtn = findViewById(R.id.leftBtn)
        rightBtn = findViewById(R.id.rightBtn)
        restartBtn = findViewById(R.id.restartBtn)
        livesText = findViewById(R.id.livesText)

        updateLives()

        car.x = carX

        leftBtn.setOnClickListener {
            if (!gameOver) {
                carX -= 50f
                car.x = carX
            }
        }

        rightBtn.setOnClickListener {
            if (!gameOver) {
                carX += 50f
                car.x = carX
            }
        }

        restartBtn.setOnClickListener {
            restartGame()
        }

        handler.post(gameLoop)
    }

    private fun moveEnemy() {
        var y = enemy.y
        y += 20

        if (y > 2000) {
            y = -200f
        }

        enemy.y = y
    }

    private fun checkCollision() {
        if (isColliding(car, enemy)) {

            enemy.y = -200f

            lives--
            updateLives()

            Toast.makeText(this, "נפגעת! ירד חיים", Toast.LENGTH_SHORT).show()

            if (lives <= 0) {
                gameOver = true
                restartBtn.visibility = View.VISIBLE
                Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isColliding(a: View, b: View): Boolean {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y
    }

    private fun updateLives() {
        livesText.text = "Lives: $lives"
    }

    private fun restartGame() {
        lives = 3
        gameOver = false
        carX = 300f

        car.x = carX
        enemy.y = -200f

        restartBtn.visibility = View.GONE

        updateLives()
        handler.post(gameLoop)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(gameLoop)
    }
}