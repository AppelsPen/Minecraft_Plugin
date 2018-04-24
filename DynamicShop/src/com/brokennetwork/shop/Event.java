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
import org.bukkit.event.block.BlockDamageEvent;
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
public class Event implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void interactSell(PlayerInteractEvent event) throws NoLoanPermittedException, UserDoesNotExistException {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block clickedBlock = event.getClickedBlock();

		// Stop accidental destruction of our game signs!
		// Even stops people with Gamemode 1!!
		if (action == Action.LEFT_CLICK_BLOCK) {
			if (!(clickedBlock.getType() == Material.SIGN || clickedBlock.getType() == Material.SIGN_POST
					|| clickedBlock.getType() == Material.WALL_SIGN))
				return;

			// Sign kriegen
			Sign sign = (Sign) clickedBlock.getState();

			// Sign Auslesen
			String line1 = sign.getLine(0);
			if (line1.contains("DShop")) {

				// Preise und prozente kriegen
				String buy_sell = sign.getLine(2);

				// Cholor strippen
				ChatColor.stripColor(buy_sell);

				// Replacen von divider
				buy_sell = buy_sell.replace('|', ' ');

				// Splitten der Angaben
				String[] parts = buy_sell.split(" ");

				// Auslesen der Werte
				String value = ChatColor.stripColor(parts[0]);
				value = value.replace(',', '.');
				String valueb = ChatColor.stripColor(parts[1]);
				valueb = valueb.replace(',', '.');
				String valuep = ChatColor.stripColor(parts[2]);
				valuep = valuep.replace("%", "");

				// Schreiben der Werte in Doubles
				double buy = Double.valueOf(valueb);
				double sell = Double.valueOf(value);
				int anzahl = Integer.valueOf(ChatColor.stripColor(sign.getLine(1)));

				// Material Kriegen
				Material tosell = Material.getMaterial(ChatColor.stripColor(sign.getLine(3)));

				// Material aus inventar abziehen
				if (player.getInventory().contains(tosell, anzahl)) {
					player.getInventory().removeItem(new ItemStack(tosell, anzahl));

					// Geld geben
					Economy.add(player.getName(), sell);
					player.getPlayer()
							.sendMessage(String.format(
									"%sDu hast " + anzahl + " " + tosell.name() + " für " + sell + "$ verkauft",
									ChatColor.GREEN));

					// Neuen Wert des Gegenstandes Ermitteln
					double diff = buy - sell;
					double prozent = 100 - Integer.valueOf(valuep);
					double newsell = (sell / 100.0) * prozent;
					buy = newsell + diff;

					// Minimal Wert fürs verkaufen
					if (newsell <= 10) {
						newsell = 10;
					}

					// Minimal Wert fürs kaufen
					if (buy <= newsell) {
						buy = newsell;
					}

					// Runden der doubles
					NumberFormat n = NumberFormat.getInstance();
					n.setMaximumFractionDigits(2);

					// Updaten des Schildes
					sign.setLine(2, ChatColor.DARK_GREEN + "" + n.format(newsell) + ChatColor.BLACK + "|"
							+ ChatColor.DARK_RED + n.format(buy) + ChatColor.BLACK + "|" + valuep + "%");
					sign.update();
				}

				event.setCancelled(true);

			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void interactBuy(PlayerInteractEvent event) throws NoLoanPermittedException, UserDoesNotExistException {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block clickedBlock = event.getClickedBlock();

		// Stop accidental destruction of our game signs!
		// Even stops people with Gamemode 1!!
		if (action == Action.RIGHT_CLICK_BLOCK) {
			if (!(clickedBlock.getType() == Material.SIGN || clickedBlock.getType() == Material.SIGN_POST
					|| clickedBlock.getType() == Material.WALL_SIGN))
				return;

			// Sign kriegen
			Sign sign = (Sign) clickedBlock.getState();

			// Sign Auslesen
			String line1 = sign.getLine(0);
			if (line1.contains("DShop")) {

				// Preise und prozente kriegen
				String buy_sell = sign.getLine(2);

				// Cholor strippen
				ChatColor.stripColor(buy_sell);

				// Replacen von divider
				buy_sell = buy_sell.replace('|', ' ');

				// Splitten der Angaben
				String[] parts = buy_sell.split(" ");

				// Auslesen der Werte
				String value = ChatColor.stripColor(parts[0]);
				value = value.replace(',', '.');
				String valueb = ChatColor.stripColor(parts[1]);
				valueb = valueb.replace(',', '.');
				String valuep = ChatColor.stripColor(parts[2]);
				valuep = valuep.replace("%", "");
				// Schreiben der Werte in Doubles
				double buy = Double.valueOf(valueb);
				double sell = Double.valueOf(value);
				int anzahl = Integer.valueOf(ChatColor.stripColor(sign.getLine(1)));

				// Material Kriegen
				Material tosell = Material.getMaterial(ChatColor.stripColor(sign.getLine(3)));

				// Material aus inventar abziehen
				if (Economy.getMoney(player.getName()) >= buy) {
					player.getInventory().addItem(new ItemStack(tosell, anzahl));

					// Geld geben
					Economy.subtract(player.getName(), buy);
					player.getPlayer()
							.sendMessage(String.format(
									"%sDu hast " + anzahl + " " + tosell.name() + " für " + buy + "$ gekauft",
									ChatColor.GREEN));

					// Neuen Wert des Gegenstandes Ermitteln
					double diff = buy - sell;
					double prozent = 100 + Integer.valueOf(valuep);
					double newsell = (sell / 100.0) * prozent;
					buy = newsell + diff;

					// Minimal Wert fürs verkaufen
					if (newsell <= 10) {
						newsell = 10;
					}

					// Minimal Wert fürs kaufen
					if (buy <= newsell) {
						buy = newsell;
					}

					// Runden der doubles
					NumberFormat n = NumberFormat.getInstance();
					n.setMaximumFractionDigits(2);

					// Updaten des Schildes
					sign.setLine(2, ChatColor.DARK_GREEN + "" + n.format(newsell) + ChatColor.BLACK + "|"
							+ ChatColor.DARK_RED + n.format(buy) + ChatColor.BLACK + "|" + valuep + "%");
					sign.update();
				} else {
					player.getPlayer().sendMessage(String.format("%sDu hast nicht genug Geld!", ChatColor.RED));

				}

				event.setCancelled(true);

			}
		}
	}

}