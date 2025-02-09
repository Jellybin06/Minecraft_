package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.OtherModule.Vector;
import org.jeongbin.physicalfighters.PhysicalFighters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;



public class Guard
        extends AbilityBase
{
    public Guard()
    {
        if (!PhysicalFighters.Toner) {
            InitAbility("목둔", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.A, new String[] {
                    "능력을 사용하면 나무 벽을 설치해 상대를 가둘 수 있다." });
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

        Vector playervec = new Vector(ploc.getX(), ploc.getY(), ploc.getZ());
        Vector targetvec = new Vector(loc.getX(), loc.getY(), loc.getZ());
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getPlayer())) &&
                (ItemCheck(ACC.DefaultItem)) &&
                (Event.getPlayer().getTargetBlock(null, 0).getType() != Material.BEDROCK)) {
            if ((playervec.distance(targetvec) <= 40.0D) && (b.getY() != 0)) {
                return 0;
            }
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
        for (int j = 0; j <= 8; j++) {
            l2.setY(l1.getY() + j);
            for (int i = 0; i <= 5; i++) {
                l2.setX(l1.getX() + i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                for (int k = 0; k <= 5; k++) {
                    l2.setZ(l1.getZ() + k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                }
            }
            for (int i = 0; i <= 5; i++) {
                l2.setX(l1.getX() - i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                for (int k = 0; k <= 5; k++) {
                    l2.setZ(l1.getZ() - k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                }
            }
            for (int i = 0; i <= 5; i++) {
                l2.setX(l1.getX() - i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                for (int k = 0; k <= 5; k++) {
                    l2.setZ(l1.getZ() + k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                }
            }
            for (int i = 0; i <= 5; i++) {
                l2.setX(l1.getX() + i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                for (int k = 0; k <= 5; k++) {
                    l2.setZ(l1.getZ() - k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.OAK_WOOD);
                }
            }
        }
        for (int j = 1; j <= 6; j++) {
            l2.setY(l1.getY() + j);
            for (int i = 0; i <= 3; i++) {
                l2.setX(l1.getX() + i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.AIR);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() + k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.AIR);
                }
            }
            for (int i = 0; i <= 3; i++) {
                l2.setX(l1.getX() - i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.AIR);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() - k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.AIR);
                }
            }
            for (int i = 0; i <= 3; i++) {
                l2.setX(l1.getX() - i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.AIR);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() + k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                        p.getWorld().getBlockAt(l2).setType(Material.AIR);
                }
            }
            for (int i = 0; i <= 3; i++) {
                l2.setX(l1.getX() + i);
                if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK)
                    p.getWorld().getBlockAt(l2).setType(Material.AIR);
                for (int k = 0; k <= 3; k++) {
                    l2.setZ(l1.getZ() - k);
                    if (p.getWorld().getBlockAt(l2).getType() != Material.BEDROCK) {
                        p.getWorld().getBlockAt(l2).setType(Material.AIR);
                    }
                }
            }
        }
    }
}
