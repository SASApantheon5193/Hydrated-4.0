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

@Autonomous(name = "Drive36CalibArmTest", group = "Test")
public class Drive36CalibArmTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // ─── 1) Correct your inPerTick by measured/commanded ───────────────────────
        // Robot actually moved ~41.25" when told 36", so scale = 41.25/36
        double measuredInches  = 41.25;
        double commandedInches = 36.0;
        double scale          = measuredInches / commandedInches;

        // Adjust the static params before building the drive
        MecanumDrive.Params p = MecanumDrive.PARAMS;
        p.inPerTick       *= scale;
        p.lateralInPerTick = p.inPerTick;

        telemetry.addData("Calib scale", "%.3f", scale);
        telemetry.update();

        // ─── 2) Initialize drive & arm ────────────────────────────────────────────
        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        ArmSubsystem arm = new ArmSubsystem(hardwareMap);
        MoveArmToPositionCommand moveArm = new MoveArmToPositionCommand(arm, 500);

        telemetry.addLine("Ready: drive 36\" & move arm");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // ─── 3) Kick off the arm movement ─────────────────────────────────────────
        moveArm.initialize();  // sets RUN_TO_POSITION + power for your arm
        // (no loop needed—motor will drive itself in background)

        // ─── 4) Drive forward 36" ─────────────────────────────────────────────────
        Action forward36 = drive
                .actionBuilder(startPose)
                .splineToConstantHeading(new Vector2d(36, 0), 0.0)
                .build();
        Actions.runBlocking(forward36);

        // ─── 5) Wait for arm to finish, then end it cleanly ───────────────────────
        while (opModeIsActive() && !moveArm.isFinished()) {
            sleep(20);
        }
        moveArm.end();  // holds or stops the motor

        // ─── 6) Report your results ────────────────────────────────────────────────
        Pose2d finalPose = drive.getPoseEstimate();
        telemetry.clearAll();
        telemetry.addData("Final X (in)",        "%.2f", finalPose.position.x);
        telemetry.addData("Arm Encoder (ticks)", arm.getCurrentPosition());
        telemetry.update();

        // give you time to read it
        sleep(5000);
    }
}
