package org.jeongbin.physicalfighters.AbilityList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jeongbin.physicalfighters.MainModule.AbilityBase;

public class Clocking extends AbilityBase {

    public Clocking() {
        InitAbility("클로킹", AbilityBase.Type.Active_Continue, AbilityBase.Rank.A, new String[] {
                "능력 사용시 일정시간동안 다른 사람에게 보이지 않습니다.",
                "클로킹 상태에서는 타인에게 공격 받지 않습니다." });
        InitAbility(35, 5, true);
        RegisterLeftClickEvent();
    }

    @Override
    public int A_Condition(Event event, int CustomData) {
        if (CustomData == 0) {
            PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
            if (PlayerCheck(interactEvent.getPlayer()) && ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem)) {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public void A_DurationStart() {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        for (Player p : onlinePlayers) {
            p.hidePlayer(GetPlayer());
        }
    }

    @Override
    public void A_FinalDurationEnd() {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        for (Player p : onlinePlayers) {
            p.showPlayer(GetPlayer());
        }
    }

    @Override
    public void A_Effect(Event event, int CustomData) {
        // Implement if needed
    }
}
