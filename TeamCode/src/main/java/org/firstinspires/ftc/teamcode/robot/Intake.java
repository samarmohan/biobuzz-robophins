package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private DcMotor intake;
    private Servo intakePitch;

    private static final double UP_POS = 0.55;
    private static final double DOWN_OFFSET = -0.35;

    public Intake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakePitch = hardwareMap.get(Servo.class, "intakePitch");


    }

    public void on() {
        intake.setPower(1);
    }

    public void off() {
        intake.setPower(0);
    }

    public void out() {
        intake.setPower(-1);
    }

    public void down() {
        intakePitch.setPosition(UP_POS + DOWN_OFFSET);
    }

    public void up() {
        intakePitch.setPosition(UP_POS);
    }

//    public void intakeDie() {
//        intakePitch.setPwmDisable();
//    }

}
