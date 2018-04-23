package com.brokennetwork.shop;

import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class CreateSign extends JavaPlugin implements Listener {
	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		
		// Initilizen vom event
		getServer().getPluginManager().registerEvents(this, this);
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {

	}

	// SchildPlatzieren event
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void signPlace(SignChangeEvent e) {

		// Checken of es ein DynamichShop Schild ist
		if (e.getLine(0).contains("[Dshop]")) {
			
			// Ersetzten des DynamicShop Schildes
			e.setLine(0, ChatColor.DARK_RED + "[Shop]");
			
			// Wie viele Einheiten MAX 64 ( 1 STACK )
			if (Integer.valueOf(e.getLine(1)) <= 64) {
				
				// Replacen der Angabe
				e.setLine(1, ChatColor.DARK_BLUE + e.getLine(1));
				
				// Fehler Meldung wenn Anzahl > 64
			} else {
				e.getBlock().breakNaturally();
				e.getPlayer().sendMessage(String.format("%sFalsche Angabe: Max 64", ChatColor.RED));
			}

			// Buy / Sell (Preis)
			String buy = null;
			String sell = null;
			String buy_sell = e.getLine(2);
			// Splitten der Angaben
			String[] parts = buy_sell.split(" ");
			
			// Fehler validierung
			try {
				
				// Ersetzten der Angaben
				buy = parts[0];
				sell = parts[1];
				e.setLine(2, ChatColor.DARK_RED + buy + ChatColor.BLACK + " | " + ChatColor.DARK_GREEN + sell);

			} catch (ArrayIndexOutOfBoundsException b) {

				e.getBlock().breakNaturally();
				e.getPlayer().sendMessage(String.format("%sFalsche Angaben Vorgabe: Buy Sell", ChatColor.RED));

			}

			
			// Objekt das verkauft wird
			Material tosell = null;
			try {
				
				// Objekt als variable
				tosell = Material.getMaterial(Integer.valueOf(e.getLine(3)));
				if (!tosell.equals(null)) {
					
					// checken ob der Objekt Name kleiner als 15 Zeichen ist
					if (tosell.name().length() <= 15) {
					e.setLine(3, ChatColor.DARK_GRAY + tosell.name());
					
					// Sonst anzeige als nummer
					} else {
						e.setLine(3, ChatColor.DARK_GRAY + e.getLine(3));	
					}
				}
				// Fehler meldung
			} catch (NullPointerException b) {
				e.getBlock().breakNaturally();
				e.getPlayer().sendMessage(String.format("%sNicht Existierendes Item", ChatColor.RED));
			}
			
			// Erfolgs meldung
			e.getPlayer().sendMessage(String.format("%sShop Schild Erfolgreich Erstellt", ChatColor.GREEN));
		}
	}

}