package org.team2471.candybot

import com.ctre.phoenix6.controls.DutyCycleOut
import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.team2471.frc.lib.ctre.addFollower
import org.team2471.frc.lib.ctre.applyConfiguration
import org.team2471.frc.lib.ctre.brakeMode
import org.team2471.frc.lib.ctre.coastMode
import org.team2471.frc.lib.ctre.currentLimits
import org.team2471.frc.lib.ctre.inverted
import org.team2471.frc.lib.ctre.statorCurrentLimit
import org.team2471.frc.lib.util.demoSpeed
import kotlin.math.pow


object Drive: SubsystemBase("Drive") {

    val leftDrive = TalonFX(Talons.LEFT_DRIVE_ONE)
    val rightDrive = TalonFX(Talons.RIGHT_DRIVE_ONE)

    init {
        leftDrive.applyConfiguration {
            currentLimits(30.0, 40.0, 1.0)
            inverted(true)
            statorCurrentLimit(20.0)
        }
        rightDrive.applyConfiguration {
            currentLimits(30.0, 40.0, 1.0)
            inverted(false)
            statorCurrentLimit(20.0)
        }
        leftDrive.addFollower(Talons.LEFT_DRIVE_TWO)
        rightDrive.addFollower(Talons.RIGHT_DRIVE_TWO)
    }

    fun joystickDrive(): Command {
        return run {
            val forward = OI.driveTranslationY
            val turn = OI.driveRotation.pow(3)

            leftDrive.setControl(DutyCycleOut((forward - turn) * demoSpeed))
            rightDrive.setControl(DutyCycleOut((forward + turn) * demoSpeed))
        }
    }


    fun brakeMode() {
        // set all drive motors to brake mode
        leftDrive.brakeMode()
        rightDrive.brakeMode()
    }

    fun coastMode() {
        // set all drive motors to coast mode
        leftDrive.coastMode()
        rightDrive.coastMode()
    }
}
