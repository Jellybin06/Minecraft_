package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Zoro extends AbilityBase
{
    int dama = 0;

    public Zoro() { InitAbility("조로", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
            "철괴 왼쪽클릭시 칼의 데미지가 랜덤으로 설정됩니다." });
        InitAbility(45, 0, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamageByEntity.add(new EventData(this, 1));
    }

    public int A_Condition(Event event, int CustomData)
    {
        if (CustomData == 1) {
            EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;
            if ((PlayerCheck(Event.getDamager())) && ((ItemCheck(Material.DIAMOND_SWORD)) || (ItemCheck(Material.WOODEN_SWORD)) || (ItemCheck(Material.IRON_SWORD)) || (ItemCheck(Material.GOLDEN_SWORD)))) {
                if (this.dama != 0) {
                    Event.setDamage(this.dama);
                } else {
                    this.dama = ((int)Event.getDamage());
                }
            }
        } else if (CustomData == 0)
        {
            PlayerInteractEvent Event1 = (PlayerInteractEvent)event;
            if ((PlayerCheck(Event1.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem))) {
                return 0;
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        int rand = 0;
        Random random = new Random();
        rand = random.nextInt(5) + 5;

        this.dama = rand;
        Event.getPlayer().sendMessage(org.bukkit.ChatColor.RED + "데미지가 " + this.dama + "로 설정되었습니다.");
    }
}

