package org.google.code.archetypes.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents archetype group object.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class Group {
  private String name;

  private String groupId;
  private String prefix;
  private String version;
  private String notes;

  private List<Archetype> archetypes = new ArrayList<Archetype>();
  private List<String> repositories = new ArrayList<String>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<Archetype> getArchetypes() {
    return archetypes;
  }

  public void setArchetypes(List<Archetype> archetypes) {
    this.archetypes = archetypes;
  }

  public List<String> getRepositories() {
    return repositories;
  }

  public void setRepositories(List<String> repositories) {
    this.repositories = repositories;
  }

  public String toString() {
    return "Group ( \n" +
        "name: " + name + "\n" +
        "archetypes: " + archetypes + "\n" +
        "repositories: " + repositories + "\n" +
        ")";
  }

}
