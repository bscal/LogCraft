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
		Log(CreateSpacer(topBorder, edge, width));
		Log(FmtLine(border, border, width, keyTitle, valTitle));
		Log(CreateSpacer(topBorder, edge, width));
		for (int i = 0; i < keys.length; i++)
		{
			String key = "NULL";
			String val = "NULL";

			if (keys[i] != null || keys[i] instanceof String && !((String) keys[i]).isBlank())
				key = keys[i].toString();

			if (i < values.length || values[i] != null)
				val = values[i].toString();

			Log(FmtLine(width, key, val));
		}
		Log(CreateSpacer(topBorder, edge, width));
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

	@SuppressWarnings("unchecked")
	private static String LogArray(Object[] array, boolean root)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Printing Array (Size: " + array.length + ") [");
		for (int i = 0; i < array.length; i++)
		{
			if (array[i].getClass().isArray())
				sb.append(LogArray((Object[]) array[i], false));
			else if (array[i] instanceof List)
				sb.append(LogArray(((List<Object>) array[i]).toArray(), false));
			sb.append(array[i]);
			sb.append(", ");
		}
		sb.append(']');
		String s = sb.toString();
		if (root)
			Log(s);
		return s;
	}

	public static void LogArray(Object[] array)
	{
		LogArray(array, true);
	}
	
	public static <T> void LogArray(List<T> array)
	{
		LogArray(array.toArray(), true);
	}

	public static <K, V> void LogMap(Map<K, V> map)
	{
		LogMap(map, 1);
	}

	@SuppressWarnings("unchecked")
	private static <K, V> String LogMap(Map<K, V> map, int sub)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t[");
		for (var pair : map.entrySet())
		{
			sb.append('\n');
			for (int i = 0; i < sub; i++)
				sb.append('\t');

			if (pair.getKey().getClass().isArray())
				sb.append(LogArray((Object[]) pair.getKey(), false));
			else if (pair.getKey() instanceof List)
				sb.append(LogArray(((List<Object>) pair.getKey()).toArray(), false));
			else if (pair.getKey() instanceof Map)
				sb.append(LogMap((Map<Object, Object>) pair.getKey(), sub++));
			else
				sb.append(pair.getKey());

			sb.append(" = ");

			if (pair.getValue().getClass().isArray())
				sb.append(LogArray((Object[]) pair.getValue(), false));
			else if (pair.getValue() instanceof List)
				sb.append(LogArray(((List<Object>) pair.getValue()).toArray(), false));
			else if (pair.getValue() instanceof Map)
				sb.append(LogMap((Map<Object, Object>) pair.getValue(), sub++));
			else
				sb.append(pair.getValue());
		}
		sb.append("\n\t]");
		String s = sb.toString();
		if (sub == 1)
			Log(s);
		return s;
	}

	public <K, V> void LogMapAsTable(Map<K, V> map)
	{
		LogTable(48, "Keys", "Values", map.keySet().toArray(), map.values().toArray());
	}
}
