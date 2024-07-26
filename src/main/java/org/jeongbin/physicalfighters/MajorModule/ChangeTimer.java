package org.jeongbin.physicalfighters.MajorModule;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MinerModule.AUC;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChangeTimer {
    public static Timer ct = new Timer();

    public static void start() {
        ct.schedule(new TimerTask() {
            public void run() {
                Bukkit.broadcastMessage(ChatColor.RED + "모든 플레이어의 능력이 랜덤으로 재추첨됩니다.");
                Collection<? extends Player> pl = Bukkit.getOnlinePlayers();

                for (AbilityBase ab : AbilityList.AbilityList) {
                    ab.SetPlayer(null, true);
                }
                Random r = new Random();
                for (Player p : pl) {
                    AbilityBase a;
                    do {
                        int i;
                        do {
                            i = r.nextInt(AbilityList.AbilityList.size());
                        } while (i == 0);
                        a = AbilityList.AbilityList.get(i);
                    } while (a.GetPlayer() != null);
                    a.SetPlayer(p, true);
                    a.SetRunAbility(true);
                    AUC.InfoTextOut(p);
                }
            }
        }, 600000L, 600000L);
    }
}
