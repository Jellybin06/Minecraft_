package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.ShowText;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;

public class MachineGun extends AbilityBase
{
    private int bullet = 0;
    private Material item;
    private HashMap<Player, Boolean> relo = new HashMap();

    public MachineGun() {
        InitAbility("기관총", AbilityBase.Type.Active_Immediately, AbilityBase.Rank.S, new String[] {
                "고속으로 화살을 발사합니다. 금괴를 들고 오른클릭을 누르면 연사가",
                "가능합니다. 철괴를 탄창으로 사용하며 한 탄창은 30발입니다.",
                "피격시 10% 확률로 방어력을 무시하고 체력을 2 감소시키는",
                "크리티컬이 발생합니다. 기본 탄환 데미지는 1입니다." });
        InitAbility(0, 0, true, AbilityBase.ShowText.Custom_Text);

        RegisterRightClickEvent();
        EventManager.onEntityDamageByEntity.add(new EventData(this, 3));
        EventManager.onProjectileHitEvent.add(new EventData(this, 5));
        this.item = Material.GOLD_INGOT;
    }

    public int A_Condition(Event event, int CustomData) {
        switch (CustomData) {
            case 1:
                PlayerInteractEvent Event0 = (PlayerInteractEvent)event;
                if ((!EventManager.DamageGuard) &&
                        (PlayerCheck(Event0.getPlayer())) && (ItemCheck(this.item))) {
                    if (this.bullet != 0) {
                        return 10;
                    }


                    if (GetPlayer().getInventory().contains(Material.IRON_INGOT)) {
                        return 20;
                    }
                    GetPlayer().sendMessage(ChatColor.RED + "탄창이 없습니다.");

                    if (this.relo.containsKey(GetPlayer()))
                    {
                        GetPlayer().sendMessage(ChatColor.RED + "장전중입니다.");
                    }
                }
                break;
            case 3:
                EntityDamageByEntityEvent Event1 = (EntityDamageByEntityEvent)event;
                if ((Event1.getDamager() instanceof Arrow)) {
                    Arrow a = (Arrow)Event1.getDamager();
                    if (PlayerCheck((Player)a.getShooter())) {
                        if (((Event1.getEntity() instanceof Player)) &&
                                ((Player)a.getShooter() ==
                                        (Player)Event1
                                                .getEntity()))
                            return -1;
                        return 3;
                    }
                }
                break;
            case 5:
                ProjectileHitEvent Event2 = (ProjectileHitEvent)event;
                if ((Event2.getEntity() instanceof Arrow)) {
                    Arrow a = (Arrow)Event2.getEntity();
                    if (((a.getShooter() instanceof Player)) &&
                            (PlayerCheck((Player)a.getShooter()))) {
                        a.remove();
                        return -2;
                    }
                }

                break;
        }

        return -1;
    }


    public void A_Effect(Event event, int CustomData) {
        switch (CustomData) {
            case 3:
                EntityDamageByEntityEvent Event0 = (EntityDamageByEntityEvent) event;
                Event0.setDamage(1);
                if (((Event0.getEntity() instanceof Player)) && (Math.random() <= 0.1D)) {
                    Player p = (Player) Event0.getEntity();
                    p.getWorld().createExplosion(p.getLocation(), 0.0F);
                    if (((Damageable) p).getHealth() > 2.0D) {
                        p.setHealth(((Damageable) p).getHealth() - 2);
                    }
                }
                break;
            case 10:
                PlayerInteractEvent Event1 = (PlayerInteractEvent) event;
                if (this.bullet % 5 == 0) {
                    Event1.getPlayer().sendMessage(ChatColor.AQUA + "남은 탄환 : " + ChatColor.WHITE + this.bullet + "개");
                }
                this.bullet -= 1;
                shootArrow(Event1.getPlayer());
                break;
            case 20:
                PlayerInteractEvent Event2 = (PlayerInteractEvent) event;
                if (!this.relo.containsKey(Event2.getPlayer())) {
                    Event2.getPlayer().sendMessage("탄환이 다 떨어졌습니다. 장전합니다 [3초소요]");
                    this.relo.put(Event2.getPlayer(), Boolean.TRUE);
                    Timer timer = new Timer();
                    timer.schedule(new onReload(), 3000L);
                }
                break;
        }
    }

    private void shootArrow(Player player) {
        World world = player.getWorld();
        Location location = player.getEyeLocation();
        Arrow arrow = world.spawnArrow(location, location.getDirection(), 2.0F, 0.0F); // Adjust speed and spread as needed
        arrow.setShooter(player);
    }

    class onReload extends TimerTask
    {
        onReload() {}

        public void run()
        {
            PlayerInventory inv = MachineGun.this.GetPlayer().getInventory();
            int sell = inv.first(Material.IRON_INGOT);
            if (inv.getItem(sell).getAmount() == 1) {
                inv.clear(sell);
            } else {
                inv.getItem(sell).setAmount(inv.getItem(sell).getAmount() - 1);
            }
            MachineGun.this.GetPlayer().updateInventory();
            MachineGun.this.bullet = 30;
            MachineGun.this.GetPlayer().sendMessage(ChatColor.GREEN + "재장전 완료");
            MachineGun.this.relo.remove(MachineGun.this.GetPlayer());
        }
    }
}

