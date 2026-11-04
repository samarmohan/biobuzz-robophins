package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public Drivetrain drivetrain;
    //public Turret turret;
    //ADD LATER
    public Intake intake;

    public Wall wall;

    public Flower flower;

    public Kickup kickup;


    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap);
        // turret = new Turret(hardwareMap);
        intake = new Intake(hardwareMap);
        wall = new Wall(hardwareMap);
        flower = new Flower(hardwareMap);
        kickup = new Kickup(hardwareMap);
    }

}
