package quotdata.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
    public FileConfiguration config = this.getConfig();
    private GeoDatabase geoDatabase;
	
	@Override
    public void onEnable() {
        // start up
		// reloads
        // plug in reloads
        config.addDefault("DatabasePath", "C:\\Users\\Peter\\Documents\\QuotroBlockunitDatabase.csv");
        saveConfig();
        this.geoDatabase = new GeoDatabase(this, config.getString("DatabasePath"));
    }

    @Override
    public void onDisable() {
        // shutdown
        // reloads
        // plug in reloads
        this.geoDatabase.writeDataBase(this);
    }    
    
    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /countblockunits
        if (command.getName().equalsIgnoreCase("countblockunits")) {
            Integer count = this.geoDatabase.countBlockunits(this);
            sender.sendMessage("Total number of Blockunits loaded: " + Integer.toString(count));
            return true;
        }
        // /getdata <blockunit> [parameter]=buildcount
        if (command.getName().equalsIgnoreCase("getdata")) {
            int blockUnit;
            String parameter = "buildcount";
            try {
                blockUnit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Please enter a number for the Blockunit!");
                return false;
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage("Please specify a Blockunit!");
                return false;
            }
            try {
                parameter = args[1].toLowerCase();
            } catch (ArrayIndexOutOfBoundsException e) {}
            
            this.Log("Looking for parameter: '"+parameter+"'");
            String data = this.geoDatabase.getBlockUnitParameter(this, blockUnit, parameter);
            sender.sendMessage(data);
            return true;
        }
        // /addbuilding <blockunit>
        if (command.getName().equalsIgnoreCase("addbuilding")) {
            int blockUnit;
            try {
                blockUnit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Please enter a number for the Blockunit!");
                return false;
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage("Please specify a Blockunit!");
                return false;
            }
            sender.sendMessage(this.geoDatabase.tryAddBuilding(this, blockUnit));
            this.geoDatabase.writeDataBase(this);
            return true;
        }
        // /removebuilding <blockunit>
        if (command.getName().equalsIgnoreCase("removebuilding")) {
            int blockUnit;
            try {
                blockUnit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Please enter a number for the Blockunit!");
                return false;
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage("Please specify a Blockunit!");
                return false;
            }
            sender.sendMessage(this.geoDatabase.tryRemoveBuilding(this, blockUnit));
            this.geoDatabase.writeDataBase(this);
            return true;
        }
        // /getdetails <blockunit>
        if (command.getName().equalsIgnoreCase("getdetails")) {
            int blockUnit;
            try {
                blockUnit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Please enter a number for the Blockunit!");
                return false;
            } catch (ArrayIndexOutOfBoundsException e) {
                sender.sendMessage("Please specify a Blockunit!");
                return false;
            }
            sender.sendMessage(this.geoDatabase.tryPrintDetails(this, blockUnit));
            return true;
        }
        // /writedatabase
        if (command.getName().equalsIgnoreCase("writedatabase")) {
            this.geoDatabase.writeDataBase(this);
            return true;
        }
        // /getbuildingdefinition
        if (command.getName().equalsIgnoreCase("getbuildingdefinition")) {
            String definitionString = "1. A room is an enclosed space within a structure that meets the following conditions\n"+
            "   a. There is at least one source of light inside the room.\n"+
            "   b. There is at least one door between the inside and outside of the room. This may be a door block, a piston door or a trapdoor.\n"+
            "   c. A two-meter cube can be fully enclosed, sitting flat on the ground, anywhere within the room with all furniture removed.\n"+
            "2. An enclosed space is a volume of air that when all doors are closed is completely surrounded by blocks, except the air space required for rail passages, or any other reasonably small area of space that does detract from a sense of being inside.\n"+
            "3. A structure is to be considered a building for statistical purposes, if it meets all of the following conditions\n"+
            "   a. The area of the building exceeds 16 square meters; walls may only be counted if they are above ground.\n"+
            "   b. It contains at least one room.\n"+
            "   c. The structure does not share any blocks with any other building, except underground or anywhere above ground if passage is still possible between the buildings. A structure that meets all but this criterium and 1(f) is to be called a room of a greater building.\n"+
            "   d. If the construction of the structure began after 1 July 2018, at least one room within the structure must not be floored with any of the following materials:\n"+
            "       i. Dirt, grass, crops, podzol, mycelium, grass path or coarse dirt.\n"+
            "       ii. Sand, except red sand.\n"+
            "       iii. Leaves\n"+
            "       iv. Liquids or other non-solid blocks.\n"+
            "       v. Slime blocks.\n"+
            "       vi. Any block that is not a full one metre long and wide.\n"+
            "       vii. Doors\n"+
            "       viii. Trapdoors, unless there are at least four other acceptable blocks in the room.\n"+
            "       ix. Redstone repeaters or comparators.\n"+
            "       x. Any block without a flat surface, except stairs.\n"+
            "       xi. Barrier blocks, structure void or air.\n"+
            "       xii. Ice, packed ice or blue ice.\n"+
            "       xiii. Snow layers\n"+
            "       xiv. Beds\n"+
            "       xv. TNT";
            sender.sendMessage(definitionString);
            return true;
        }
        return false;
    }
    public void Log(String message) {
        System.out.println("[QuotData] " + message);
    }
}