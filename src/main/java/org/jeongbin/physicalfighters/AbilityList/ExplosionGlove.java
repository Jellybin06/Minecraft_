package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import java.util.Collection;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class ExplosionGlove extends AbilityBase
{
    public ExplosionGlove()
    {
        InitAbility("폭파장갑", org.jeongbin.physicalfighters.MainModule.AbilityBase.Type.Active_Immediately, AbilityBase.Rank.A, new String[] { "능력 사용시 주변의 플레이어에게 폭발을 일으키며", "공중으로 띄웁니다." });
        InitAbility(50, 0, true);
        RegisterLeftClickEvent();
    }

    public int A_Condition(Event event, int CustomData) {
        if (CustomData == 0) {
            PlayerInteractEvent e = (PlayerInteractEvent)event;
            if ((PlayerCheck(e.getPlayer())) && (ItemCheck(Material.IRON_INGOT)) && !EventManager.DamageGuard) {
                return 0;
            }
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData) {
        if (CustomData == 0) {
            PlayerInteractEvent e = (PlayerInteractEvent)event;
            Player[] pl = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation(), 5.0F);
            for (Player p : pl) {
                if ((p != GetPlayer()) &&
                        (p.getLocation().distance(GetPlayer().getLocation()) <= 15.0D)) {
                    p.getWorld().createExplosion(p.getLocation(), 3.0F);
                    p.damage(14, e.getPlayer());
                    p.setVelocity(new org.bukkit.util.Vector(0.0D, 1.5D, 0.0D));
                }
            }
        }
    }
}
