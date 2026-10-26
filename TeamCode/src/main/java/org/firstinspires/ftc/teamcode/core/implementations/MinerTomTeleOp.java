package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.components.DriveBase;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;
import org.firstinspires.ftc.teamcode.hardware.SmartServo;
import org.firstinspires.ftc.teamcode.hardware.gobilda.GoBildaLEDIndicator;

@TeleOp(name = "Miner Tom TeleOp")
public class MinerTomTeleOp extends TeleOpCore {

    protected static DriveBase driveBase;
    protected static GoBildaLEDIndicator powerLED;
    protected static CRServo collectorServoLeft;
    protected static CRServo collectorServoRight;

    @Override
    protected void onInitialize() {
        try {
            driveBase = new DriveBase(hardware);
        } catch (Exception e) {
            prettyTelem.error("Drive base failed to initialize, skipping: " + e.getMessage());
        }

        try {
            powerLED = hardware.getLEDIndicator("RearLED");
            powerLED.setColor(GoBildaLEDIndicator.IndicatorColor.BLUE);
        } catch (Exception e) {
            prettyTelem.error("Power LED failed to initialize, skipping: " + e.getMessage());
        }

        try {
            collectorServoLeft = hardwareMap.crservo.get("collectorServoLeft");
            collectorServoRight = hardwareMap.crservo.get("collectorServoRight");

            collectorServoLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            collectorServoRight.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception e) {
            prettyTelem.error("Collector Servos failed to initialize, skipping: " + e.getMessage());
        }
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {

        if (driveBase != null) {
            if (gamepad1.rightBumperPressed()) {
                driveBase.setPowerFactor(1);
            }
            if (gamepad1.leftBumperPressed()) {
                driveBase.setPowerFactor(0.4);
            }

            driveBase.moveUsingInput(gamepad1.leftStickX, gamepad1.leftStickY, gamepad1.rightStickX);
        }

        if (collectorServoLeft != null && collectorServoRight != null) {
            if (gamepad1.aPressed()) {
                collectorServoRight.setPower(1);
                collectorServoLeft.setPower(1);
            }
            if (gamepad1.bPressed()) {
                collectorServoRight.setPower(0);
                collectorServoLeft.setPower(0);
            }
        }

    }

    @Override
    protected void onRun() {
        powerLED.setColor(GoBildaLEDIndicator.IndicatorColor.GREEN);
    }

    @Override
    protected void onTick() {

    }
}