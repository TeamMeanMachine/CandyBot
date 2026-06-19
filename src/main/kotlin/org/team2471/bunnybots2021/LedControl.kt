package org.team2471.bunnybots2021

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj.util.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.team2471.frc.lib.coroutines.periodic
import org.team2471.frc.lib.framework.Subsystem
import org.team2471.frc.lib.math.linearMap

object LedControl : Subsystem ("LedControl"){
    val led = AddressableLED(Leds.LED_PORT)
    val ledBuffer = AddressableLEDBuffer(Leds.LED_LENGTH)
    val controllerTestLength = 12
    val controlerTestEnabled = true
    val defaultDisabledColor = Color(255, 25, 0)
    var rainbowFirstPixelHue = 0
    var blinkCounter = 0
    var rainbowOn = false
    var blinkingOn = false
    var animationData = emptyArray<Int>()
    var animationColor = Color.kRed


    init {
        println("LEDS YAYYYYY")
        led.setLength(ledBuffer.length)

        staticRainbow(255, 2)
        //setDotted(Color.kRed, 5)

        led.setData(ledBuffer)

        led.start()

        //startBlinking(Color.kRed, 5)

        GlobalScope.launch {
            periodic(0.08) {
                //updateRainbow(255, 2)
                when {
                    rainbowOn -> updateRainbow(animationData[0], animationData[1], animationData[2])
                    blinkingOn -> updateBlink(animationColor, animationData[0], animationData[1])
                }
                if (controlerTestEnabled and !isEnabled) {
                    if (OI.driverController.b or (OI.driverController.leftThumbstick.length >= 1)) {
                        overlayDotted(Color(0.5, 0.5, 0.5), 2)
                        led.setData(ledBuffer)
                    } else {
                        overlayDotted(Color.kBlack, 2)
                        led.setData(ledBuffer)
                    }
                }
            }
        }
    }

    fun stopAnimations() {
        rainbowOn = false
        blinkingOn = false
    }

    fun startRainbow(brightness: Int = 255, rainbowCount: Int = 1, speed: Int = 1) {
        stopAnimations()
        animationData = arrayOf(brightness, rainbowCount, speed)
        rainbowOn = true
    }

    fun startBlinking(color: Color, delay: Int, brightness: Int = 255) {
        stopAnimations()
        animationColor = color
        animationData = arrayOf(delay, brightness)
        blinkingOn = true
    }

    fun staticRainbow(brightness: Int = 255, rainbowCount: Int = 1) {
        stopAnimations()
        for (i in 0 until ledBuffer.length) {
            ledBuffer.setHSV(i, linearMap(0.0, ledBuffer.length.toDouble() / rainbowCount, 0.0, 180.0, rainbowFirstPixelHue + i.toDouble()).toInt(), 255, brightness)
//            ledBuffer.setRGB(i, 255, 0, 0)
        }
        led.setData(ledBuffer)
    }

    private fun updateBlink(color: Color, delay: Int, brightness: Int = 255) {
        if (blinkCounter > delay) {
            for (i in 0 until ledBuffer.length) {
                ledBuffer.setRGB(i, (color.red * 255).toInt(), (color.green * 255).toInt(), (color.blue * 255).toInt())
            }
        } else {
            for (i in 0 until ledBuffer.length) {
                ledBuffer.setRGB(i, 0, 0, 0)
            }
        }
        led.setData(ledBuffer)
        blinkCounter += 1
        blinkCounter %= delay * 2
    }

    private fun updateRainbow(brightness: Int = 255, rainbowCount: Int = 1, speed: Int = 1) {
        for (i in 0 until ledBuffer.length) {
            ledBuffer.setHSV(i, linearMap(0.0, ledBuffer.length.toDouble() / rainbowCount, 0.0, 180.0, rainbowFirstPixelHue + i.toDouble()).toInt(), 255, brightness)
        }
        rainbowFirstPixelHue += speed
        rainbowFirstPixelHue %= 180

        led.setData(ledBuffer)
    }

    fun setSolid(color: Color) {
        stopAnimations()
        for (i in 0 until ledBuffer.length) {
            //println(color.red)
            ledBuffer.setRGB(i, (color.red * 255).toInt(), (color.green * 255).toInt(), (color.blue * 255).toInt())
        }
        led.setData(ledBuffer)
    }

    fun setDotted(color: Color, spacing: Int) {
        stopAnimations()
        for (i in 0 until ledBuffer.length) {
            if (i % spacing == 1) {
                ledBuffer.setRGB(i, (color.red * 255).toInt(), (color.green * 255).toInt(), (color.blue * 255).toInt())
                //println("dot")
            }
            else {
                ledBuffer.setRGB(i, 0, 0, 0)
                //println("no dot")
            }
        }
        led.setData(ledBuffer)
    }

    fun overlayDotted(color: Color, spacing: Int) {
        //stopAnimations()
        for (i in 0 until ledBuffer.length) {
            if (i % spacing == 1) {
                ledBuffer.setRGB(i, (color.red * 255).toInt(), (color.green * 255).toInt(), (color.blue * 255).toInt())
            }
        }
        led.setData(ledBuffer)
    }

}