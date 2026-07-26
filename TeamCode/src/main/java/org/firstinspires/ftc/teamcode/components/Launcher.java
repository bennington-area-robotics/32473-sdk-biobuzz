package org.firstinspires.ftc.teamcode.components;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.core.OpModeCore;
import org.firstinspires.ftc.teamcode.hardware.SmartMotor;
import org.firstinspires.ftc.teamcode.hardware.controllers.PID;
import org.firstinspires.ftc.teamcode.hardware.controllers.VelocityPID;

@Configurable
public class Launcher {

    private final ElapsedTime timer = new ElapsedTime();



    private final VelocityPID controller;

    public static double kP = 0.003, kI = 0, kD = 0, kf = 0.00075, tolerance = 30;

    private final SmartMotor motor;

    private double targetVelocity = 0;

    public Launcher(SmartMotor motor) {
        this.controller = new VelocityPID.Builder()
                .setKP(() -> kP)
                .setKI(() -> kI)
                .setKD(() -> kD)
                .setKF(() -> kf)
                .setTolerance(tolerance)
                .build();
        this.motor = motor;
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        OpModeCore.getTelemetry().addData("Launcher Result", controller::result);
        OpModeCore.getTelemetry().addData("Launcher Velocity", this::getCurrentVelocity);
    }

    public void setTargetVelocity(double targetVelocity) {
        this.targetVelocity = targetVelocity;
    }

    public void incrementTargetVelocity(double increment) {
        setTargetVelocity(targetVelocity + increment);
    }

    public void stop() {
        setTargetVelocity(0);
    }

    public void tick(){
        motor.setPower(controller.calcVelocity(targetVelocity, getCurrentVelocity()));
    }

    public double getCurrentVelocity() {
        float ticksPerDegree = (537.7f / 360f);

        return motor.getVelocity() / ticksPerDegree;
    }

    public boolean atSpeed() {
        return !controller.isBusy();
    }
}
