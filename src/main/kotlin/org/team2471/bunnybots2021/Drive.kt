package org.team2471.bunnybots2021

import com.ctre.phoenix6.signals.NeutralModeValue
import edu.wpi.first.networktables.NetworkTableInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.team2471.frc.lib.actuators.FalconID
import org.team2471.frc.lib.actuators.MotorController
import org.team2471.frc.lib.coroutines.*
import org.team2471.frc.lib.framework.Subsystem
import org.team2471.frc.lib.motion.following.ArcadeDrive
import org.team2471.frc.lib.motion.following.hybridDrive
import org.team2471.frc.lib.motion_profiling.following.ArcadeParameters
import org.team2471.frc.lib.units.*
import kotlin.math.absoluteValue

object Drive : Subsystem("Drive"), ArcadeDrive {

    val navXGyroEntry = NetworkTableInstance.getDefault().getTable(name).getEntry("NavX Gyro")

    private val leftDriveMotor = MotorController(FalconID(Falcons.LEFT_DRIVE_ONE), FalconID(Falcons.LEFT_DRIVE_TWO))
    private val rightDriveMotor = MotorController(FalconID(Falcons.RIGHT_DRIVE_ONE), FalconID(Falcons.RIGHT_DRIVE_TWO))

    //    val gyro: Gyro? = null
//    private var navX: NavxWrapper? = NavxWrapper()
//    private var analogDevices: ADXRS450_Gyro? = ADXRS450_Gyro()
//    private var meanGyro : TheBestGyroEver? = TheBestGyroEver()
//    val gyro: NavxWrapper? = NavxWrapper()
//    val gyro = ADXRS450_Gyro()

//    val gyro = navX /*if (navXGyroEntry.getBoolean(true) && navX != null) navX else if (analogDevices != null) analogDevices else meanGyro*/

    private var gyroOffset = 0.0.degrees

    override var heading: Angle
        get() = gyroOffset - (0.0.degrees)
        set(value) {
            gyroOffset = value
//            gyro.reset()
        }

    override val headingRate: AngularVelocity
        get() = 0.0.degrees.perSecond

    override val parameters = ArcadeParameters(
        trackWidth = 21.0.inches.asFeet,
        scrubFactor = 1.0,
        leftFeedForwardCoefficient = 0.0,
        leftFeedForwardOffset = 0.0,
        rightFeedForwardCoefficient = 0.0,
        rightFeedForwardOffset = 0.0,
        driveTurningP = 0.01,
        headingFeedForward = 0.0,
    )

    override fun startFollowing() {
        super.startFollowing()
        leftDriveMotor.position = 0.0
        rightDriveMotor.position = 0.0
    }

    override fun driveOpenLoop(leftPower: Double, rightPower: Double) {
        leftDriveMotor.setPercentOutput(leftPower)
        rightDriveMotor.setPercentOutput(rightPower)
    }

    override fun driveClosedLoop(
        leftDistance: Double,
        leftFeedForward: Double,
        rightDistance: Double,
        rightFeedForward: Double
    ) {
        println("l:[$leftDistance] r:[$rightDistance]           d: ${leftDistance - rightDistance}")
        leftDriveMotor.setPositionSetpoint(leftDistance, leftFeedForward)
        rightDriveMotor.setPositionSetpoint(rightDistance, rightFeedForward)
    }

    init {
        println("drive init")
        //SmartDashboard.putData("Gyro", gyro!!.getNavX())

        leftDriveMotor.config {
            inverted(true)
            followersInverted(true)
            brakeMode()
            ctreFollowers.first().setNeutralMode(NeutralModeValue.Coast)
            feedbackCoefficient = DriveTrainConstants.FEEDBACK_COEFFICIENT
            pid {
                p(1e-9)
                i(0.000)
              d(0.00000)
            }
        }
        rightDriveMotor.config {
            inverted(false)
            followersInverted(false)
            brakeMode()
            ctreFollowers.first().setNeutralMode(NeutralModeValue.Coast)
            feedbackCoefficient = DriveTrainConstants.FEEDBACK_COEFFICIENT
            pid {
                p(1e-9)
                i(0.0000)
                 d(0.00000)
            }
        }


/*

        try {
            analogDevices = ADXRS450_Gyro()
        } catch(ex: Exception) {
            analogDevices = null
            println("Analog Devices is having troubles. $ex")
        }
*/

        GlobalScope.launch(MeanlibDispatcher) {
            println("in drive global scope")
            val table = NetworkTableInstance.getDefault().getTable(name)

            val headingEntry = table.getEntry("Heading")
            val xEntry = table.getEntry("X")
            val yEntry = table.getEntry("Y")

            periodic {
//                val (x, y) = position
//                xEntry.setDouble(x)
//                yEntry.setDouble(y)
                headingEntry.setDouble(heading.asDegrees)
            }
        }
    }

    fun zeroGyro() = {}



    override suspend fun default() {
        periodic {
            var turn = OI.driveRotation
//            printEncoderValues()
//            println("Test hybridDrive")
            hybridDrive(OI.driveTranslation.y * 0.7,
                if (OI.driveTranslation.y.absoluteValue > 1.0e-3) 0.40 * turn else 0.0,  // driving turn
                if (OI.driveTranslation.y.absoluteValue < 1.0e-3) 0.30 * turn else 0.0   // turn in place
            )
//            println("gyro: ${gyro.angle}")
        }
    }

    fun brakeMode() {
//        for (moduleCount in 0..3) {
//            val module = (modules[moduleCount] as Module)
//            module.driveMotor.brakeMode()
//        }
    }

    fun coastMode() {
    }



}
