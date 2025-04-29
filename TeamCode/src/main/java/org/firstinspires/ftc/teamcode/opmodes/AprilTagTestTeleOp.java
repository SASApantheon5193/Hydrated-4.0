package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name="AprilTag Test (No Drive)", group="Test")
public class AprilTagTestTeleOp extends LinearOpMode {
    private Limelight3A limelight;

    @Override
    public void runOpMode() {
        // 1) Initialize the Limelight3A (must match your RC camera config name)
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(10);  // 10 Hz polling is plenty for testing
        limelight.start();            // :contentReference[oaicite:0]{index=0}

        telemetry.addLine("Limelight3A initialized");
        telemetry.addLine("Press PLAY to begin");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();  // :contentReference[oaicite:1]{index=1}

            if (result != null && result.isValid()) {
                // 2) List all fiducials seen this frame
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();  // :contentReference[oaicite:2]{index=2}
                if (fiducials != null && !fiducials.isEmpty()) {
                    for (LLResultTypes.FiducialResult fr : fiducials) {
                        telemetry.addData("Tag ID", fr.getFiducialId());
                    }
                } else {
                    telemetry.addLine("No fiducial results");
                }

                // 3) Show the raw fused Pose3D from Limelight
                Pose3D botpose = result.getBotpose();  // :contentReference[oaicite:3]{index=3}
                if (botpose != null) {
                    telemetry.addData("Botpose", botpose.toString());
                } else {
                    telemetry.addLine("Botpose null");
                }
            } else {
                telemetry.addLine("No valid AprilTag detection");
            }

            telemetry.update();
            sleep(100);
        }

        limelight.stop();
    }
}
