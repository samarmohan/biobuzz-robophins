
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;

public class Kickup {

    public CRServo pollenKickup;
    public CRServo nectarTransfer;

    public DcMotorEx flywheel;

    public Kickup(HardwareMap hardwareMap) {
        pollenKickup = hardwareMap.get(CRServo.class, "pollenKickup");
        nectarTransfer = hardwareMap.get(CRServo.class, "nectarKickup");
    }

    public void shoot() {
        pollenKickup.setPower(0.75);
        nectarTransfer.setPower(-0.75);
    }

    public void stop() {
        pollenKickup.setPower(0);
        nectarTransfer.setPower(0);
    }
}
