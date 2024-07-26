package org.jeongbin.physicalfighters.MajorModule;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.ShowText;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class DurationTimer extends org.jeongbin.physicalfighters.MinerModule.TimerBase
{
    private AbilityBase ab;
    private CoolDownTimer ctimer;

    public DurationTimer(AbilityBase ab, CoolDownTimer ctimer)
    {
        this.ab = ab;
        this.ctimer = ctimer;
    }

    public void EventStartTimer()
    {
        this.ab.A_DurationStart();
    }

    public void EventRunningTimer(int count)
    {
        if (((count <= 3) && (count >= 1) && (this.ab.GetShowText() == AbilityBase.ShowText.All_Text)) || (this.ab.GetShowText() == AbilityBase.ShowText.No_CoolDownText)) {
            this.ab.GetPlayer().sendMessage(String.format(ChatColor.GREEN + "지속 시간" + ChatColor.WHITE + " %d초 전", new Object[] { Integer.valueOf(count) }));
        }
    }

    public void EventEndTimer() {
        this.ab.A_DurationEnd();
        if (this.ab.GetShowText() != AbilityBase.ShowText.Custom_Text)
            this.ab.GetPlayer().sendMessage(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultDurationEnd);
        this.ctimer.StartTimer(this.ab.GetCoolDown(), true);
    }

    public void FinalEventEndTimer()
    {
        this.ab.A_FinalDurationEnd();
    }
}

