package org.google.code.archetypes.data;

/**
 * This class represents archetype object.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class Archetype {
  private String name;
  private String version;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String toString() {
    return name;
  }

}
