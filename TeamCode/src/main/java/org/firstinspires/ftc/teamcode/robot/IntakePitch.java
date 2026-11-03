
package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class IntakePitch {

    private static final double UP_POS = 0.55;
    private static final double DOWN_OFFSET = -0.35;

    private Servo intakePitch;

    public IntakePitch(HardwareMap hardwareMap) {
        intakePitch = hardwareMap.get(Servo.class, "intakePitch");
    }

    public void intakeDown() {
        intakePitch.setPosition(UP_POS + DOWN_OFFSET);
    }

    public void intakeUp() {
        intakePitch.setPosition(UP_POS);
    }

//    public void intakeDie() {
//        intakePitch.setPwmDisable();
//    }
}
