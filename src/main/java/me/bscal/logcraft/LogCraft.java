package me.bscal.logcraft;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class LogCraft
{

	public final LogLevel level;

	private final ConsoleCommandSender m_cs;

	private final String m_formattedName;
	private final ChatColor m_infoColor;
	private final ChatColor m_errColor;

	public LogCraft(final Plugin plugin, final LogLevel level)
	{
		m_cs = plugin.getServer().getConsoleSender();

		m_formattedName = MessageFormat.format("{1}[{2}{0}{1}]: ", plugin.getName(), ChatColor.DARK_PURPLE,
				ChatColor.LIGHT_PURPLE);

		m_infoColor = ChatColor.YELLOW;
		m_errColor = ChatColor.RED;

		this.level = level;
	}

	public boolean IsLevel(LogLevel lvl) {
		return level.id >= lvl.id;
	}

	public void SendToConsole(String msg, ChatColor color)
	{
		m_cs.sendMessage(m_formattedName + color + msg);
	}

	public void Log(String msg)
	{
		SendToConsole(msg, m_infoColor);
	}

	public void Log(Object... msg)
	{
		Log(StringUtils.join(msg, ", "));
	}

	public void LogFormat(String pattern, Object... params)
	{
		Log(MessageFormat.format(pattern, params));
	}

	public void LogNewLine(String title, Object... msg)
	{
		SendToConsole("========= Printing " + title + " =========\n\t", m_infoColor);
		SendToConsole(StringUtils.join(msg, ",\n\t"), m_infoColor);
	}

	public void LogFmtErr(String title, Object... msg)
	{
		SendToConsole("========= Printing " + title + " =========\n\t", m_errColor);
		SendToConsole(StringUtils.join(msg, ",\n\t"), m_errColor);
	}

	public void LogTable(String[] keys, Object[] values)
	{
		LogTable(32, keys, values);
	}

	public void LogTable(int width, Object[] keys, Object[] values)
	{
		LogTable('*', '*', '*', width, "", "", keys, values);
	}

	public void LogTable(int width, String keyTitle, String valTitle, Object[] keys,
			Object[] values)
	{
		LogTable('*', '*', '*', width, keyTitle, valTitle, keys, values);
	}

	public void LogTable(char border, char topBorder, char edge, int width, String keyTitle,
			String valTitle, Object[] keys, Object[] values)
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

			if (i < values.length && values[i] != null)
				val = values[i].toString();

			Log(FmtLine(width, key, val));
		}
		Log(CreateSpacer(topBorder, edge, width));
	}

	private String CreateSpacer(char chr, char edge, int width)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(edge);
		sb.append(String.valueOf(chr).repeat(Math.max(0, width - 2)));
		sb.append(edge);
		return sb.toString();
	}

	private String FmtLine(int width, String key, String val)
	{
		return FmtLine('*', '*', width, key, val);
	}

	private String FmtLine(char border, char mid, int width, String key, String val)
	{
		int trueWidth = width - 7;
		StringBuilder sb = new StringBuilder();
		sb.append(border).append(' ');
		sb.append(FitStr(trueWidth, key));
		sb.append(' ').append(mid).append(' ');
		sb.append(FitStr(trueWidth, val));
		sb.append(' ').append(border);
		return sb.toString();
	}

	private String FitStr(int width, String str)
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

	public void LogErr(String msg)
	{
		SendToConsole(msg, m_errColor);
	}

	public void LogErr(Object... msg)
	{
		Log(StringUtils.join(msg, ", "));
	}

	public void LogErrFormat(String pattern, Object... params)
	{
		LogErr(MessageFormat.format(pattern, params));
	}

	@SuppressWarnings("unchecked")
	private String LogArray(Object[] array, boolean root)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Printing Array (Size: ").append(array.length).append(") [");
		for (Object o : array)
		{
			if (o.getClass().isArray())
				sb.append(LogArray((Object[]) o, false));
			else if (o instanceof List)
				sb.append(LogArray(((List<Object>) o).toArray(), false));
			sb.append(o);
			sb.append(", ");
		}
		sb.append(']');
		String s = sb.toString();
		if (root)
			Log(s);
		return s;
	}

	public void LogArray(Object[] array)
	{
		LogArray(array, true);
	}

	public <T> void LogArray(List<T> array)
	{
		LogArray(array.toArray(), true);
	}

	public <K, V> void LogMap(Map<K, V> map)
	{
		LogMap(map, 1);
	}

	@SuppressWarnings("unchecked")
	private <K, V> String LogMap(Map<K, V> map, int sub)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t[");
		for (var pair : map.entrySet())
		{
			sb.append('\n');
			sb.append("\t".repeat(Math.max(0, sub)));

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

	public void LogObject(Object obj)
	{
		Log("Logging Object: {0} | {1}", obj.getClass().getSimpleName(), obj);
		for (var field : obj.getClass().getFields())
		{
			try
			{
				Log("{0} = {1}", field.getName(), field.get(obj));
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
}
