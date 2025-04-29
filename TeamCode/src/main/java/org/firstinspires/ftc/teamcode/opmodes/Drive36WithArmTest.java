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

@Autonomous(name = "Drive36WithArmTest", group = "Test")
public class Drive36WithArmTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Drive setup
        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        // Arm setup
        ArmSubsystem arm = new ArmSubsystem(hardwareMap);
        MoveArmToPositionCommand moveArm = new MoveArmToPositionCommand(arm, 500);

        // Initialize arm command
        moveArm.initialize();

        telemetry.addLine("Ready: Drive 36\" & Move Arm");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // Build and run the 36" forward action
        Action forward36 = drive.actionBuilder(startPose)
                .splineToConstantHeading(new Vector2d(36, 0), 0.0)
                .build();
        Actions.runBlocking(forward36);

        // Meanwhile, keep polling arm until it’s done
        while (!moveArm.isFinished() && opModeIsActive()) {
            moveArm.execute();
            sleep(20);
        }
        moveArm.end();

        // Report final states
        Pose2d finalPose = drive.getPoseEstimate();
        telemetry.clearAll();
        telemetry.addData("Final X (in)", "%.2f", finalPose.position.x);
        telemetry.addData("Arm Position (ticks)", arm.getCurrentPosition());
        telemetry.update();

        sleep(5000);
    }
}
