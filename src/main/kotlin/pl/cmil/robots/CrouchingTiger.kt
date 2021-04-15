package pl.cmil.robots

import pl.cmil.robots.services.FiringSolutionService
import pl.cmil.robots.services.ManeuverService
import pl.cmil.robots.services.TrackingService
import robocode.*
import java.awt.Color

class CrouchingTiger : AdvancedRobot() {
    private val trackingService = TrackingService(this, NO_TARGET_SCAN_SPEED_DEGREES)
    private val maneuverService = ManeuverService(this)
    private val firingSolutionService =
        FiringSolutionService(this, FIRE_POWER_SCALING_DISTANCE, FIRE_SOLUTION_TOLERANCE_DEGREES)

    override fun run() {
        setBodyColor(Color.black)
        setGunColor(Color.black)
        setRadarColor(Color.gray)

        isAdjustGunForRobotTurn = false
        isAdjustRadarForGunTurn = false
        isAdjustRadarForRobotTurn = false

        while (true) {
            turnRadarRightRadians(Double.POSITIVE_INFINITY);

            scan()
            execute()
        }
    }


    override fun onScannedRobot(e: ScannedRobotEvent) {
        trackingService.handleScanEvent(e)
        val lastRadarContact = trackingService.scanForCurrentTarget()

        if (lastRadarContact != null) {
            firingSolutionService.aimAndFire(lastRadarContact)
            maneuverService.getCloserToEnemy(lastRadarContact)
        } else {
            maneuverService.lookForEnemy()
        }

        execute()
    }

    override fun onRobotDeath(event: RobotDeathEvent) {
        trackingService.handleRobotDeathEvent(event)
        execute()
    }

    override fun onHitRobot(event: HitRobotEvent) {
        maneuverService.handleHitRobotEvent(event)
        execute()
    }

    override fun onHitByBullet(event: HitByBulletEvent) {
        maneuverService.handleHitByBulletEvent(event)
        execute()
    }

    override fun onHitWall(event: HitWallEvent) {
        maneuverService.handleHitWallEvent(event)
        execute()
    }

    companion object {
        const val NO_TARGET_SCAN_SPEED_DEGREES = 100.0
        const val FIRE_SOLUTION_TOLERANCE_DEGREES = 1.0
        const val FIRE_POWER_SCALING_DISTANCE = 400.0
    }
}


data class EnemyRobot(val name: String, val bearing: Double, val bearingRadians: Double, val distance: Double)