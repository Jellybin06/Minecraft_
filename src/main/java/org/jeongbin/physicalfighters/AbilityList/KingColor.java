package org.jeongbin.physicalfighters.AbilityList;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
import org.bukkit.event.player.PlayerInteractEvent;


public class KingColor extends AbilityBase {

    public KingColor()
    {
        InitAbility("패왕색", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "철괴 왼쪽클릭시 20초간 주변 적에게 디버프를 부여하며 자신의 공격력을 증가시킵니다."});
        InitAbility(60, 0, true);
        RegisterLeftClickEvent();
    }

    public int A_Condition(Event event, int CustomData) {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        if ((!EventManager.DamageGuard) && (PlayerCheck(Event.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem)))
        {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Timer timer = new Timer();
        timer.schedule(new Pauck(Event.getPlayer().getName()), 500L, 1500L);
    }

    class Pauck extends TimerTask
    {
        private int num = 0;
        private String name = null;

        public Pauck(String name1) { this.name = name1; }


        public void run() {
            Player p = Bukkit.getPlayer(this.name);
            if (p != null && p.isOnline()) { // 플레이어가 온라인 상태인지 확인
                Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers(); // 명시적 캐스팅
                for (Player target : players) {
                    if (!target.equals(p) && target.getGameMode() != GameMode.CREATIVE) { // 크리에이티브 모드가 아닌 플레이어에게만 화염 효과 부여
                        Location loc = target.getLocation();
                        if (p.getLocation().distance(loc) <= 10.0) { // 거리가 10 이내인 경우
                            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5, 2));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5, 2));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 5, 2));
                        }
                    }

                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 20, 1));
            }
            // 반복 횟수가 15회 이상이면 타이머 종료
            if (this.num >= 15) {
                cancel();
            }
            this.num += 1;
        }


    }



}
