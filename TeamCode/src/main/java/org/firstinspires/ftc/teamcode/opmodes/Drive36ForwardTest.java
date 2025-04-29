package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.commands.MoveArmToPositionCommand;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;

@Autonomous(name = "Drive36ForwardTestWithArmHold", group = "Test")
public class Drive36ForwardTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // 1) Define your starting drive pose at (0, 0) facing 0 radians
        Pose2d startPose = new Pose2d(0, 0, 0);

        // 2) Initialize your drive
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        // ✅ Arm setup
        ArmSubsystem arm = new ArmSubsystem(hardwareMap);
        MoveArmToPositionCommand moveArmCollapsed = new MoveArmToPositionCommand(arm, 500);

        // 3) Thread to continuously hold arm at 500 ticks
        Thread armHoldThread = new Thread(() -> {
            while (opModeIsActive() && !Thread.currentThread().isInterrupted()) {
                moveArmCollapsed.execute();
                try {
                    Thread.sleep(20);  // update every 20ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        armHoldThread.setDaemon(true);
        armHoldThread.start();

        telemetry.addLine("Ready to drive 36\" forward with arm held at 500");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            armHoldThread.interrupt();
            return;
        }

        // 4) Build a one-segment trajectory to X=36", Y=0"
        Action forward36 = drive
                .actionBuilder(startPose)
                .splineToConstantHeading(new Vector2d(36, 0), 0.0)
                .build();

        // 5) Run it (blocks until complete)
        Actions.runBlocking(forward36);

        // 6) Stop the arm-hold thread
        armHoldThread.interrupt();

        // 7) Report final drive pose
        Pose2d finalPose = drive.getPoseEstimate();
        telemetry.clearAll();
        telemetry.addData("Final X (in)", "%.2f", finalPose.position.x);
        telemetry.addData("Final Y (in)", "%.2f", finalPose.position.y);
        telemetry.update();

        // give you time to read telemetry
        sleep(5000);
    }
}
