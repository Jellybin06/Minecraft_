package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.PhysicalFighters;
import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Angel extends AbilityBase
{
    public static String pp = "false";
    public static boolean ppon = false;

    public Angel() {
        if (PhysicalFighters.Gods) {
            InitAbility("천사", org.jeongbin.physicalfighters.MainModule.AbilityBase.Type.Active_Immediately, AbilityBase.Rank.GOD, new String[] {
                    "철괴로 타격받은 대상에게 10초간 자신이 받는 데미지의 반을 흡수시킵니다.",
                    "독, 질식, 낙하 데미지를 받지 않습니다." });
            InitAbility(80, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this));
            EventManager.onEntityDamage.add(new EventData(this, 3));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        if (CustomData == 0) {
            EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
            if ((PlayerCheck(Event.getDamager())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem)) &&
                    (!EventManager.DamageGuard) &&
                    (pp == "false") && (!ppon)) {
                return 0;
            }

            if ((PlayerCheck(Event.getEntity())) &&
                    (!EventManager.DamageGuard) &&
                    (pp != "false") && (ppon)) {
                org.bukkit.Bukkit.getPlayer(pp).damage((int) (Event.getDamage() / 2.0D), Event.getEntity());
                Event.setDamage((int) (Event.getDamage() / 2.0D));
            }

        }
        else if (CustomData == 3) {
            EntityDamageEvent Event2 = (EntityDamageEvent)event;
            if (PlayerCheck(Event2.getEntity())) {
                if ((Event2.getCause() == EntityDamageEvent.DamageCause.POISON) ||
                        (Event2.getCause() == EntityDamageEvent.DamageCause.DROWNING)) {
                    Event2.setCancelled(true);
                }
                else if (Event2.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    GetPlayer().sendMessage(
                            ChatColor.GREEN + "사뿐하게 떨어져 데미지를 받지 않았습니다.");
                    Event2.setCancelled(true);
                }
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        pp = ((Player)Event.getEntity()).getName();
        GetPlayer().sendMessage(ChatColor.GREEN + ((Player)Event.getEntity()).getName() + "님은 이제 10초간 당신의 데미지의 반을 흡수합니다.");
        ((Player)Event.getEntity()).sendMessage(ChatColor.RED + "당신은 10초간 " + GetPlayer().getName() + "님이 받는 데미지의 반을 흡수합니다.");
        ppon = true;
        Timer timer = new Timer();
        timer.schedule(new offTimer(), 10000L);
    }

    class offTimer extends TimerTask {
        offTimer() {}

        public void run() {
            Angel.ppon = false;
            Angel.pp = "false";
            Angel.this.GetPlayer().sendMessage(ChatColor.DARK_PURPLE + "지속시간이 끝났습니다.");
        }
    }
}
