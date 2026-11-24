package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public Drivetrain drivetrain;
    public Turret turret;
    public Intake intake;
    public Kickup kickup;
    public Limelight limelight;

    public Robot(HardwareMap hardwareMap) {

        drivetrain = new Drivetrain(hardwareMap);
        turret = new Turret(hardwareMap);
        intake = new Intake(hardwareMap);
        kickup = new Kickup(hardwareMap);
        limelight = new Limelight(hardwareMap);

    }
}
