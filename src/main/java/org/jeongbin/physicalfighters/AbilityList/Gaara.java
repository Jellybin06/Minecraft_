package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.OtherModule.Vector;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;


public class Gaara
        extends AbilityBase
{
    public Gaara()
    {
        if (!PhysicalFighters.Toner) {
            InitAbility("가아라", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.B, new String[] {
                    "능력을 사용하면 모래를 떨어뜨리고 폭발시킨다." });
            InitAbility(30, 0, true);
            RegisterLeftClickEvent();
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Player p = Event.getPlayer();
        Block b = p.getTargetBlock(null, 0);
        Location loc = b.getLocation();
        Location ploc = p.getLocation();

        if ((PlayerCheck(Event.getPlayer())) && (ItemCheck(ACC.DefaultItem))) {
            if ((loc.distance(ploc) <= 40.0D) && (b.getY() != 0)) {
                if (!EventManager.DamageGuard)
                    return 0;
            } else
                p.sendMessage(String.format(ChatColor.RED + "거리가 너무 멉니다.", new Object[0]));
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Player p = Event.getPlayer();
        Location l1 = p.getTargetBlock(null, 0).getLocation();
        Location l2 = p.getTargetBlock(null, 0).getLocation();
        Timer timer = new Timer();
        Block block = Event.getPlayer().getTargetBlock(null, 0);
        timer.schedule(new ExplosionTimer(block), 4000L);
        for (int j = 4; j <= 8; j++) {
            l2.setY(l1.getY() + j);
            for (int i = 0; i <= 3; i++)
            {
                l2.setX(l1.getX() + i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.SAND);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() + k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.SAND);
                }
            }
            for (int i = 0; i <= 3; i++)
            {
                l2.setX(l1.getX() - i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.SAND);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() - k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.SAND);
                }
            }
            for (int i = 0; i <= 3; i++)
            {
                l2.setX(l1.getX() - i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.SAND);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() + k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.SAND);
                }
            }
            for (int i = 0; i <= 3; i++)
            {
                l2.setX(l1.getX() + i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.SAND);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() - k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.SAND);
                }
            }
        }
    }

    class ExplosionTimer extends TimerTask {
        World world;
        Location location;

        ExplosionTimer(Block block) { this.world = block.getWorld();
            this.location = block.getLocation();
        }

        public void run()
        {
            this.world.createExplosion(this.location, 5.0F);
            this.world.createExplosion(this.location, 5.0F);
            this.world.createExplosion(this.location, 5.0F);
        }
    }
}
