package org.jeongbin.physicalfighters.Script;

import org.jeongbin.physicalfighters.AbilityList.Mirroring;
import org.jeongbin.physicalfighters.MainModule.AbilityBase;
import org.jeongbin.physicalfighters.MajorModule.AbilityList;
import org.jeongbin.physicalfighters.MinerModule.TimerBase;
import org.jeongbin.physicalfighters.PhysicalFighters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class S_GameReady
{
    private MainScripter mainscripter;
    private S_ScriptTimer stimer = new S_ScriptTimer();
    private int peoplecount = 0;

    public S_GameReady(MainScripter mainscripter) {
        this.mainscripter = mainscripter;
    }

    public void GameReady(Player p) {
        if (p.isOp()) {
            if (MainScripter.Scenario == MainScripter.ScriptStatus.NoPlay) {
                MainScripter.Scenario = MainScripter.ScriptStatus.ScriptStart;
                Bukkit.broadcastMessage(ChatColor.YELLOW + "(!)잠시 후 게임을 시작합니다.");
                this.stimer.StartTimer(9);
            } else {
                p.sendMessage(ChatColor.RED + "(!)이미 게임이 시작되어있습니다.");
            }
        } else
            p.sendMessage(ChatColor.RED + "(!)당신은 권한이 없습니다.");
    }

    public void GameReadyStop() {
        this.stimer.StopTimer();
    }

    public final class S_ScriptTimer
            extends TimerBase
    {
        public S_ScriptTimer() {}

        public void EventStartTimer() {}

        public void EventRunningTimer(int count)
        {
            switch (count) {
                case 0:
                    Bukkit.broadcastMessage(ChatColor.AQUA + "인식된 플레이어 목록");
                    Bukkit.broadcastMessage(ChatColor.GOLD + "==========");
                    Collection<? extends Player> templist = Bukkit.getOnlinePlayers();
                    Player[] playersArray = new Player[templist.size()];
                    templist.toArray(playersArray);

                    for (int l = 0; l < playersArray.length; l++) {
                        Player player = playersArray[l];
                        if (!S_GameReady.this.mainscripter.ExceptionList.contains(player)) {
                            if (l < AbilityBase.GetAbilityCount()) {
                                MainScripter.PlayerList.add(player);
                                Bukkit.broadcastMessage(String.format(
                                        ChatColor.GREEN + "%d. " + ChatColor.WHITE + "%s",
                                        l, player.getName()));
                            } else {
                                Bukkit.broadcastMessage(String.format(
                                        ChatColor.RED + "%d. %s (Error)",
                                        l, player.getName()));
                            }
                        }
                    }

                    S_GameReady.this.peoplecount = playersArray.length - S_GameReady.this.mainscripter.ExceptionList.size();

                    if (S_GameReady.this.peoplecount <= AbilityBase.GetAbilityCount()) {
                        Bukkit.broadcastMessage(String.format(
                                ChatColor.YELLOW + "총 인원수 : %d명 ",
                                S_GameReady.this.peoplecount));
                    } else {
                        Bukkit.broadcastMessage(String.format(
                                ChatColor.RED + "총 인원수 : %d명 ",
                                S_GameReady.this.peoplecount));
                        Bukkit.broadcastMessage("인원이 능력의 갯수보다 많습니다. Error 처리된분들은 능력을");
                        Bukkit.broadcastMessage("받을수 없으며 모든 게임 진행 대상에서 제외됩니다.");
                    }
                    Bukkit.broadcastMessage(ChatColor.GOLD + "==========");
                    if (MainScripter.PlayerList.size() == 0) {
                        Bukkit.broadcastMessage(ChatColor.RED +
                                "경고, 실질 플레이어가 없습니다. 게임 강제 종료.");
                        MainScripter.Scenario = MainScripter.ScriptStatus.NoPlay;
                        S_GameReady.this.stimer.StopTimer();
                        Bukkit.broadcastMessage(ChatColor.GRAY + "모든 설정이 취소됩니다.");
                        MainScripter.PlayerList.clear();
                        return;
                    }
                    break;

                case 3:
                    Bukkit.broadcastMessage(String.format(ChatColor.DARK_RED +
                            "Physical Fighters", new Object[0]));

                    Bukkit.broadcastMessage(String.format(
                            ChatColor.RED + "VER. %d", new Object[] {
                                    Integer.valueOf(PhysicalFighters.BuildNumber) }));
                    Bukkit.broadcastMessage(ChatColor.GREEN + "제작 : " +
                            ChatColor.WHITE + "염료");
                    break;
                case 7:
                    if (!PhysicalFighters.NoAbilitySetting) {
                        Bukkit.broadcastMessage(ChatColor.GRAY +
                                "능력 설정 초기화 및 추첨 준비...");
                        for (AbilityBase ab : AbilityList.AbilityList) {
                            ab.SetRunAbility(false);
                            ab.SetPlayer(null, false);
                        }
                    } else {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "능력을 추첨하지 않습니다.");
                        Bukkit.broadcastMessage("시작전에 능력이 이미 부여되었다면 보존됩니다.");
                        S_GameReady.this.mainscripter.OKSign.clear();
                        for (Player pl : MainScripter.PlayerList) {
                            S_GameReady.this.mainscripter.OKSign.add(pl);
                        }
                        for (AbilityBase ab : AbilityList.AbilityList) {
                            ab.SetRunAbility(true);
                        }
                        S_GameReady.this.mainscripter.s_GameStart.GameStart();
                        StopTimer();
                    }
                    break;
                case 9:
                    MainScripter.Scenario = MainScripter.ScriptStatus.AbilitySelect;

                    if (S_GameReady.this.peoplecount < AbilityBase.GetAbilityCount()) {
                        for (Player p : MainScripter.PlayerList)
                            if (RandomAbility(p) == null) {
                                p.sendMessage(ChatColor.RED + "경고, 능력의 갯수가 부족합니다.");
                            } else {
                                p.sendMessage(ChatColor.GRAY + "(!)/va help " +
                                        ChatColor.WHITE + "= 능력 확인");
                                p.sendMessage(ChatColor.YELLOW + "(!)/va yes " +
                                        ChatColor.WHITE + "= 능력 사용.");
                                p.sendMessage(ChatColor.YELLOW + "(!)/va no " +
                                        ChatColor.WHITE + "= 능력 재추첨.(1회)");
                            }
                        for (Player p : S_GameReady.this.mainscripter.ExceptionList) {
                            p.sendMessage(ChatColor.GREEN + "능력 추첨중입니다");
                        }

                        S_GameReady.this.mainscripter.s_GameWarnning.GameWarnningStart();
                    } else {
                        Bukkit.broadcastMessage(ChatColor.AQUA +
                                "능력 갯수보다 플레이어 수가 같거나 많으므로 즉시 확정됩니다.");
                        for (Player p : MainScripter.PlayerList)
                            if (RandomAbility(p) == null) {
                                p.sendMessage(ChatColor.RED + "경고, 능력의 갯수가 부족합니다.");
                            } else {
                                S_GameReady.this.mainscripter.OKSign.add(p);
                                p.sendMessage(ChatColor.GREEN +
                                        "당신에게 능력이 부여되었습니다. " + ChatColor.YELLOW +
                                        "/va help" + ChatColor.WHITE + "로 확인하세요.");
                            }
                        for (Player p : S_GameReady.this.mainscripter.ExceptionList) {
                            p.sendMessage(ChatColor.GREEN + "능력 추첨 완료");
                        }
                        S_GameReady.this.mainscripter.s_GameStart.GameStart();
                    }


                    break;
            }

        }



        public void EventEndTimer() {}


        private AbilityBase RandomAbility(Player p)
        {
            ArrayList<AbilityBase> Alist = new ArrayList();
            Random r = new Random();
            int Findex = r.nextInt(AbilityList.AbilityList.size() - 1);
            int saveindex; if (Findex == 0) {
            saveindex = AbilityList.AbilityList.size();
        } else {
            saveindex = Findex - 1;
        }
            for (int i = 0; i < AbilityList.AbilityList.size(); i++) {
                if ((((AbilityBase)AbilityList.AbilityList.get(Findex)).GetPlayer() == null) && (
                        (MainScripter.PlayerList.size() > 6) ||
                                (AbilityList.AbilityList.get(Findex) != AbilityList.mirroring)))
                {
                    Alist.add((AbilityBase)AbilityList.AbilityList.get(Findex));
                }

                Findex++;

                if (Findex == saveindex)
                    break;
                if (Findex == AbilityList.AbilityList.size())
                    Findex = 0;
            }
            if (Alist.size() == 0) {
                return null;
            }
            if (Alist.size() == 1) {
                ((AbilityBase)Alist.get(0)).SetPlayer(p, false);
                return (AbilityBase)Alist.get(0);
            }

            int ran2 = r.nextInt(Alist.size() - 1);
            ((AbilityBase)Alist.get(ran2)).SetPlayer(p, false);
            return (AbilityBase)Alist.get(ran2);
        }
    }
}
