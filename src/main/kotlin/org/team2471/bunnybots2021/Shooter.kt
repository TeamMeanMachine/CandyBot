package org.team2471.bunnybots2021

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.util.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.team2471.frc.lib.coroutines.delay
import org.team2471.frc.lib.coroutines.periodic
import org.team2471.frc.lib.framework.Subsystem

object Shooter: Subsystem("Shooter") {
    private val table = NetworkTableInstance.getDefault().getTable("Yeeter")
    private val shootTimeEntry = table.getEntry("Catapult Fling Time")
    private val feedingTimeEntry = table.getEntry("Feeder Fling Time")
    private val feedingDelayEntry = table.getEntry("Feeding Delay")
    private val yeetingEntry = table.getEntry("yeeting")
    private val feedingEntry = table.getEntry("feeding")


//    private val pneumaticHub = PneumaticHub(OtherCan.PNEUMATICHUB)
    private val shooter: Solenoid = Solenoid(OtherCan.PNEUMATICHUB, PneumaticsModuleType.CTREPCM, Solenoids.YEETER)
    private val loader: Solenoid = Solenoid(OtherCan.PNEUMATICHUB, PneumaticsModuleType.CTREPCM, Solenoids.LOADER)

    val feedDelay
        get() = feedingDelayEntry.getDouble(1.0)
    val feedTime
        get() = feedingTimeEntry.getDouble(1.0)
    val shootTime
        get() = shootTimeEntry.getDouble(1.0)


    var shooterState: Boolean
        get() = shooter.get()
        set(value) = shooter.set(value)

    var feederState: Boolean
        get() = loader.get()
        set(value) = loader.set(value)




    init {
        //set networkTableEntries and persistence if entries do not exist
        if (!shootTimeEntry.exists()) {
            shootTimeEntry.setDouble(1.0)
            shootTimeEntry.setPersistent()
        }
        if (!feedingTimeEntry.exists()) {
            feedingTimeEntry.setDouble(1.0)
            feedingTimeEntry.setPersistent()
        }
        if (!feedingDelayEntry.exists()) {
            feedingDelayEntry.setDouble(1.0)
            feedingDelayEntry.setPersistent()
        }

        GlobalScope.launch {
            periodic {
//                println("${pneumaticHub.version.hardwareMajor}")

                feedingEntry.setBoolean(feederState)
                yeetingEntry.setBoolean(shooterState)
            }
        }
    }



    suspend fun yeetThenFeed() {
        feederState = false //reset feeder if at extended state

        //shoot cargo
        shooterState = true  //shoot already loaded cargo
        delay(shootTime) //delay to keep the arm out
        shooterState = false //retract arm
        if (LedControl.isEnabled) LedControl.setSolid(Color.kRed)

        delay(feedDelay) //delay so arm is fully down

        //load next cargo
        feederState = true
        if (LedControl.isEnabled) LedControl.startBlinking(Color.kYellow, 5)
        delay(feedTime)
        feederState = false
    }

    suspend fun shoot() {
        if (shooterState) { //if arm is extended for whatever reason, will reset then do full shot.
            shooterState = false
            delay(feedDelay)
        }

        shooterState = true
        delay(shootTime)
        shooterState = false
    }




}