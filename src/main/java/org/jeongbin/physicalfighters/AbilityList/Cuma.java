package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Cuma extends AbilityBase
{
    public Cuma()
    {
        if (PhysicalFighters.SRankUsed)
        {
            InitAbility("바솔로뮤 쿠마", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.S, new String[] {
                    "일정 확률르 받은 공격을 상대에게 되돌려주며, 공격받을시 상대를 뒤로 넉백시킵니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        if (PlayerCheck(Event.getEntity()) && !EventManager.DamageGuard) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        Player p = (Player)Event.getEntity();
        Player pn = (Player)Event.getDamager();
        if (Math.random() <= 0.15D) {
            pn.damage(Event.getDamage());
            Event.setCancelled(true);
        }
        pn.getWorld().createExplosion(pn.getLocation(), 0.0F);
        pn.setVelocity(pn.getVelocity().add(
                p.getLocation().toVector()
                        .subtract(pn.getLocation().toVector()).normalize()
                        .multiply(-1)));
    }
}

