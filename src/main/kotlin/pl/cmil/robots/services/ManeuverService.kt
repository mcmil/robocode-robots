package pl.cmil.robots.services

import pl.cmil.robots.EnemyRobot
import pl.cmil.robots.normalizeAngle
import robocode.AdvancedRobot
import robocode.HitByBulletEvent
import robocode.HitRobotEvent
import robocode.HitWallEvent
import robocode.util.Utils
import kotlin.math.abs

class ManeuverService(private val robot: AdvancedRobot) {
    private var evasiveActionsToExecute: (() -> Unit)? = null

    fun lookForEnemy() = executeIfNoEvasiveActions {

        goAhead()
    }

    fun getCloserToEnemy(enemyRobot: EnemyRobot) = executeIfNoEvasiveActions {
        if(enemyRobot.distance >100) {
            robot.setTurnRightRadians(Utils.normalRelativeAngle(enemyRobot.bearingRadians))
            robot.setAhead(enemyRobot.distance * 0.2)
        }
    }

    fun handleHitByBulletEvent(event: HitByBulletEvent) {
        //evasiveActionsToExecute = { evasiveManeuvers() }
    }

    fun handleHitRobotEvent(event: HitRobotEvent) {
        evasiveActionsToExecute = {
            robot.setTurnRight(30.0)
            if (abs(event.bearing) > 90.0) {
                robot.out.println("AHEAD")
                runAhead()
            } else {
                robot.setBack(200.0)
                robot.out.println("BACK")
            }
        }
    }


    fun handleHitWallEvent(event: HitWallEvent) {
        evasiveActionsToExecute = {
            robot.setTurnRight(30.0)
            if (abs(event.bearing) > 90.0) {
                runAhead()
            } else {
                backoff()
            }
        }
    }


    private fun executeIfNoEvasiveActions(actionToExecute: () -> Unit) {
        val lastEvasiveAction = evasiveActionsToExecute
        if (lastEvasiveAction != null) {
            lastEvasiveAction()
            evasiveActionsToExecute = null
        } else {
            actionToExecute()
        }
    }


    private fun evasiveManeuvers() {
        robot.setTurnLeft(20.0)
        robot.setBack(10.0)
    }

    private fun backoff() {
        robot.setBack(200.0)
    }

    private fun goAhead() {
        robot.setAhead(10.0)
    }

    private fun runAhead() {
        robot.setAhead(100.0)
    }

}