package org.jeongbin.physicalfighters.MinerModule;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;

public class EventData
{
    public AbilityBase ab;
    public int parameter = 0;

    public EventData(AbilityBase ab) { this(ab, 0); }

    public EventData(AbilityBase ab, int parameter) { this.ab = ab;
        this.parameter = parameter;
    }
}
