package org.firstinspires.ftc.teamcode.components;

import com.bylazar.configurables.annotations.Configurable;
import org.firstinspires.ftc.teamcode.hardware.SmartMotor;
import org.firstinspires.ftc.teamcode.hardware.controllers.PID;

@Configurable
public class Turret {
    PID turretPID;

    public static float kP = 0.01f, kI = 0, kD = 0.02f, kF = 0;

    public static float ticksPerYawRevolution = 288;

    double targetPosition = 0;

    SmartMotor yawMotor;

    public Turret(SmartMotor yawMotor) {
        this.yawMotor = yawMotor;

        turretPID = PID.builder()
                .setKP(() -> kP)
                .setKI(() -> kI)
                .setKD(() -> kD)
                .setKF(() -> kF)
                .setTolerance(1)
                .setDirectionalKF(true)
                .build();
    }

    public void setTargetPosition(double degrees) {
        targetPosition = degrees;
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    public double getCurrentPosition() {
        return (((double)yawMotor.getCurrentPosition()) / ticksPerYawRevolution)*360;
    }

    public void tick() {
        turretPID.calcPosition(targetPosition, getCurrentPosition());

        yawMotor.setPower(turretPID.result());
    }

    public void reset() {
        yawMotor.resetEncoder();
    }
}