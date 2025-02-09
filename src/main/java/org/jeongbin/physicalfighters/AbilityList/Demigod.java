package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Demigod extends AbilityBase
{
    public Demigod()
    {
        if (PhysicalFighters.SRankUsed)
        {
            InitAbility("데미갓", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.S, new String[] {
                    "반은 인간, 반은 신인 능력자입니다.",
                    "데미지를 받으면 일정 확률로 10초간 랜덤 버프가 발동됩니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDamage.add(new EventData(this));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageEvent Event = (EntityDamageEvent)event;
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getEntity()))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageEvent Event = (EntityDamageEvent)event;
        Player p1 = (Player)Event.getEntity();
        if (Math.random() <= 0.05D) {
            p1.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 0), true);
        } else if (Math.random() <= 0.05D) {
            p1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0), true);
        } else if (Math.random() <= 0.1D) {
            p1.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 200, 0), true);
        } else if (Math.random() <= 0.1D) {
            p1.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 200, 0), true);
        } else if (Math.random() <= 0.1D) {
            p1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0), true);
        } else if ((Math.random() <= 0.05D) &&
                (((Damageable)p1).getHealth() < 20.0D)) {
            p1.setHealth(((Damageable)p1).getHealth() + 1);
        }
    }
}


