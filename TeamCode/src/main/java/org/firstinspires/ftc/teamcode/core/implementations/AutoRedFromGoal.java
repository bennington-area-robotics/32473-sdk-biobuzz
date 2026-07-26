package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveBase;
import org.firstinspires.ftc.teamcode.components.Feeders;
import org.firstinspires.ftc.teamcode.components.LaunchControl;
import org.firstinspires.ftc.teamcode.components.Launcher;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Red From Goal")
public class AutoRedFromGoal extends TeleOpCore {

    private String state = "START";

    private final ElapsedTime timer = new ElapsedTime();


    protected static DriveBase driveBase;

    protected static Launcher launcher;

    protected static Feeders feeders;

    protected static LaunchControl launchControl;


    @Override
    protected void onTick() {
        switch (state) {
            case("START"): {
                timer.reset();
                state = "DRIVE1";
                break;
            }
            case("DRIVE1"): {
                driveBase.moveUsingInput(0, 0.5, 0);
                if (timer.milliseconds() >= 2000) {
                    timer.reset();
                    state = "DONE";
                }
                break;
            }
            case("DONE"): {
                driveBase.moveUsingInput(0, 0, 0);
                break;
            }
        }

        launchControl.tick();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        try {
            driveBase = new DriveBase(hardware);
        } catch (Exception e) {
            prettyTelem.error("Drive base failed to initialize, skipping: " + e.getMessage());
        }

        try {
            feeders = new Feeders(
                    hardwareMap.get(CRServo.class, "leftFeeder"),
                    hardwareMap.get(CRServo.class, "rightFeeder")
            );
        } catch (Exception e) {
            prettyTelem.error("Feeder servos failed to initialize, skipping: " + e.getMessage());
        }

        try {
            launcher = new Launcher(
                    hardware.getMotor("launchMotor")
            );
        } catch (Exception e) {
            prettyTelem.error("Launch motor failed to initialize, skipping: " + e.getMessage());
        }

        try {
            launchControl = new LaunchControl(
                    feeders,
                    launcher
            );
            launchControl.startStopping();
        } catch (Exception e) {
            prettyTelem.error("Launch controller failed to initialize, skipping: " + e.getMessage());
        }
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {
        // DONT PUT ANYTHING HERE!!!
    }
}
