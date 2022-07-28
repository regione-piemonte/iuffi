package it.csi.iuffi.iuffiweb.geo;

import java.util.Arrays;
import java.util.List;

public class Point extends Geometry {
  
  List<Double> coordinates;
  
  public Point(double latitude, double longitude) {
    super();
    this.type = "Point";
    this.coordinates = Arrays.asList(new Double[] {longitude,latitude});
  }

  public List<Double> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<Double> coordinates) {
    this.coordinates = coordinates;
  }

}
