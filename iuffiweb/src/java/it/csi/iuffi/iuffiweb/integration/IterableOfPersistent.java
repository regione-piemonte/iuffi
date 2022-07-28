package it.csi.iuffi.iuffiweb.integration;

public class IterableOfPersistent implements IIterableOfPersistent
{
  /** serialVersionUID */
  private static final long     serialVersionUID = 334963497501978420L;
  private Iterable<IPersistent> iterable;

  public IterableOfPersistent(Iterable<IPersistent> iterable)
  {
    this.iterable = iterable;
  }

  @Override
  public Iterable<IPersistent> getIterable()
  {
    return iterable;
  }
}
