package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class Ace extends AbilityBase
{
    public Ace()
    {
        InitAbility("에이스", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "철괴 왼쪽클릭시 20초간 자신의 주변에 있는 적들을 불태웁니다." });
        InitAbility(40, 0, true);
        RegisterLeftClickEvent();
    }

    public int A_Condition(Event event, int CustomData) {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Timer timer = new Timer();
        timer.schedule(new Pauck(Event.getPlayer().getName()), 500L, 1500L);
    }

    class Pauck extends TimerTask
    {
        private int num = 0;
        private String name = null;

        public Pauck(String name1) { this.name = name1; }


        public void run() {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            Player p = Bukkit.getPlayer(this.name);
            if (p != null) {
                for (Player player : onlinePlayers) {
                    if (player != p && player.getGameMode() != GameMode.CREATIVE) {
                        Location lo = player.getLocation();
                        if (p.getLocation().distance(lo) <= 10.0D && lo.getY() != 0.0D) {
                            player.setFireTicks(80);
                        }
                    }
                }
            }
            if (this.num > 16) cancel();
            this.num += 1;
        }
    }
}
