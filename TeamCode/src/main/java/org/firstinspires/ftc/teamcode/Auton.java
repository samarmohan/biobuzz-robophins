
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Robot;

@Autonomous(name = "Auton")
public class Auton extends LinearOpMode {

    private static final double POLLEN_RPM = 5900;

    private static final double RPM_TOLERANCE = 50;
    private static final double SPINUP_TIMEOUT_S = 4.0;

    private static final double FIRST_SHOT_S = 3.0;
    private static final double WALL_OPEN_WAIT_S = 3.0;
    private static final double SECOND_SHOT_S = 3.0;

    private Robot r;
    private String stage = "";

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        r = new Robot(hardwareMap);

        r.wall.down();
        r.flower.up();
        r.drivetrain.drive(0, 0, 0, 0);

        telemetry.addData("Status", "Ready");
        telemetry.update();

        waitForStart();

        if (!opModeIsActive()) return;

        // Spin up Pollen flywheel
        stage = "Spinning up";
        r.intake.on();
        r.turret.setTargetRPM(POLLEN_RPM);

        waitForFlywheels();

        // First batch
        stage = "Shooting first balls";
        kick(FIRST_SHOT_S);

        // Open wall
        stage = "Opening wall";
        r.wall.up();
        hold(WALL_OPEN_WAIT_S);

        // Recover flywheel speed
        stage = "Recovering RPM";
        waitForFlywheels();

        // Second batch
        stage = "Shooting more";
        kick(SECOND_SHOT_S);

        // Stop flywheel
        stage = "Done";
        r.turret.stopFlywheels();
        hold(0.3);

        // Move robot
        r.drivetrain.drive(0, -0.5, 0, 0);

        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 0.5) {
            idle();
        }

        r.drivetrain.drive(0, 0, 0, 0);
    }

    private void hold(double seconds) {
        ElapsedTime t = new ElapsedTime();

        while (opModeIsActive() && t.seconds() < seconds) {
            loopOnce();
        }
    }

    private void kick(double seconds) {
        ElapsedTime t = new ElapsedTime();

        while (opModeIsActive() && t.seconds() < seconds) {
            r.kickup.shoot();
            loopOnce();
        }

        r.kickup.stop();
    }

    private void waitForFlywheels() {
        ElapsedTime t = new ElapsedTime();

        while (opModeIsActive()
                && t.seconds() < SPINUP_TIMEOUT_S
                && !flywheelsAtSpeed()) {

            loopOnce();
        }
    }

    private boolean flywheelsAtSpeed() {
        return Math.abs(r.turret.getRPM() - POLLEN_RPM)
                < RPM_TOLERANCE;
    }

    private void loopOnce() {

        r.turret.update();

        telemetry.addData("Stage", stage);

        telemetry.addData("Pollen RPM", "%.0f / %.0f",
                r.turret.getRPM(),
                r.turret.getTargetRPM());

        telemetry.update();

        sleep(10);
    }
}
