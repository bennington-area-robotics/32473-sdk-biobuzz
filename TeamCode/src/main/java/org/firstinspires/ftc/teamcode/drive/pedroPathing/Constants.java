package org.firstinspires.ftc.teamcode.drive.pedroPathing;

import com.pedropathing.algorithm.Algorithm;
import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("LFront");
        c.frontRightName.set("RFront");
        c.backLeftName.set("LRear");
        c.backRightName.set("RRear");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-6.341852503498708);
        c.yPodOffset.set(7.269904069074496);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.1733776528058304);
                Controller secondaryTranslationalForward = Controller.proportional(0.06405840997554905);
                Controller primaryTranslationalLateral = Controller.proportional(0.21258181217764038);
                Controller secondaryTranslationalLateral = Controller.proportional(0.07854329930900131);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016488906774340017));
                c.brake.set(Controller.proportionalFeedforward(0.014015570758189013));

                c.headingFeedback.set(Controller.proportional(3.6586339226730256));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04150066540512564, 0.007155627888393733));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07364858649384394, 0.050884555844738534));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0010800471907571206, 0.0017146734692338446));

                c.maxAchievableForwardVelocity.set(63.04845374035041);
                c.maxAchievableStrafeVelocity.set(19.109187634351734);
                c.naturalForwardDeceleration.set(33.97076617646635);
                c.naturalStrafeDeceleration.set(78.68110408792354);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(new PinpointLocalizer(h, localizerConfig), new Mecanum(h, drivetrainConfig), new Foresight(foresightConfig));
    }
}