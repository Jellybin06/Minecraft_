package org.jeongbin.physicalfighters.MajorModule;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.ShowText;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class CoolDownTimer extends org.jeongbin.physicalfighters.MinerModule.TimerBase
{
    private AbilityBase ab;

    public CoolDownTimer(AbilityBase ab)
    {
        this.ab = ab;
    }

    public void EventStartTimer()
    {
        this.ab.A_CoolDownStart();
    }

    public void EventRunningTimer(int count)
    {
        if (((count <= 3) && (count >= 1) && (this.ab.GetShowText() == AbilityBase.ShowText.All_Text)) || (this.ab.GetShowText() == AbilityBase.ShowText.No_DurationText)) {
            this.ab.GetPlayer().sendMessage(String.format(ChatColor.RED + "%d초 뒤" + ChatColor.WHITE + " 능력사용이 가능합니다.", new Object[] { Integer.valueOf(count) }));
        }
    }

    public void EventEndTimer() {
        this.ab.A_CoolDownEnd();
        if (this.ab.GetShowText() != AbilityBase.ShowText.Custom_Text) {
            this.ab.GetPlayer().sendMessage(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultCoolDownEnd);
        }
    }
}

