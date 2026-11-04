package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Flower {

    private Servo flower;

    public Flower(HardwareMap hardwareMap) {
        flower = hardwareMap.get(Servo.class, "flowerFront");
    }

    public void down() {
        flower.setPosition(0);
    }

    public void up() {
        flower.setPosition(1);
    }
}