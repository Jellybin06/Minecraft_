package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class Boom extends AbilityBase
{
    public Boom()
    {
        InitAbility("붐포인트", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "철괴 왼쪽클릭시 20초간 자신의 주변에 있는적을을 폭발시킵니다." });
        InitAbility(60, 0, true);
        RegisterLeftClickEvent();
    }

    public int A_Condition(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getPlayer())) && (ItemCheck(ACC.DefaultItem))) {
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


        public void run()
        {
            // Convert Collection<Player> to Player[]
            Player[] p1 = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            Player p = Bukkit.getPlayer(this.name);
            if (p != null) {
                for (int i = 0; i < p1.length; i++) { // Changed to p1.length
                    if ((p1[i] != p) && (p1[i].getGameMode() != GameMode.CREATIVE)) {
                        Location lo = p1[i].getLocation();
                        if ((p.getLocation().distance(p1[i].getLocation()) <= 10.0D) && (lo.getY() != 0.0D)) {
                            Location loc2 = p1[i].getLocation();
                            p1[i].getWorld().createExplosion(loc2, 0.3F);
                        }
                    }
                }
            }
            if (this.num > 16) cancel();
            this.num += 1;
        }

    }
}
