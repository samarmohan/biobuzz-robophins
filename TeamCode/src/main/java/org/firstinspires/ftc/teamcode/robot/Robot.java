package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public Drivetrain drivetrain;
    public Turret turret;
    public Intake intake;


    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap);
        //turret = new Turret(hardwareMap);
        intake = new Intake(hardwareMap);
    }

}
