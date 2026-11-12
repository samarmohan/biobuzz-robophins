package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.Turret;

@Autonomous()
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

        stage = "Spinning up";
        r.intake.on();
        r.turret.setTargetRPM(Turret.Side.POLLEN, POLLEN_RPM);
        waitForFlywheels();

        stage = "Shooting first balls";
        kick(FIRST_SHOT_S);

        stage = "Opening wall";
        r.wall.up();
        hold(WALL_OPEN_WAIT_S);

        stage = "Recovering RPM";
        waitForFlywheels();
        stage = "Shooting more";
        kick(SECOND_SHOT_S);

        stage = "Done";
        r.turret.stopFlywheels();
        hold(0.3);

        r.drivetrain.drive(0, -0.5, 0, 0);
        runtime.reset();
        while (runtime.seconds() < 0.5) {
            continue;
        }
//
//        r.drivetrain.drive(0.5, 0,0,0);
//        runtime.reset();
//        while (runtime.seconds() < 2.5) {
//            continue;
//        }
//
//        r.drivetrain.drive(0, -0.25,0,0);
//        runtime.reset();
//        while (runtime.seconds() < 2) {
//            continue;
//        }
//        r.drivetrain.drive(0, 0, 0, 0);
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
        while (opModeIsActive() && t.seconds() < SPINUP_TIMEOUT_S && !flywheelsAtSpeed()) {
            loopOnce();
        }
    }

    private boolean flywheelsAtSpeed() {
        return atSpeed(Turret.Side.POLLEN);
    }

    private boolean atSpeed(Turret.Side side) {
        double target = r.turret.getTargetRPM(side);
        if (target <= 0) return true;
        return Math.abs(r.turret.getRPM(side) - target) < RPM_TOLERANCE;
    }

    private void loopOnce() {
        r.turret.update();

        telemetry.addData("Stage", stage);
        telemetry.addData("Pollen RPM", "%.0f / %.0f",
                r.turret.getRPM(Turret.Side.POLLEN), r.turret.getTargetRPM(Turret.Side.POLLEN));
        telemetry.addData("Nectar RPM", "%.0f / %.0f",
                r.turret.getRPM(Turret.Side.NECTAR), r.turret.getTargetRPM(Turret.Side.NECTAR));
        telemetry.update();

        sleep(10);
    }
}
