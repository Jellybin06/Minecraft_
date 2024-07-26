package org.jeongbin.physicalfighters.MinerModule;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract interface CommandInterface
{
  public abstract boolean onCommandEvent(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString);
}
