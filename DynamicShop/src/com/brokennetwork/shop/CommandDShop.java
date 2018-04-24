package com.brokennetwork.shop;

import org.bukkit.Material;

import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import net.ess3.api.Economy;

@SuppressWarnings("unused")
public class CommandDShop implements CommandExecutor {

	// This method is called, when somebody uses our command
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = ((Player) sender);
		Block selected = player.getTargetBlock(null, 15);
		if (selected.getType().equals(Material.SIGN) || selected.getType().equals(Material.SIGN_POST)
				|| selected.getType().equals(Material.WALL_SIGN)) {
			try {
				if (!args[0].equals(null)) {

					Sign sign = (Sign) player.getTargetBlock(null, 15).getState();
					String anzahl = args[0];
					String verkaufsPreis = args[1];
					String verkaufsPreisdiff = args[2];
					String material = args[3];
					String p = args[4];
					String kaufPreis = String
							.valueOf(Integer.valueOf(verkaufsPreis) + Integer.valueOf(verkaufsPreisdiff));

					if (!anzahl.equals(null) && !verkaufsPreis.equals(null) && !kaufPreis.equals(null)
							&& !material.equals(null) && !p.equals(null)) {
						if (Integer.valueOf(anzahl) <= 64) {
							// Material das verkauft wird
							Material tosell = Material.getMaterial(Integer.valueOf(material));
							// Fehler validierung
							try {
								// Fehler abfrage
								if (!tosell.equals(null)) {
								}
							} catch (NullPointerException b) {
								player.sendMessage(
										String.format("%sDie Angegebene ID existiert nicht!", ChatColor.RED));
								return false;
							}

							// Schild Editieren
							sign.setLine(0, ChatColor.DARK_RED + "[DShop]");
							sign.setLine(1, ChatColor.DARK_BLUE + anzahl);
							sign.setLine(2, ChatColor.DARK_GREEN + verkaufsPreis + ChatColor.BLACK + "|"
									+ ChatColor.DARK_RED + kaufPreis + ChatColor.BLACK + "|" + p + "%");
							// Abfrage nach Item names Länge
							if (tosell.name().length() <= 15) {
								sign.setLine(3, ChatColor.DARK_GRAY + tosell.name());

								// Sonst anzeige als nummer
							} else {
								sign.setLine(3, ChatColor.DARK_GRAY + material);
							}

							sign.update();
							player.sendMessage(String.format("%sShop Schild Erfolgreich Erstellt", ChatColor.GREEN));
						} else {
							player.sendMessage(String.format("%sMaximale Anzahl sind 64!", ChatColor.RED));
							return false;
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException b) {
				player.sendMessage("/dshop Anzahl | SellPreis | Differenz | Item ID | %");
				return false;
			}
		}
		return true;
	}
}
