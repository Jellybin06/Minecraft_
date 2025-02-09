package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kaiji extends AbilityBase
{
    public Kaiji()
    {
        if (!PhysicalFighters.Toner) {
            InitAbility("카이지", AbilityBase.Type.Passive_Manual, AbilityBase.Rank.S, new String[] {
                    "다이아몬드로 상대를 타격할시에 30%확률로 상대를 즉사시키고,", "70%확률로 사망합니다." });
            InitAbility(20, 0, true);
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
                EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
                if ((!EventManager.DamageGuard) &&
                        (PlayerCheck(Event.getDamager())) &&
                        (ItemCheck(Material.DIAMOND)))
                    return 0;
                break;
            case 1:
                PlayerDropItemEvent Event1 = (PlayerDropItemEvent)event;
                if ((PlayerCheck(Event1.getPlayer())) &&
                        (Event1.getItemDrop().getItemStack().getType() == Material.DIAMOND)) {
                    PlayerInventory inv = Event1.getPlayer().getInventory();
                    if (!inv.contains(Material.DIAMOND, 1)) {
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
                EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
                Player p = (Player)Event.getEntity();
                if (Math.random() <= 0.3D) {
                    p.damage(5000);
                    Bukkit.broadcastMessage(String.format(ChatColor.RED +
                            "%s님이  카'의지'에 능력에 의지가 꺾였습니다.", new Object[] {
                            p.getName() }));
                    if (PhysicalFighters.AutoKick) {
                        p.kickPlayer("카이지에 의해 사망했습니다.");
                        if (PhysicalFighters.AutoBan)
                            p.kickPlayer("카이지에 의해 사망했습니다.");
                    }
                } else {
                    GetPlayer().damage(5000);
                    Bukkit.broadcastMessage(String.format(ChatColor.RED +
                            "%s님이  도박하다가 손목이 날라갔습니다.", new Object[] {
                            GetPlayer().getName() }));
                    if (PhysicalFighters.AutoKick) {
                        GetPlayer().kickPlayer("카이지에 의해 사망했습니다.");
                        if (PhysicalFighters.AutoBan)
                            GetPlayer().kickPlayer("카이지에 의해 사망했습니다.");
                    }
                }
                break;
            case 1:
                PlayerDropItemEvent Event1 = (PlayerDropItemEvent)event;
                Event1.getPlayer().sendMessage(ChatColor.RED + "다이아는 버릴 수 없습니다.");
                Event1.setCancelled(true);
                break;
            case 2:
                PlayerRespawnEvent Event2 = (PlayerRespawnEvent)event;
                PlayerInventory inv = Event2.getPlayer().getInventory();
                inv.setItem(8, new ItemStack(Material.DIAMOND, 1));
                break;
            case 3:
                EntityDeathEvent Event3 = (EntityDeathEvent)event;
                List<ItemStack> itemlist = Event3.getDrops();
                for (int l = 0; l < itemlist.size(); l++) {
                    if (((ItemStack)itemlist.get(l)).getType() == Material.DIAMOND)
                        itemlist.remove(l);
                }
        }
    }

    public void A_SetEvent(Player p) {
        p.getInventory().setItem(8, new ItemStack(Material.DIAMOND, 1));
    }

    public void A_ResetEvent(Player p)
    {
        p.getInventory().setItem(8, new ItemStack(Material.DIAMOND, 1));
    }
}
