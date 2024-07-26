package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Blind extends AbilityBase
{
    public Blind()
    {
        if (!PhysicalFighters.Specialability) {
            InitAbility("블라인드", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.C, new String[] {
                    "자신에게 공격받은 사람은 3초간 시야를 잃습니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getDamager())) &&
                ((Event.getEntity() instanceof Player))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        Player p = (Player)Event.getEntity();
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0),
                true);
    }
}
