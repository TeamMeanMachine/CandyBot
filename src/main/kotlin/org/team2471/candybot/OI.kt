package org.team2471.candybot

import edu.wpi.first.math.filter.Debouncer
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj.Alert
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.team2471.frc.lib.control.LoopLogger
import org.team2471.frc.lib.control.MeanCommandXboxController
import org.team2471.frc.lib.math.deadband
import org.team2471.frc.lib.math.normalize

object OI: SubsystemBase("OI") {
//    private val table = NetworkTableInstance.getDefault().getTable("OI")

    val driverController = MeanCommandXboxController(0, false)
    val operatorController = MeanCommandXboxController(1)

    const val DRIVER_DEADBAND = 0.08
    const val OPERATOR_DEADBAND = 0.1

    val driveTranslationX: Double
        get() = driverController.leftY.deadband(DRIVER_DEADBAND)

    val driveTranslationY: Double
        get() = driverController.leftX.deadband(DRIVER_DEADBAND)

    val rawDriveTranslation: Translation2d
        get() {
            val translation = Translation2d(driveTranslationX, driveTranslationY)
            return if (translation.norm > 1.0) {
                translation.normalize()
            } else {
                translation
            }
        }

    val driveRotation: Double
        get() = -driverController.rightX.deadband(DRIVER_DEADBAND)

    val driveLeftTrigger: Double
        get() = driverController.leftTriggerAxis

    val driveLeftTriggerFullPress: Boolean
        get() = driverController.leftTriggerAxis > 0.95

    val driveRightTrigger: Double
        get() = driverController.rightTriggerAxis

    val driveRightTriggerFullPress: Boolean
        get() = driverController.rightTriggerAxis > 0.95

    val operatorLeftTrigger: Double
        get() = operatorController.leftTriggerAxis

    val operatorLeftY: Double
        get() = operatorController.leftY.deadband(OPERATOR_DEADBAND)

    val operatorLeftX: Double
        get() = operatorController.leftX.deadband(OPERATOR_DEADBAND)

    val operatorRightTrigger: Double
        get() = operatorController.rightTriggerAxis

    val operatorRightX: Double
        get() = operatorController.rightX.deadband(OPERATOR_DEADBAND)

    val operatorRightY: Double
        get() = operatorController.rightY.deadband(OPERATOR_DEADBAND)

    private val driverNotConnectedAlert: Alert = Alert("DRIVER JOYSTICK DISCONNECTED", Alert.AlertType.kError)
    private val operatorNotConnectedAlert: Alert = Alert("OPERATOR JOYSTICK DISCONNECTED", Alert.AlertType.kError)
    private val driverDebouncer = Debouncer(0.05)
    private val operatorDebouncer = Debouncer(0.05)



    init {
        println("inside OI init")

//        Drive.defaultCommand = Drive.joystickDrive()


        /**
         * Controls:
         *
         * left trigger - shoot then feed
         * a - extend feeder when held, retract when released
         * y - extend flingy arm when held, retract flingy arm when released
         *
         **/



    }

    override fun periodic() {
        LoopLogger.record("b4 OI piodc")

        driverNotConnectedAlert.set(driverDebouncer.calculate(!driverController.isConnected))
        operatorNotConnectedAlert.set(operatorDebouncer.calculate(!operatorController.isConnected))

        LoopLogger.record("OI piodc")
    }
}