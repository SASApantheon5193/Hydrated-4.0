package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Limelight3VisionSubsystem;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;

@Autonomous(name = "Vision-Augmented Auto (Square Loop)", group = "Auto")
public class VisionAuto extends LinearOpMode {
    private MecanumDrive drive;
    private Limelight3VisionSubsystem vision;

    @Override
    public void runOpMode() throws InterruptedException {
        // 1) Starting pose at origin, facing 0 radians
        Pose2d startPose = new Pose2d(0, 0, 0);

        // 2) Initialize drive & vision
        drive  = new MecanumDrive(hardwareMap, startPose);
        vision = new Limelight3VisionSubsystem(hardwareMap);

        telemetry.addData("Status", "Ready for continuous 48\" square");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // 3) Start vision-fusion thread: periodically overwrite X/Y and log corrections
        Thread visionThread = new Thread(() -> {
            while (opModeIsActive() && !Thread.currentThread().isInterrupted()) {
                Pose2d current = drive.getPoseEstimate();
                Pose2d fused = vision.getFieldPose(current.heading.toDouble());
                if (fused != null) {
                    drive.setPoseEstimate(fused);
                    telemetry.addData("Vision Correction",
                            "X=%.1f in, Y=%.1f in",
                            fused.position.x, fused.position.y);
                    telemetry.update();
                }
                sleep(50);
            }
        });
        visionThread.setDaemon(true);
        visionThread.start();

        // 4) Pre-build your 48" square Action once
        Action squareAction = drive.actionBuilder(startPose)
                .splineToConstantHeading(new Vector2d(24,  0), 0.0)  // East 48"
                .splineToConstantHeading(new Vector2d(24, 24), 0.0)  // North 48"
                .splineToConstantHeading(new Vector2d( 0, 24), 0.0)  // West 48"
                .splineToConstantHeading(new Vector2d( 0,  0), 0.0)  // South 48" back
                .build();

        // 5) Loop the square until OpMode is stopped
        while (opModeIsActive()) {
            Actions.runBlocking(squareAction);
        }

        // 6) Clean up vision thread when stopping
        visionThread.interrupt();

        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}
