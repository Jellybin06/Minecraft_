package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class Akainu
        extends AbilityBase
{
    public static Block[][][] B = new Block[5][5][3];
    public static Material[][][] M = new Material[5][5][3];

    public Akainu() { if ((!PhysicalFighters.Toner) &&
            (PhysicalFighters.SRankUsed)) {
        InitAbility("아카이누", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.SS, new String[] {
                "바라보는 곳의 땅을 3초동안 용암으로 바꿔버립니다.", "3초 뒤에 용암이 굳으며 땅속에 갇힙니다.", "용암속에서 데미지를 받지 않습니다." });
        InitAbility(30, 0, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamage.add(new EventData(this, 3));
    }
    }

    public int A_Condition(Event event, int CustomData)
    {
        if (CustomData == 0) {
            PlayerInteractEvent Event = (PlayerInteractEvent)event;
            if ((!EventManager.DamageGuard) &&
                    (PlayerCheck(Event.getPlayer())) &&
                    (ItemCheck(ACC.DefaultItem)) &&
                    (Event.getPlayer().getTargetBlock(null, 0).getType() != Material.BEDROCK)) {
                return 0;
            }
        }
        if (CustomData == 3) {
            EntityDamageEvent Event2 = (EntityDamageEvent)event;
            if ((PlayerCheck(Event2.getEntity())) && (
                    (Event2.getCause() == EntityDamageEvent.DamageCause.LAVA) ||
                            (Event2.getCause() == EntityDamageEvent.DamageCause.FIRE) ||
                            (Event2.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))) {
                Event2.setCancelled(true);
            }
        }

        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Player p = Event.getPlayer();
        World w = Event.getPlayer().getWorld();
        Location loc = p.getTargetBlock(null, 0).getLocation();
        Timer timer = new Timer();
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                for (int k = -2; k < 1; k++) {
                    B[(i + 2)][(j + 2)][(k + 2)] = w.getBlockAt((int)loc.getX() + i, (int)loc.getY() + k, (int)loc.getZ() + j);
                    M[(i + 2)][(j + 2)][(k + 2)] = w.getBlockAt((int)loc.getX() + i, (int)loc.getY() + k, (int)loc.getZ() + j).getType();
                    w.getBlockAt((int)loc.getX() + i, (int)loc.getY() + k, (int)loc.getZ() + j).setType(Material.GRASS_BLOCK);
                }
            }
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 1; k++) {
                    w.getBlockAt((int)loc.getX() + i, (int)loc.getY() + k, (int)loc.getZ() + j).setType(Material.LAVA);
                }
            }
        }
        timer.schedule(new ExplosionTimer(w), 3000L);
    }

    class ExplosionTimer extends TimerTask {
        private World w;

        public ExplosionTimer(World w1) { this.w = w1; }


        public void run()
        {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    for (int k = -2; k < 1; k++) {
                        Location loc = Akainu.B[(i + 2)][(j + 2)][(k + 2)].getLocation();
                        this.w.getBlockAt(loc).setType(Akainu.M[(i + 2)][(j + 2)][(k + 2)]);
                    }
                }
            }
        }
    }
}
