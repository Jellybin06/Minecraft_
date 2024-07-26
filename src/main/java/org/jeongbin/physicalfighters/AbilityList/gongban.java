package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class gongban extends AbilityBase
{
    public static boolean ppon = false;

    public gongban() {
        InitAbility("공격반사", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "철괴 좌클릭으로 능력을 사용합니다.",
                "능력 사용 후 5초간 받는 모든 데미지를 돌려줍니다." });
        InitAbility(60, 0, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamageByEntity.add(new EventData(this, 1));
    }

    public int A_Condition(Event event, int CustomData)
    {
        if (CustomData == 0) {
            PlayerInteractEvent e = (PlayerInteractEvent)event;
            if ((PlayerCheck(e.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem)) && !EventManager.DamageGuard) {
                return 0;
            }
        }

        if (CustomData == 1) {
            EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
            if ((PlayerCheck(Event.getEntity())) && (ppon) &&
                    ((Event.getDamager() instanceof Player)) && !EventManager.DamageGuard) {
                Player p = (Player)Event.getEntity();
                Player t = (Player)Event.getDamager();
                t.damage(Event.getDamage(), p);
                Event.setCancelled(true);
            }
        }

        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        PlayerInteractEvent e = (PlayerInteractEvent)event;
        Bukkit.broadcastMessage(ChatColor.GRAY + "지금부터 5초간 " + e.getPlayer().getName() + "님을 공격시 가한 데미지를 돌려받습니다.");
        ppon = true;
        Timer timer = new Timer();
        timer.schedule(new offTimer(), 5000L); }

    class offTimer extends TimerTask { offTimer() {}

        public void run() { gongban.ppon = false;
            gongban.this.GetPlayer().sendMessage(ChatColor.DARK_PURPLE + "지속시간이 끝났습니다.");
        }
    }
}

