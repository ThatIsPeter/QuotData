package quotdata.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.apache.logging.log4j.core.util.Assert;

// Loads the server database and provides manipulations
public class GeoDatabase {
    String serverDatabasePath;
    Map<Integer, BlockUnit> dataBase;
    int[] idArray;
    String csvHeader;

    public GeoDatabase(Main main, String serverDatabasePath) {
        this.serverDatabasePath = serverDatabasePath;
        this.dataBase = new HashMap<Integer, BlockUnit>();
        this.idArray = new int[]{};
        BufferedReader csvReader;
        try {
            main.Log("Loading the Blockunit database from: " + this.serverDatabasePath);
            csvReader = new BufferedReader(new FileReader(this.serverDatabasePath));
            String row = csvReader.readLine(); // Initialise read line and ignore the first line (headers)
            this.csvHeader = row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");

                int id = Integer.parseInt(data[0]);
                String description = data[1];
                String district = data[2];
                String borough = data[3];
                String settlement = data[4];
                String urbanArea = data[5];
                String landmass = data[6];
                int buildingCount = Integer.parseInt(data[7]);
                int landArea = Integer.parseInt(data[8]);
                String comments = data[9];
                
                this.idArray = Arrays.copyOf(idArray, idArray.length+1); // TODO a more efficient way of doing this.
                this.idArray[idArray.length-1] = id;

                BlockUnit blockUnit = new BlockUnit(id, description, district, borough, settlement, urbanArea, landmass, buildingCount, landArea, comments);
                // main.Log("Succuessfully loaded the Blockunit '" + blockUnit.description + "'");
                dataBase.put(id, blockUnit);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            main.Log("Failed to load Blockunit database!");
        } catch (IOException e) {
            e.printStackTrace();
            main.Log("IO Exception when attempting to load the Blockunit database!");
        }
    }
    public void writeDataBase(Main main) {
        // Writes the database to the csv file.
        File csvOutputFile = new File(this.serverDatabasePath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.write(this.csvHeader);
            for (int id : this.idArray) {
                String blockunitRow = this.dataBase.get(id).returnCsvLineString();
                pw.write("\n" + blockunitRow);
            }
            main.Log("Successfully updated the Blockunit database!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            main.Log("Unable to save the Blockunit database!");
        }
    }
    public Integer countBlockunits(Main main) {
        // Returns the number of Blockunits in the database
        return this.dataBase.size();
    }
    public String tryAddBuilding(Main main, int id) {
        // Trys to add a building to the specified Blockunit. Returns comment for sender.
        String senderStatement;
        try {
            this.dataBase.get(id).addBuilding();
            senderStatement = "Successfully added building to "+String.valueOf(id)+". Building count is now: "+String.valueOf(this.dataBase.get(id).buildingCount);
        } catch (NullPointerException e) {
            senderStatement = "Please enter a valid Blockunit number!";
        }
        return senderStatement;
    }
    public String tryRemoveBuilding(Main main, int id) {
        // Trys to add a building to the specified Blockunit. Returns comment for sender.
        String senderStatement;
        try {
            this.dataBase.get(id).removeBuilding();
            senderStatement = "Successfully removed building from "+String.valueOf(id)+". Building count is now: "+String.valueOf(this.dataBase.get(id).buildingCount);
        } catch (NullPointerException e) {
            senderStatement = "Please enter a valid Blockunit number!";
        }
        return senderStatement;
    }
    public String tryPrintDetails(Main main, int id) {
        // Trys to retrieve information on a Blockunit
        String senderStatement;
        try {
            senderStatement = this.dataBase.get(id).returnBlockUnitDetails();
        } catch (NullPointerException e) {
            senderStatement = "Please enter a valid Blockunit number!";
        }
        return senderStatement;
    }
    public String getBlockUnitParameter(Main main, int id, String parameter) {
        // Returns the specified parameter of specified blockunit
        String blockUnitData = "Please enter a valid attribute of a Blockunit!";
        try {
            if (parameter == "buildcount" || parameter == "buildingcount" || parameter == "buildings") {
                blockUnitData = String.valueOf(this.dataBase.get(id).buildingCount);
            }
            else if (parameter == "landarea") {
                blockUnitData = String.valueOf(this.dataBase.get(id).landArea);
            }
            else if (parameter == "description" || parameter == "name") {
                blockUnitData = this.dataBase.get(id).description;
            }
            else if (parameter == "district") {
                blockUnitData = this.dataBase.get(id).district;
            }
            else if (parameter == "borough") {
                blockUnitData = this.dataBase.get(id).borough;
            }
            else if (parameter == "settlement") {
                blockUnitData = this.dataBase.get(id).settlement;
            }
            else if (parameter == "urbanarea" || parameter == "urban_area" || parameter == "urban") {
                blockUnitData = this.dataBase.get(id).urbanArea;
            }
            else if (parameter == "landmass" || parameter == "island") {
                blockUnitData = this.dataBase.get(id).landmass;
            }
            else if (parameter == "comments" || parameter == "info" || parameter == "other") {
                blockUnitData = this.dataBase.get(id).comments;
            }
        } catch (NullPointerException e) {
            blockUnitData = "Please enter a valid Blockunit number!";
        }
        return blockUnitData;
    }
    public GeoDatabase() {
    }
}