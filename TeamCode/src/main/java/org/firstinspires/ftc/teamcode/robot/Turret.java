package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.PID;

public class Turret {

    public enum Side { POLLEN, NECTAR }

    /** One flywheel + its PID + one pitch servo. Pollen and nectar each get their own. */
    private static class Shooter {
        final DcMotorEx flywheel;
        final Servo pitch;
        final PID pid;
        double targetRPM = 0;
        double currentRPM = 0;
        double pitchPos = PITCH_START;

        Shooter(HardwareMap hardwareMap, String flywheelName, String pitchName,
                double kP, double kI, double kD, double kF, double deadzone) {
            flywheel = hardwareMap.get(DcMotorEx.class, flywheelName);
            flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            pitch = hardwareMap.get(Servo.class, pitchName);

            pid = new PID(kP, kI, kD, kF, deadzone);
            pid.setFlywheel(true);
        }

        void update() {
            currentRPM = flywheel.getVelocity() * 60.0 / FLYWHEEL_TICKS_PER_REV;

            if (targetRPM <= 0) {
                flywheel.setPower(0);
                pid.reset();
            } else {
                double power = pid.update(targetRPM, currentRPM);
                flywheel.setPower(power); // never drive a flywheel backwards
            }
        }
    }

    private final Shooter pollen;
    private final Shooter nectar;

    public Turret(HardwareMap hardwareMap) {
        pollen = new Shooter(hardwareMap, "pollenFlywheel", "pollenPitch",
                POLLEN_FLYWHEEL_KP, POLLEN_FLYWHEEL_KI, POLLEN_FLYWHEEL_KD,
                POLLEN_FLYWHEEL_KF, POLLEN_FLYWHEEL_DEADZONE);

        nectar = new Shooter(hardwareMap, "nectarFlywheel", "nectarPitch",
                NECTAR_FLYWHEEL_KP, NECTAR_FLYWHEEL_KI, NECTAR_FLYWHEEL_KD,
                NECTAR_FLYWHEEL_KF, NECTAR_FLYWHEEL_DEADZONE);

        // TODO: DIRECTION
        pollen.flywheel.setDirection(DcMotor.Direction.FORWARD);
        nectar.flywheel.setDirection(DcMotor.Direction.FORWARD);
        pollen.pitch.setDirection(Servo.Direction.FORWARD);
        nectar.pitch.setDirection(Servo.Direction.FORWARD);

        setPitch(Side.POLLEN, PITCH_START);
        setPitch(Side.NECTAR, PITCH_START);
    }

    private Shooter get(Side side) {
        return side == Side.POLLEN ? pollen : nectar;
    }

    public void setTargetRPM(Side side, double rpm) {
        get(side).targetRPM = Range.clip(rpm, 0, MAX_FLYWHEEL_RPM);
    }

    public void adjustTargetRPM(Side side, double delta) {
        setTargetRPM(side, get(side).targetRPM + delta);
    }

    public double getTargetRPM(Side side) {
        return get(side).targetRPM;
    }

    public double getRPM(Side side) {
        return get(side).currentRPM;
    }

    public void stopFlywheels() {
        pollen.targetRPM = 0;
        nectar.targetRPM = 0;
    }

    public void setPitch(Side side, double position) {
        Shooter s = get(side);
        s.pitchPos = Range.clip(position, PITCH_MIN, PITCH_MAX);
        s.pitch.setPosition(s.pitchPos);
    }

    public void adjustPitch(Side side, double delta) {
        setPitch(side, get(side).pitchPos + delta);
    }

    public double getPitch(Side side) {
        return get(side).pitchPos;
    }

    public void update() {
        pollen.update();
        nectar.update();
    }
}