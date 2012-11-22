/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jitsi.impl.neomedia;

import java.awt.*;
import java.lang.ref.*;
import java.util.*;
import java.util.List;

import javax.media.*;

import org.jitsi.service.configuration.*;
import org.jitsi.service.libjitsi.*;
import org.jitsi.service.neomedia.*;
import org.jitsi.service.neomedia.event.*;
import org.jitsi.util.*;

/**
 * Controls media service volume input or output. If a playback volume level
 * is set we change it on all current players, as we synchronize volume
 * on all players. Implements interface exposed from media service, also
 * implements the interface used in the Renderer part of JMF and merges the two
 * functionalities to work as one.
 *
 * @author Damian Minkov
 * @author Lyubomir Marinov
 */
public class AbstractVolumeControl
    implements VolumeControl,
               GainControl
{
    /**
     * The <tt>Logger</tt> used by the <tt>VolumeControlImpl</tt> class and
     * its instances for logging output.
     */
    private static final Logger logger
        = Logger.getLogger(AbstractVolumeControl.class);

    /**
     * The minimum volume level accepted by <tt>AbstractVolumeControl</tt>.
     */
    protected static final float MIN_VOLUME_LEVEL = 0.0F;

    /**
     * The minimum volume level expressed in percent accepted by
     * <tt>AbstractVolumeControl</tt>.
     */
    public static final int MIN_VOLUME_PERCENT = 0;

    /**
     * The maximum volume level accepted by <tt>AbstractVolumeControl</tt>.
     */
    protected static final float MAX_VOLUME_LEVEL = 1.0F;

    /**
     * The maximum volume level expressed in percent accepted by
     * <tt>AbstractVolumeControl</tt>.
     */
    public static final int MAX_VOLUME_PERCENT = 200;

    /**
     * The <tt>VolumeChangeListener</tt>s interested in volume change events
     * through the <tt>VolumeControl</tt> interface.
     * <p>
     * Because the instances of <tt>AbstractVolumeControl</tt> are global at the
     * time of this writing and, consequently, they cause the
     * <tt>VolumeChangeListener</tt>s to be leaked, the listeners are referenced
     * using <tt>WeakReference</tt>s. 
     * </p>
     */
    private final List<WeakReference<VolumeChangeListener>>
        volumeChangeListeners
            = new ArrayList<WeakReference<VolumeChangeListener>>();

    /**
     * Listeners interested in volume change inside FMJ/JMF.
     */
    private List<GainChangeListener> gainChangeListeners;

    /**
     * The current volume level.
     */
    protected float volumeLevel;

    /**
     * The power level reference used to compute equivelents between the volume
     * power level and the gain in decibels.
     */
    private float gainReferenceLevel;

    /**
     * Current mute state, by default we start unmuted.
     */
    private boolean mute = false;

    /**
     * Current level in db.
     */
    private float db;

    /**
     * The name of the configuration property which specifies the value of the
     * volume level of this <tt>AbstractVolumeControl</tt>.
     */
    private final String volumeLevelConfigurationPropertyName;

    /**
     * Creates volume control instance and initializes initial level value
     * if stored in the configuration service.
     *
     * @param volumeLevelConfigurationPropertyName the name of the configuration
     * property which specifies the value of the volume level of the new
     * instance
     */
    public AbstractVolumeControl(
        String volumeLevelConfigurationPropertyName)
    {
        // Initializes default values.
        this.volumeLevel = getDefaultVolumeLevel();
        this.gainReferenceLevel = getGainReferenceLevel();

        this.volumeLevelConfigurationPropertyName
            = volumeLevelConfigurationPropertyName;

        // Read the initial volume level from the ConfigurationService.
        try
        {
            ConfigurationService cfg = LibJitsi.getConfigurationService();

            if (cfg != null)
            {
                String volumeLevelString
                    = cfg.getString(this.volumeLevelConfigurationPropertyName);

                if (volumeLevelString != null)
                {
                    this.volumeLevel = Float.parseFloat(volumeLevelString);
                    if(logger.isDebugEnabled())
                    {
                        logger.debug("Restored volume: " + volumeLevelString);
                    }
                }
            }
        }
        catch (Throwable t)
        {
            logger.warn("Error restoring volume", t);
        }
    }

    /**
     * Applies the gain specified by <tt>gainControl</tt> to the signal defined
     * by the <tt>length</tt> number of samples given in <tt>buffer</tt>
     * starting at <tt>offset</tt>.
     *
     * @param gainControl the <tt>GainControl</tt> which specifies the gain to
     * apply
     * @param buffer the samples of the signal to apply the gain to
     * @param offset the start of the samples of the signal in <tt>buffer</tt>
     * @param length the number of samples of the signal given in
     * <tt>buffer</tt>
     */
    public static void applyGain(
            GainControl gainControl,
            byte[] buffer, int offset, int length)
    {
        if (gainControl.getMute())
            Arrays.fill(buffer, offset, offset + length, (byte) 0);
        else
        {
            // Assign a maximum of MAX_VOLUME_PERCENT to the volume scale.
            float level = gainControl.getLevel() * (MAX_VOLUME_PERCENT / 100);

            if (level != 1)
            {
                for (int i = offset, toIndex = offset + length;
                        i < toIndex;
                        i += 2)
                {
                    int i1 = i + 1;
                    short s = (short) ((buffer[i] & 0xff) | (buffer[i1] << 8));

                    /* Clip, don't wrap. */
                    int si = s;

                    si = (int) (si * level);
                    if (si > Short.MAX_VALUE)
                        s = Short.MAX_VALUE;
                    else if (si < Short.MIN_VALUE)
                        s = Short.MIN_VALUE;
                    else
                        s = (short) si;

                    buffer[i] = (byte) s;
                    buffer[i1] = (byte) (s >> 8);
                }
            }
        }
    }

    /**
     * Current volume value.
     *
     * @return the current volume level.
     *
     * @see org.jitsi.service.neomedia.VolumeControl
     */
    public float getVolume()
    {
        return volumeLevel;
    }

    /**
     * Get the current gain set for this
     * object as a value between 0.0 and 1.0
     *
     * @return The gain in the level scale (0.0-1.0).
     *
     * @see javax.media.GainControl
     */
    public float getLevel()
    {
        return volumeLevel;
    }

    /**
     * Returns the minimum allowed volume value.
     *
     * @return the minimum allowed volume value.
     *
     * @see org.jitsi.service.neomedia.VolumeControl
     */
    public float getMinValue()
    {
        return MIN_VOLUME_LEVEL;
    }

    /**
     * Returns the maximum allowed volume value.
     *
     * @return the maximum allowed volume value.
     *
     * @see org.jitsi.service.neomedia.VolumeControl
     */
    public float getMaxValue()
    {
        return MAX_VOLUME_LEVEL;
    }

    /**
     * Changes volume level.
     *
     * @param value the new level to set.
     * @return the actual level which was set.
     *
     * @see org.jitsi.service.neomedia.VolumeControl
     */
    public float setVolume(float value)
    {
        return this.setVolumeLevel(value);
    }

    /**
     * Set the gain using a floating point scale
     * with values between 0.0 and 1.0.
     * 0.0 is silence; 1.0 is the loudest
     * useful level that this <code>GainControl</code> supports.
     *
     * @param level The new gain value specified in the level scale.
     * @return The level that was actually set.
     *
     * @see javax.media.GainControl
     */
    public float setLevel(float level)
    {
        return this.setVolumeLevel(level);
    }

    /**
     * Internal implementation combining setting level from JMF
     * and from outside Media Service.
     *
     * @param value the new value, changed if different from current
     * volume settings.
     * @return the value that was changed or just the current one if
     * the same.
     */
    private float setVolumeLevel(float value)
    {
        if (value < MIN_VOLUME_LEVEL)
            value = MIN_VOLUME_LEVEL;
        else if (value > MAX_VOLUME_LEVEL)
            value = MAX_VOLUME_LEVEL;

        if (volumeLevel == value)
            return value;

        volumeLevel = value;
        updateHardwareVolume();
        fireVolumeChange();

        /*
         * Save the current volume level in the ConfigurationService so that we
         * can restore it on the next application run.
         */
        ConfigurationService cfg = LibJitsi.getConfigurationService();

        if (cfg != null)
        {
            cfg.setProperty(
                    this.volumeLevelConfigurationPropertyName,
                    String.valueOf(volumeLevel));
        }

        db = getDbFromPowerRatio(value, this.gainReferenceLevel);
        fireGainEvents();

        return volumeLevel;
    }

    /**
     * Mutes current sound.
     *
     * @param mute mutes/unmutes.
     */
    public void setMute(boolean mute)
    {
        if (this.mute != mute)
        {
            this.mute = mute;

            fireVolumeChange();
            fireGainEvents();
        }
    }

    /**
     * Get mute state of sound.
     *
     * @return mute state of sound.
     */
    public boolean getMute()
    {
        return mute;
    }

    /**
     * Set the gain in decibels.
     * Setting the gain to 0.0 (the default) implies that the audio
     * signal is neither amplified nor attenuated.
     * Positive values amplify the audio signal and negative values attenuate
     * the signal.
     *
     * @param gain The new gain in dB.
     * @return The gain that was actually set.
     *
     * @see javax.media.GainControl
     */
    public float setDB(float gain)
    {
        if(this.db != gain)
        {
            this.db = gain;
            float volumeLevel = getPowerRatioFromDb(gain, gainReferenceLevel);

            setVolumeLevel(volumeLevel);
        }
        return this.db;
    }

    /**
     * Get the current gain set for this object in dB.
     * @return The gain in dB.
     */
    public float getDB()
    {
        return this.db;
    }

    /**
     * Register for gain change update events.
     * A <code>GainChangeEvent</code> is posted when the state
     * of the <code>GainControl</code> changes.
     *
     * @param listener The object to deliver events to.
     */
    public void addGainChangeListener(GainChangeListener listener)
    {
        if(listener != null)
        {
            if(gainChangeListeners == null)
                gainChangeListeners = new ArrayList<GainChangeListener>();
            gainChangeListeners.add(listener);
        }
    }

    /**
     * Remove interest in gain change update events.
     *
     * @param listener The object that has been receiving events.
     */
    public void removeGainChangeListener(GainChangeListener listener)
    {
        if(listener != null && gainChangeListeners != null)
            gainChangeListeners.remove(listener);
    }

    /**
     * Adds a <tt>VolumeChangeListener</tt> to be informed for any change
     * in the volume levels.
     *
     * @param listener volume change listener.
     */
    public void addVolumeChangeListener(VolumeChangeListener listener)
    {
        synchronized (volumeChangeListeners)
        {
            Iterator<WeakReference<VolumeChangeListener>> i
                = volumeChangeListeners.iterator();
            boolean contains = false;

            while (i.hasNext())
            {
                VolumeChangeListener l = i.next().get();

                if (l == null)
                    i.remove();
                else if (l.equals(listener))
                    contains = true;
            }
            if(!contains)
                volumeChangeListeners.add(
                        new WeakReference<VolumeChangeListener>(listener));
        }
    }

    /**
     * Removes a <tt>VolumeChangeListener</tt>.
     *
     * @param listener the volume change listener to be removed.
     */
    public void removeVolumeChangeListener(VolumeChangeListener listener)
    {
        synchronized (volumeChangeListeners)
        {
            Iterator<WeakReference<VolumeChangeListener>> i
                = volumeChangeListeners.iterator();

            while (i.hasNext())
            {
                VolumeChangeListener l = i.next().get();

                if ((l == null) || l.equals(listener))
                    i.remove();
            }
        }
    }

    /**
     * Fire a change in volume to interested listeners.
     */
    private void fireVolumeChange()
    {
        List<VolumeChangeListener> ls;

        synchronized (volumeChangeListeners)
        {
            Iterator<WeakReference<VolumeChangeListener>> i
                = volumeChangeListeners.iterator();

            ls
                = new ArrayList<VolumeChangeListener>(
                        volumeChangeListeners.size());
            while (i.hasNext())
            {
                VolumeChangeListener l = i.next().get();

                if (l == null)
                    i.remove();
                else
                    ls.add(l);
            }
        }

        VolumeChangeEvent changeEvent
            = new VolumeChangeEvent(this, volumeLevel, mute);

        for(VolumeChangeListener l : ls)
            l.volumeChange(changeEvent);
    }

    /**
     * Fire events informing for our current state.
     */
    private void fireGainEvents()
    {
        if(gainChangeListeners != null)
        {
            GainChangeEvent gainchangeevent
                = new GainChangeEvent(this, mute, db, volumeLevel);

            for(GainChangeListener gainchangelistener : gainChangeListeners)
                gainchangelistener.gainChange(gainchangeevent);
        }
    }

    /**
     * Not used.
     * @return null
     */
    public Component getControlComponent()
    {
        return null;
    }

    /**
     * Returns the decibel value for a ratio between a power level required and
     * the reference power level.
     *
     * @param powerLevelRequired The power level wished for the signal
     * (corresponds to the mesured power level).
     * @param referencePowerLevel The reference power level.
     *
     * @return The gain in Db.
     */
    private static float getDbFromPowerRatio(
            float powerLevelRequired,
            float referencePowerLevel)
    {
        float powerRatio = powerLevelRequired / referencePowerLevel;

        // Limits the lowest power ratio to be 0.0001.
        float minPowerRatio = 0.0001F;
        float flooredPowerRatio = Math.max(powerRatio, minPowerRatio);

        return (float) (20.0 * Math.log10(flooredPowerRatio));
    }

    /**
     * Returns the mesured power level corresponding to a gain in decibel and
     * compared to the reference power level.
     *
     * @param gainInDb The gain in Db.
     * @param referencePowerLevel The reference power level.
     *
     * @return The power level the signal, which corresponds to the mesured
     * power level.
     */
    private static float getPowerRatioFromDb(
            float gainInDb,
            float referencePowerLevel)
    {
        return (float) Math.pow(10, (gainInDb / 20)) * referencePowerLevel;
    }

    /**
     * Returns the default volume level.
     *
     * @return The default volume level.
     */
    protected static float getDefaultVolumeLevel()
    {
        return MIN_VOLUME_LEVEL
            + (MAX_VOLUME_LEVEL - MIN_VOLUME_LEVEL)
                / ((MAX_VOLUME_PERCENT - MIN_VOLUME_PERCENT) / 100);
    }

    /**
     * Returns the reference volume level for computing the gain.
     *
     * @return The reference volume level for computing the gain.
     */
    protected static float getGainReferenceLevel()
    {
        return getDefaultVolumeLevel();
    }

    /**
     * Modifies the hardware microphone sensibility (hardaware amplification).
     * This is a void function for AbstractVolumeControl sincei it does not have
     * any connection to hardware volume. But, this function must be redefined
     * by any extending class.
     */
    protected void updateHardwareVolume()
    {
        // Nothing to do. This AbstractVolumeControl only modifies the gain.
    }
}
