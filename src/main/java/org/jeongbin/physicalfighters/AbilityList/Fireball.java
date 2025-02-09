package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fireball extends AbilityBase
{
    public Fireball()
    {
        InitAbility("파이어볼", org.jeongbin.physicalfighters.MainModule.AbilityBase.Type.Active_Immediately, AbilityBase.Rank.B, new String[] {
                "바라보는 방향에 약한 화염구를 날립니다." });
        InitAbility(15, 0, true);
        RegisterLeftClickEvent();
        EventManager.onProjectileHitEvent.add(new org.jeongbin.physicalfighters.MinerModule.EventData(this, 1));
    }

    public int A_Condition(Event event, int CustomData)
    {
        switch (CustomData)
        {
            case 0:
                PlayerInteractEvent Event = (PlayerInteractEvent)event;
                if ((PlayerCheck(Event.getPlayer())) && (ItemCheck(org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem)) && !EventManager.DamageGuard)
                    return 0;
                break;
            case 1:
                ProjectileHitEvent Event1 = (ProjectileHitEvent)event;
                if ((Event1.getEntity() instanceof org.bukkit.entity.Fireball))
                {
                    Event1.getEntity().getWorld().createExplosion(Event1.getEntity().getLocation(), 3.0F, true);
                    Event1.getEntity().getWorld().createExplosion(Event1.getEntity().getLocation(), 2.5F, true);
                    Event1.getEntity().getWorld().createExplosion(Event1.getEntity().getLocation(), 3.0F, true);
                }
                break;
        }

        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        Player p = Event.getPlayer();
        World a = p.getWorld();
        Location loc = p.getLocation();
        Location l = p.getLocation();
        l.setY(p.getLocation().getY() + 2.0D);
        double degrees = Math.toRadians(-(l.getYaw() % 360.0F));
        double ydeg = Math.toRadians(-(l.getPitch() % 360.0F));
        loc.setX(l.getX() + 1.2D * (
                Math.sin(degrees) * Math.cos(ydeg)));
        loc.setY(l.getY() + 1.2D *
                Math.sin(ydeg));
        loc.setZ(l.getZ() + 1.2D * (
                Math.cos(degrees) * Math.cos(ydeg)));
        a.spawn(loc, org.bukkit.entity.Fireball.class);
    }
}
