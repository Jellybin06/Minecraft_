package org.jeongbin.physicalfighters.AbilityList;

import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Rank;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Phoenix extends AbilityBase
{
    private int ReviveCounter = 0;
    private boolean AbilityUse = false;

    public Phoenix() {
        if (!PhysicalFighters.Toner) {
            InitAbility("불사조", AbilityBase.Type.Passive_Manual, AbilityBase.Rank.A, new String[] {
                    "자연사할시 무제한으로 인벤토리를 잃지 않고 부활합니다.",
                    "타인에게 사망할 경우 1회에 한하여 자연사와 같이 부활합니다.",
                    "부활시 자신의 능력이 모두에게 알려지게 됩니다.", "하지만 이 능력도 데스노트만은 막을수 없을것입니다." });
            InitAbility(0, 0, true);
            EventManager.onEntityDeath.add(new EventData(this, 0));
            EventManager.onPlayerRespawn.add(new EventData(this, 1));
        }
    }

    public int A_Condition(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                EntityDeathEvent Event0 = (EntityDeathEvent)event;
                if (PlayerCheck(Event0.getEntity())) {
                    return 0;
                }
                break;
            case 1:
                PlayerRespawnEvent Event1 = (PlayerRespawnEvent)event;
                if (PlayerCheck(Event1.getPlayer()))
                    return 1;
                break;
        }
        return -1;
    }

    public void A_Effect(Event event, int CustomData)
    {
        switch (CustomData) {
            case 0:
                PlayerDeathEvent Event0 = (PlayerDeathEvent)event;
                Player killed = Event0.getEntity();

                EventManager.invsave.put(killed, killed.getInventory()
                        .getContents());
                EventManager.arsave.put(killed, killed.getInventory()
                        .getArmorContents());
                Event0.getDrops().clear();

                if (this.AbilityUse) {
                    Bukkit.broadcastMessage(ChatColor.RED +
                            "불사조가 죽었습니다. 더 이상 부활할수 없습니다.");
                    if (PhysicalFighters.AutoKick) {
                        if (PhysicalFighters.AutoBan) {
                            killed.kickPlayer("당신은 죽었습니다. 게임에서 퇴장합니다.");
                        } else {
                            killed.kickPlayer("당신은 죽었습니다. 게임에서 퇴장합니다.");
                        }
                    }
                } else {
                    Bukkit.broadcastMessage(ChatColor.GREEN +
                            "불사조가 죽었습니다. 다시 부활할 수 있습니다.");
                }

                if ((killed.getKiller() instanceof Player)) {
                    this.AbilityUse = true;
                }
                this.ReviveCounter += 1;

                break;
            case 1:
                PlayerRespawnEvent Event1 = (PlayerRespawnEvent)event;
                ItemStack[] ar = (ItemStack[])EventManager.arsave.get(Event1.getPlayer());
                ItemStack[] inv = (ItemStack[])EventManager.invsave.get(Event1.getPlayer());

                if (ar != null) {
                    Event1.getPlayer().getInventory().setArmorContents(ar);
                }
                if (inv != null) {
                    Event1.getPlayer().getInventory().setContents(inv);
                }
                EventManager.arsave.remove(Event1.getPlayer());
                EventManager.invsave.remove(Event1.getPlayer());
                if (!this.AbilityUse) {
                    Bukkit.broadcastMessage(ChatColor.GREEN +
                            "불사조가 부활하였습니다. 부활 횟수 : " +
                            String.valueOf(this.ReviveCounter) + "회");
                }

                Event1.getPlayer().addPotionEffect(
                        new PotionEffect(PotionEffectType.HASTE,
                                600, 0), true);
                Event1.getPlayer().addPotionEffect(
                        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600,
                                0), true);
                Event1.getPlayer().addPotionEffect(
                        new PotionEffect(PotionEffectType.JUMP_BOOST, 600, 0), true);
                Event1.getPlayer().addPotionEffect(
                        new PotionEffect(PotionEffectType.SPEED, 600, 0), true);
                Event1.getPlayer().addPotionEffect(
                        new PotionEffect(PotionEffectType.WATER_BREATHING, 600,
                                0), true);
                Event1.getPlayer()
                        .addPotionEffect(
                                new PotionEffect(PotionEffectType.REGENERATION,
                                        600, 0), true);
                Event1.getPlayer().addPotionEffect(
                        new PotionEffect(PotionEffectType.STRENGTH,
                                600, 0), true);
        }
    }

    public void A_SetEvent(Player p)
    {
        this.ReviveCounter = 0;
        this.AbilityUse = false;
    }
}

