/**
 * A location in the US 
 * @author Suzanne Balik
 */
public class Location {

  /** Radius of the earth in miles */
  public static final  double EARTH_RADIUS_MILES = 3958.75;
  
  /** Name of location */
  private String name;
  
  /** State where location is found */ 
  private String state;
  
  /** Latitude of location in degrees */
  private double latitude;
  
  /** Longitude of location in degrees */
  private double longitude;
  
  /**
   * Constructs and initializes a location.
   * @param name name of location
   * @param state state where location is found
   * @param latitude latitude of location in degrees
   * @param longitude of location in degrees
   * @throws NullPointerException if name or state are null
   */
  public Location (String name, String state, double latitude, double longitude) {
    if (name == null || state == null) {
      throw new NullPointerException("name and/or state is/are null");
    }
    this.name = name;
    this.state = state;
    this.latitude = latitude;
    this.longitude = longitude;
  }
  
  /**
   * Returns the name of this location
   * @return name of this location
   */
  public String getName() {
    return name;
  }
  
  /**
   * Returns the state where this location is found
   * @return state where this location is found
   */
  public String getState() {
    return state;
  }
  
  /**
   * Returns latitude of location in degrees
   * @return latitude of location in degrees
   */
  public double getLatitude() {
    return latitude;
  }
  
  /**
   * Returns longitude of location in degrees
   * @return longitude of location in degrees
   */
  public double getLongitude() {
    return longitude;
  }
  
  
  /**
   * Returns the great-circle distance between this location and 
   * another location in miles. The calculation is based on 
   * the procedure provided in Programming Project 5 on page 193
   * of Building Java Programs, 3rd edition, by Reges and Stepp
   * @param other the other location
   * @return the great-circle distance between this location and 
   * another location in miles 
   */
  public double getDistance(Location other) {
    double latitude1 = Math.toRadians(latitude);
    double longitude1 = Math.toRadians(longitude);
    double latitude2 = Math.toRadians(other.latitude);
    double longitude2 = Math.toRadians(other.longitude);
    
    double longitudinalDifference = longitude1 - longitude2;
    double angularDifference = Math.acos(
                               Math.sin(latitude1) * Math.sin(latitude2) +
                               Math.cos(latitude1) * Math.cos(latitude2) *
                               Math.cos(longitudinalDifference));
    
    return angularDifference * EARTH_RADIUS_MILES;
  }
}
  
  