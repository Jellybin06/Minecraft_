package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ExplosionPa extends AbilityBase
{
    LinkedList<String> gigong = new LinkedList();
    HashMap<String, Location> pLoc = new HashMap();

    public ExplosionPa() {
        InitAbility(
                "기공파",
                org.jeongbin.physicalfighters.MainModule.AbilityBase.Type.Active_Immediately,
                AbilityBase.Rank.S,
                new String[] { "바라보는 방향으로 강한폭발을 여러차례 일으킵니다. 시전시간은 약 5초정도 되며, 5초간 움직일 수 없습니다." });
        InitAbility(40, 0, true);
        RegisterLeftClickEvent();
        EventManager.onPlayerMoveEvent.add(new EventData(this, 1));
    }

    public int A_Condition(Event event, int CustomData) {
        switch (CustomData) {
            case 0:
                PlayerInteractEvent Event = (PlayerInteractEvent)event;
                if ((PlayerCheck(Event.getPlayer())) &&
                        (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem)) && !EventManager.DamageGuard)
                    return 0;
                break;
            case 1:
                PlayerMoveEvent Event2 = (PlayerMoveEvent)event;
                if ((PlayerCheck(Event2.getPlayer())) &&
                        (this.gigong.contains(Event2.getPlayer().getName()))) {
                    Event2.getPlayer().teleport((Location)this.pLoc.get(Event2.getPlayer().getName()));
                }
                break;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        this.gigong.add(GetPlayer().getName());
        this.pLoc.put(GetPlayer().getName(), GetPlayer().getLocation());
        Timer timer = new Timer();
        timer.schedule(new giTimer(), 500L, 500L);
    }

    public class giTimer extends java.util.TimerTask {
        private int ab = 10;
        private int i = 0;

        public giTimer() {}

        public void run() {
            Location l = ExplosionPa.this.GetPlayer().getLocation();
            Location l2 = ExplosionPa.this.GetPlayer().getLocation();
            double degrees = Math.toRadians(-(l.getYaw() % 360.0F));
            double ydeg = Math.toRadians(-(l.getPitch() % 360.0F));
            l2.setX(l.getX() + (2 * this.i + 2) * (
                    Math.sin(degrees) * Math.cos(ydeg)));
            l2.setY(l.getY() + (2 * this.i + 2) * Math.sin(ydeg));
            l2.setZ(l.getZ() + (2 * this.i + 2) * (
                    Math.cos(degrees) * Math.cos(ydeg)));
            l2.getWorld().createExplosion(l2, 0.0F);

            Player[] pl = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            for (Player pp : pl) {
                if (pp != ExplosionPa.this.GetPlayer()) {
                    Location loc = pp.getLocation();
                    if (l2.getWorld().getBlockAt(l2).getLocation().distance(loc) <= 4.0D) {
                        pp.damage(10, ExplosionPa.this.GetPlayer());
                    }
                }
            }

            this.ab -= 1;
            this.i += 1;
            if (this.ab <= 0) {
                ExplosionPa.this.gigong.remove(ExplosionPa.this.GetPlayer().getName());
                ExplosionPa.this.pLoc.remove(ExplosionPa.this.GetPlayer().getName());
                cancel();
            }
        }

    }
}


