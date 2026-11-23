package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class AutoAimRobot {

    public Turret turret;
    public Limelight limelight;

    public AutoAimRobot(HardwareMap hardwareMap) {
        turret = new Turret(hardwareMap);
        limelight = new Limelight(hardwareMap);
    }
}