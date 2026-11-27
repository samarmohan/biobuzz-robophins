
package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.PID;

public class Turret {

    // Motors
    private final DcMotorEx flywheel;
    private final DcMotorEx turretSpin;

    // PID controllers
    private final PID flywheelPID;
    private final PID aimPID;

    // Flywheel
    private double targetRPM = 0;
    private double currentRPM = 0;
    private double flywheelPower = 0;

    // Turret rotation
    private static final double AIM_MAX_POWER = 1.0;
    private static final double AIM_SIGN = 1.0;
    private static final double AIM_MULTIPLIER = 1.0;

    private double aimPower = 0;
    private double manualPower = 0;
    private double spinPower = 0;

    public Turret(HardwareMap hardwareMap) {

        // Flywheel motor
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");

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

        // Turret rotation motor
        turretSpin = hardwareMap.get(DcMotorEx.class, "turretSpin");

        turretSpin.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turretSpin.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretSpin.setDirection(DcMotorSimple.Direction.FORWARD);

        // Autoaim PID
        aimPID = new PID(
                AIM_KP,
                AIM_KI,
                AIM_KD,
                AIM_KF,
                AIM_DEADZONE
        );
    }

    // -------------------------
    // Flywheel controls
    // -------------------------

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

    public void stopFlywheel() {
        targetRPM = 0;
    }

    // -------------------------
    // Manual turret rotation
    // -------------------------

    public void manualRotate(double power) {
        manualPower = Range.clip(power, -AIM_MAX_POWER, AIM_MAX_POWER);
    }

    public void stopRotation() {
        manualPower = 0;
        aimPower = 0;
        aimPID.reset();
    }

    // -------------------------
    // Autoaim
    // -------------------------

    public void autoAim(boolean hasTarget, double tx) {

        if (!hasTarget || Double.isNaN(tx)) {
            holdSpin();
            return;
        }

        double output = -aimPID.update(0.0, tx);
        aimPower = AIM_SIGN * output * AIM_MULTIPLIER;
    }

    public void holdSpin() {
        aimPower = 0;
        aimPID.reset();
    }

    public double getSpinPower() {
        return spinPower;
    }

    // -------------------------
    // Update
    // -------------------------

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

        // Turret rotation
        // Manual control takes priority over autoaim.
        if (Math.abs(manualPower) > 0.001) {
            spinPower = manualPower;
        } else {
            spinPower = Range.clip(
                    aimPower,
                    -AIM_MAX_POWER,
                    AIM_MAX_POWER
            );
        }

        turretSpin.setPower(spinPower);

        // Clear autoaim command after applying it.
        aimPower = 0;
    }
}
