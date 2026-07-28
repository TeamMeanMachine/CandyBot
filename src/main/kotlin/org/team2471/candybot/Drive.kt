package org.team2471.candybot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.team2471.frc.lib.motion.following.ArcadeDrive
import org.team2471.frc.lib.motion.following.ArcadeParameters
import org.team2471.frc.lib.units.degrees
import org.team2471.frc.lib.units.perSecond


object Drive: SubsystemBase("Drive"), ArcadeDrive {



    override val parameters = ArcadeParameters()
    override val headingRate = 0.0.degrees.perSecond //noop


    init {
        TODO("Not yet implemented")
    }


    fun driveJoystickCommand(): Command = run {
        TODO("Not yet implemented")
    }

    override fun driveOpenLoop(leftPower: Double, rightPower: Double) {
        TODO("Not yet implemented")
    }

    fun brakeMode() {
        // set all drive motors to brake mode
    }

    fun coastMode() {
        // set all drive motors to coast mode
    }
}
