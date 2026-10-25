package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;
import org.firstinspires.ftc.teamcode.hardware.SmartMotor;
import org.firstinspires.ftc.teamcode.hardware.SmartServo;

@TeleOp(name = "Turret test")
public class TurretTest extends TeleOpCore {

    private SmartMotor yawMotor;
    private SmartServo pitchServo;

    public static float degreesPerYaw = 270;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        yawMotor = hardware.getMotor("yawMotor");
        pitchServo = hardware.getServo("pitchServo");

        prettyTelem.addData("Servo Pos", pitchServo::getPosition);
        prettyTelem.addData("Servo Degrees", () -> pitchServo.getPosition() * degreesPerYaw);
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {
        yawMotor.setPower(gamepad1.leftStickX);
        pitchServo.setPosition(pitchServo.getPosition() + (gamepad1.leftStickY * 0.01));

        if (gamepad1.aPressed()) {
            pitchServo.setPosition(0.5);
        }
    }
}