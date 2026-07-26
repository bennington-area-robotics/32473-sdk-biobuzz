package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Feeders {
    private final CRServo leftFeeder;
    private final CRServo rightFeeder;

    private double power = 1;

    public Feeders(CRServo leftFeeder, CRServo rightFeeder) {
        this.leftFeeder = leftFeeder;
        leftFeeder.setDirection(DcMotorSimple.Direction.REVERSE);
        this.rightFeeder = rightFeeder;
        rightFeeder.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void start(Boolean reversed) {
        double servoPower = (reversed ? -1 : 1) * power;

        this.leftFeeder.setPower(servoPower);
        this.rightFeeder.setPower(servoPower);
    }

    public void stop() {
        this.leftFeeder.setPower(0);
        this.rightFeeder.setPower(0);
    }

    public void setPower(double power) {
        this.power = power;
    }
}
