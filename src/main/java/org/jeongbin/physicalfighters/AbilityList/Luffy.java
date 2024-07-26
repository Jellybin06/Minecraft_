package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.ShowText;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Luffy extends AbilityBase {

    private Material item;

    public Luffy() {
        InitAbility("루피", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "철괴를 들고 왼쪽클릭을 하면 주먹질을 합니다 [쿨타임 없음]",
                "금괴를 들고 왼쪽클릭을 하면 30초간 속도,점프력,공격력,방어력이 높아집니다. [체력 5 소모, 쿨타임없음]",
                "버프스킬을 사용시에  부작용이 있습니다.",
                "*주의* 금괴를 들고 왼쪽클릭을 난타하시다가 사망하실 수 있습니다." });
        InitAbility(0, 0, true, ShowText.Custom_Text);
        RegisterLeftClickEvent();
        this.item = Material.IRON_INGOT;
    }

    @Override
    public int A_Condition(Event event, int CustomData) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Player p = e.getPlayer();

        if (PlayerCheck(p) && ItemCheck(this.item) && !EventManager.DamageGuard) {
            return 1;
        }

        if (PlayerCheck(p) && ((Damageable) p).getHealth() >= 6.0 && ItemCheck(Material.GOLD_INGOT)) {
            return 2;
        }

        return -1;
    }

    @Override
    public void A_Effect(Event event, int CustomData) {
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Player p = e.getPlayer();
        Location l = p.getLocation();
        Location l2 = p.getLocation();
        double degrees = Math.toRadians(-(l.getYaw() % 360.0));
        double ydeg = Math.toRadians(-(l.getPitch() % 360.0));

        switch (CustomData) {
            case 1:
                Timer timer = new Timer();
                for (int i = 1; i < 5; i++) {
                    l2.setX(l.getX() + (1 * i + 1) * (Math.sin(degrees) * Math.cos(ydeg)));
                    l2.setY(l.getY() + (1 * i + 1) * Math.sin(ydeg));
                    l2.setZ(l.getZ() + (1 * i + 1) * (Math.cos(degrees) * Math.cos(ydeg)));

                    if (l2.getWorld().getBlockAt(l2).getType() != Material.SANDSTONE) {
                        timer.schedule(new ExplosionTimer2(l2.getWorld().getBlockAt(l2).getType(), l2.getWorld().getBlockAt(l2)), 70L);
                        l2.getWorld().getBlockAt(l2).setType(Material.SAND);
                    }

                    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                    for (Player pp : players) {
                        if (pp != p && l2.distance(pp.getLocation()) <= 3.0) {
                            pp.damage(1, p);
                        }
                    }
                }
                break;
            case 2:
                p.setHealth(p.getHealth() - 5);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 6000, 0), true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0), true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 0), true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 6000, 0), true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 6000, 0), true);
                p.sendMessage(org.bukkit.ChatColor.GREEN + "기어세컨드를 사용하였습니다.");
                break;
        }
    }

    class ExplosionTimer extends TimerTask {
        private World world;
        private Location location;

        ExplosionTimer(Material type, Block block) {
            this.world = block.getWorld();
            this.location = block.getLocation();
        }

        @Override
        public void run() {
            this.world.getBlockAt(this.location).breakNaturally();
        }
    }

    class ExplosionTimer2 extends TimerTask {
        private World world;
        private Location location;
        private Material originalType;

        ExplosionTimer2(Material type, Block block) {
            this.world = block.getWorld();
            this.location = block.getLocation();
            this.originalType = type;
        }

        @Override
        public void run() {
            this.world.getBlockAt(this.location).setType(this.originalType);
        }
    }
}
