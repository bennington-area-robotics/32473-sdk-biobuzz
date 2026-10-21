package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.SmartMotor;

public class DriveBase {

    private final SmartMotor leftFront, leftBack, rightFront, rightBack;
    private double powerFactor = 0.5;

    public DriveBase(Hardware hardware) {
        leftFront = hardware.getMotor("leftFrontMotor");
        leftBack = hardware.getMotor("leftBackMotor");
        rightFront = hardware.getMotor("rightFrontMotor");
        rightBack = hardware.getMotor("rightBackMotor");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public double[] scaledInputs(double x, double y, double turn) {
        final double scale = 2;

        return new double[]{
            scaleValue(x, scale) * powerFactor,
            scaleValue(y, scale) * powerFactor,
            scaleValue(turn, scale) * powerFactor
        };
    }

    public double scaleValue(double value, double scale) {
        return Math.signum(value) * Math.pow(Math.abs(value), scale);
    }

    public void moveUsingInput(double x, double y, double turn) {
        double[] scaled = scaledInputs(x, y, turn);
        double scaledX = scaled[0];
        double scaledY = scaled[1];
        double scaledTurn = scaled[2];

        double denominator = Math.max(Math.abs(scaledY) + Math.abs(scaledX) + Math.abs(scaledTurn), 1);
        double leftFront = ((-scaledX + scaledY - scaledTurn) / denominator) * powerFactor;
        double leftBack = ((scaledX + scaledY - scaledTurn) / denominator) * powerFactor;
        double rightFront = ((-scaledX - scaledY - scaledTurn) / denominator) * powerFactor;
        double rightBack = ((scaledX - scaledY - scaledTurn) / denominator) * powerFactor;

        setMotorPowers(leftFront, leftBack, rightFront, rightBack);
    }

    public void setMotorPowers(double lf, double lb, double rf, double rb) {
        // This function will likely change in the future, Callam has some
        // interesting implementation of pedropathing in place of this which
        // I'd like to learn about.

        leftFront.setPower(lf);
        leftBack.setPower(lb);
        rightFront.setPower(rf);
        rightBack.setPower(rb);
    }

    public void setPowerFactor(double powerFactor) {
        this.powerFactor = powerFactor;
    }
}
