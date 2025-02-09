package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Assassin
        extends AbilityBase
{
    public Assassin()
    {
        InitAbility("어쌔신", AbilityBase.Type.Passive_AutoMatic, AbilityBase.Rank.C, new String[] {
                "뒤에서 공격할시에 데미지를 두배로 입히고 눈을 가립니다." });
        InitAbility(0, 0, true);
        EventManager.onEntityDamageByEntity.add(new EventData(this));
    }

    public int A_Condition(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        if ((PlayerCheck(Event.getDamager())) &&
                ((Event.getEntity() instanceof Player))) {
            Player p = (Player)Event.getEntity();
            Player p1 = (Player)Event.getDamager();
            if (AbilityBase.Direction(p) == AbilityBase.Direction(p1))
            {
                return 0;
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
        Player p = (Player)Event.getEntity();
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0), true);
        Event.setDamage((int) (Event.getDamage() * 2.0D));
        ((Player)Event.getDamager()).sendMessage(ChatColor.GREEN + "백스텝 성공!");
        p.sendMessage(ChatColor.RED + "백스텝을 당하셨습니다.");
    }
}

