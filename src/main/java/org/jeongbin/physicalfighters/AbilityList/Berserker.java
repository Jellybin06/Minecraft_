package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Berserker extends AbilityBase
{
    public Berserker()
    {
        InitAbility("광전사", AbilityBase.Type.Passive_Manual, AbilityBase.Rank.A, new String[] {
                "체력이 70% 이하로 떨어지면 데미지가 1.5배로 증폭되며",
                "체력이 40% 이하로 떨어지면 데미지가 2배로 증폭됩니다.",
                "체력이 20% 이하로 떨어지면 데미지가 3배로 증폭됩니다.",
                "체력이 5% 이하로 떨어지면 데미지가 5배로 증폭됩니다." });
        InitAbility(0, 0, true);
        EventManager.onEntityDamageByEntity.add(new EventData(this));
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        if (PlayerCheck(Event.getDamager())) {
            Player p = (Player)Event.getDamager();
            if ((((Damageable)p).getHealth() <= 14.0D) && (((Damageable)p).getHealth() > 8.0D))
                return 0;
            if (((Damageable)p).getHealth() <= 8.0D)
                return 1;
            if (((Damageable)p).getHealth() <= 4.0D)
                return 2;
            if (((Damageable)p).getHealth() <= 1.0D)
                return 3;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        switch (CustomData) {
            case 0:
                Event.setDamage((int)(Event.getDamage() * 1.5D));
                break;
            case 1:
                Event.setDamage((int) (Event.getDamage() * 2.0D));
                break;
            case 2:
                Event.setDamage((int) (Event.getDamage() * 3.0D));
                break;
            case 3:
                Event.setDamage((int) (Event.getDamage() * 5.0D));
        }
    }
}
