
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.Robot;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

@TeleOp(name = "DriveTeleOpUpdated")
public class DriveTeleOp extends OpMode {

    private Robot r;

    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();

    private boolean kickupOn = false;

    @Override
    public void init() {

        r = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initial flywheel RPM
        r.turret.setTargetRPM(0.0);
    }

    @Override
    public void start() {

        telemetry.setMsTransmissionInterval(10);
        r.drivetrain.resetFieldCentric();


        r.limelight.start();
        r.limelight.setPipeline(1);
    }

    @Override
    public void stop() {

        r.limelight.stop();

        r.turret.holdSpin();
        r.turret.stopFlywheel();
        r.turret.update();

        r.kickup.stop();
        r.intake.off();
    }

    @Override
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        drive();
        aim();
        turret();
        intake();
        shoot();
        telemetry();
    }

    // -------------------------
    // Drivetrain
    // -------------------------

    public void drive() {

        double y = -currentGamepad1.left_stick_y;
        double x = currentGamepad1.left_stick_x;
        double rx = currentGamepad1.right_stick_x;

        // D-pad strafe overrides left stick X
        if (currentGamepad1.dpad_left) {
            x = -1.0;
        } else if (currentGamepad1.dpad_right) {
            x = 1.0;
        }

        //double accelerator = currentGamepad1.left_trigger;

        r.drivetrain.drive(
                y, -x, -rx, 0
        );

        if (currentGamepad1.options &&
                !previousGamepad1.options) {

            r.drivetrain.resetFieldCentric();
        }
    }

    // -------------------------
    // Intake
    // -------------------------

    public void intake() {

        if (currentGamepad1.a) {

            r.intake.on();

        } else if (currentGamepad1.b) {

            r.intake.off();

        } else if (currentGamepad1.y) {

            r.intake.out();
        }
    }

    // -------------------------
    // Kickup
    // -------------------------

    public void shoot() {

        if (currentGamepad1.right_trigger > 0.5) {

            kickupOn = true;
            r.kickup.shoot();

        } else {

            kickupOn = false;
            r.kickup.stop();
        }
    }

    // -------------------------
    // Limelight Autoaim
    // -------------------------

    public void aim() {

        r.limelight.update();

        if (currentGamepad1.left_trigger > 0.5) {

            r.turret.autoAim(
                    r.limelight.hasTarget(),
                    r.limelight.getTx()
            );

        } else if (previousGamepad1.left_trigger > 0.5) {

            r.turret.holdSpin();
        }
    }

    // -------------------------
    // Turret / Flywheel
    // -------------------------

    public void turret() {

        // Gamepad 1: Increase RPM
        if (currentGamepad1.dpad_up &&
                !previousGamepad1.dpad_up) {

            r.turret.adjustTargetRPM(RPM_INCREMENT);
        }

        // Gamepad 1: Decrease RPM
        if (currentGamepad1.dpad_down &&
                !previousGamepad1.dpad_down) {

            r.turret.adjustTargetRPM(-RPM_INCREMENT);
        }

        // Gamepad 2: Increase RPM
        if (currentGamepad2.y &&
                !previousGamepad2.y) {

            r.turret.adjustTargetRPM(RPM_INCREMENT);
        }

        // Gamepad 2: Decrease RPM
        if (currentGamepad2.a &&
                !previousGamepad2.a) {

            r.turret.adjustTargetRPM(-RPM_INCREMENT);
        }

        // Stop flywheel
        if (currentGamepad2.b &&
                !previousGamepad2.b) {

            r.turret.stopFlywheel();
        }

        r.turret.update();
    }

    // -------------------------
    // Telemetry
    // -------------------------

    public void telemetry() {

        telemetry.addLine("========== AUTO AIM ==========");

        telemetry.addData("AutoAim",
                (currentGamepad1.left_trigger > 0.5) ? "ACTIVE" : "INACTIVE");

        telemetry.addData("Has Target",
                r.limelight.hasTarget());

        telemetry.addData("TX (deg)",
                "%.2f", r.limelight.getTx());

        telemetry.addData("TA",
                "%.2f", r.limelight.getTa());

        telemetry.addData("Turret Spin Power",
                "%.3f", r.turret.getSpinPower());

        telemetry.addLine("========== FLYWHEEL SYSTEM ==========");

        telemetry.addData("Kickup", kickupOn);

        // Flywheel
        telemetry.addData(
                "Flywheel Target RPM",
                "%.0f",
                r.turret.getTargetRPM()
        );

        telemetry.addData(
                "Flywheel Actual RPM",
                "%.0f",
                r.turret.getRPM()
        );

        telemetry.addData(
                "Flywheel Power",
                "%.3f",
                r.turret.getPower()
        );

        telemetry.addLine("========== LL ==========");

        // Limelight
        telemetry.addData(
                "LL Has Target",
                r.limelight.hasTarget()
        );

        telemetry.addData(
                "LL TX (deg)",
                "%.2f",
                r.limelight.getTx()
        );

        telemetry.addData(
                "LL TA",
                "%.2f",
                r.limelight.getTa()
        );

        telemetry.addData(
                "LL Debug",
                r.limelight.debug()
        );

        telemetry.addData(
                "Turret Spin Power",
                "%.3f",
                r.turret.getSpinPower()
        );

        telemetry.update();
    }
}
