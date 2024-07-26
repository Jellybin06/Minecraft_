package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Blaze extends AbilityBase
{
    public Blaze()
    {
        if (!PhysicalFighters.Specialability) {
            InitAbility("블레이즈", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.C, new String[] {
                    "용암과 불 데미지를 입지 않습니다.", "능력에서 파생되는 화염 데미지도 막습니다.",
                    "모든 종류의 폭발 데미지를 50%로 줄여 받습니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDamage.add(new EventData(this));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageEvent Event = (EntityDamageEvent)event;
        if (PlayerCheck(Event.getEntity())) {
            if ((Event.getCause() == EntityDamageEvent.DamageCause.LAVA) ||
                    (Event.getCause() == EntityDamageEvent.DamageCause.FIRE) ||
                    (Event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))
                return 0;
            if ((Event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) ||
                    (Event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                return 1;
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageEvent Event = (EntityDamageEvent)event;
        switch (CustomData) {
            case 0:
                Event.setCancelled(true);
                Event.getEntity().setFireTicks(0);
                break;
            case 1:
                Event.setDamage((int) (Event.getDamage() / 2.0D));
        }
    }
}

