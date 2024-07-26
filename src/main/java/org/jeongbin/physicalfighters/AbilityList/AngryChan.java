package org.jeongbin.physicalfighters.AbilityList;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Random;

public class AngryChan extends AbilityBase {

    public AngryChan()
    {
        InitAbility("김병찬", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.A, new String[] {
                "캐드 시험에 떨어져 절망하고 있습니다.",
                "철괴 왼쪽클릭시 세상에 대한 원망을 분출하여 10초 동안 공격력이 대폭 상승할 수도 있습니다?",
                "허약한 체격으로 인해 받는 데미지가 1.5배 늘어났습니다."});
        InitAbility(30, 0, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamage.add(new EventData(this));
    }

    public int A_Condition(Event event, int CustomData) {
        PlayerInteractEvent Event1 = (PlayerInteractEvent)event;
        if ((PlayerCheck(Event1.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Player p = Event.getPlayer();
        int randomNumber = new Random().nextInt(11); // 무작위 값 생성
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, randomNumber)); // 10초 동안 효과 부여
    }

    @org.bukkit.event.EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(PlayerCheck(p)){
                double originalDamage = event.getDamage();
                event.setDamage(originalDamage * 1.5);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 10000, 1));
            }
        }
    }
}
