package org.jeongbin.physicalfighters.Script;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MajorModule.AbilityList;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class S_GameWarnning
{
    private S_ScriptTimer stimer = new S_ScriptTimer();
    private MainScripter ms;

    public S_GameWarnning(MainScripter ms)
    {
        this.ms = ms;
    }

    public void GameWarnningStart() {
        this.stimer.StartTimer(99999999);
    }

    public void GameWarnningStop() {
        this.stimer.EndTimer();
    }

    public final class S_ScriptTimer extends org.jeongbin.physicalfighters.MinerModule.TimerBase
    {
        public S_ScriptTimer() {}

        public void EventStartTimer() {}

        public void EventRunningTimer(int count) {
            if ((count >= 20) && (count % 20 == 0)) {
                Bukkit.broadcastMessage(ChatColor.RED +
                        "경고, 게임이 올바르게 시작되지 않았습니다.");
                Bukkit.broadcastMessage(ChatColor.RED +
                        "/va yes나 /va no 명령으로 능력을 확정하세요.");
                for (int l = 0; l < AbilityList.AbilityList.size(); l++) {
                    if (((AbilityBase)AbilityList.AbilityList.get(l)).GetPlayer() != null) {
                        AbilityBase tempab = (AbilityBase)AbilityList.AbilityList.get(l);
                        if (!S_GameWarnning.this.ms.OKSign.contains(tempab.GetPlayer())) {
                            tempab.GetPlayer().sendMessage(
                                    "당신의 능력이 올바르게 확정되지 않았습니다.");
                        }
                    }
                }
            }
        }

        public void EventEndTimer() {}
    }
}


