package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class Jumper extends AbilityBase {

    public Jumper() {
        InitAbility("점퍼", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.B, new String[] {
                "최대 40칸 거리를 순간이동 할 수 있습니다.",
                "단, 벽은 통과할 수 없고 낙사 데미지도 받습니다.",
                "자신이 갈 장소의 바닥 블럭을 클릭해야 텔포가 됩니다.",
                "사용시 유의하세요. 허공엔 사용이 안됩니다!" });
        InitAbility(20, 0, true);
        RegisterLeftClickEvent();
    }

    @Override
    public int A_Condition(Event event, int CustomData) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        if (PlayerCheck(e.getPlayer()) && ItemCheck(ACC.DefaultItem)) {
            Player p = e.getPlayer();
            Block b = p.getTargetBlock(null, 0);
            Location loc = b.getLocation();
            Location ploc = p.getLocation();

            if (b.getType().isAir() && b.getRelative(0, 1, 0).getType().isAir()) {
                // Check if the block and the block above it are air (empty)
                return 0;
            }

            p.sendMessage(ChatColor.RED + "거리가 너무 멉니다.");
        }
        return -1;
    }

    @Override
    public void A_Effect(Event event, int CustomData) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Player p = e.getPlayer();
        Block b = p.getTargetBlock(null, 0);
        Location loc = b.getLocation();

        if (b.getType().isAir() && b.getRelative(0, 1, 0).getType().isAir()) {
            loc.setY(b.getWorld().getHighestBlockYAt(b.getX(), b.getZ()) + 1.0);
        } else {
            loc.setY(b.getLocation().getY() + 1.0);
        }

        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        loc.setPitch(p.getLocation().getPitch());
        loc.setYaw(p.getLocation().getYaw());

        p.teleport(loc);
    }
}
