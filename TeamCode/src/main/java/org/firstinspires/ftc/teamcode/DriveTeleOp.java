
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Robot;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

@TeleOp(name="DriveTeleOp")
public class DriveTeleOp extends OpMode {

    private Robot r;
    private ElapsedTime runtime = new ElapsedTime();

    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();

    private boolean wallUp = false;
    private boolean flowerUp = true;
    private boolean kickupOn = false;

    @Override
    public void init() {
        r = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        r.flower.up();

        // Initial Pollen flywheel RPM
        r.turret.setTargetRPM(5900.0);
    }

    @Override
    public void start() {
        telemetry.setMsTransmissionInterval(10);
        runtime.reset();

        r.wall.down();
        r.limelight.start();
        r.limelight.setPipeline(1);
    }

    @Override
    public void stop() {
        r.limelight.stop();
        r.turret.holdSpin();
        r.turret.stopFlywheels();
        r.turret.update();
        r.kickup.stop();
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
        wall();
        flower();
        shoot();
        telemetry();
    }

    public void drive() {
        double y = -currentGamepad1.left_stick_y;
        double x = currentGamepad1.left_stick_x;
        double rx = currentGamepad1.right_stick_x;
        double accelerator = currentGamepad1.left_trigger;

        r.drivetrain.driveFieldCentric(y, x, rx, accelerator);

        if (currentGamepad1.options) {
            r.drivetrain.resetFieldCentric();
        }
    }

    public void intake() {
        if (currentGamepad1.a) {
            r.intake.on();
        } else if (currentGamepad1.b) {
            r.intake.off();
        } else if (currentGamepad1.y) {
            r.intake.out();
        }
    }

    public void wall() {
        if ((currentGamepad1.right_bumper && !previousGamepad1.right_bumper)
                || (currentGamepad2.right_bumper && !previousGamepad2.right_bumper)) {

            wallUp = !wallUp;

            if (wallUp) {
                r.wall.up();
            } else {
                r.wall.down();
            }
        }
    }

    public void flower() {
        if (currentGamepad1.dpad_up) {
            flowerUp = true;
        } else if (currentGamepad1.dpad_down) {
            flowerUp = false;
        }

        if (flowerUp) {
            r.flower.up();
        } else {
            r.flower.down();
        }
    }

    public void shoot() {
        if (currentGamepad1.right_trigger > 0.5) {
            kickupOn = true;
            r.kickup.shoot();
        } else {
            kickupOn = false;
            r.kickup.stop();
        }
    }

    public void aim() {
        r.limelight.update();

        if (currentGamepad1.left_bumper) {
            r.turret.autoAim(
                    r.limelight.hasTarget(),
                    r.limelight.getTx()
            );
        } else if (previousGamepad1.left_bumper) {
            r.turret.holdSpin();
        }
    }

    public void turret() {

        // Increase Pollen RPM
        if (currentGamepad2.y && !previousGamepad2.y) {
            r.turret.adjustTargetRPM(RPM_INCREMENT);
        }

        // Decrease Pollen RPM
        if (currentGamepad2.a && !previousGamepad2.a) {
            r.turret.adjustTargetRPM(-RPM_INCREMENT);
        }

        // Stop Pollen flywheel
        if (currentGamepad2.b && !previousGamepad2.b) {
            r.turret.stopFlywheels();
        }

        r.turret.update();
    }

    public void telemetry() {

        telemetry.addData("Flower Up", flowerUp);
        telemetry.addData("Wall Up", wallUp);
        telemetry.addData("Kickup", kickupOn);

        telemetry.addLine("--------------------------------");

        // Pollen turret
        telemetry.addData("Pollen Target RPM",
                "%.0f", r.turret.getTargetRPM());

        telemetry.addData("Pollen Actual RPM",
                "%.0f", r.turret.getRPM());

        telemetry.addData("Pollen Flywheel Power",
                "%.3f", r.turret.getPower());

        telemetry.addData("Pollen Pitch",
                "%.3f", r.turret.getPitch());

        telemetry.addLine("--------------------------------");

        // Limelight
        telemetry.addData("LL Has Target",
                r.limelight.hasTarget());

        telemetry.addData("LL TX (deg)",
                "%.2f", r.limelight.getTx());

        telemetry.addData("LL TA",
                "%.2f", r.limelight.getTa());

        telemetry.addData("LL Debug",
                r.limelight.debug());

        telemetry.addData("Spin Power",
                "%.3f", r.turret.getSpinPower());

        telemetry.update();
    }
}
