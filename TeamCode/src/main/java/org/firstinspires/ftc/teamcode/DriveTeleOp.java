package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.Turret;

@TeleOp(name="TeleOp")
public class DriveTeleOp extends OpMode {

    private Robot r;
    private ElapsedTime runtime = new ElapsedTime();

    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();
    private double lastTime;

    // Which turret the controls currently affect
    private Turret.Side selectedTurret = Turret.Side.POLLEN;

    @Override
    public void init() {
        r = new Robot(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void start() {
        telemetry.setMsTransmissionInterval(10);
        runtime.reset();
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);
        drive();
        // turret();
        telemetry();
        intake();
        lastTime = runtime.seconds();
    }

    public void drive(){
        double y = -currentGamepad1.left_stick_y;
        double x = currentGamepad1.left_stick_x;
        double rx = currentGamepad1.right_stick_x;
        double accelerator = currentGamepad1.left_trigger;

        r.drivetrain.drive(y,x,rx,accelerator);

        if(currentGamepad1.options){
            r.drivetrain.resetFieldCentric();
        }
    }

    public void intake() {
        if (currentGamepad1.a) {
            r.intake.intakeOn();
        } else if (currentGamepad1.b) {
            r.intake.intakeOff();
        } else if (currentGamepad1.y) {
            r.intake.intakeOut();
        }
    }

    // A is toggle
    // Left stick y is pitch of turret
    // dpad up/down increments flywheel
    // b is stop
    public void turret() {
        if (currentGamepad1.a && !previousGamepad1.a) {
            selectedTurret = (selectedTurret == Turret.Side.POLLEN)
                    ? Turret.Side.NECTAR
                    : Turret.Side.POLLEN;
        }

        if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
            r.turret.adjustTargetRPM(selectedTurret, RPM_INCREMENT);
        }
        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            r.turret.adjustTargetRPM(selectedTurret, -RPM_INCREMENT);
        }
        if (currentGamepad1.b && !previousGamepad1.b) {
            r.turret.stopFlywheels();
        }

        double dt = Math.min(runtime.seconds() - lastTime, 0.1);
        double stick = -currentGamepad1.left_stick_y;
        if (Math.abs(stick) > STICK_DEADBAND) {
            r.turret.adjustPitch(selectedTurret, stick * PITCH_SPEED * dt);
        }
        r.turret.update();
    }

    public void telemetry() {
        telemetry.addData("Selected Turret", selectedTurret);
        //addTurretTelemetry("Pollen", Turret.Side.POLLEN);
        //addTurretTelemetry("Nectar", Turret.Side.NECTAR);
        telemetry.update();

    }

    private void addTurretTelemetry(String label, Turret.Side side) {
        telemetry.addData(label + " Target RPM", "%.0f", r.turret.getTargetRPM(side));
        telemetry.addData(label + " Actual RPM", "%.0f", r.turret.getRPM(side));
        telemetry.addData(label + " Pitch", "%.3f", r.turret.getPitch(side));
    }

}