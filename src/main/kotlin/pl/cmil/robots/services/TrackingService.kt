package pl.cmil.robots.services

import pl.cmil.robots.CrouchingTiger
import pl.cmil.robots.Degrees
import pl.cmil.robots.EnemyRobot
import pl.cmil.robots.normalizeAngle
import robocode.AdvancedRobot
import robocode.RobotDeathEvent
import robocode.ScannedRobotEvent
import robocode.util.Utils
import robocode.util.Utils.NEAR_DELTA
import kotlin.math.abs

class TrackingService(
    private val robot: AdvancedRobot,
    private val noTargetRadarSpeed: Degrees
) {
    private var currentlyTracked: EnemyRobot? = null

    fun scanForCurrentTarget(): EnemyRobot? {
        return currentlyTracked
    }

    fun handleScanEvent(event: ScannedRobotEvent) {
        val lastRadarContact = currentlyTracked
        val newRadarContact = EnemyRobot(event.name, event.bearing, event.bearingRadians, event.distance)
        if (lastRadarContact == null || newRadarContact.name == lastRadarContact.name || (newRadarContact.distance < lastRadarContact.distance && newRadarContact.distance - lastRadarContact.distance > 50)) {
            currentlyTracked = newRadarContact
        }
        setRadarOn(newRadarContact)
    }

    fun handleRobotDeathEvent(event: RobotDeathEvent) {
        val lastRadarContact = currentlyTracked
        if (lastRadarContact != null && event.name == lastRadarContact.name) {
            currentlyTracked = null
        }
    }

    private fun setRadarOn(enemy: EnemyRobot) {
        val correctedRadarHeading = (robot.headingRadians + enemy.bearingRadians - robot.radarHeadingRadians)
        robot.setTurnRadarRightRadians(1.0 * Utils.normalRelativeAngle(correctedRadarHeading))
    }
}