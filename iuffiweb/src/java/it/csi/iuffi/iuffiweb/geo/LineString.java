package it.csi.iuffi.iuffiweb.geo;

import java.util.ArrayList;
import java.util.List;

public class LineString extends Geometry {
  
  List<List<Double>> coordinates;

  public LineString(List<Point> points) {
    super();
    this.type = "LineString";
    List<List<Double>> coordinates = new ArrayList<List<Double>>();
    for(Point point : points) {
      coordinates.add(point.getCoordinates());
    }
    //points.forEach(x->{
    //  coordinates.add(x.getCoordinates());
   // });
    this.coordinates = coordinates;
  }

  public List<List<Double>> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<List<Double>> coordinates) {
    this.coordinates = coordinates;
  }
}