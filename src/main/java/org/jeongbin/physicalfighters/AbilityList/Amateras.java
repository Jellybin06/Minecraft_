package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.ShowText;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import java.util.Timer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;


public class Amateras
        extends AbilityBase
{
    Timer timer = new Timer();

    public Amateras() {
        InitAbility("아마테라스", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "능력 사용시 체력을 소비해서 보고있는 사물을 태워버립니다.",
                "*아카이누와 블레이즈 등 불에 내성이 있는 적에게는 통하지 않습니다." });
        InitAbility(0, 0, true, AbilityBase.ShowText.Custom_Text);
        RegisterRightClickEvent();
    }

    public int A_Condition(Event event, int CustomData)
    {
        PlayerInteractEvent Event = (PlayerInteractEvent)event;
        if ((PlayerCheck(Event.getPlayer())) && (ItemCheck(ACC.DefaultItem)) &&
                (!EventManager.DamageGuard)) {
            return 0;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        Block block = GetPlayer().getTargetBlock(null, 0);
        Location ll = block.getLocation();
        Location l2 = block.getLocation();
        l2.setY(ll.getY() + 1.0D);
        if (block.getWorld().getBlockAt(l2).getType() == Material.AIR) {
            block.getWorld().getBlockAt(l2).setType(Material.FIRE);
            GetPlayer().setHealth((int) (((Damageable)GetPlayer()).getHealth() - 0.0D));
        } else {
            block.getWorld().getHighestBlockAt(ll).setType(Material.FIRE);
            GetPlayer().setHealth((int) (((Damageable)GetPlayer()).getHealth() - 0.0D));
        }
    }
}

