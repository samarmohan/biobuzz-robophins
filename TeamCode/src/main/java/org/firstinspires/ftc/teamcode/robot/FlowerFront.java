package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerFront {
    private static final double UP_POS = 0.55;
    // TUNE: how far to move from up to reach the down position.
    // Negative means down is a smaller servo value than up.
    private static final double DOWN_OFFSET = -0.35;

    private Servo flowerFront;

    public FlowerFront(HardwareMap hardwareMap) {
        flowerFront = hardwareMap.get(Servo.class, "flowerFront");
        flowerUp();
    }

    public void flowerDown() {
        flowerFront.setPosition(UP_POS + DOWN_OFFSET);
    }

    public void flowerUp() {
        flowerFront.setPosition(UP_POS);
    }
}