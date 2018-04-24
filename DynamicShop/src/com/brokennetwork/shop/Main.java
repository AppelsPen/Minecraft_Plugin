package com.brokennetwork.shop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	// Fired when plugin is first enabled
	@Override
	public void onEnable() {

		// Initilizen vom event
		this.getCommand("dshop").setExecutor(new CommandDShop());
		Bukkit.getServer().getPluginManager().registerEvents(new Event(), this);
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {

	}
}