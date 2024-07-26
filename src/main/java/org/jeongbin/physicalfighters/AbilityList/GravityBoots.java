package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Timer;
import java.util.TimerTask;

public class GravityBoots extends AbilityBase {
    private Block b = null;
    private Material originalType; // Store the original block type

    public GravityBoots() {
        InitAbility("중력장화", AbilityBase.Type.Active_Immediately, Rank.S, new String[] {
                "아무것도 신지 않고, 철괴로 왼쪽클릭을 하면, 바라보는 블럭에 10초간 청금석이 생깁니다.",
                "능력 사용자를 제외한 청금석 주변의 플레이어는 지속 데미지와 함께 청금석으로 끌려옵니다.",
                "아무 것도 신지 않고 있는 경우 낙하 데미지를 받지 않습니다." });
        InitAbility(40, 10, true);
        RegisterLeftClickEvent();
        EventManager.onEntityDamage.add(new EventData(this, 1));
        EventManager.onBlockBreakEvent.add(new EventData(this, 2));
    }

    @Override
    public int A_Condition(Event event, int CustomData) {
        switch (CustomData) {
            case 0:
                PlayerInteractEvent e = (PlayerInteractEvent) event;
                if (PlayerCheck(e.getPlayer()) && ItemCheck(ACC.DefaultItem) && !EventManager.DamageGuard) {
                    Player p = e.getPlayer();
                    if (p.getInventory().getBoots() == null) {
                        Block targetBlock = p.getTargetBlock(null, 0);
                        if (p.getLocation().distance(targetBlock.getLocation()) < 30.0) {
                            return 0;
                        } else {
                            p.sendMessage(ChatColor.RED + "너무 멉니다.");
                        }
                    }
                }
                break;
            case 1:
                EntityDamageEvent e2 = (EntityDamageEvent) event;
                if (PlayerCheck(e2.getEntity())) {
                    Player p = (Player) e2.getEntity();
                    if (p.getInventory().getBoots() == null &&
                            e2.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        e2.setCancelled(true);
                    }
                }
                break;
            case 2:
                BlockBreakEvent e3 = (BlockBreakEvent) event;
                if (e3.getBlock().equals(this.b)) {
                    e3.setCancelled(true);
                }
                break;
        }
        return -1;
    }

    @Override
    public void A_Effect(Event event, int CustomData) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Player p = e.getPlayer();
        Block targetBlock = p.getTargetBlock(null, 0);
        this.originalType = targetBlock.getType(); // Store the original block type
        targetBlock.setType(Material.LAPIS_BLOCK);
        this.b = targetBlock;
        Timer timer = new Timer();
        timer.schedule(new GTimer(), 1000L, 1000L);
    }

    public class GTimer extends TimerTask {
        private int a = 10;

        public GTimer() {}

        @Override
        public void run() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (GravityBoots.this.b.getLocation().distance(p.getLocation()) < 10.0 && p != GravityBoots.this.GetPlayer()) {
                    p.teleport(GravityBoots.this.b.getLocation());
                    p.damage(3, GravityBoots.this.GetPlayer());
                }
            }
            this.a--;
            if (this.a <= 0) {
                Location l = GravityBoots.this.b.getLocation().clone();
                l.setY(GravityBoots.this.b.getLocation().getY() + 1.0);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (GravityBoots.this.b.getLocation().distance(p.getLocation()) < 10.0 && p != GravityBoots.this.GetPlayer()) {
                        p.teleport(l);
                        p.damage(5, GravityBoots.this.GetPlayer());
                    }
                }
                GravityBoots.this.b.setType(GravityBoots.this.originalType); // Reset to original block type
                GravityBoots.this.b = null;
                cancel();
            }
        }
    }
}
