@file:JvmName("Main")

package org.team2471.bunnybots2021

import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.Color
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.team2471.bunnybots2021.Shooter.feedDelay
import org.team2471.bunnybots2021.Shooter.feedTime
import org.team2471.bunnybots2021.Shooter.feederState
import org.team2471.bunnybots2021.Shooter.shootTime
import org.team2471.bunnybots2021.Shooter.shooterState
import org.team2471.frc.lib.coroutines.parallel
import org.team2471.frc.lib.coroutines.suspendUntil
import org.team2471.frc.lib.framework.MeanlibRobot
import org.team2471.frc.lib.units.degrees
import java.net.NetworkInterface


object Robot : MeanlibRobot() {
    var startMeasureTime = getSystemTimeSeconds()
    var lastMeasureTime = startMeasureTime
    var isCompBot = true

    val inComp = false

    init {
        val networkInterfaces =  NetworkInterface.getNetworkInterfaces()
        println("retrieving network interfaces")
//        for (iFace in networkInterfaces) {
//            println(iFace.name)
//            if (iFace.name == "eth0") {
//                println("NETWORK NAME--->${iFace.name}<----")
//                var macString = ""
//                for (byteVal in iFace.hardwareAddress){
//                    macString += String.format("%s", byteVal)
//                }
//                println("FORMATTED---->$macString<-----")
//
//                isCompBot = (macString != "0-1284751573")
//                println("I am compbot = $isCompBot")
//            }
//        }

        // i heard the first string + double concatenations were expensive...
//        repeat(25) {
//            println("RANDOM NUMBER: ${Math.random()}")
//        }
        println("NEVER GONNA GIVE YOU UP")

        LedControl
        LedControl.startBlinking(Color.kRed, 2, 200)

        OI
        println("Activating OI! ${OI.driverController.leftThumbstickX}")
        Drive
        Drive.zeroGyro()
        Drive.heading = 0.0.degrees
        println("Activating Drive! heading = ${Drive.heading}")
        Shooter

        println("Activated Shooter! Shoot Time: $shootTime  Feed Time: $feedTime  Feed Delay: $feedDelay  Solenoid states: $shooterState $feederState")


        // drop down menu for selecting tests
        val testChooser = SendableChooser<String?>().apply {
            setDefaultOption("None", null)
            addOption("Drive Tests", "Drive Tests")
        }
        SmartDashboard.putData("RobotTests", testChooser)
        LedControl.setSolid(LedControl.defaultDisabledColor)
    }

    override suspend fun enable() {
        initTimeMeasurement()
        println("starting enable")
        var done = false
        GlobalScope.launch {
            parallel(
                {Drive.enable(); println("after drive ${totalTimeTaken()}")},
                {Shooter.enable(); println("after shooter ${totalTimeTaken()}")},
                {LedControl.enable(); println("after Leds ${totalTimeTaken()}")}
            )
            done = true
        }
        suspendUntil { done }
//        println("field centric? ${SmartDashboard.getBoolean("Use Gyro", true) && !DriverStation.isAutonomous()}")
        println("ending enable ${totalTimeTaken()}")
        LedControl.setSolid(Color.kRed)
    }

    override suspend fun autonomous() {
        println("autonomous starting")
    }

    override suspend fun teleop() {
        println("telop begin")
    }

    override suspend fun test()  {
        println("test mode begin. Hi.")
//        Drive.driveTests()
//        Drive.steeringTests()
//        Pivot.feedForwardTest()
        println("test mode done")
    }


    override suspend fun disable() {
        Drive.disable()
        Shooter.disable()
        LedControl.disable()

        OI.driverController.rumble = 0.0
        OI.operatorController.rumble = 0.0
        LedControl.setSolid(LedControl.defaultDisabledColor)
    }

    fun getSystemTimeSeconds(): Double {
        return System.currentTimeMillis() / 1000.0
    }

    private fun initTimeMeasurement() {
        startMeasureTime = getSystemTimeSeconds()
        lastMeasureTime = startMeasureTime
    }

    private fun updateSecondsTaken() {
        lastMeasureTime = getSystemTimeSeconds()
    }

    fun totalTimeTaken(): Double {
        return getSystemTimeSeconds() - startMeasureTime
    }

    fun recentTimeTaken(): Double {
        val timeTaken = getSystemTimeSeconds() - lastMeasureTime
        updateSecondsTaken()
        return timeTaken.toDouble()
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    println("start robot")
    RobotBase.startRobot { Robot }
}