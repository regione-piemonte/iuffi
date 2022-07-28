package it.csi.iuffi.iuffiweb.geo;

import java.util.List;

import it.csi.iuffi.iuffiweb.geo.Feature;

public class FeatureCollection
{
  String type;
  List<Feature> features;
  
  public FeatureCollection(List<Feature> features) {
    super();
    this.type = "FeatureCollection";
    this.features = features;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }
  
}
