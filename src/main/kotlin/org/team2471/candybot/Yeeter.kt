package org.team2471.candybot

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Yeeter: SubsystemBase("Yeeter")  {

    private val table = NetworkTableInstance.getDefault().getTable("Yeeter")
    private val yeetingEntry = table.getEntry("Yeeter")
    private val feedingEntry = table.getEntry("Feeding")


    val yeeter = Solenoid(OtherCan.PNEUMATIC_HUB, PneumaticsModuleType.REVPH, Solenoids.YEETER)
    val feeder = Solenoid(OtherCan.PNEUMATIC_HUB, PneumaticsModuleType.REVPH, Solenoids.FEEDER)

    var yeeterState: Boolean = false
        get() = yeeter.get()
        set(value) {
            yeeter.set(value)
            field = value
        }
    var feederState: Boolean = false
        get() = feeder.get()
        set(value) {
            feeder.set(value)
            field = value
        }

    override fun periodic() {
        yeetingEntry.setBoolean(yeeterState)
        feedingEntry.setBoolean(feederState)
    }

    init {
        println("Yeeter init")

    }
}