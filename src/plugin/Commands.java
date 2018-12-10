package plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        int[][] maze = {
                {1, 1, 1, 1, 1},
                {0, 0, 1, 0, 1},
                {1, 1, 1, 0, 1},
                {0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1},
        };

        Location start = ((Player) sender).getLocation();
        start.setX(((Player) sender).getLocation().getX());
        start.setY(((Player) sender).getLocation().getY() - 1);
        start.setZ(((Player) sender).getLocation().getZ());

        //Location forcycle = start;

        for (int row = 0; row < 5; row++)
            for (int column = 0; column < 5; column++)
            {
                start.setX(start.getX() + row);
                start.setZ(start.getZ() + column);

                if (maze[row][column] == 0) start.getBlock().setType(Material.COAL_BLOCK);
                else start.getBlock().setType(Material.WOOL);

                start.setX(((Player) sender).getLocation().getX());
                start.setZ(((Player) sender).getLocation().getZ());
            }

        return true;
    }
}
