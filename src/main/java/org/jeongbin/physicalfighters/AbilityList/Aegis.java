package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Aegis extends AbilityBase {
    public Aegis() {
        InitAbility("이지스", AbilityBase.Type.Active_Continue, AbilityBase.Rank.A, new String[]{
                "능력 사용시 일정시간동안 무적이 됩니다. 무적은 대부분의",
                "데미지를 무력화시키며 능력 사용중엔 Mirroring 능력도 ", "무력화됩니다."
        });
        InitAbility(28, 6, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamage.add(new EventData(this));
    }

    public int A_Condition(Event event, int CustomData) {
        if (!PhysicalFighters.ReverseMode) {
            if (event instanceof PlayerInteractEvent) {
                PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
                if (PlayerCheck(interactEvent.getPlayer()) && ItemCheck(ACC.DefaultItem) && !EventManager.DamageGuard)
                    return 0;
            }
        } else {
            if (event instanceof EntityDamageEvent) {
                EntityDamageEvent damageEvent = (EntityDamageEvent) event;
                if (PlayerCheck(damageEvent.getEntity()))
                    return 0;
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        if (!PhysicalFighters.ReverseMode) {
            if (event instanceof EntityDamageEvent) {
                EntityDamageEvent damageEvent = (EntityDamageEvent) event;
                if (PlayerCheck(damageEvent.getEntity())) {
                    Player p = (Player) damageEvent.getEntity();
                    p.setFireTicks(0);
                    damageEvent.setCancelled(true);
                }
            }
        } else {
            if (event instanceof EntityDamageEvent) {
                EntityDamageEvent damageEvent = (EntityDamageEvent) event;
                damageEvent.setDamage((int) (damageEvent.getDamage() * 1000.0D));
            }
        }
    }


}
