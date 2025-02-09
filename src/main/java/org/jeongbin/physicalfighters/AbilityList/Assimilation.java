package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.CommandManager;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MajorModule.AbilityList;
import org.jeongbin.physicalfighters.MinerModule.CommandInterface;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Assimilation
        extends AbilityBase implements CommandInterface
{
    private boolean ActiveAss = false;

    public Assimilation() {
        if (PhysicalFighters.SRankUsed) {
            InitAbility("흡수", AbilityBase.Type.Passive_Manual, AbilityBase.Rank.S, new String[] {
                    "자신이 죽인 사람의 능력을 흡수합니다. 액티브 능력은",
                    "1개만 가능합니다. 미러링도 흡수가 가능하며 데스 노트의 경우",
                    "이미 능력을 썼더라도 다시 쓸수 있습니다. 자신에게 타격받은",
                    "사람은 배고픔이 빠르게 감소합니다. \"/va a\" 명령으로",
                    "자신이 흡수한 능력을 볼수 있습니다.", "흡수가 가능한 능력의 갯수에는 제한이 없습니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this, 0));
            EventManager.onEntityDeath.add(new EventData(this, 1));
            cm.RegisterCommand(this);
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDamageByEntityEvent Event0 = (EntityDamageByEntityEvent)event;
                if (PlayerCheck(Event0.getDamager()))
                    return 0;
                break;
            case 1:
                EntityDeathEvent Event1 = (EntityDeathEvent)event;
                if (((Event1.getEntity() instanceof Player)) &&
                        (PlayerCheck(Event1.getEntity().getKiller())))
                    return 1;
                break;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDamageByEntityEvent Event0 = (EntityDamageByEntityEvent)event;
                if ((Event0.getEntity() instanceof Player)) {
                    Player p = (Player)Event0.getEntity();
                    p.setSaturation(0.0F);
                }
                break;
            case 1:
                EntityDeathEvent Event1 = (EntityDeathEvent)event;
                if ((Event1.getEntity() instanceof Player)) {
                    Player p = (Player)Event1.getEntity();
                    AbilityBase a = AbilityBase.FindAbility(p);
                    if (a != null) {
                        a.AbilityCTimerCancel();
                        a.AbilityDTimerCancel();
                        if ((a.GetAbilityType() == AbilityBase.Type.Passive_AutoMatic) ||
                                (a.GetAbilityType() == AbilityBase.Type.Passive_Manual)) {
                            a.SetPlayer(GetPlayer(), false);
                            GetPlayer().sendMessage(
                                    ChatColor.GREEN + "새로운 패시브 능력을 흡수하였습니다.");
                            GetPlayer().sendMessage(
                                    ChatColor.YELLOW + "새로운 능력 : " +
                                            ChatColor.WHITE + a.GetAbilityName());
                        } else if (!this.ActiveAss) {
                            a.SetPlayer(GetPlayer(), false);
                            GetPlayer().sendMessage(
                                    ChatColor.GREEN + "새로운 액티브 능력을 흡수하였습니다.");
                            GetPlayer().sendMessage(
                                    ChatColor.YELLOW + "새로운 능력 : " +
                                            ChatColor.WHITE + a.GetAbilityName());
                            GetPlayer().sendMessage(
                                    ChatColor.RED + "이제 액티브 흡수는 불가능합니다.");
                            this.ActiveAss = true;
                        } else {
                            GetPlayer().sendMessage(
                                    ChatColor.RED + "흡수할수 없는 능력을 가지고 있었습니다.");
                        }
                    }
                }
                break;
        }
    }

    public void A_SetEvent(Player p)
    {
        this.ActiveAss = false;
    }

    public boolean onCommandEvent(CommandSender sender, Command command, String label, String[] data)
    {
        if (((sender instanceof Player)) && (PlayerCheck((Player)sender)) &&
                (data[0].equalsIgnoreCase("a")) && (data.length == 1)) {
            sender.sendMessage(ChatColor.GREEN + "-- 당신이 소유한 능력 --");
            for (AbilityBase a : AbilityList.AbilityList) {
                if (a.PlayerCheck(GetPlayer())) {
                    GetPlayer().sendMessage(a.GetAbilityName());
                }
            }
            return true;
        }

        return false;
    }
}
