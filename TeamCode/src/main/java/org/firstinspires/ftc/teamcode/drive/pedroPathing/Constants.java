package org.firstinspires.ftc.teamcode.drive.pedroPathing;

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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-6.335447416530819);
        c.yPodOffset.set(6.997152613842581);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

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

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2177341061934516);
                Controller secondaryTranslationalForward = Controller.proportional(0.08044693427601193);
                Controller primaryTranslationalLateral = Controller.proportional(0.23593984319280914);
                Controller secondaryTranslationalLateral = Controller.proportional(0.08717346763102254);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.017473387861776907));
                c.brake.set(Controller.proportionalFeedforward(0.01485237968251037));

                c.headingFeedback.set(Controller.proportional(3.8454293330992257));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04020483098875712, 0.00671668724551551));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06342092408831568, 0.027304633655215127));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0012532095283790926, 0.0023346093590366737));

                c.maxAchievableForwardVelocity.set(60.85491717476192);
                c.maxAchievableStrafeVelocity.set(50.327006274760535);
                c.naturalForwardDeceleration.set(31.55288943514311);
                c.naturalStrafeDeceleration.set(56.761271169206765);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(new PinpointLocalizer(h, localizerConfig), new Mecanum(h, drivetrainConfig), new Foresight(foresightConfig));
    }
}