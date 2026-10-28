package org.firstinspires.ftc.teamcode.utils;

public class TeleOpConstants {

    // Pollen flywheel
    public static final double POLLEN_FLYWHEEL_KP = 0.001;
    public static final double POLLEN_FLYWHEEL_KI = 0.0;
    public static final double POLLEN_FLYWHEEL_KD = 0.0;
    public static final double POLLEN_FLYWHEEL_KF = 0.0;
    public static final double POLLEN_FLYWHEEL_DEADZONE = 0.0;

    // Nectar flywheel
    public static final double NECTAR_FLYWHEEL_KP = 0.001;
    public static final double NECTAR_FLYWHEEL_KI = 0.0;
    public static final double NECTAR_FLYWHEEL_KD = 0.0;
    public static final double NECTAR_FLYWHEEL_KF = 0.0;
    public static final double NECTAR_FLYWHEEL_DEADZONE = 0.0;

    public static final double FLYWHEEL_TICKS_PER_REV = 28.0;

    public static final double MAX_FLYWHEEL_RPM = 6000.0;
    public static final double RPM_INCREMENT = 100.0;

    public static final double PITCH_MIN = 0.0;
    public static final double PITCH_MAX = 1.0;
    public static final double PITCH_START = 0.5;          // servo moves here on init

    public static final double PITCH_SPEED = 0.25;         // servo position change per second at full stick
    public static final double STICK_DEADBAND = 0.05;
}