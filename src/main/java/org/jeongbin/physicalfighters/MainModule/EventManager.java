package org.jeongbin.physicalfighters.MainModule;

import org.jeongbin.physicalfighters.AbilityList.Phoenix;
import org.jeongbin.physicalfighters.MainModule.AbilityBase.Type;
import org.jeongbin.physicalfighters.MajorModule.AbilityList;
import org.jeongbin.physicalfighters.MinerModule.EventData;
import org.jeongbin.physicalfighters.PhysicalFighters;
import org.jeongbin.physicalfighters.Script.MainScripter;
import org.jeongbin.physicalfighters.Script.MainScripter.ScriptStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class EventManager implements Listener
{
	public static ArrayList<AbilityBase> LeftHandEvent = new ArrayList();
	public static ArrayList<AbilityBase> RightHandEvent = new ArrayList();
	public static boolean DamageGuard = false;

	public static HashMap<Player, ItemStack[]> invsave = new HashMap();
	public static HashMap<Player, ItemStack[]> arsave = new HashMap();


	public static ArrayList<EventData> onEntityDamage = new ArrayList();
	public static ArrayList<EventData> onEntityDamageByEntity = new ArrayList();

	@EventHandler
	public static void onEntityDamage(EntityDamageEvent event) {
		if ((event.getEntity() instanceof Player)) {
			if (DamageGuard) {
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
			}
			if (PhysicalFighters.InfinityDur) {
				Player p = (Player)event.getEntity();
				PlayerInventory inv = p.getInventory();
				if (inv.getChestplate() != null) {
					inv.getChestplate().setDurability((short)0);
				}
				if (inv.getHelmet() != null) {
					inv.getHelmet().setDurability((short)0);
				}
				if (inv.getLeggings() != null) {
					inv.getLeggings().setDurability((short)0);
				}
				if (inv.getBoots() != null)
					inv.getBoots().setDurability((short)0);
			}
		}
		AbilityExcuter(onEntityDamage, event);

		if ((event instanceof EntityDamageByEntityEvent)) {
			EntityDamageByEntityEvent Event = (EntityDamageByEntityEvent)event;

			if ((PhysicalFighters.HalfMonsterDamage) &&
					(!(Event.getDamager() instanceof Player)))
				Event.setDamage(Event.getDamage() / 2);
			AbilityExcuter(onEntityDamageByEntity, event);
		}
	}


	public static ArrayList<EventData> onEntityTarget = new ArrayList();

	@EventHandler
	public static void onEntityTarget(EntityTargetEvent event) {
		AbilityExcuter(onEntityTarget, event);
	}


	public static ArrayList<EventData> onFoodLevelChange = new ArrayList();

	@EventHandler
	public static void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (PhysicalFighters.NoFoodMode) {
			event.setFoodLevel(20);
			event.setCancelled(true);
			return;
		}
		AbilityExcuter(onFoodLevelChange, event);
	}



	@EventHandler
	public static void onChat(PlayerChatEvent event) {}



	public static ArrayList<EventData> onEntityRegainHealth = new ArrayList();

	@EventHandler
	public static void onEntityRegainHealth(EntityRegainHealthEvent event) {
		AbilityExcuter(onEntityRegainHealth, event); }



	public static ArrayList<EventData> onBlockPlaceEvent = new ArrayList();

	@EventHandler
	public static void onBlockPlaceEvent(BlockPlaceEvent event) {
		AbilityExcuter(onBlockPlaceEvent, event);
	}


	public static ArrayList<EventData> onBlockBreakEvent = new ArrayList();

	@EventHandler
	public static void onBlockBreakEvent(BlockBreakEvent event) {
		AbilityExcuter(onBlockBreakEvent, event);
	}


	public static ArrayList<EventData> onSignChangeEvent = new ArrayList();

	@EventHandler
	public static void onSignChangeEvent(SignChangeEvent event) {
		AbilityExcuter(onSignChangeEvent, event);
	}

	public static ArrayList<EventData> onPlayerToggleSneakEvent = new ArrayList();

	@EventHandler
	public static void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		AbilityExcuter(onPlayerToggleSneakEvent, event);
	}


	public static ArrayList<EventData> onProjectileLaunchEvent = new ArrayList();

	@EventHandler
	public static void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		AbilityExcuter(onProjectileLaunchEvent, event);
	}


	public static ArrayList<EventData> onPlayerDropItem = new ArrayList();

	@EventHandler
	public static void onPlayerDropItem(PlayerDropItemEvent event) {
		AbilityExcuter(onPlayerDropItem, event);
	}

	public static ArrayList<EventData> onPlayerPickupItem = new ArrayList();

	@EventHandler
	public static void onPlayerPickupItem(PlayerPickupItemEvent event) {
		AbilityExcuter(onPlayerPickupItem, event);
	}


	public static ArrayList<EventData> onPlayerRespawn = new ArrayList();

	@EventHandler
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		if ((PhysicalFighters.InventorySave) &&
				(!AbilityList.phoenix.PlayerCheck(event.getPlayer()))) {
			ItemStack[] ar = (ItemStack[])arsave.get(event.getPlayer());
			ItemStack[] inv = (ItemStack[])invsave.get(event.getPlayer());

			if (ar != null) {
				event.getPlayer().getInventory().setArmorContents(ar);
			}
			if (inv != null) {
				event.getPlayer().getInventory().setContents(inv);
			}
			arsave.remove(event.getPlayer());
			invsave.remove(event.getPlayer());
		}
		AbilityExcuter(onPlayerRespawn, event);
	}


	public static ArrayList<EventData> onEntityDeath = new ArrayList();

	@EventHandler
	public static void onEntityDeath(EntityDeathEvent event) {
		AbilityExcuter(onEntityDeath, event);

		if ((MainScripter.Scenario == MainScripter.ScriptStatus.GameStart) &&
				((event instanceof PlayerDeathEvent))) {
			PlayerDeathEvent pde = (PlayerDeathEvent)event;
			Player killed = (Player)event.getEntity();
			Player killerP = killed.getKiller();

			if ((PhysicalFighters.InventorySave) &&
					(!AbilityList.phoenix.PlayerCheck(killed))) {
				invsave.put(killed, killed.getInventory().getContents());
				arsave.put(killed, killed.getInventory().getArmorContents());
				pde.getDrops().clear();
			}

			if ((event.getEntity().getKiller() instanceof Player))
			{
				killed.getInventory().setHelmet(null);
				killed.getInventory().setChestplate(null);
				killed.getInventory().setLeggings(null);
				killed.getInventory().setBoots(null);
				killed.getInventory().clear();

				if ((PhysicalFighters.AutoKick) &&
						(!AbilityList.phoenix.PlayerCheck(killed))) {
					if ((PhysicalFighters.AutoBan) && (!killed.isOp())) {
						killed.kickPlayer("당신은 죽었습니다. 게임에서 퇴장합니다.");
					} else {
						killed.kickPlayer("당신은 죽었습니다. 게임에서 퇴장합니다.");
					}
				}

				PhysicalFighters.log.info(pde.getDeathMessage());
				if (PhysicalFighters.KillerOutput) {
					pde.setDeathMessage(String.format(ChatColor.GREEN + "%s" +
							ChatColor.WHITE + "님이 " + ChatColor.RED + "%s" +
							ChatColor.WHITE + "님의 살겠다는 의지를 꺾었습니다.", new Object[] {
							killerP.getName(), killed.getName() }));
				} else {
					pde.setDeathMessage(String.format(ChatColor.RED + "%s" +
							ChatColor.WHITE + "님이 누군가에게 살해당했습니다.", new Object[] {
							killed.getName() }));
				}
			}
			else if (!PhysicalFighters.AllowND) {
				killed.getInventory().setHelmet(null);
				killed.getInventory().setChestplate(null);
				killed.getInventory().setLeggings(null);
				killed.getInventory().setBoots(null);
				killed.getInventory().clear();

				if ((PhysicalFighters.AutoKick) &&
						(!AbilityList.phoenix.PlayerCheck(killed))) {
					if ((PhysicalFighters.AutoBan) && (!killed.isOp())) {
						killed.kickPlayer("당신은 죽었습니다. 게임에서 퇴장합니다.");
					} else {
						killed.kickPlayer("당신은 죽었습니다. 게임에서 퇴장합니다.");
					}
				}

				PhysicalFighters.log.info(pde.getDeathMessage());
				pde.setDeathMessage(String.format(ChatColor.RED + "%s" +
						ChatColor.WHITE + "님이 대자연에 의해 의지를 꺾였습니다.", new Object[] {
						killed.getName() }));
			}
		}
	}



	public static ArrayList<EventData> onPlayerInteract = new ArrayList();

	@EventHandler
	public static void onPlayerInteract(PlayerInteractEvent event) {
		_AbilityEventFilter(event);
		if (PhysicalFighters.InfinityDur)
			event.getPlayer().getItemInHand().setDurability((short)0);
		AbilityExcuter(onPlayerInteract, event);
		Player p = event.getPlayer();
		if ((p.getItemInHand() != null) &&
				(p.getItemInHand().getType() == Material.ENCHANTED_BOOK)) {
			ItemStack i = p.getItemInHand();
			if ((i.hasItemMeta()) &&
					(i.getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "[능력서]"))) {
				String name = i.getItemMeta().getDisplayName();
				int n = Integer.valueOf(name.split("f")[1].split("\\.")[0]).intValue();
				usebook(p, n);
				p.setItemInHand(null);
				p.updateInventory();
			}
		}

		if (p.getItemInHand().getType() == Material.CARROT) {
			p.setLevel(222);
			p.setItemInHand(null);
			p.updateInventory();
		}
	}

	public static void usebook(Player p, int abicode) { Player pn = p;
		if ((pn != null) &&
				(abicode >= 0) &&
				(abicode < AbilityList.AbilityList.size())) {
			AbilityBase a =
					(AbilityBase)AbilityList.AbilityList.get(abicode);

			if (PhysicalFighters.AbilityOverLap) {
				if ((a.GetAbilityType() == AbilityBase.Type.Active_Continue) ||
						(a.GetAbilityType() == AbilityBase.Type.Active_Immediately)) {
					for (AbilityBase ab : AbilityList.AbilityList) {
						if ((ab.PlayerCheck(pn)) && (
								(ab.GetAbilityType() == AbilityBase.Type.Active_Continue) ||
										(ab.GetAbilityType() == AbilityBase.Type.Active_Immediately))) {
							ab.SetPlayer(null, true);
						}
					}
				}
			} else {
				for (AbilityBase ab : AbilityList.AbilityList) {
					if (ab.PlayerCheck(pn)) {
						ab.SetPlayer(null, true);
					}
				}
			}
			a.SetPlayer(pn, true);
			a.SetRunAbility(true);
			org.bukkit.Bukkit.broadcastMessage(String.format(ChatColor.GOLD +
							"%s님이 능력을 부여받았습니다.",
					new Object[] { p.getName() }));
		}
	}


	public static ArrayList<EventData> onPlayerMoveEvent = new ArrayList();

	@EventHandler
	public static void onPlayerMove(PlayerMoveEvent event) {
		AbilityExcuter(onPlayerMoveEvent, event);
	}


	public static ArrayList<EventData> onProjectileHitEvent = new ArrayList();

	@EventHandler
	public static void onProjectileHit(ProjectileHitEvent event) {
		AbilityExcuter(onProjectileHitEvent, event);
	}

	private static void AbilityExcuter(ArrayList<EventData> ED, Event event){
		for(EventData ed : ED){
			if(ed.ab.GetAbilityType() == Type.Active_Continue){
				if(ed.ab.AbilityDuratinEffect(event, ed.parameter)){
					return;}
			}
			else{
				if(ed.ab.AbilityExcute(event, ed.parameter)){
					return;
				}
			}
		}
	}


	private static void _AbilityEventFilter(PlayerInteractEvent event){
		int i=0;
		if(event.getAction().equals(Action.LEFT_CLICK_AIR) ||
				event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			while(i<LeftHandEvent.size() && LeftHandEvent.size() != 0){
				if(LeftHandEvent.get(i).AbilityExcute(event, 0) == true){
					return;
				}
				++i;
			}
		}

		else if(event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
				event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			while(i<RightHandEvent.size() && RightHandEvent.size() != 0){
				if(RightHandEvent.get(i).AbilityExcute(event, 1) == true){
					return;
				}
				++i;
			}
		}
	}
}

