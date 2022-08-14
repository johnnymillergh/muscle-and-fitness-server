package com.jmsoftware.maf.springcloudstarter.quartz.constant

/**
 * Description: MisfirePolicy, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 8:20 AM
 * @see Trigger
 *
 * @see CronTrigger
 */
enum class MisfirePolicy(val value: Byte) {
    /**
     * Instructs the `[Scheduler]` that the
     * `Trigger` will never be evaluated for a misfire situation,
     * and that the scheduler will simply try to fire it as soon as it can,
     * and then update the Trigger as if it had fired at the proper time.
     *
     *
     * NOTE: if a trigger uses this instruction, and it has missed
     * several of its scheduled firings, then several rapid firings may occur
     * as the trigger attempt to catch back up to where it would have been.
     * For example, a SimpleTrigger that fires every 15 seconds which has
     * misfired for 5 minutes will fire 20 times once it gets the chance to
     * fire.
     */
    MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY((-1).toByte()),

    /**
     * Instructs the `[Scheduler]` that upon a mis-fire
     * situation, the `updateAfterMisfire()` method will be called
     * on the `Trigger` to determine the mis-fire instruction,
     * which logic will be trigger-implementation-dependent.
     *
     * In order to see if this instruction fits your needs, you should look at
     * the documentation for the `updateAfterMisfire()` method
     * on the particular `Trigger` implementation you are using.
     */
    MISFIRE_INSTRUCTION_SMART_POLICY(0.toByte()),

    /**
     * Instructs the `[Scheduler]` that upon a mis-fire
     * situation, the `[CronTrigger]` wants to be fired now
     * by `Scheduler`.
     */
    MISFIRE_INSTRUCTION_FIRE_ONCE_NOW(1.toByte()),

    /**
     * Instructs the `[Scheduler]` that upon a mis-fire
     * situation, the `[CronTrigger]` wants to have it's
     * next-fire-time updated to the next time in the schedule after the
     * current time (taking into account any associated `[Calendar]`,
     * but it does not want to be fired now.
     */
    MISFIRE_INSTRUCTION_DO_NOTHING(2.toByte());

    companion object {
        fun getByValue(value: Byte): MisfirePolicy? {
            val values = values()
            for (enumeration in values) {
                if (enumeration.value == value) {
                    return enumeration
                }
            }
            return null
        }
    }
}
