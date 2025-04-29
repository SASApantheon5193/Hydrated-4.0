package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

/**
 * Fetches the latest Limelight3A AprilTag pose (inches),
 * converts X/Y → meters, and returns a Pose2d with the given heading.
 */
public class Limelight3VisionSubsystem {
    private final Limelight3A limelight;

    public Limelight3VisionSubsystem(HardwareMap hardwareMap) {
        // "limelight" must match your camera name in the Robot Controller app
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(50);  // 50 Hz updates
        limelight.start();
    }

    /**
     * @param currentHeadingRad your robot’s current heading (rad)
     * @return a Pose2d(x, y in meters, heading) or null if no valid AprilTag
     */
    public Pose2d getFieldPose(double currentHeadingRad) {
        LLResult result = limelight.getLatestResult();              // :contentReference[oaicite:1]{index=1}
        if (result == null || !result.isValid()) {
            return null;
        }

        Pose3D botpose = result.getBotpose();                       // :contentReference[oaicite:2]{index=2}
        if (botpose == null) {
            return null;
        }

        // Position is in inches; convert to meters
        Position p = botpose.getPosition();
        double xMeters = p.x * 0.0254;
        double yMeters = p.y * 0.0254;

        // Use your existing Road-Runner Pose2d constructor (x, y, heading)
        return new Pose2d(xMeters, yMeters, currentHeadingRad);      // :contentReference[oaicite:3]{index=3}
    }
}
