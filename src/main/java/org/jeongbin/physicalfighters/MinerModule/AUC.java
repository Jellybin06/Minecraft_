package org.jeongbin.physicalfighters.MinerModule;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.PhysicalFighters;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class AUC
{
    public static final void InfoTextOut(Player p)
    {
        AbilityBase a;
        if (org.jeongbin.physicalfighters.MajorModule.AbilityList.assimilation.GetPlayer() == p) {
            a = org.jeongbin.physicalfighters.MajorModule.AbilityList.assimilation;
        }
        else
        {
            a = AbilityBase.FindAbility(p);
        }

        if (a != null) {
            p.sendMessage(ChatColor.GREEN + "---------------");
            p.sendMessage(ChatColor.GOLD + "- 능력 정보 -");
            p.sendMessage(ChatColor.DARK_AQUA + "참고 : 능력 리스트중 가장 상단의 능력만 보여줍니다.");
            if (PhysicalFighters.ReverseMode) {
                p.sendMessage(ChatColor.AQUA + a.GetAbilityName() + ChatColor.WHITE + " [" + a.GetRank().GetText() + ChatColor.WHITE + "] ");
            }
            else
                p.sendMessage(ChatColor.AQUA + a.GetAbilityName() + ChatColor.WHITE + " [" + TypeTextOut(a) + "] " + a.GetRank().GetText());
            for (int l = 0; l < a.GetGuide().length; l++) {
                p.sendMessage(a.GetGuide()[l]);
            }
            if (!PhysicalFighters.ReverseMode)
                p.sendMessage(TimerTextOut(a));
            p.sendMessage(ChatColor.GREEN + "---------------");
            return;
        }
        p.sendMessage(ChatColor.RED + "능력이 없거나 옵저버입니다.");
    }

    public static final String TypeTextOut(AbilityBase ab) {
        AbilityBase.Type type = ab.GetAbilityType();
        if (!ab.GetRunAbility()) return ChatColor.RED + "능력 비활성화됨" + ChatColor.WHITE;
        if (type == AbilityBase.Type.Active_Continue) return ChatColor.GREEN + "액티브 " + ChatColor.WHITE + "/ " + ChatColor.GOLD + "지속" + ChatColor.WHITE;
        if (type == AbilityBase.Type.Active_Immediately) return ChatColor.GREEN + "액티브 " + ChatColor.WHITE + "/ " + ChatColor.GOLD + "즉발" + ChatColor.WHITE;
        if (type == AbilityBase.Type.Passive_AutoMatic) return ChatColor.GREEN + "패시브 " + ChatColor.WHITE + "/ " + ChatColor.GOLD + "자동" + ChatColor.WHITE;
        if (type == AbilityBase.Type.Passive_Manual) return ChatColor.GREEN + "패시브 " + ChatColor.WHITE + "/ " + ChatColor.GOLD + "수동" + ChatColor.WHITE;
        return "Unknown";
    }

    public static final String TimerTextOut(AbilityBase data) {
        if (data.GetAbilityType() == AbilityBase.Type.Active_Continue)
            return String.format(ChatColor.RED + "쿨타임 : " + ChatColor.WHITE + "%d초 / " + ChatColor.RED + "지속시간 : " + ChatColor.WHITE + "%d초", new Object[] { Integer.valueOf(data.GetCoolDown()), Integer.valueOf(data.GetDuration()) });
        if (data.GetAbilityType() == AbilityBase.Type.Active_Immediately)
            return String.format(ChatColor.RED + "쿨타임 : " + ChatColor.WHITE + "%d초 / " + ChatColor.RED + "지속시간 : " + ChatColor.WHITE + "없음", new Object[] { Integer.valueOf(data.GetCoolDown()) });
        if (data.GetAbilityType() == AbilityBase.Type.Passive_AutoMatic)
            return ChatColor.RED + "쿨타임 : " + ChatColor.WHITE + "없음 / " + ChatColor.RED + "지속시간 : " + ChatColor.WHITE + "없음";
        if (data.GetAbilityType() == AbilityBase.Type.Passive_Manual)
            return ChatColor.RED + "쿨타임 : " + ChatColor.WHITE + "없음 / " + ChatColor.RED + "지속시간 : " + ChatColor.WHITE + "없음";
        return "None";
    }
}
