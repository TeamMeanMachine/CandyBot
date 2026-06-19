package org.team2471.bunnybots2021.testing

import edu.wpi.first.wpilibj.Timer
import org.team2471.bunnybots2021.Drive
import org.team2471.bunnybots2021.OI
import org.team2471.frc.lib.framework.use
import org.team2471.frc.lib.motion.following.tuneDrivePositionController

//

suspend fun Drive.driveTests() = use(this) {

}

suspend fun Drive.tuneDrivePositionController() = use(Drive) {

    tuneDrivePositionController(OI.driverController)
}


suspend fun Drive.SimpleDriveForwardTest() = use(Drive) {
    Drive.driveOpenLoop(0.3, 0.3)
    Timer.delay(10.0)
}

