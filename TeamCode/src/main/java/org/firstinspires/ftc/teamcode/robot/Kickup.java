package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Kickup {
    private DcMotor kickup;

    public Kickup(HardwareMap hardwareMap) {
        kickup = hardwareMap.get(DcMotor.class, "kickup");
        kickup.setDirection(DcMotor.Direction.REVERSE);
    }

    public void shoot() {
        kickup.setPower(1);
    }

    public void push(){
        kickup.setPower(-0.5);
    }

    public void stop() {
        kickup.setPower(0);
    }
}