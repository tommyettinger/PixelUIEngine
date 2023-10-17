package org.mslivo.core.engine.tools.sound;

import org.mslivo.core.engine.media_manager.MediaManager;
import org.mslivo.core.engine.media_manager.media.CMediaSound;
import org.mslivo.core.engine.tools.Tools;

import java.util.HashSet;

/*
 * Plays sounds and automatically adjusts volume to distance
 */
public class SoundPlayer {
    private int range;
    private float volume;
    private float camera_x, camera_y;
    private final MediaManager mediaManager;
    HashSet<CMediaSound> playedSounds;

    public SoundPlayer(MediaManager mediaManager) {
        this(mediaManager, 0);
    }

    public SoundPlayer(MediaManager mediaManager, int range2D) {
        this.mediaManager = mediaManager;
        this.volume = 1f;
        this.playedSounds = new HashSet<>();
        setRange2D(range2D);
    }

    public float volume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = Tools.Calc.inBounds(volume, 0f, 1f);
    }

    private void setRange2D(int range2D) {
        this.range = Tools.Calc.lowerBounds(range2D, 1);
    }

    public long playSound(CMediaSound cMediaSound) {
        return playSoundInternal(cMediaSound, 1, 1, 0, false);
    }

    public long playSound(CMediaSound cMediaSound, float volume) {
        return playSoundInternal(cMediaSound, volume, 1, 0, false);
    }

    public long playSound(CMediaSound cMediaSound, float volume, float pitch) {
        return playSoundInternal(cMediaSound, volume, pitch, 0, false);
    }

    public long playSound(CMediaSound cMediaSound, float volume, float pitch, float pan) {
        return playSoundInternal(cMediaSound, volume, pitch, pan, false);
    }

    public long loopSound(CMediaSound cMediaSound) {
        return playSoundInternal(cMediaSound, 1, 1, 0, true);
    }

    public long loopSound(CMediaSound cMediaSound, float volume) {
        return playSoundInternal(cMediaSound, volume, 1, 0, true);
    }

    public long loopSound(CMediaSound cMediaSound, float volume, float pitch) {
        return playSoundInternal(cMediaSound, volume, pitch, 0, true);
    }

    public long loopSound(CMediaSound cMediaSound, float volume, float pitch, float pan) {
        return playSoundInternal(cMediaSound, volume, pitch, pan, true);
    }

    public long playSound2D(CMediaSound cMediaSound, float position_x, float position_y) {
        return playSound2DInternal(cMediaSound, position_x, position_y, 1, 1, false);
    }

    public long playSound2D(CMediaSound cMediaSound, float position_x, float position_y, float volume) {
        return playSound2DInternal(cMediaSound, position_x, position_y, volume, 1, false);
    }

    public long playSound2D(CMediaSound cMediaSound, float position_x, float position_y, float volume, float pitch) {
        return playSound2DInternal(cMediaSound, position_x, position_y, volume, pitch, false);
    }

    public long loopSound2D(CMediaSound cMediaSound, float position_x, float position_y) {
        return playSound2DInternal(cMediaSound, position_x, position_y, 1, 1, true);
    }

    public long loopSound2D(CMediaSound cMediaSound, float position_x, float position_y, float volume) {
        return playSound2DInternal(cMediaSound, position_x, position_y, volume, 1, true);
    }

    public long loopSound2D(CMediaSound cMediaSound, float position_x, float position_y, float volume, float pitch) {
        return playSound2DInternal(cMediaSound, position_x, position_y, volume, pitch, true);
    }

    private long playSoundInternal(CMediaSound cMediaSound, float volume, float pitch, float pan, boolean loop) {
        long id = loop ? mediaManager.loopCMediaSound(cMediaSound, volume * this.volume, pitch, pan) : mediaManager.playCMediaSound(cMediaSound, volume * this.volume, pitch, pan);
        playedSounds.add(cMediaSound);
        return id;
    }

    private long playSound2DInternal(CMediaSound cMediaSound, float position_x, float position_y, float volume, float pitch, boolean loop) {
        float playVolume = (range - (Tools.Calc.inBounds(Tools.Calc.Tiles.distance(camera_x, camera_y, position_x, position_y), 0, range))) / (float) range;
        playVolume = playVolume * volume * this.volume;
        float pan = 0;
        if (camera_x > position_x) {
            pan = Tools.Calc.inBounds(-((camera_x - position_x) / (float) range), -1, 0);
        } else if (camera_x < position_x) {
            pan = Tools.Calc.inBounds((position_x - camera_x) / (float) range, 0, 1);
        }
        long id = loop ? mediaManager.loopCMediaSound(cMediaSound, playVolume * this.volume, pitch, pan) : mediaManager.playCMediaSound(cMediaSound, playVolume * this.volume, pitch, pan);
        playedSounds.add(cMediaSound);
        return id;
    }

    public void stopAllSounds() {
        CMediaSound[] playedSoundsArray = playedSounds.toArray(new CMediaSound[playedSounds.size()]);
        for (int i = 0; i < playedSoundsArray.length; i++) mediaManager.getCMediaSound(playedSoundsArray[i]).stop();
    }

    public void update() {
        update(0, 0);
    }

    public void update(float camera_x, float camera_y) {
        this.camera_x = camera_x;
        this.camera_y = camera_y;
    }

    public void shutdown() {
        stopAllSounds();
        playedSounds.clear();
    }

}
