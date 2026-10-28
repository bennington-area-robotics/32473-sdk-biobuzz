package org.firstinspires.ftc.teamcode.core.implementations;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.components.Turret;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;
import org.firstinspires.ftc.teamcode.drive.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.hardware.SmartMotor;
import org.firstinspires.ftc.teamcode.hardware.SmartServo;

@TeleOp(name = "1 - Aiming TeleOp")
public class AimingTeleOp extends TeleOpCore {

    Follower follower;

    public static float degreesPerPitch = 270;


    // Red Hive -X: x=-22, y=14
    // Red Hive +X: x=22, y=14
    // Blue Hive -X: x=-22, y=-14
    // Blue Hive +X: x=22, y=-14

    private Pose negRedHive = new Pose(-22, 14);

    private Turret turret;

    private SmartMotor yawMotor;
    private SmartServo pitchServo;

    private boolean fieldCentric = true;

    @Override
    protected void onInitialize() {

        follower = Constants.create(hardwareMap);

        follower.setPose(new Pose(0, 0, 0));

        yawMotor = hardware.getMotor("yawMotor");
        pitchServo = hardware.getServo("pitchServo");

        turret = new Turret(yawMotor);
        turret.reset();

        setTurretDirection(-30, 30);

        prettyTelem.addData("Pos X", () -> follower.pose().x());
        prettyTelem.addData("Pos Y", () -> follower.pose().y());
        prettyTelem.addData("Heading", () -> follower.pose().heading());

        prettyTelem.addData("Target", turret::getTargetPosition);
        prettyTelem.addData("Current", turret::getCurrentPosition);
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {
        DrivePowers powers;

        if (fieldCentric) {
            powers = ManualDrive.fieldCentric(
                    gamepad1.leftStickY,
                    gamepad1.leftStickX,
                    -gamepad1.rightStickX,
                    follower.pose().heading()
            );
        } else {
            powers = new DrivePowers(-gamepad1.leftStickY, -gamepad1.leftStickX, -gamepad1.rightStickX);
        }

        // Red Hive -X: x=-22, y=14
        // Red Hive +X: x=22, y=14
        // Blue Hive -X: x=-22, y=-14
        // Blue Hive +X: x=22, y=-14

        if (gamepad1.rightBumperPressed()) {
            fieldCentric = !fieldCentric;
        }

        follower.manual(powers);
    }

    public void setTurretDirection(double yaw, double pitch) {
        turret.setTargetPosition(yaw);
        pitchServo.setPosition(0.5 + pitch/degreesPerPitch);
    }

    public Pose getRelativePose(Pose start, Pose end) {
        Pose relative = start.invert().compose(end);

        double bearing = Math.atan2(relative.y(), relative.x());

        return new Pose(relative.x(), relative.y(), bearing);
    }

    public void updateTurret(Pose currentPose) {
        Pose aim = getRelativePose(currentPose, negRedHive);

        double distance = Math.sqrt(Math.pow(aim.x(), 2) + Math.pow(aim.y(), 2));

        double pitch = Math.atan2(distance,48);
        double yaw = aim.heading();

        setTurretDirection(yaw, pitch);
    }

    @Override
    protected void onTick() {
        follower.update();
        updateTurret(follower.pose());
        turret.tick();
    }
}
