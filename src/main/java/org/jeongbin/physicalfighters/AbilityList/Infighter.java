package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;

public class Infighter
        extends AbilityBase
{
    public Infighter()
    {
        if (PhysicalFighters.SRankUsed)
        {
            InitAbility("인파이터", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.A, new String[] {
                    "주먹으로 모든것을 해결하는 능력입니다.",
                    "주먹으로 공격하면 대상에게 큰 충격을 받습니다.",
                    "10%확률로 폭발이 일어나며 대상이 넉백됩니다.",
                    "20%의 확률로 대상을 5초간 그로기상태로 만듭니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDamageByEntity.add(new EventData(this));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getDamager()))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        Player p = (Player) e.getDamager();
        Player t = (Player) e.getEntity();

        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            Random r = new Random();
            int dmg = 5 + r.nextInt(12);
            e.setDamage(dmg);

            if (Math.random() <= 0.1) {
                t.getWorld().createExplosion(t.getLocation(), 0.0F);
                Location l1 = p.getLocation();
                AbilityBase.goVelocity(t, l1, -3);
            }

            if (Math.random() <= 0.2) {
                t.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 0), true);
                t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0), true);
                t.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0), true);
                t.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 0), true);
            }
        }
    }
}
