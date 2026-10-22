package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.components.DriveBase;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;

@TeleOp(name = "Miner Tom TeleOp")
public class MinerTomTeleOp extends TeleOpCore {

    protected static DriveBase driveBase;

    @Override
    protected void onInitialize() {
        try {
            driveBase = new DriveBase(hardware);
        } catch (Exception e) {
            prettyTelem.error("Drive base failed to initialize, skipping: " + e.getMessage());
        }
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {

        if (driveBase != null) {
            if (gamepad1.rightBumper) {
                driveBase.setPowerFactor(0.25);
            } else if (gamepad1.leftBumper) {
                driveBase.setPowerFactor(0.85);
            } else {
                driveBase.setPowerFactor(0.5);
            }

            driveBase.moveUsingInput(gamepad1.leftStickX, gamepad1.leftStickY, gamepad1.rightStickX);
        }

    }

    @Override
    protected void onRun() {

    }

    @Override
    protected void onTick() {

    }
}