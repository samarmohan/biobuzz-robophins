package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    // backRight (motor), backLeft (motor), frontRight (motor), frontLeft (motor)
    public Drivetrain drivetrain;
    // flywheel (motor), turretSpin (motor)
    public Turret turret;
    // intake (motor)
    public Intake intake;
    // public Flower flower;
    // kickup (motor)
    public Kickup kickup;
    // limelight (Limelight)
    public Limelight limelight;


    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap);
        turret = new Turret(hardwareMap);
        intake = new Intake(hardwareMap);
        // flower = new Flower(hardwareMap);
        kickup = new Kickup(hardwareMap);
        limelight = new Limelight(hardwareMap);
    }

}
