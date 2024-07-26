package Physical.Fighters.AbilityList;

import Physical.Fighters.MainModule.EventManager;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Zombie extends Physical.Fighters.MainModule.AbilityBase
{
    public Zombie()
    {
        InitAbility("좀비", Physical.Fighters.MainModule.AbilityBase.Type.Passive_AutoMatic,
                Physical.Fighters.MainModule.AbilityBase.Rank.B, new String[] { "모든 데미지의 반을 흡수합니다.",
                        "불공격의 데미지를 8배로 받습니다." });
        InitAbility(0, 0, true);
        EventManager.onEntityDamage.add(new Physical.Fighters.MinerModule.EventData(this));
        EventManager.onEntityDamageByEntity.add(new Physical.Fighters.MinerModule.EventData(this, 1));
    }

    public int A_Condition(Event event, int CustomData) {
        if (CustomData == 0) {
            EntityDamageEvent Event = (EntityDamageEvent)event;
            if (PlayerCheck(Event.getEntity())) {
                if ((Event.getCause() == EntityDamageEvent.DamageCause.LAVA) ||
                        (Event.getCause() == EntityDamageEvent.DamageCause.FIRE) ||
                        (Event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))
                    return 0;
                if ((Event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) ||
                        (Event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
                    return 1;
                if ((Event.getCause() == EntityDamageEvent.DamageCause.FALL) ||
                        (Event.getCause() == EntityDamageEvent.DamageCause.POISON) ||
                        (Event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE))
                    return 2;
            }
        } else if (CustomData == 1) {
            EntityDamageByEntityEvent Event1 = (EntityDamageByEntityEvent)event;
            if ((PlayerCheck(Event1.getEntity())) &&
                    (((HumanEntity)Event1.getDamager()).getItemInHand()
                            .getType().getId() != Physical.Fighters.MinerModule.ACC.DefaultItem)) {
                if (((HumanEntity)Event1.getDamager()).getItemInHand()
                        .getType().getId() != Material.GOLD_INGOT.getId()) {
                    return 2;
                }
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        switch (CustomData) {
            case 0:
                EntityDamageEvent Event = (EntityDamageEvent)event;
                Event.setDamage((int)Event.getDamage() * 8);
                break;
            case 1:
                EntityDamageEvent Event2 = (EntityDamageEvent)event;
                Event2.setDamage((int)Event2.getDamage() * 4);
                break;
            case 2:
                EntityDamageByEntityEvent Event3 = (EntityDamageByEntityEvent)event;
                Event3.setDamage((int)Event3.getDamage() / 2);
        }
    }
}


