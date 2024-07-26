package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fly
        extends AbilityBase
{
    public Fly()
    {
        InitAbility("플라이", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.GOD, new String[] {
                "철괴를 휘두를시에 10초간 하늘을 날라다닐 수 있습니다.",
                "낙하 데미지를 받지 않습니다." });
        InitAbility(60, 0, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamage.add(new EventData(this, 3));
    }

    public int A_Condition(Event event, int CustomData)
    {
        if (CustomData == 0) {
            PlayerInteractEvent Event = (PlayerInteractEvent)event;
            if ((PlayerCheck(Event.getPlayer())) && (ItemCheck(ACC.DefaultItem))) {
                return 0;
            }
        }
        else if (CustomData == 3) {
            EntityDamageEvent Event2 = (EntityDamageEvent)event;
            if ((PlayerCheck(Event2.getEntity())) &&
                    (Event2.getCause() == EntityDamageEvent.DamageCause.FALL)) {
                GetPlayer().sendMessage(
                        ChatColor.GREEN + "사뿐하게 떨어져 데미지를 받지 않았습니다.");
                Event2.setCancelled(true);
            }
        }

        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        GetPlayer().setAllowFlight(true);
        GetPlayer().setFlying(true);

        Timer timer = new Timer();
        timer.schedule(new offTimer(), 10000L);
    }

    class offTimer extends TimerTask {
        offTimer() {}

        public void run() { Fly.this.GetPlayer().setFlying(false);
            Fly.this.GetPlayer().setAllowFlight(false);
            Fly.this.GetPlayer().sendMessage(ChatColor.DARK_PURPLE + "지속시간이 끝났습니다.");
        }
    }
}

