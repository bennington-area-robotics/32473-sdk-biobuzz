package org.firstinspires.ftc.teamcode.components;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.OpModeCore;

@Configurable
public class LaunchControl {
    public static double LAUNCH_VELOCITY = 1000;
    public static double PREVENT_JAM_VELOCITY = -350;
    public static double LAUNCH_TIME = 450;

    private final Feeders feeders;
    private final Launcher launcher;
    private State state;

    private final ElapsedTime launchTimer = new ElapsedTime();

    public LaunchControl(
        Feeders feeders,
        Launcher launcher
    ) {
        this.feeders = feeders;
        this.launcher = launcher;

        this.state = State.STOPPED;
        OpModeCore.getTelemetry().addLine("Launch Controller")
                .addData("State", () -> state);
    }

    public State getState(){
        return state;
    }

    public void tick() {
        switch (state) {
            case STOPPED: {

                break;
            }
            case IDLING: {
                launcher.setTargetVelocity(PREVENT_JAM_VELOCITY);
                break;
            }
            case SPINNING_UP: {
                launcher.setTargetVelocity(LAUNCH_VELOCITY);

                if (launcher.atSpeed()) {
                    startLaunching();
                }
                break;
            }
            case LAUNCHING: {
                if(launchTimer.milliseconds() > LAUNCH_TIME){
                    startIdling();
                }
            }
        }
    }

    public void startSpinningUp(){
        launcher.setTargetVelocity(LAUNCH_VELOCITY);
        state = State.SPINNING_UP;
    }

    public void startLaunching(){
        feeders.start(false);
        launchTimer.reset();
        state = State.LAUNCHING;
    }

    public void startIdling(){
        feeders.stop();
        launcher.setTargetVelocity(PREVENT_JAM_VELOCITY);
        state = State.IDLING;
    }

    public void startStopping() {
        launcher.stop();
        feeders.stop();
        state = State.STOPPED;
    }


    public enum State {
        STOPPED,
        IDLING,
        SPINNING_UP,
        LAUNCHING
    }

}
