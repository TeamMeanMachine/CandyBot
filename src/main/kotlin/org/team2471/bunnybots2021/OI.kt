package org.team2471.bunnybots2021


import edu.wpi.first.wpilibj.util.Color
import org.team2471.frc.lib.input.*
import org.team2471.frc.lib.math.Vector2
import org.team2471.frc.lib.math.deadband

object OI {
    val driverController = XboxController(0)
    val operatorController = XboxController(1)

    private val deadBandDriver = 0.1
    private val deadBandOperator = 0.1


    private val driveTranslationX: Double
        get() = driverController.leftThumbstickX.deadband(deadBandDriver)  //.squareWithSign()

    private val driveTranslationY: Double
        get() = -driverController.leftThumbstickY.deadband(deadBandDriver)  //.squareWithSign()

    val driveTranslation: Vector2
        get() = Vector2(driveTranslationX, driveTranslationY) //does owen want this cubed?

    val driveRotation: Double
        get() = -(driverController.rightThumbstickX.deadband(deadBandDriver * 0.5)) // * 0.6

    val driveLeftTrigger: Double

        get() = driverController.leftTrigger

    val driveRightTrigger: Double
        get() = driverController.rightTrigger

    val operatorLeftTrigger: Double
        get() = operatorController.leftTrigger

    val operatorLeftY: Double
        get() = operatorController.leftThumbstickY.deadband(0.2)

    val operatorLeftX: Double
        get() = operatorController.leftThumbstickX.deadband(0.2)

    val operatorRightTrigger: Double
        get() = operatorController.rightTrigger

    val operatorRightX: Double
        get() = operatorController.rightThumbstickX.deadband(0.25)

    val operatorRightY: Double
        get() = operatorController.rightThumbstickY.deadband(0.25)

    init {
        //Drive
        driverController::back.whenTrue { Drive.zeroGyro() }
        driverController::leftTriggerFullPress.whenTrue { Shooter.yeetThenFeed() }

        driverController::a.whenTrue {
            Shooter.feederState = true
            if (LedControl.isEnabled) LedControl.startBlinking(Color.kYellow, 5)
        }
        driverController::a.whenFalse { Shooter.feederState = false }

        driverController::y.whenTrue { Shooter.shooterState = true }
        driverController::y.whenFalse {
            Shooter.shooterState = false
            if (LedControl.isEnabled) LedControl.setSolid(Color.kRed)
        }
//        driverController::a.toggleWhenTrue { Uptake.motorsOn = !Uptake.motorsOn }  testing only
//        driverController::b.toggleWhenTrue { Dumper.extendDumper = !Dumper.extendDumper }
//        ({driverController.leftTrigger > 0.1}).whileTrue { shootMode() }

    }
}