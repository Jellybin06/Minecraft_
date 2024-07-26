package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class Enel extends AbilityBase
{
    public Enel()
    {
        if (PhysicalFighters.SRankUsed) {
            InitAbility("갓 에넬", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                    "바라보는 방향으로 번개를 연속발사합니다." });
            InitAbility(30, 0, true);
            RegisterLeftClickEvent();
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        if ((!EventManager.DamageGuard) &&
                (PlayerCheck(Event.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem))) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Location l = Event.getPlayer().getLocation();
        Location l2 = Event.getPlayer().getLocation();
        double degrees = Math.toRadians(-(l.getYaw() % 360.0F));

        for (int i = 1; i < 5; i++) {
            l2.setX(l.getX() + 1 * i + 4.0D * Math.sin(degrees));
            l2.setZ(l.getZ() + 1 * i + 4.0D * Math.cos(degrees));
            l2.getWorld().strikeLightning(l2);
            Player[] List = Bukkit.getOnlinePlayers();
            Player[] arrayOfPlayer1; int j = (arrayOfPlayer1 = List).length; for (int k = 0; k < j; k++) { Player pp = arrayOfPlayer1[k];
                if (pp != GetPlayer()) {
                    Location loc = pp.getLocation();


                    if ((l2.getWorld().getBlockAt(l2).getLocation().distance(loc) <= 3.0D) &&
                            (!EventManager.DamageGuard)) {
                        pp.damage(15);
                    }
                }
            }
        }
    }
}

