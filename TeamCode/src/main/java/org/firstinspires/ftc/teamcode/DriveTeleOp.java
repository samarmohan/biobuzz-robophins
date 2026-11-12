package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.Turret;

import static org.firstinspires.ftc.teamcode.utils.TeleOpConstants.*;

// Driver 1
// right trigger: shoot
// left trigger: crawl
// left bumper (toggle): auto aim
// right bumper (toggle): wall
// a: intake on
// b: intake off
// y: intake out
// dpad up: flower up
// dpad down: flower down


// Driver 2
// left trigger: auto aim
// left stick: nectar turret offset
// dpad up/down: nectar rpm +/- 100

// right stick: pollen turret offset
// y and a: pollen rpm +/- 100

@TeleOp(name="DriveTeleOp")
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

    private boolean wallUp = false; // gate starts down (MidIntake constructor sets it down)
    private boolean flowerUp = true; // starts up
    private boolean intakeUp = false;
    private boolean kickupOn = false;



    @Override
    public void init() {
        r = new Robot(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        r.flower.up();
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
        telemetry();
        intake();
        wall();
        flower();
        shoot();
        lastTime = runtime.seconds();
    }

    public void drive(){
        double y = -currentGamepad1.left_stick_y;
        double x = currentGamepad1.left_stick_x;
        double rx = currentGamepad1.right_stick_x;
        double accelerator = currentGamepad1.left_trigger;

        r.drivetrain.driveFieldCentric(y,x,rx,accelerator);

        if(currentGamepad1.options){
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
        if ((currentGamepad1.right_bumper && !previousGamepad1.right_bumper) || (currentGamepad2.right_bumper && !previousGamepad2.right_bumper)) {
            wallUp = !wallUp;

            if(wallUp){
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

    public void shoot(){
        if (currentGamepad1.right_trigger > 0.5) {
            kickupOn = true;
            r.kickup.shoot();
        } else{
            kickupOn = false;
            r.kickup.stop();
        }
    }

    public void aim() {
        r.limelight.update();

        if (currentGamepad1.left_bumper) {
            r.turret.autoAim(r.limelight.hasTarget(), r.limelight.getTx());
        } else if (previousGamepad1.left_bumper) {
            r.turret.holdSpin();
        }
    }

    // A is toggle
    // Left stick y is pitch of turret
    // dpad up/down increments flywheel
    // b is stop
    public void turret() {
//        if (currentGamepad2.a && !previousGamepad2.a) {
//            selectedTurret = (selectedTurret == Turret.Side.POLLEN)
//                    ? Turret.Side.NECTAR
//                    : Turret.Side.POLLEN;
//        }
        r.turret.setTargetRPM(Turret.Side.POLLEN, 5900.0);
        r.turret.setTargetRPM(Turret.Side.NECTAR, 0);


        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
            r.turret.adjustTargetRPM(Turret.Side.NECTAR, RPM_INCREMENT);
        }
        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
            r.turret.adjustTargetRPM(Turret.Side.NECTAR, -RPM_INCREMENT);
        }
        if (currentGamepad2.y && !previousGamepad2.y) {
            r.turret.adjustTargetRPM(Turret.Side.POLLEN, RPM_INCREMENT);
        }
        if (currentGamepad2.a && !previousGamepad2.a) {
            r.turret.adjustTargetRPM(Turret.Side.POLLEN, -RPM_INCREMENT);
        }
        if (currentGamepad2.b && !previousGamepad2.b) {
            r.turret.stopFlywheels();
        }

        double dt = Math.min(runtime.seconds() - lastTime, 0.1);
        double nectarStick = -currentGamepad2.left_stick_y;
        double pollenStick = -currentGamepad2.right_stick_y;

//        if (Math.abs(nectarStick) > STICK_DEADBAND) {
//            r.turret.adjustPitch(Turret.Side.NECTAR, nectarStick * PITCH_SPEED * dt);
//        }
//        if (Math.abs(pollenStick) > STICK_DEADBAND) {
//            r.turret.adjustPitch(Turret.Side.POLLEN, pollenStick* PITCH_SPEED * dt);
//        }
//        double nectarStick = currentGamepad2.left_stick_x;
//        double pollenStick = currentGamepad2.right_stick_x;
//        if (nectarStick > STICK_DEADBAND) {
//            r.turret.nectarServo.setPower(0.5);
//        } else if (nectarStick < -STICK_DEADBAND) {
//            r.turret.nectarServo.setPower(-0.5);
//        } else {
//            r.turret.nectarServo.setPower(0);
//        }
//        if (nectarStick > STICK_DEADBAND) {
//            r.turret.pollenTurret.setPower(0.5);
//        } else if (nectarStick < -STICK_DEADBAND) {
//            r.turret.pollenTurret.setPower(-0.5);
//        } else {
//            r.turret.pollenTurret.setPower(0);
//        }
        r.turret.update();
    }

    public void telemetry() {
        telemetry.addData("flowerUp", flowerUp);
        telemetry.addData("wallUp", wallUp);
        telemetry.addData("intakeUp", intakeUp);
        telemetry.addData("kickup", kickupOn);

        telemetry.addLine("--------------------------------");

        telemetry.addData("Selected Turret", selectedTurret);
        addTurretTelemetry("Pollen", Turret.Side.POLLEN);
        addTurretTelemetry("Nectar", Turret.Side.NECTAR);

        telemetry.addLine("--------------------------------");



        telemetry.addData("X held (aiming)", currentGamepad2.x);

        // Limelight
        telemetry.addData("LL has target", r.limelight.hasTarget());
        telemetry.addData("LL tx (deg)", "%.2f", r.limelight.getTx());
        telemetry.addData("LL ta", "%.2f", r.limelight.getTa());
        telemetry.addData("LL debug", r.limelight.debug());

        telemetry.addData("Spin power", "%.3f", r.turret.getSpinPower());

        telemetry.update();
    }

    private void addTurretTelemetry(String label, Turret.Side side) {
        telemetry.addData(label + " Target RPM", "%.0f", r.turret.getTargetRPM(side));
        telemetry.addData(label + " Actual RPM", "%.0f", r.turret.getRPM(side));
        telemetry.addData(label + " Power", "%.0f", r.turret.getPower(side));
        telemetry.addData(label + " Pitch", "%.3f", r.turret.getPitch(side));
    }

}