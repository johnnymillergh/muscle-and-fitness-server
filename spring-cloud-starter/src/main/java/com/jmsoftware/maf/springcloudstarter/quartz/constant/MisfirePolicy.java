package com.jmsoftware.maf.springcloudstarter.quartz.constant;

import com.jmsoftware.maf.common.bean.EnumerationBase;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import java.util.Calendar;

/**
 * Description: MisfirePolicy, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 8:20 AM
 * @see Trigger
 * @see CronTrigger
 **/
@Getter
@ToString
public enum MisfirePolicy implements EnumerationBase<Byte> {
    /**
     * Instructs the <code>{@link Scheduler}</code> that the
     * <code>Trigger</code> will never be evaluated for a misfire situation,
     * and that the scheduler will simply try to fire it as soon as it can,
     * and then update the Trigger as if it had fired at the proper time.
     *
     * <p>NOTE: if a trigger uses this instruction, and it has missed
     * several of its scheduled firings, then several rapid firings may occur
     * as the trigger attempt to catch back up to where it would have been.
     * For example, a SimpleTrigger that fires every 15 seconds which has
     * misfired for 5 minutes will fire 20 times once it gets the chance to
     * fire.</p>
     */
    MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY((byte) -1),
    /**
     * Instructs the <code>{@link Scheduler}</code> that upon a mis-fire
     * situation, the <code>updateAfterMisfire()</code> method will be called
     * on the <code>Trigger</code> to determine the mis-fire instruction,
     * which logic will be trigger-implementation-dependent.
     *
     * <p>
     * In order to see if this instruction fits your needs, you should look at
     * the documentation for the <code>updateAfterMisfire()</code> method
     * on the particular <code>Trigger</code> implementation you are using.
     * </p>
     */
    MISFIRE_INSTRUCTION_SMART_POLICY((byte) 0),
    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that upon a mis-fire
     * situation, the <code>{@link CronTrigger}</code> wants to be fired now
     * by <code>Scheduler</code>.
     * </p>
     */
    MISFIRE_INSTRUCTION_FIRE_ONCE_NOW((byte) 1),
    /**
     * <p>
     * Instructs the <code>{@link Scheduler}</code> that upon a mis-fire
     * situation, the <code>{@link CronTrigger}</code> wants to have it's
     * next-fire-time updated to the next time in the schedule after the
     * current time (taking into account any associated <code>{@link Calendar}</code>,
     * but it does not want to be fired now.
     * </p>
     */
    MISFIRE_INSTRUCTION_DO_NOTHING((byte) 2),
    ;

    private final Byte value;

    MisfirePolicy(Byte value) {
        this.value = value;
    }

    public static MisfirePolicy getByValue(Byte value) {
        val values = MisfirePolicy.values();
        for (val enumeration : values) {
            if(enumeration.getValue().equals(value)) {
                return enumeration;
            }
        }
        return null;
    }
}
