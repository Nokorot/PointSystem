package util.adds;

public abstract interface Logable
{
  public abstract String toLogString();
  public abstract void fromLogString(String paramString);
}