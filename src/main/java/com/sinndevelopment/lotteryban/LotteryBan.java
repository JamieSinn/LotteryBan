package com.sinndevelopment.lotteryban;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import net.erbros.lottery.events.LotteryBuyTicketEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LotteryBan extends JavaPlugin implements Listener
{
    private IEssentials ess;

    @Override
    public void onEnable()
    {
        hookEssentials();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLotteryTicketBought(LotteryBuyTicketEvent e)
    {
        Player player = e.getPlayer();
        if (player.hasMetadata("LotteryBanned") && player.getMetadata("LotteryBanned").get(0).asBoolean())
        {
            player.sendMessage(ChatColor.RED + "You are banned from Lottery.");
            e.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
    {
        if(args.length == 1)
        {
            Player target = getServer().getPlayer(args[0]);
            User offlineTarget = ess.getOfflineUser(args[0]);
            if(target != null)
            {
                if (target.hasMetadata("LotteryBanned") && target.getMetadata("LotteryBanned").get(0).asBoolean())
                {
                    target.setMetadata("LotteryBanned", new FixedMetadataValue(this, false));
                    sender.sendMessage(ChatColor.GOLD + target.getName() + " has been Lottery Unbanned");
                    getServer().getLogger().info(target.getName() + " has been Lottery Unbanned by " + sender.getName());
                } else {
                    target.setMetadata("LotteryBanned", new FixedMetadataValue(this, true));
                    sender.sendMessage(ChatColor.GOLD + target.getName() + " has been Lottery Banned");
                    getServer().getLogger().info(target.getName() + " has been Lottery Banned by " + sender.getName());
                }
            }

            if(offlineTarget != null)
            {
                if (offlineTarget.getBase().hasMetadata("LotteryBanned") && offlineTarget.getBase().getMetadata("LotteryBanned").get(0).asBoolean())
                {
                    offlineTarget.getBase().setMetadata("LotteryBanned", new FixedMetadataValue(this, false));
                    sender.sendMessage(ChatColor.GOLD + offlineTarget.getName() + " has been Lottery Unbanned");
                    getServer().getLogger().info(offlineTarget.getName() + " has been Lottery Unbanned by " + sender.getName());
                }
                else
                {
                    offlineTarget.getBase().setMetadata("LotteryBanned", new FixedMetadataValue(this, true));
                    sender.sendMessage(ChatColor.GOLD + offlineTarget.getName() + " has been Lottery Banned");
                    getServer().getLogger().info(offlineTarget.getName() + " has been Lottery Banned by " + sender.getName());
                }
            }
            sender.sendMessage(ChatColor.RED+ "Invalid target player.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Syntax: /lottoban <player>");
        return true;
    }

    public boolean hookEssentials()
    {
        final PluginManager pm = this.getServer().getPluginManager();
        final Plugin essPlugin = pm.getPlugin("Essentials");
        if (essPlugin == null || !essPlugin.isEnabled())
        {
            this.setEnabled(false);
            this.getLogger().warning("Couldn't hook Essentials");
            return false;
        }
        ess = (IEssentials) essPlugin;
        this.getLogger().info("Essentials hooked.");
        return true;
    }
}
