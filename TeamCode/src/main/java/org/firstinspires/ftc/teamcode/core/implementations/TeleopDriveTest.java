package org.firstinspires.ftc.teamcode.core.implementations;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.core.SmartGamepad;
import org.firstinspires.ftc.teamcode.core.TeleOpCore;
import org.firstinspires.ftc.teamcode.hardware.SmartMotor;

@TeleOp(name = "TeleopDriveTest")
public class TeleopDriveTest extends TeleOpCore {
    private SmartMotor leftFront, leftRear, rightFront, rightRear, collectorMotor;
    private CRServo turretAim;
    private int power;

    @Override
    protected void onInitialize() {
        leftFront = hardware.getMotor("LFront");
        rightFront = hardware.getMotor("RFront");
        leftRear = hardware.getMotor("LRear");
        rightRear = hardware.getMotor("RRear");
        turretAim = hardwareMap.get(CRServo.class,"turretAim");
        collectorMotor = hardware.getMotor("collectorMotor");
        power = 0;

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        collectorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        turretAim.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        collectorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    protected void checkGamepads(SmartGamepad gamepad1, SmartGamepad gamepad2) {
        moveUsingPower(gamepad1.leftStickX,gamepad1.leftStickY,gamepad1.rightStickX,1);
        collection(gamepad1.xPressed(),gamepad1.aPressed());
        turretControl(gamepad1.dpadLeft,gamepad1.dpadRight);
    }

    public void collection(boolean x, boolean a){
        if (x) {
            if (power != 0) {
                collectorMotor.setPower(0);
                power = 0;
            }
            else {
                collectorMotor.setPower(0.50);
                power = 1;
            }
        }
        if (a) {
            if (power != 0) {
                collectorMotor.setPower(0);
                power = 0;
            }
            else {
                collectorMotor.setPower(-0.75);
                power = -1;
            }
        }
    }


    public void turretControl(boolean dpadLeft, boolean dpadRight){
        if (dpadLeft) {
            turretAim.setPower(0.9);
        }
        else if (dpadRight) {
            turretAim.setPower(-0.9);
        }
        else {
            turretAim.setPower(0);
        }
    }
    public void moveUsingPower(double x, double y, double turn, double powerFactor){
        // Denominator is the largest motor power (absolute value) or
        // This ensures all the powers maintain the correct ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
        double leftFront = ((y - x - turn) / denominator) * powerFactor;
        double leftRear = ((y + x - turn) / denominator) * powerFactor;
        double rightFront = ((y + x + turn) / denominator) * powerFactor;
        double rightRear = ((y - x + turn) / denominator) * powerFactor;

        this.leftFront.setPower(leftFront);
        this.rightFront.setPower(rightFront);
        this.rightRear.setPower(rightRear);
        this.leftRear.setPower(leftRear);

        telemetry.clear();
        telemetry.addData("LFront",leftFront);
        telemetry.update();
    }
}