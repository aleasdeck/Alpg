package plugin;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

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

    private int[][] generateMaze(int size) {

        int[][] maze = new int[size][size];
        int cellsCount = 0;

        // Генерация пустого лабиринта с клетками
        for(int row = 0; row < size; row++)
            for(int column = 0; column < size; column++)
            {
                if(row % 2 == 0) {
                    if (column % 2 == 0) {
                        maze[row][column] = 1;
                        cellsCount++;
                    }
                    else maze[row][column] = 0;
                }
                else
                    maze[row][column] = 0;
            }

        breakTheWall(maze, cellsCount, size);

        return maze;
    }

    private int[][] breakTheWall(int[/*row*/][/*column*/] emptyMaze, int cellsCount, int size) {

        Random random = new Random();
        int stopCounter = 1, position = 0;

        // Наполняем массив с пройденными клетками
        mazeCells[][] isUsed = new mazeCells[size][size];
        isUsed[0][0] = new mazeCells(position, true);

        // Цикл выламывания(c yjub) стен
        int x = 0, y = 0;
        while(stopCounter < cellsCount){
            switch(random.nextInt(4 )) {
                case 1:                                         // row(x) - 2
                    if(x - 2 >= 0) {
                        if(isUsed[x-2][y].isUsed == false) {
                            position++;
                            emptyMaze[x-1][y] = 1;
                            x -= 2;
                            isUsed[x][y] = new mazeCells(position, true);
                        }


                    }
                case 2: // row(x) + 2

                case 3: // column(y) - 2

                case 4: // column(y) + 2

            }

        }

        return emptyMaze;
    }

    private class mazeCells{

        public int number;
        public boolean isUsed;

        public mazeCells (int number, boolean isUsed) {
            this.number = number;
            this.isUsed = isUsed;
        }
    }
}
