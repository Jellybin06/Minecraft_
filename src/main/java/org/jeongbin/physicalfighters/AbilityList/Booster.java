package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Booster extends AbilityBase
{
    public Booster()
    {
        InitAbility("부스터", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.B, new String[] {
                "공격시에 딜레이가 매우 낮습니다. 단 당신의 데미지는 3~6로 랜덤입니다." });
        InitAbility(0, 0, true);
        EventManager.onEntityDamageByEntity.add(new EventData(this));
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        if ((PlayerCheck(Event.getDamager())) &&
                ((Event.getEntity() instanceof Player))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        Player p = (Player)Event.getEntity();
        p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 0), true);
        p.setNoDamageTicks(8);
        Random rand = new Random();
        int r = rand.nextInt(6);
        switch (r) {
            case 0:  Event.setDamage((int) 3.0D);
            case 1:
                Event.setDamage((int) 5.0D);
            case 2:
                Event.setDamage((int) 4.0D);
            case 5:
                Event.setDamage((int) 6.0D);
            case 3:
                Event.setDamage((int) 5.0D);
            case 4:
                Event.setDamage((int) 3.0D);
        }
    }
}

