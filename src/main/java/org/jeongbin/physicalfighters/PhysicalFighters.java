package org.jeongbin.physicalfighters;

import org.jeongbin.physicalfighters.MainModule.CommandManager;
import org.jeongbin.physicalfighters.MainModule.EventManager;
import org.jeongbin.physicalfighters.MajorModule.AbilityList;
import org.jeongbin.physicalfighters.MinerModule.ACC;
import org.jeongbin.physicalfighters.Script.MainScripter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

public class PhysicalFighters extends org.bukkit.plugin.java.JavaPlugin
{
    public static int BuildNumber = 170802;

    public static boolean Invincibility = false;
    public static boolean DefaultArmed = true;
    public static boolean SRankUsed = true;
    public static boolean Respawn = false;
    public static boolean AutoKick = true;
    public static boolean AutoBan = true;
    public static boolean HalfMonsterDamage = false;
    public static boolean AutoDifficultySetting = true;
    public static boolean MaxLevelSurvival = false;
    public static int Setlev = 222;
    public static int EarlyInvincibleTime = 10;
    public static boolean NoFoodMode = true;
    public static boolean KillerOutput = true;
    public static boolean AutoCoordinateOutput = true;
    public static boolean NoAnimal = false;
    public static boolean NoAbilitySetting = false;
    public static boolean NoClearInventory = false;
    public static boolean PrintTip = true;
    public static boolean ReverseMode = false;
    public static boolean AutoSave = false;
    public static boolean InventorySave = false;
    public static boolean AbilityOverLap = false;
    public static boolean InfinityDur = true;
    public static int RestrictionTime = 15;
    public static boolean Kimiedition = false;
    public static boolean Specialability = false;
    public static boolean WoodGive = false;
    public static boolean LapisGive = false;
    public static boolean TableGive = false;
    public static boolean Gods = false;
    public static boolean Toner = false;
    public static boolean AllowND = true;
    public static boolean LOL = false;
    public static boolean canstart = true;
    public static boolean easteregg = false;
    public static boolean change = false;

    public static Logger log = Logger.getLogger("Minecraft");

    public static Timer TracerTimer;

    public CommandManager cm;
    public MainScripter scripter;
    public AbilityList A_List;

    public void onEnable()
    {
        org.jeongbin.physicalfighters.MinerModule.ACC.DefaultItem = Material.IRON_INGOT;
        if (ACC.DefaultItem == Material.IRON_INGOT) {
            log.info("기본값이 철로 변경되었습니다.");
        }

        log.info(String.format("(!)빌드정보 " + String.valueOf(BuildNumber),
                new Object[0]));
        log.info(String.format("(!)Edit By 염료", new Object[0]));
        this.cm = new CommandManager(this);

        getServer().getPluginManager().registerEvents(new EventManager(), this);

        log.info(String.format("(!)기본설정 로드중입니다.", new Object[0]));
        getConfig().options().copyDefaults(true);
        saveConfig();

        Invincibility = getConfig().getBoolean("시작후 초반 무적");
        DefaultArmed = getConfig().getBoolean("기본 무장 제공");
        Respawn = getConfig().getBoolean("시작시 리스폰으로 이동");
        AutoKick = getConfig().getBoolean("사망시 자동으로 킥");
        AutoBan = getConfig().getBoolean("사망시 자동으로 밴(킥이 활성화 되어야 가능)");
        SRankUsed = getConfig().getBoolean("S랭크 능력 사용");
        HalfMonsterDamage = getConfig().getBoolean("몬스터의 공격력 반감");
        AutoDifficultySetting = getConfig().getBoolean("난이도 자동으로 Easy로 설정");
        MaxLevelSurvival = getConfig().getBoolean("레벨 지급");
        Setlev = getConfig().getInt("레벨 설정");
        EarlyInvincibleTime = getConfig().getInt("초반 무적 시간(분 단위)");
        NoFoodMode = getConfig().getBoolean("배고픔 무한 모드(관련 능력은 알아서 상향됨)");
        KillerOutput = getConfig().getBoolean("죽을 경우 죽인 사람을 보여줌");
        AutoCoordinateOutput = getConfig().getBoolean("일정시간마다 좌표 표시");
        NoAnimal = getConfig().getBoolean("동물 비활성화");
        NoAbilitySetting = getConfig().getBoolean("시작시 능력 추첨 안함");
        NoClearInventory = getConfig().getBoolean("시작시 인벤토리 초기화 안함");
        PrintTip = getConfig().getBoolean("시작후 팁 출력함");
        AutoSave = getConfig().getBoolean("서버 오토 세이브");
        InventorySave = getConfig().getBoolean("인벤토리 세이브");
        AbilityOverLap = getConfig().getBoolean("능력 중복 가능");
        InfinityDur = getConfig().getBoolean("내구도 무한");
        RestrictionTime = getConfig().getInt("일부 능력 금지 시간(분 단위, 0은 사용 안함)");
        Kimiedition = getConfig().getBoolean("극한모드");
        Specialability = getConfig().getBoolean("안좋은능력제거");
        TableGive = getConfig().getBoolean("책장 지급");
        WoodGive = getConfig().getBoolean("나무 지급");
        Gods = getConfig().getBoolean("신등급활성화");
        Toner = getConfig().getBoolean("지형파괴능력제거");
        AllowND = getConfig().getBoolean("자연사허용");
        LOL = getConfig().getBoolean("팀전");
        change = getConfig().getBoolean("10분마다능력변경");

        log.info(String.format("(!)능력을 초기화합니다.", new Object[0]));
        org.jeongbin.physicalfighters.MainModule.AbilityBase.InitAbilityBase(this, this.cm);
        this.A_List = new AbilityList();

        log.info(String.format("(!)스크립터를 초기화합니다.", new Object[0]));
        this.scripter = new MainScripter(this, this.cm);
        if ((Invincibility) && (EarlyInvincibleTime <= 0)) {
            log.info(String.format("(!)초반무적이 1분으로 설정됩니다. [E.시간이 0분 이하입니다]",
                    new Object[0]));
            EarlyInvincibleTime = 1;
        }

        if (RestrictionTime < 0) {
            log.info(String.format("(!)제약 시간 값은 0보다 커야합니다. 0으로 설정됩니다.",
                    new Object[0]));
            RestrictionTime = 0;
        }

        log.info(String.format("(!)능력 %d개가 등록되있습니다.", new Object[] {
                Integer.valueOf(AbilityList.AbilityList.size() - 1) }));
        if (Kimiedition) {
            log.info(String.format("(!)극한모드 적용", new Object[0]));
        }
        if (Specialability) {
            log.info(String.format("(!)안좋은 능력을 제거합니다.", new Object[0]));
        }
        if (Gods) {
            log.info(String.format("(!)'신' 등급 활성화!", new Object[0]));
        }
        if (AutoSave)
            for (World w : org.bukkit.Bukkit.getServer().getWorlds())
                w.setAutoSave(true);
    }

    public void onDisable() {
        TracerTimer.cancel();
        log.info(String.format("(!)플러그인을 종료합니다.", new Object[0]));
    }
}