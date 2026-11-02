package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public Drivetrain drivetrain;
    //public Turret turret;
    //ADD LATER
    public Intake intake;

    public MidIntake midIntake;

    public FlowerFront flowerFront;

    public IntakePitch intakePitch;

    public Shoot pollenTransfer;

    //public Shoot nectarTransfer

    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap);
        //turret = new Turret(hardwareMap);
        intake = new Intake(hardwareMap);
        midIntake = new MidIntake(hardwareMap);
        flowerFront = new FlowerFront(hardwareMap);
    }

}
