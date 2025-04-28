package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class ArmSubsystem {
    private final DcMotorEx armMotor;

    public ArmSubsystem(HardwareMap hardwareMap) {
        armMotor = hardwareMap.get(DcMotorEx.class, "left_arm");

        // Motor Settings
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Current Limit Alert (5 Amps)
        armMotor.setCurrentAlert(5, CurrentUnit.AMPS);
    }

    public void setArmPosition(int position) {
        armMotor.setTargetPosition(position);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(1.0);  // ✅ Changed from setVelocity() to setPower()
    }

    public int getCurrentPosition() {
        return armMotor.getCurrentPosition();
    }

    public int getTargetPosition() {
        return armMotor.getTargetPosition();
    }

    public boolean isOverCurrent() {
        return armMotor.isOverCurrent();
    }

    public boolean isAtTarget(int tolerance) {
        return Math.abs(getCurrentPosition() - getTargetPosition()) <= tolerance;
    }
}
