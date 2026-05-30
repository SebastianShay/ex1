package com.example.roadevadergame

import android.content.Context
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val ROWS = 5
    private val COLS = 3
    private lateinit var obstacleMatrix: Array<Array<ImageView>>
    private lateinit var playerCar: ImageView
    private lateinit var carLane0: ImageView
    private lateinit var carLane2: ImageView

    // משתני הלבבות
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView

    private var lives = 3
    private var carLane = 1
    private var gameOver = false
    private val handler = Handler(Looper.getMainLooper())

    private val gameLoop = object : Runnable {
        override fun run() {
            if (!gameOver) {
                moveObstacles()
                checkCollision()
                handler.postDelayed(this, 500)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        playerCar = findViewById(R.id.playerCar)
        carLane0 = findViewById(R.id.car_lane_0)
        carLane2 = findViewById(R.id.car_lane_2)

        heart1 = findViewById(R.id.heart1)
        heart2 = findViewById(R.id.heart2)
        heart3 = findViewById(R.id.heart3)

        obstacleMatrix = Array(ROWS) { row ->
            Array(COLS) { col -> findViewById(resources.getIdentifier("obs_${row}_${col}", "id", packageName)) }
        }

        findViewById<Button>(R.id.leftBtn).setOnClickListener {
            if (carLane > 0) { carLane--; updateCarPosition() }
        }
        findViewById<Button>(R.id.rightBtn).setOnClickListener {
            if (carLane < COLS - 1) { carLane++; updateCarPosition() }
        }
        findViewById<Button>(R.id.restartBtn).setOnClickListener { restartGame() }

        handler.post(gameLoop)
    }


    private fun updateHearts() {
        heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
        heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
        heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE
    }

    private fun updateCarPosition() {
        carLane0.visibility = if (carLane == 0) View.VISIBLE else View.INVISIBLE
        playerCar.visibility = if (carLane == 1) View.VISIBLE else View.INVISIBLE
        carLane2.visibility = if (carLane == 2) View.VISIBLE else View.INVISIBLE
    }

    private fun moveObstacles() {
        for (i in ROWS - 1 downTo 1) {
            for (j in 0 until COLS) {
                obstacleMatrix[i][j].visibility = obstacleMatrix[i - 1][j].visibility
            }
        }
        for (j in 0 until COLS) obstacleMatrix[0][j].visibility = View.INVISIBLE

        var exists = false
        for (i in 0 until ROWS) for (j in 0 until COLS) if (obstacleMatrix[i][j].visibility == View.VISIBLE) exists = true

        if (!exists) obstacleMatrix[0][Random.nextInt(COLS)].visibility = View.VISIBLE
    }

    private fun checkCollision() {
        if (obstacleMatrix[ROWS - 1][carLane].visibility == View.VISIBLE) {
            vibrate()
            Toast.makeText(this, "U GOT HIT", Toast.LENGTH_SHORT).show()

            lives--
            updateHearts()

            obstacleMatrix[ROWS - 1][carLane].visibility = View.INVISIBLE

            if (lives <= 0) {
                gameOver = true
                findViewById<Button>(R.id.restartBtn).visibility = View.VISIBLE
            }
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }
    }

    private fun restartGame() {
        lives = 3
        carLane = 1
        updateHearts()
        updateCarPosition()
        gameOver = false
        findViewById<Button>(R.id.restartBtn).visibility = View.GONE
        for (row in obstacleMatrix) for (col in row) col.visibility = View.INVISIBLE
        handler.post(gameLoop)
    }
}