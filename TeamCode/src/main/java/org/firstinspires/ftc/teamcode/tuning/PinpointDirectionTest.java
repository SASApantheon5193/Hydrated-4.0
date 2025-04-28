package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;

@TeleOp(name = "Pinpoint Encoder Direction Test", group = "Testing")
public class PinpointDirectionTest extends LinearOpMode {
    private GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing Pinpoint...");
        telemetry.update();

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "odo");

        // ✅ Choose your directions here:
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,    // Parallel (X pod)
                GoBildaPinpointDriver.EncoderDirection.REVERSED    // Perpendicular (Y pod)
        );

        telemetry.addLine("Pinpoint Ready. Waiting for start.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            pinpoint.update();

            telemetry.addLine(">>> Push robot FORWARD to test PARALLEL encoder.");
            telemetry.addLine(">>> Push robot LEFT to test PERPENDICULAR encoder.");
            telemetry.addData("Parallel (X pod)", pinpoint.getEncoderX());
            telemetry.addData("Perpendicular (Y pod)", pinpoint.getEncoderY());
            telemetry.addData("Heading (radians)", pinpoint.getHeading());
            telemetry.update();

            sleep(100);
        }
    }
}

