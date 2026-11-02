
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;

public class Shoot {

    private CRServo pollenTransfer;
    //private CRServo nectarTransfer;

    public Shoot(HardwareMap hardwareMap) {
        pollenTransfer = hardwareMap.get(CRServo.class, "pollenTransfer");
    }

    public void shoot(){
        pollenTransfer.setPower(1);
        //nectarTranster.setPower(1);

    }

    public void stop(){
        pollenTransfer.setPower(0);
        //nectarTranster.setPower(0);
    }
}
