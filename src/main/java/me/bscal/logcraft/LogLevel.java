package me.bscal.logcraft;

public enum LogLevel
{

	NONE(0), INFO_ONLY(1), DEVELOPER(2);

	public final int id;

	private LogLevel(final int id)
	{
		this.id = id;
	}

	public static boolean IsLevel(int curLvl, int reqLvl)
	{
		return curLvl >= reqLvl;
	}

	public static boolean IsLevel(LogLevel curLvl, LogLevel reqLvl)
	{
		return IsLevel(curLvl.id, reqLvl.id);
	}

	public static LogLevel IntToLevel(int lvl)
	{
		return values()[Math.max(0, Math.min(lvl, values().length))];
	}

}
