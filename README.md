# LogCraft

[![](https://jitpack.io/v/bscal/LogCraft.svg)](https://jitpack.io/#bscal/LogCraft)

Very simple, basic logging library for Spigot Minecraft plugins.

Built with PaperSpigot-1.16.4 and Java 11.

This is not a plugin, so you will need to shade it into your plugin. It is quite small.

I'm not sure how useful this library is but I wanted to make it to standardize logging and reduce copying code for plugins
I make. Not really a finished product or well documented.

### Usage

1. Added the library to your pom.
2. Initialize the library with your plugin.
3. Done.

All functions are in LogCraft class.

Example:
```
public class Main extends JavaPlugin
{
    public void onEnable()
    {
        LogCraft.Init(this);
		
        LogCraft.Log("Testing 123 Testing", getName(), health);
        LogCraft.LogErr("Errored...");
    }
}
```



### Maven
```
<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.bscal</groupId>
	<artifactId>LogCraft</artifactId>
	<version>TAG <-- Replace this</version>
</dependency>
```
