package org.jeongbin.physicalfighters.MainModule;

import org.jeongbin.physicalfighters.MajorModule.AbilityList;
import org.jeongbin.physicalfighters.MajorModule.CoolDownTimer;
import org.jeongbin.physicalfighters.MajorModule.DurationTimer;
import org.jeongbin.physicalfighters.MajorModule.RestrictionTimer;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.PhysicalFighters;

import java.util.Collection;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public abstract class AbilityBase
{
    protected static CommandManager cm;
    protected static PhysicalFighters va;
    public static int AbilityCount = 0;

    public static final RestrictionTimer restrictionTimer = new RestrictionTimer();
    private Rank rank;
    private Player player;
    private String AbilityName;
    private Type type;
    private String[] Guide;
    private int CoolDown = 0;
    private int Duration = 0;
    private CoolDownTimer CTimer;
    private DurationTimer DTimer;
    private boolean RunAbility = true;
    private ShowText showtext = ShowText.All_Text;


    public abstract int A_Condition(Event paramEvent, int paramInt);


    public abstract void A_Effect(Event paramEvent, int paramInt);


    public void A_CoolDownStart() {}
    public void A_CoolDownEnd() {}
    public void A_DurationStart() {}
    public void A_DurationEnd() {}
    public void A_FinalDurationEnd() {}
    public void A_SetEvent(Player p) {}
    public void A_ResetEvent(Player p) {}

    public final void RegisterLeftClickEvent()
    {
        EventManager.LeftHandEvent.add(this);
    }

    public final void RegisterRightClickEvent() {
        EventManager.RightHandEvent.add(this);
    }

    public final Player GetPlayer() {
        return this.player;
    }

    public final boolean hasPlayer() {
        return this.player != null;
    }

    public final void SetPlayer(Player p, boolean textout) {
        this.DTimer.StopTimer();
        this.CTimer.StopTimer();
        if (this.player != null) {
            if (textout) {
                this.player.sendMessage(String.format(ChatColor.RED + "%s" +
                                ChatColor.WHITE + " 능력이 해제되었습니다.",
                        new Object[] { GetAbilityName() }));
            }
            A_ResetEvent(this.player);
        }
        if ((p != null) && (this.RunAbility)) {
            if (textout) {
                p.sendMessage(String.format(ChatColor.GREEN + "%s" +
                                ChatColor.WHITE + " 능력이 설정되었습니다.",
                        new Object[] { GetAbilityName() }));
            }
            A_SetEvent(p);
        }
        this.player = p;
    }

    public final String GetAbilityName() {
        return this.AbilityName;
    }

    public final Type GetAbilityType() {
        return this.type == null ? null : this.type;
    }


    public final String[] GetGuide() { return this.Guide; }

    public final LinkedList<String> GetGuide2() {
        LinkedList<String> s = new LinkedList();
        String[] arrayOfString; int j = (arrayOfString = this.Guide).length; for (int i = 0; i < j; i++) { String z = arrayOfString[i];
            s.add(z);
        }
        return s;
    }

    public final int GetCoolDown() {
        return this.CoolDown;
    }

    public final Rank GetRank() {
        return this.rank;
    }

    public final int GetDuration() {
        return this.Duration;
    }

    public final boolean GetDurationState() {
        return this.DTimer.GetTimerRunning();
    }

    public final void SetRunAbility(boolean RunAbility) {
        this.RunAbility = RunAbility;
    }

    public final boolean GetRunAbility() {
        return this.RunAbility;
    }

    public final ShowText GetShowText() {
        return this.showtext;
    }

    public final void AbilityDTimerCancel() {
        this.DTimer.StopTimer();
    }

    public final void AbilityCTimerCancel() {
        this.CTimer.StopTimer();
    }

    public final boolean PlayerCheck(Player p) {
        if ((this.player != null) &&
                (p.getName().equalsIgnoreCase(this.player.getName())) && (
                (this.player.isDead()) || (Bukkit.getServer().getPlayerExact(this.player.getName()) != null))) {
            this.player = p;
            return true;
        }

        return false;
    }

    public final boolean PlayerCheck(Entity e) {
        return ((e instanceof Player)) && ((Player)e != null) &&
                (PlayerCheck((Player)e));
    }

    public final boolean ItemCheck(Material itemID) {
        return GetPlayer().getItemInHand().getType() == itemID;
    }


    public final boolean AbilityExcute(Event event) {
        return AbilityExcute(event, 0);
    }

    public final boolean AbilityExcute(Event event, int CustomData) {
        if ((this.player != null) && (this.RunAbility)) {
            int cd = A_Condition(event, CustomData);
            if (cd == -2) {
                return true;
            }
            if (cd != -1) {
                if ((this.type == Type.Active_Continue) ||
                        (this.type == Type.Active_Immediately)) {
                    if (this.DTimer.GetTimerRunning())
                    {
                        GetPlayer().sendMessage(String.format(ChatColor.WHITE + "%d초" + ChatColor.GREEN+ " 후 지속시간이 끝납니다.", new Object[] {Integer.valueOf(this.DTimer.GetCount())}));
                        return true;
                    }
                    if (this.CTimer.GetTimerRunning()) {
                        if (GetShowText() != ShowText.No_CoolDownText) {
                            GetPlayer().sendMessage(String.format(ChatColor.WHITE + "%d초" + ChatColor.RED + " 후 능력을 다시 사용하실 수 있습니다.", new Object[] {Integer.valueOf(this.CTimer.GetCount())}));
                        }
                        return true;
                    }

                    if (GetShowText() != ShowText.Custom_Text)
                        GetPlayer().sendMessage(ACC.DefaultAbilityUsed);
                }
                if (this.type == Type.Active_Immediately) {
                    A_Effect(event, cd);
                    if (GetCoolDown() != 0)
                        this.CTimer.StartTimer(GetCoolDown(), true);
                } else if (this.type == Type.Active_Continue) {
                    this.DTimer.StartTimer(GetDuration(), true);
                } else {
                    A_Effect(event, cd);
                }
                return true;
            }
        }
        return false;
    }

    public final int goPlayerVelocity(Player p1, Player p2, int value) {
        p1.setVelocity(p1.getVelocity().add(
                p2.getLocation().toVector()
                        .subtract(p1.getLocation().toVector()).normalize()
                        .multiply(value)));
        return 0;
    }

    public static final int goVelocity(Player p1, Location lo, int value) {
        p1.setVelocity(p1.getVelocity().add(
                lo.toVector().subtract(p1.getLocation().toVector()).normalize()
                        .multiply(value)));
        return 0;
    }

    public static final int ArrowVelocity(Arrow a, Location lo, int value) {
        a.setVelocity(a.getVelocity().add(
                lo.toVector().subtract(a.getLocation().toVector()).normalize()
                        .multiply(value)));
        return 0;
    }

    public static final int Direction(Player p) {
        Location loc = p.getLocation();
        Location loc2 = p.getTargetBlock(null, 0).getLocation();
        int x = (int)Math.abs(Math.abs(loc.getX()) - Math.abs(loc2.getX()));
        int z = (int)Math.abs(Math.abs(loc.getZ()) - Math.abs(loc2.getZ()));
        if (loc == loc2) {
            return 10;
        }
        if (x > z) {
            if (loc.getX() > loc2.getX()) {
                return 1;
            }

            return 3;
        }

        if (loc.getZ() > loc2.getZ()) {
            return 2;
        }

        return 4;
    }

    public static final int LookAngle(Location l, Location l2, int value) {
        double degrees = Math.toRadians(-(l.getYaw() % 360.0F));
        double ydeg = Math.toRadians(-(l.getPitch() % 360.0F));
        l2.setX(l.getX() + 2 * value + 1.0D * (Math.sin(degrees) * Math.cos(ydeg)));
        l2.setY(l.getY() + 2 * value + 1.0D * Math.sin(ydeg));
        l2.setZ(l.getZ() + 2 * value + 1.0D * (Math.cos(degrees) * Math.cos(ydeg)));
        return 0;
    }

    public final boolean AbilityDuratinEffect(Event event) {
        return AbilityDuratinEffect(event, 0);
    }

    public final boolean AbilityDuratinEffect(Event event, int CustomData) {
        if ((this.player != null) && (this.DTimer.GetTimerRunning())) {
            A_Effect(event, CustomData);
            return true;
        }
        return false;
    }

    protected final void InitAbility(String AbilityName, Type type, Rank rank, String... Manual)
    {
        this.AbilityName = AbilityName;
        this.type = type;
        this.Guide = new String[Manual.length];
        for (int loop = 0; loop < Manual.length; loop++)
            this.Guide[loop] = Manual[loop];
        this.CTimer = new CoolDownTimer(this);
        this.DTimer = new DurationTimer(this, this.CTimer);
        this.rank = rank;
    }

    protected final void InitAbility(int CoolDown, int Duration, boolean RunAbility)
    {
        InitAbility(CoolDown, Duration, RunAbility, ShowText.All_Text);
    }

    protected final void InitAbility(int CoolDown, int Duration, boolean RunAbility, ShowText showtext)
    {
        this.CoolDown = ((this.type == Type.Active_Continue) || (this.type == Type.Active_Immediately) ? CoolDown : -1);
        this.Duration = (this.type == Type.Active_Continue ? Duration : -1);
        this.RunAbility = RunAbility;
        this.showtext = showtext;
        AbilityList.AbilityList.add(this);
        AbilityCount += 1;
    }

    public static final int GetAbilityCount() {
        return AbilityCount;
    }

    public static final AbilityBase FindAbility(Player p) {
        for (AbilityBase a : AbilityList.AbilityList)
            if (a.PlayerCheck(p))
                return a;
        return null;
    }

    public static final AbilityBase FindAbility(String name) {
        for (AbilityBase a : AbilityList.AbilityList)
            if (a.GetAbilityName().equalsIgnoreCase(name))
                return a;
        return null;
    }

    public static final void InitAbilityBase(PhysicalFighters va, CommandManager cm)
    {
        AbilityBase.va = va;
        AbilityBase.cm = cm;
    }

    public static enum Rank {
        SSS(ChatColor.GOLD + "Special Rank"),  SS(ChatColor.GRAY + "SS Rank"),  S(
                ChatColor.RED + "S Rank"),  A(ChatColor.GREEN + "A Rank"),  B(
                ChatColor.BLUE + "B Rank"),  C(ChatColor.YELLOW + "C Rank"),  F(
                ChatColor.BLACK + "F Rank"),  FF(ChatColor.BLACK + "개가 싸놓은 똥"),  GOD(
                ChatColor.WHITE + "신");

        private String s;

        private Rank(String s) {
            this.s = s;
        }


        public String GetText() { return this.s; }
    }

    public final boolean isSword(ItemStack is) {
        if ((is.getType() == Material.WOODEN_SWORD) || (is.getType() == Material.STONE_SWORD) || (is.getType() == Material.GOLDEN_SWORD) || (is.getType() == Material.IRON_SWORD) || (is.getType() == Material.DIAMOND_SWORD)) {
            return true;
        }
        return false;
    }

    public void ExplosionDMG(Player p, int distance, int dmg) {
        Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
        for (Player t : pl) {
            if ((t != p) && (p.getLocation().distance(t.getLocation()) <= distance))
                t.damage(dmg, p);
        }
    }

    public void ExplosionDMG(Player p, Location l, int distance, int dmg) {
        Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
        for (Player t : pl) {
            if ((t != p) && (l.distance(t.getLocation()) <= distance))
                t.damage(dmg, p);
        }
    }

    public void ExplosionDMGL(Player p, Location l, int distance, int dmg) {
        Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
        for (Player t : pl)
            if ((t != p) && (l.distance(t.getLocation()) <= distance)) {
                t.damage(dmg, p);
                t.getWorld().strikeLightning(t.getLocation());
            }
    }

    public void ExplosionDMGPotion(Player p, Location l, int distance, int dmg, PotionEffectType pet, int dura, int amp) {
        Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
        for (Player t : pl)
            if ((t != p) && (l.distance(t.getLocation()) <= distance)) {
                t.damage(dmg, p);
                t.addPotionEffect(new PotionEffect(pet, dura, amp));
            }
    }

    public void ExplosionDMG(Location l, int distance, int dmg) {
        Collection<? extends Player> pl = Bukkit.getOnlinePlayers(); // Collection<? extends Player> java 제네릭 타입을 사용한 표현
        for (Player t : pl) {
            if (t.getLocation().distance(l) <= distance)
                t.damage(dmg);
        }
    }

    public void setCool(int i) {
        this.CTimer.SetCount(i);
    }


    public int getCool() { return this.CTimer.GetCount(); }

    public static enum ShowText {
        All_Text,  No_CoolDownText,  No_DurationText,  No_Text,  Custom_Text;
    }

    public static enum Type {
        Passive_AutoMatic,  Passive_Manual,  Active_Immediately,  Active_Continue;
    }
}
