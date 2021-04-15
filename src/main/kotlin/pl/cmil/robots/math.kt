package pl.cmil.robots

typealias Degrees = Double

fun Degrees.normalizeAngle(): Degrees {
    var angle = this
    while (angle > 180) angle -= 360;
    while (angle < -180) angle += 360;
    return angle;
}