package org.team2471.candybot


//object Leds {
//    const val LED_PORT = 0
//    const val LED_LENGTH = 60
//}

object Talons {
    const val LEFT_DRIVE_ONE = 0
    const val LEFT_DRIVE_TWO = 1
    const val RIGHT_DRIVE_ONE = 15
    const val RIGHT_DRIVE_TWO = 14
}

 object Solenoids {
     const val YEETER = 0
     const val LOADER = 1

}
object OtherCan {
    const val PNEUMATIC_HUB = 5
}

object DriveTrainConstants {
    const val  WHEEL_DIAMETER = 5.0
    const val  TICKS_PER_REV = 2048.0
    const val FEEDBACK_COEFFICIENT = 1.0 / (TICKS_PER_REV * (WHEEL_DIAMETER / 12.0) * Math.PI * (74.0/ 8.0) * 0.5 * (16.5 / 14.5))
}
