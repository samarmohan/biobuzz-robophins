
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;

public class Kickup {

    public CRServo pollenTransfer;
    public CRServo nectarTransfer;

    public DcMotorEx flywheel;

    public Kickup(HardwareMap hardwareMap) {
        pollenTransfer = hardwareMap.get(CRServo.class, "pollenTransfer");
        nectarTransfer = hardwareMap.get(CRServo.class, "nectarTransfer");


        flywheel = hardwareMap.get(DcMotorEx.class, "pollenFlywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void shoot() {
        pollenTransfer.setPower(1);
        nectarTransfer.setPower(-1);
        flywheel.setPower(1);
    }

    public void stop() {
        pollenTransfer.setPower(0);
        nectarTransfer.setPower(0);
    }
}
