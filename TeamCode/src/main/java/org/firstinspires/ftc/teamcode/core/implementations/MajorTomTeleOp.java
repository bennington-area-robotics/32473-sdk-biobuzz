package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.components.DriveBase;
import org.firstinspires.ftc.teamcode.components.Feeders;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.LaunchControl;
import org.firstinspires.ftc.teamcode.components.Launcher;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;

import java.util.Objects;

@TeleOp(name = "Major Tom TeleOp")
public class MajorTomTeleOp extends TeleOpCore {
    protected static DriveBase driveBase;
    protected static Feeders feeders;
    protected static Intake intake;
    protected static Launcher launcher;
    protected static LaunchControl launchControl;

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
            intake = new Intake(
                hardware.getMotor("intakeMotor")
            );
        } catch (Exception e) {
            prettyTelem.error("Intake motor failed to initialize, skipping: " + e.getMessage());
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
        } catch (Exception e) {
            prettyTelem.error("Launch controller failed to initialize, skipping: " + e.getMessage());
        }
    }

    @Override
    protected void onRun(){
        launchControl.startIdling();
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

        if (intake != null) {
            if (gamepad1.back) {
                intake.start();
            }
            if (gamepad1.start) {
                intake.stop();
            }
        }

        if (launcher != null) {
            if (gamepad1.y) {
                launcher.setTargetVelocity(LaunchControl.LAUNCH_VELOCITY);
            }
            if (gamepad1.x) {
                launcher.setTargetVelocity(LaunchControl.PREVENT_JAM_VELOCITY);
            }
            if (gamepad1.leftTrigger > 0.9) {
                launcher.incrementTargetVelocity(10);
            }
            if (gamepad1.rightTrigger > 0.9) {
                launcher.incrementTargetVelocity(-10);
            }
        }

        if (launchControl != null) {
            if (gamepad1.b) {
                launchControl.startSpinningUp();
            }
            if (gamepad1.a) {
                launchControl.startStopping();
            }

            if (feeders != null) {
                if (gamepad1.dpadUp) {
                    feeders.start(false);
                } else if (gamepad1.dpadDown) {
                    feeders.start(true);
                } else if (launchControl.getState() != LaunchControl.State.LAUNCHING) {
                    feeders.stop();
                }
            }
        }
    }

    @Override
    protected void onTick() {
        if (launchControl != null) {
            launchControl.tick();
        }

        if(launcher != null){
            launcher.tick();
        }
    }
}
