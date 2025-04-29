package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;

public class MoveArmToPositionCommand {
    private final ArmSubsystem arm;
    private final int targetPosition;
    private static final int TOLERANCE = 10;  // ticks

    public MoveArmToPositionCommand(ArmSubsystem arm, int targetPosition) {
        this.arm = arm;
        this.targetPosition = targetPosition;
    }

    /** Call once before you start issuing execute(). */
    public void initialize() {
        arm.setArmPosition(targetPosition);
    }

    /** Call continuously (e.g. every 20 ms). */
    public void execute() {
        // no-op: the subsystem is already in RUN_TO_POSITION
    }

    /** @return true once the arm is within tolerance of the target */
    public boolean isFinished() {
        return arm.isAtTarget(TOLERANCE);
    }

    /** Call once when you’re done or want to abort. */
    public void end() {
        arm.setArmPosition(arm.getCurrentPosition()); // hold where you are
    }
}
