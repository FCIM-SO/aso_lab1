package aso_lab1.team_rocket;


import javax.sound.sampled.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static aso_lab1.team_rocket.logger.Log.info;
import static aso_lab1.team_rocket.logger.Log.infoFormat;


/**
 * Custom Implementation of TimerTask for ASO lab 1
 */
public class CosneanuValeriuImpl extends TimerTask {
    private final Timer timer;
    private Clip audioClip;
    private long duration = 0;

    /**
     * Constructor
     *
     * @param path  path of audio file
     * @param timer timer what should be bound with TimerTask
     */
    public CosneanuValeriuImpl(final String path, Timer timer) {
        info("CONSTRUCTOR IS RUNNING");

        this.timer = timer;
        final File audioFile = new File(path);

        try {
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            final AudioFormat format = audioStream.getFormat();
            final DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            duration = audioClip.getMicrosecondLength() / 1_000_000;
        } catch (Exception exception) {
            infoFormat("[ CONSTRUCTOR THROW EXCEPTION: {%s}]", exception.toString());
        }
    }

    /**
     * @return duration of audio file
     */
    public long getDuration() {
        return duration;
    }

    /**
     * overriding TimerTask run method
     * start playing audio file
     */
    @Override
    public void run() {
        try {
            audioClip.start();
            info("METHOD: {run} IS RUNNING");
            Thread.sleep(audioClip.getMicrosecondLength() / 1000);
            audioClip.stop();
            audioClip.setFramePosition(0);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            timer.cancel();
            infoFormat("ERROR PLAYING AUDIO {%s}", e.toString());
        }
    }
}
