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

    public AllianceColor allianceColor = AllianceColor.RED;

    private Pose negRedHive = new Pose(-22, 14);
    private Pose posRedHive = new Pose(22, 14);
    private Pose negBlueHive = new Pose(-22, -14);
    private Pose posBlueHive = new Pose(22, -14);

    private Turret turret;

    private SmartMotor yawMotor;
    private SmartServo pitchServo;

    private boolean fieldCentric = false;

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

       if (gamepad1.aPressed()) {
           if (allianceColor == AllianceColor.BLUE) {
               allianceColor = AllianceColor.RED;
           } else {
               allianceColor = AllianceColor.BLUE;
           }
       }

        if (gamepad1.rightBumperPressed()) {
            fieldCentric = !fieldCentric;
        }

        follower.manual(powers);
    }

    public void setTurretDirection(double yaw, double pitch) {
        turret.setTargetPosition(yaw);
        pitchServo.setPosition(0.5 + pitch/degreesPerPitch);
    }

    public void updateTurret(Pose currentPose, Pose targetPose) {
        double relativeX = targetPose.x() - currentPose.x();
        double relativeY = targetPose.y() - currentPose.y();

        double bearing = Math.toDegrees(Math.atan2(relativeX, relativeY));

        double distance = Math.sqrt(Math.pow(relativeX, 2) + Math.pow(relativeY, 2));

        double pitch = Math.toDegrees(Math.atan2(60,distance));
        double yaw = bearing - 90 + Math.toDegrees(currentPose.heading());

        setTurretDirection(yaw, pitch);
    }

    public Pose getTargetHive(Pose currentPose, AllianceColor allianceColor) {
        if (currentPose.x() > 0) {
            return allianceColor == AllianceColor.BLUE ? posBlueHive : posRedHive;
        } else {
            return allianceColor == AllianceColor.BLUE ? negBlueHive : negRedHive;
        }
    }

    @Override
    protected void onTick() {
        follower.update();
        Pose currentPose = follower.pose();
        updateTurret(currentPose, getTargetHive(currentPose, allianceColor));
        turret.tick();
    }

    public enum AllianceColor {
            BLUE,
            RED
    }
}
