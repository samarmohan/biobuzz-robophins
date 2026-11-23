
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp(name = "AutoAimTest")
public class AutoAimTest extends OpMode {

    private Robot r;

    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();

    @Override
    public void init() {
        r = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        r.limelight.start();
        r.limelight.setPipeline(1);
    }

    @Override
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        aim();
        telemetry();
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

        // Update turret rotation
        r.turret.update();
    }

    public void telemetry() {

        telemetry.addData("LL Has Target",
                r.limelight.hasTarget());

        telemetry.addData("LL TX (deg)",
                "%.2f", r.limelight.getTx());

        telemetry.addData("LL TA",
                "%.2f", r.limelight.getTa());

        telemetry.addData("LL Debug",
                r.limelight.debug());

        telemetry.addData("AutoAim",
                currentGamepad1.left_bumper ? "ACTIVE" : "INACTIVE");

        telemetry.addData("Spin Power",
                "%.3f", r.turret.getSpinPower());

        telemetry.update();
    }

    @Override
    public void stop() {
        r.limelight.stop();
        r.turret.holdSpin();
        r.turret.update();
    }
}
