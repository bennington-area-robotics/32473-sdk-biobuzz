package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.hardware.SmartMotor;


public class Intake {
    private final SmartMotor motor;

    public Intake(SmartMotor motor) {
        this.motor = motor;
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void start() {
        motor.setPower(1);
    }

    public void stop() {
        motor.setPower(0);
    }
}
