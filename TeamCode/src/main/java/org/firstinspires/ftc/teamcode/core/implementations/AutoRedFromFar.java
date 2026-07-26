package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveBase;
import org.firstinspires.ftc.teamcode.components.Feeders;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Red From Far")
public class AutoRedFromFar extends TeleOpCore {

    private String state = "START";

    private final ElapsedTime timer = new ElapsedTime();


    protected static DriveBase driveBase;


    @Override
    protected void onTick() {
        switch (state) {
            case("START"): {
                timer.reset();
                state = "DRIVE1";
                break;
            }
            case("DRIVE1"): {
                driveBase.moveUsingInput(0, -0.5, 0);
                if (timer.milliseconds() >= 2000) {
                    state = "DONE";
                }
                break;
            }
            case("DONE"): {
                driveBase.moveUsingInput(0, 0, 0);
                break;
            }
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        try {
            driveBase = new DriveBase(hardware);
        } catch (Exception e) {
            prettyTelem.error("Drive base failed to initialize, skipping: " + e.getMessage());
        }
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {
        // DONT PUT ANYTHING HERE!!!
    }
}
