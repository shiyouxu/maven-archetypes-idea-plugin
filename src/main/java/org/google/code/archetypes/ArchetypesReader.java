package org.google.code.archetypes;

import org.google.code.archetypes.data.Archetype;
import org.google.code.archetypes.data.Group;
import org.google.code.archetypes.common.XmlHelper;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents archetype object.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesReader extends XmlHelper {

  private List<Group> groups = new ArrayList<Group>();

  public List<Group> getGroups() {
    return groups;
  }

  public void readConfigFile(String fileName) throws JDOMException, IOException {
    groups.clear();

    InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
    try {
      if (is != null) {
        read(is);
      } else {
        read(new File(fileName));
      }
    }
    finally {
      if (is != null) {
        is.close();
      }
    }

    Element root = document.getRootElement();

    List children = root.getChildren();

    for (Object o : children) {
      if (o instanceof Element) {
        groups.add(readGroup((Element) o));
      }
    }
  }

  private Group readGroup(Element element) {
    String name = element.getAttribute("name").getValue();
    String groupId = element.getAttribute("groupId").getValue();
    String version = element.getAttribute("version").getValue();

    Attribute prefixAttribute = element.getAttribute("prefix");

    String prefix;

    if (prefixAttribute != null) {
      prefix = prefixAttribute.getValue();
    } else {
      prefix = "";
    }

    Group group = new Group();

    group.setName(name);
    group.setGroupId(groupId);
    group.setPrefix(prefix);
    group.setVersion(version);

    List<Archetype> archetypes = group.getArchetypes();
    archetypes.clear();

    List children1 = getElementByName(element, "archetypes").getChildren();

    for (Object o : children1) {
      if (o instanceof Element) {
        archetypes.add(readArchetype((Element) o, group));
      }
    }

    List<String> repositories = group.getRepositories();
    repositories.clear();

    Element repositoriesElement = getElementByName(element, "repositories");

    if (repositoriesElement != null) {
      List children2 = repositoriesElement.getChildren();

      for (Object o : children2) {
        if (o instanceof Element) {
          repositories.add(((Element) o).getText());
        }
      }
    }

    Element notesElement = getElementByName(element, "notes");

    if (notesElement != null) {
      group.setNotes(notesElement.getText());
    }

    return group;
  }

  private Archetype readArchetype(Element element, Group group) {
    String prefix = group.getPrefix();

    String name;

    if (prefix != null) {
      name = prefix + element.getAttribute("name").getValue();
    } else {
      name = element.getAttribute("name").getValue();
    }

    Attribute descriptionAttribute = element.getAttribute("description");

    String description;

    if (descriptionAttribute != null) {
      description = descriptionAttribute.getValue();
    } else {
      description = "";
    }

    Attribute versionAttribute = element.getAttribute("version");

    String version;

    if (versionAttribute != null) {
      version = versionAttribute.getValue();
    } else {
      version = group.getVersion();
    }

    Archetype archetype = new Archetype();

    archetype.setName(name);
    archetype.setDescription(description);
    archetype.setVersion(version);

    return archetype;
  }

}

