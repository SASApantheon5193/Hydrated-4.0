package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;

/**
 * Command-Based implementation to move the arm to a target position.
 * Non-blocking and designed to be called repeatedly in the loop (safe for FTC OpMode structure).
 */
public class MoveArmToPositionCommand {
    private final ArmSubsystem arm;
    private final int targetPosition;
    private static final int TOLERANCE = 10;  // Adjust as needed

    public MoveArmToPositionCommand(ArmSubsystem arm, int targetPosition) {
        this.arm = arm;
        this.targetPosition = targetPosition;
    }

    /**
     * Should be called repeatedly in your OpMode loop.
     * This method handles applying power to the arm motor.
     */
    public void execute() {
        arm.setArmPosition(targetPosition);
    }

    /**
     * Use this to check if the arm has reached its position.
     * Call this from your OpMode if you want to know when it's done.
     */
    public boolean isFinished() {
        return Math.abs(arm.getCurrentPosition() - targetPosition) <= TOLERANCE;
    }
}

