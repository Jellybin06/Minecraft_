package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FallArrow extends AbilityBase
{
    public FallArrow()
    {
        if (PhysicalFighters.SRankUsed) {
            InitAbility("중력화살", AbilityBase.Type.Passive_Manual, AbilityBase.Rank.S, new String[] {
                    "화살에 맞은 플레이어는 공중으로 뜹니다. [추가타 가능]" });
            InitAbility(0, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this, 0));
            EventManager.onPlayerDropItem.add(new EventData(this, 1));
            EventManager.onPlayerRespawn.add(new EventData(this, 2));
            EventManager.onEntityDeath.add(new EventData(this, 3));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDamageByEntityEvent Event0 = (EntityDamageByEntityEvent)event;
                if ((Event0.getDamager() instanceof Arrow)) {
                    Arrow a = (Arrow)Event0.getDamager();
                    if (PlayerCheck((Player)a.getShooter())) {
                        if (((Event0.getEntity() instanceof Player)) &&
                                ((Player)a.getShooter() ==
                                        (Player)Event0
                                                .getEntity()))
                            if (!EventManager.DamageGuard)
                                return 9999;
                        return 0;
                    }
                }
                break;
            case 1:
                PlayerDropItemEvent Event1 = (PlayerDropItemEvent)event;
                if ((PlayerCheck(Event1.getPlayer())) &&
                        (Event1.getItemDrop().getItemStack().getType() == Material.ARROW)) {
                    PlayerInventory inv = Event1.getPlayer().getInventory();
                    if (!inv.contains(Material.ARROW, 64)) {
                        return 1;
                    }
                }
                break;
            case 2:
                PlayerRespawnEvent Event2 = (PlayerRespawnEvent)event;
                if (PlayerCheck(Event2.getPlayer()))
                    return 2;
                break;
            case 3:
                EntityDeathEvent Event3 = (EntityDeathEvent)event;
                if (PlayerCheck(Event3.getEntity()))
                    return 3;
                break;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDamageByEntityEvent Event0 = (EntityDamageByEntityEvent)event;
                Player p = (Player)Event0.getEntity();
                Location l1 = Event0.getEntity().getLocation();
                Location l2 = Event0.getEntity().getLocation();
                l2.setY(l1.getY() + 4.0D);
                goVelocity(p, l2, 1);
                Event0.getEntity().getWorld()
                        .createExplosion(Event0.getEntity().getLocation(), 0.0F);
                p.teleport(l2);
                break;
            case 1:
                PlayerDropItemEvent Event1 = (PlayerDropItemEvent)event;
                Event1.getPlayer().sendMessage(
                        ChatColor.RED + "소유한 화살이 64개 이하일시 화살을 버릴수 없습니다.");
                Event1.setCancelled(true);
                break;
            case 2:
                PlayerRespawnEvent Event2 = (PlayerRespawnEvent)event;
                Event2.getPlayer().sendMessage(
                        ChatColor.GREEN + "이전에 소유했던 화살은 모두 소멸하며 다시 지급됩니다.");
                PlayerInventory inv = Event2.getPlayer().getInventory();
                inv.remove(new ItemStack(Material.ARROW, 64));
                inv.setItem(8, new ItemStack(Material.ARROW, 64));
                inv.setItem(7, new ItemStack(Material.BOW, 1));
                break;
            case 3:
                EntityDeathEvent Event3 = (EntityDeathEvent)event;
                List<ItemStack> itemlist = Event3.getDrops();
                for (int l = 0; l < itemlist.size(); l++) {
                    if (((ItemStack)itemlist.get(l)).getType() == Material.ARROW)
                        itemlist.remove(l);
                }
        }
    }

    public void A_SetEvent(Player p) {
        p.getInventory().setItem(8, new ItemStack(Material.ARROW, 64));
        p.getInventory().setItem(7, new ItemStack(Material.BOW, 1));
    }

    public void A_ResetEvent(Player p)
    {
        p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.ARROW, 64) });
    }
}
