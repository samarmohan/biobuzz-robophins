
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;

public class Kickup {

    public CRServo pollenKickup;

    public Kickup(HardwareMap hardwareMap) {
        pollenKickup = hardwareMap.get(CRServo.class, "pollenKickup");
    }

    public void shoot() {
        pollenKickup.setPower(0.75);
    }

    public void stop() {
        pollenKickup.setPower(0);
    }
}
