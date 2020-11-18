package me.bscal.logcraft;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

public class LogCraft
{

	static ConsoleCommandSender CS;

	static String FMT_NAME;

	static ChatColor INFO;
	static ChatColor ERR;

	public static void Init (Plugin pl)
	{
		CS = pl.getServer().getConsoleSender();
		
		FMT_NAME = MessageFormat.format("{1}[{2}{0}{1}]: ", pl.getName(), ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);
		INFO = ChatColor.YELLOW;
		ERR = ChatColor.RED;
	}

	public static void Log (String msg)
	{
		CS.sendMessage(FMT_NAME + INFO + msg);
	}

	public static void Log (Object... msg)
	{
		Log(StringUtils.join(msg, ", "));
	}

	public static void LogFmt (String title, Object... msg)
	{
		CS.sendMessage(FMT_NAME + INFO + "========= Printing " + title + " =========\n\t");
		CS.sendMessage(FMT_NAME + INFO + StringUtils.join(msg, ",\n\t"));
	}

	public static void LogTable (int width, String[] keys, Object[] values)
	{
		final char SPACER = '-';
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(CreateSpacer(width, SPACER));
		for (int i = 0; i < keys.length; i++) {
			String key = "NULL";
			String val = "NULL";
			
			if (keys[i] != null || keys[i].isBlank())
				key = keys[i];
			
			if (i < values.length || values[i] != null)
				val = values[i].toString();
			
			sb.append(FmtLine(width, key, val));
		}
		sb.append(CreateSpacer(width, SPACER));
	}

	private static String CreateSpacer (int width, char chr)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('+');
		for (int i = 0; i < width - 2; i++)
		{
			sb.append(chr);
		}
		sb.append('+');
		return sb.toString();
	}

	private static String FmtLine (int width, String key, String val)
	{
		int trueWidth = width - 7;
		StringBuilder sb = new StringBuilder();
		sb.append("| ");
		sb.append(FitStr(trueWidth, key));
		sb.append(" | ");
		sb.append(FitStr(trueWidth, val));
		sb.append(" |");
		return sb.toString();
	}

	private static String FitStr(int width, String str)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++)
		{
			if (i <= str.length())
				sb.append(str.charAt(i));
			else
				sb.append(" ");
		}
		return sb.toString();
	}

	public static void LogErr (String msg)
	{
		CS.sendMessage(FMT_NAME + ERR + msg);
	}

	public static void LogErr (Object... msg)
	{
		Log(StringUtils.join(msg, ", "));
	}

}
