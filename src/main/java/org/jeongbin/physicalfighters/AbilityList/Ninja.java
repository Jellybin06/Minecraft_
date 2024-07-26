package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Ninja
        extends AbilityBase
{
    public Ninja()
    {
        InitAbility("닌자", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "철괴 왼쪽클릭시 매우 빠르게 화살을 발사합니다. ",
                "이 화살에 플레이어가 맞을 경우 10%확률로 플레이어를 폭발시키고, ",
                "30%의 확률로 플레이어에게 불을 붙히고,", "65%의 확률로 쿨타임이 초기화됩니다." });
        InitAbility(10, 0, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamageByEntity.add(new EventData(this, 1));
    }

    public int A_Condition(Event event, int CustomData) {
        switch (CustomData) {
            case 0:
                PlayerInteractEvent Event = (PlayerInteractEvent)event;
                if ((!EventManager.DamageGuard) && (PlayerCheck(Event.getPlayer())) &&
                        (ItemCheck(ACC.DefaultItem))) {
                    return 0;
                }

                break;
            case 1:
                EntityDamageByEntityEvent E = (EntityDamageByEntityEvent)event;
                if ((E.getDamager() instanceof Arrow)) {
                    Arrow a = (Arrow)E.getDamager();
                    if (PlayerCheck((Player)a.getShooter())) {
                        Player p = (Player)a.getShooter();
                        E.setDamage(E.getDamage() + 13);
                        if (Math.random() <= 0.65D) {
                            AbilityCTimerCancel();
                            p.sendMessage(ChatColor.YELLOW + "플레이어를 맞춰 쿨타임이 초기화되었습니다.");
                        }
                        if (Math.random() <= 0.1D) {
                            World w = E.getEntity().getWorld();
                            w.createExplosion(E.getEntity().getLocation(), 4.0F);
                        }
                    }
                }
                break;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
        Player player = playerInteractEvent.getPlayer();
        World world = player.getWorld();

        // 화살 생성 및 발사
        Arrow arrow = player.launchProjectile(Arrow.class);

        // 화살의 속도 조절
        arrow.setVelocity(arrow.getVelocity().multiply(8));

        // 일정 확률로 화살에 불 효과 부여
        if (Math.random() <= 0.3D) {
            arrow.setFireTicks(20);
        }
    }

}


