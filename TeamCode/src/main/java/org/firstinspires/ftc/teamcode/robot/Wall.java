package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Wall {
    private static final double DOWN_POS = 1.20;
    // TUNE: how far to move from down to open the gate.
    // Use a negative number if the gate opens in the other direction.
    private static final double UP_OFFSET = -0.35;

    private Servo wall;

    public Wall(HardwareMap hardwareMap) {
        wall = hardwareMap.get(Servo.class, "midIntake");
    }

    public void down() {
        wall.setPosition(DOWN_POS);
    }
    public void up() {
        wall.setPosition(DOWN_POS + UP_OFFSET);
    }
}
