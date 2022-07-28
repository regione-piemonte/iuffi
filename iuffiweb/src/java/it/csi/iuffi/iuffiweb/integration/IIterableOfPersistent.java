package it.csi.iuffi.iuffiweb.integration;

public interface IIterableOfPersistent extends IPersistent
{
  public Iterable<IPersistent> getIterable();
}
