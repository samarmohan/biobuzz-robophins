package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Limelight {
    private final Limelight3A limelight;
    private LLResult result;
    private LLStatus status;

    private int pipeline = 0;
    private boolean started = false;

    private final ElapsedTime sinceStart = new ElapsedTime();
    private final ElapsedTime sinceSwitch = new ElapsedTime();
    private final ElapsedTime sinceStatus = new ElapsedTime();

    public Limelight(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    public void start() {
        limelight.start();
        started = true;
        sinceStart.reset();
        sinceSwitch.reset();
    }

    public void stop() {
        limelight.stop();
        started = false;
    }

    public void setPipeline(int pipeline) {
        this.pipeline = pipeline;
        limelight.pipelineSwitch(pipeline);
        sinceSwitch.reset();
    }

    public void update() {
        // A pipeline switch sent right as polling starts can get dropped, so keep re-sending it
        // every 0.5 s for the first 3 s after start() (harmless if it already took).
        if (started && sinceStart.seconds() < 3.0 && sinceSwitch.seconds() > 0.5) {
            limelight.pipelineSwitch(pipeline);
            sinceSwitch.reset();
        }
        result = limelight.getLatestResult();
    }

    public boolean hasTarget() {
        return result != null
                && result.isValid();
    }

    public double getTx() {
        return hasTarget() ? result.getTx() : 0.0;
    }

    public double getTa() {
        return hasTarget() ? result.getTa() : 0.0;
    }

    /** One-line status for telemetry: what the SDK actually sees. */
    public String debug() {
        if (sinceStatus.seconds() > 1.0) { // getStatus() is a device call, so only refresh it about once a second
            status = limelight.getStatus();
            sinceStatus.reset();
        }

        String res = result == null ? "null" : (result.isValid() ? "valid" : "invalid");
        String stale = result == null ? "-" : result.getStaleness() + "ms";
        String pipe = status == null ? "?" : String.valueOf(status.getPipelineIndex());
        String fps = status == null ? "?" : String.format("%.0f", status.getFps());

        return "connected=" + limelight.isConnected()
                + " running=" + limelight.isRunning()
                + " result=" + res
                + " stale=" + stale
                + " pipe=" + pipe + " (want " + pipeline + ")"
                + " fps=" + fps;
    }
}