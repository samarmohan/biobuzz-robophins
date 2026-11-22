
package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.PID;

public class Turret {

    // Pollen flywheel and pitch
    private final DcMotorEx flywheel;
    private final Servo pitch;
    private final CRServo pollenTurret;

    private final PID flywheelPID;
    private final PID aimPID;

    private double targetRPM = 0;
    private double currentRPM = 0;
    private double flywheelPower = 0;
    private double pitchPos = PITCH_START;

    // Auto aim
    private static final double AIM_MAX_POWER = 0.6;
    private static final double AIM_SIGN = 1.0;

    private double aimPower = 0;
    private double spinPower = 0;

    public Turret(HardwareMap hardwareMap) {

        // Flywheel
        flywheel = hardwareMap.get(DcMotorEx.class, "pollenFlywheel");

        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel.setDirection(DcMotorSimple.Direction.FORWARD);

        flywheelPID = new PID(
                POLLEN_FLYWHEEL_KP,
                POLLEN_FLYWHEEL_KI,
                POLLEN_FLYWHEEL_KD,
                POLLEN_FLYWHEEL_KF,
                POLLEN_FLYWHEEL_DEADZONE
        );

        flywheelPID.setFlywheel(true);

        // Pitch
        pitch = hardwareMap.get(Servo.class, "pollenPitch");
        pitch.setDirection(Servo.Direction.FORWARD);
        setPitch(PITCH_START);

        // Turret rotation
        pollenTurret = hardwareMap.get(CRServo.class, "pollenServo");
        pollenTurret.setDirection(DcMotorSimple.Direction.FORWARD);

        // Auto aim PID
        aimPID = new PID(AIM_KP, AIM_KI, AIM_KD, AIM_KF, AIM_DEADZONE);
    }

    // Flywheel controls

    public void setTargetRPM(double rpm) {
        targetRPM = Range.clip(rpm, 0, MAX_FLYWHEEL_RPM);
    }

    public void adjustTargetRPM(double delta) {
        setTargetRPM(targetRPM + delta);
    }

    public double getTargetRPM() {
        return targetRPM;
    }

    public double getRPM() {
        return currentRPM;
    }

    public double getPower() {
        return flywheelPower;
    }

    public void stopFlywheels() {
        targetRPM = 0;
    }

    // Pitch controls

    public void setPitch(double position) {
        pitchPos = Range.clip(position, PITCH_MIN, PITCH_MAX);
        pitch.setPosition(pitchPos);
    }

    public void adjustPitch(double delta) {
        setPitch(pitchPos + delta);
    }

    public double getPitch() {
        return pitchPos;
    }

    // Auto aim

    public void autoAim(boolean hasTarget, double tx) {

        if (!hasTarget || Double.isNaN(tx)) {
            holdSpin();
            return;
        }

        double output = -aimPID.update(0.0, tx);
        aimPower = AIM_SIGN * output;
    }

    public void holdSpin() {
        aimPower = 0;
        aimPID.reset();
    }

    public double getSpinPower() {
        return spinPower;
    }

    // Update

    public void update() {

        // Flywheel PID
        currentRPM = flywheel.getVelocity()
                * 60.0 / FLYWHEEL_TICKS_PER_REV;

        if (targetRPM <= 0) {
            flywheel.setPower(0);
            flywheelPower = 0;
            flywheelPID.reset();
        } else {
            flywheelPower = flywheelPID.update(targetRPM, currentRPM);
            flywheel.setPower(flywheelPower);
        }

        // Pollen turret rotation
        spinPower = Range.clip(
                aimPower,
                -AIM_MAX_POWER,
                AIM_MAX_POWER
        );

        aimPower = 0;

        pollenTurret.setPower(spinPower);
    }
}
