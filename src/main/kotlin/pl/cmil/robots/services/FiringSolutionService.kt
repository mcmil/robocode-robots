package pl.cmil.robots.services

import pl.cmil.robots.Degrees
import pl.cmil.robots.EnemyRobot
import pl.cmil.robots.normalizeAngle
import robocode.AdvancedRobot
import robocode.util.Utils
import kotlin.math.abs

class FiringSolutionService(
    private val robot: AdvancedRobot,
    private val distancePowerScaling: Double,
    private val fireSolutionTolerance: Degrees,
) {

    fun aimAndFire(enemyRobot: EnemyRobot) {
        //if (abs(targetGunHeading(enemyRobot)) < fireSolutionTolerance) {
        if(robot.gunTurnRemaining == 0.0){
            robot.fire(distancePowerScaling / enemyRobot.distance)
        }
        setGunOn(enemyRobot)
    }

    private fun setGunOn(enemy: EnemyRobot) {
        val correctedGunHeading = targetGunHeading(enemy)
        robot.setTurnGunRightRadians(Utils.normalRelativeAngle(correctedGunHeading))

    }

    private fun targetGunHeading(enemyRobot: EnemyRobot): Double =
        robot.headingRadians + enemyRobot.bearingRadians - robot.gunHeadingRadians


}