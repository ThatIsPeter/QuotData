package quotdata.plugin;
// The elemental store for the geographic information on the server
public class BlockUnit {
    int id;
    String description;
    String district;
    String borough;
    String settlement;
    String urbanArea;
    String landmass;
    int buildingCount;
    int landArea;
    String comments;
    double buildingDensity;

    public BlockUnit(int id, String description, String district, String borough, String settlement, String urbanArea,
            String landmass, int buildingCount, int landArea, String comments) {
        this.id = id;
        this.description = description;
        this.district = district;
        this.borough = borough;
        this.settlement = settlement;
        this.urbanArea = urbanArea;
        this.landmass = landmass;
        this.buildingCount = buildingCount;
        this.landArea = landArea;
        this.comments = comments;
        this.buildingDensity = this.buildingCount / this.landArea * 1000000;
    }
    public String[] returnArray() {
        // Returns the Blockunit as an array
        String[] stringArray = new String[] {
            String.valueOf(this.id),
            this.description,
            this.district,
            this.borough,
            this.settlement,
            this.urbanArea,
            this.landmass,
            String.valueOf(this.buildingCount),
            String.valueOf(this.landArea),
            this.comments
        };
        return stringArray;
    }
    public void addBuilding() {
        // Increments the building count in the Blockunit by one
        this.buildingCount += 1;
    }
    public void removeBuilding() {
        // Decrements the building count in the Blockunit by one
        this.buildingCount -= 1;
    }
    public String returnBlockUnitDetails() {
        // Returns a string with details about the Blockunit for easy reading
        String detailString = (
            "==== Blockunit "+String.valueOf(this.id) +" ====\n"+
            "Description: " + this.description +"\n"+
            "District: " + this.district +"\n"+
            "Borough: " + this.borough +"\n"+
            "Settlement: " + this.settlement +"\n"+
            "Urban area: " + this.urbanArea +"\n"+
            "Landmass: " + this.landmass +"\n"+
            "Building Count: " + String.valueOf(this.buildingCount) +"\n"+
            "Land area: " + String.valueOf(this.landArea) +" square metres\n"+
            "Building density: " + String.valueOf(this.buildingDensity) + " buildings per square kilometre\n"+
            "Other information: " + this.comments
        );
        return detailString;
    }
    public String returnCsvLineString() {
        // Returns the Blockunit as a string for saving as a line in CSV
        String csvLine = (
            String.valueOf(this.id) +","+
            this.description +","+
            this.district +","+
            this.borough +","+
            this.settlement +","+
            this.urbanArea +","+
            this.landmass +","+
            String.valueOf(this.buildingCount) +","+
            String.valueOf(this.landArea) +","+
            this.comments
        );
        return csvLine;
    }
}
