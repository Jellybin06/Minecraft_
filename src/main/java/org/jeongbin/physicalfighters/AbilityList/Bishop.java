package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bishop extends AbilityBase
{
    private final int DurationTime = 300;

    public Bishop() {
        if (!PhysicalFighters.Specialability) {
            InitAbility("비숍", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.B, new String[] {
                    "철괴 왼클릭시 맞은 사람에게 각종 축복을 겁니다.", "철괴 오른클릭시 자신에게 각종 축복을 겁니다.",
                    "금괴를 적에게 왼클릭시 각종 저주를 겁니다.", "세 기능은 쿨타임을 공유하며 모든 효과 지속시간은",
                    "15초입니다." });
            InitAbility(30, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this));
            RegisterRightClickEvent();
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDamageByEntityEvent Event1 = (EntityDamageByEntityEvent)event;
                if (((Event1.getEntity() instanceof Player)) &&
                        (PlayerCheck(Event1.getDamager())) && !EventManager.DamageGuard) {
                    if (ItemCheck(ACC.DefaultItem))
                        return 0;
                    if (ItemCheck(Material.GOLD_INGOT)) {
                        return 2;
                    }
                }
                break;
            case 1:
                PlayerInteractEvent Event2 = (PlayerInteractEvent)event;
                if ((PlayerCheck(Event2.getPlayer())) &&
                        (ItemCheck(ACC.DefaultItem)) && !EventManager.DamageGuard) {
                    return 1;
                }
                break;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDamageByEntityEvent Event0 = (EntityDamageByEntityEvent)event;
                Player p0 = (Player)Event0.getEntity();
                p0.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                        300, 0), true);
                p0.addPotionEffect(new PotionEffect(
                        PotionEffectType.RESISTANCE, 300, 0), true);
                p0.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST,
                        300, 0), true);
                p0.addPotionEffect(new PotionEffect(
                        PotionEffectType.WATER_BREATHING, 300, 0), true);
                p0.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,
                        300, 0), true);
                p0.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                        300, 0), true);
                p0.sendMessage(ChatColor.GREEN + "비숍이 당신에게 축복을 걸었습니다. 15초 지속.");
                Event0.setCancelled(true);
                break;
            case 1:
                PlayerInteractEvent Event1 = (PlayerInteractEvent)event;
                Player p1 = Event1.getPlayer();
                p1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                        300, 0), true);
                p1.addPotionEffect(new PotionEffect(
                        PotionEffectType.RESISTANCE, 300, 0), true);
                p1.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST,
                        300, 0), true);
                p1.addPotionEffect(new PotionEffect(
                        PotionEffectType.WATER_BREATHING, 300, 0), true);
                p1.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,
                        300, 0), true);
                p1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                        300, 0), true);
                p1.sendMessage(ChatColor.GREEN + "자신에게 축복을 걸었습니다. 15초 지속.");
                break;
            case 2:
                EntityDamageByEntityEvent Event2 = (EntityDamageByEntityEvent)event;
                Player p2 = (Player)Event2.getEntity();
                p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,
                        300, 0), true);
                p2.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,
                        300, 0), true);
                p2.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                        300, 0), true);
                p2.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,
                        300, 0), true);
                p2.sendMessage(ChatColor.RED + "비숍이 당신에게 저주를 걸었습니다. 15초 지속.");
        }
    }
}
