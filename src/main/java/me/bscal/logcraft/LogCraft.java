package me.bscal.logcraft;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

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

	public static void Init(Plugin pl)
	{
		CS = pl.getServer().getConsoleSender();

		FMT_NAME = MessageFormat.format("{1}[{2}{0}{1}]: ", pl.getName(), ChatColor.DARK_PURPLE,
				ChatColor.LIGHT_PURPLE);
		INFO = ChatColor.YELLOW;
		ERR = ChatColor.RED;
	}

	public static void Log(String msg)
	{
		CS.sendMessage(FMT_NAME + INFO + msg);
	}

	public static void Log(Object... msg)
	{
		Log(StringUtils.join(msg, ", "));
	}

	public static void LogFmt(String title, Object... msg)
	{
		CS.sendMessage(FMT_NAME + INFO + "========= Printing " + title + " =========\n\t");
		CS.sendMessage(FMT_NAME + INFO + StringUtils.join(msg, ",\n\t"));
	}

	public static void LogTable(String[] keys, Object[] values)
	{
		LogTable(32, keys, values);
	}

	public static void LogTable(int width, Object[] keys, Object[] values)
	{
		LogTable('*', '*', '*', width, "", "", keys, values);
	}

	public static void LogTable(int width, String keyTitle, String valTitle, Object[] keys, Object[] values)
	{
		LogTable('*', '*', '*', width, keyTitle, valTitle, keys, values);
	}

	public static void LogTable(char border, char topBorder, char edge, int width, String keyTitle, String valTitle,
			Object[] keys, Object[] values)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(CreateSpacer(topBorder, edge, width));
		sb.append(FmtLine(border, border, width, keyTitle, valTitle));
		sb.append(CreateSpacer(topBorder, edge, width));
		for (int i = 0; i < keys.length; i++)
		{
			String key = "NULL";
			String val = "NULL";

			if (keys[i] != null || keys[i] instanceof String && !((String) keys[i]).isBlank())
				key = keys[i].toString();

			if (i < values.length || values[i] != null)
				val = values[i].toString();

			sb.append(FmtLine(width, key, val));
		}
		sb.append(CreateSpacer(topBorder, edge, width));
	}

	private static String CreateSpacer(char chr, char edge, int width)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(edge);
		for (int i = 0; i < width - 2; i++)
		{
			sb.append(chr);
		}
		sb.append(edge);
		return sb.toString();
	}

	private static String FmtLine(int width, String key, String val)
	{
		return FmtLine('*', '*', width, key, val);
	}

	private static String FmtLine(char border, char mid, int width, String key, String val)
	{
		int trueWidth = width - 7;
		StringBuilder sb = new StringBuilder();
		sb.append(border + " ");
		sb.append(FitStr(trueWidth, key));
		sb.append(" " + mid + " ");
		sb.append(FitStr(trueWidth, val));
		sb.append(" " + border);
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

	public static void LogErr(String msg)
	{
		CS.sendMessage(FMT_NAME + ERR + msg);
	}

	public static void LogErr(Object... msg)
	{
		Log(StringUtils.join(msg, ", "));
	}

	public static void LogArray(Object[] array)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Printing Array (Size: " + array.length + ") [");
		for (int i = 0; i < array.length; i++)
		{
			if (array[i].getClass().isArray())
				LogArray((Object[]) array[i]);
			else if (array[i] instanceof List)
				LogArray((List<Object>) array[i]);
			sb.append(array[i]);
			sb.append(", ");
		}
		sb.append(']');
	}

	public static <T> void LogArray(List<T> array)
	{
		LogArray(array.toArray());
	}

	public static <K, V> void LogMap(Map<K, V> map)
	{
		LogMap(map, 1);
	}

	private static <K, V> void LogMap(Map<K, V> map, int sub)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (var pair : map.entrySet())
		{
			sb.append('\n');
			for (int i = 0; i < sub; i++)
				sb.append('\t');

			if (pair.getKey().getClass().isArray())
				LogArray((Object[]) pair.getKey());
			else if (pair.getKey() instanceof List)
				LogArray((List<Object>) pair.getKey());
			else if (pair.getKey() instanceof Map)
				LogMap((Map<Object, Object>) pair.getKey(), sub++);
			else
				sb.append(pair.getKey());

			sb.append('=');

			if (pair.getValue().getClass().isArray())
				LogArray((Object[]) pair.getValue());
			else if (pair.getValue() instanceof List)
				LogArray((List<Object>) pair.getValue());
			else if (pair.getValue() instanceof Map)
				LogMap((Map<Object, Object>) pair.getValue(), sub++);
			else
				sb.append(pair.getValue());
		}
		sb.append(']');
		Log(sb.toString());
	}

	public <K, V> void LogMapAsTable(Map<K, V> map)
	{
		LogTable(48, "Keys", "Values", map.keySet().toArray(), map.values().toArray());
	}
}
