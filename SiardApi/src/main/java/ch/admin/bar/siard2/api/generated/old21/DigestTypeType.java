package ch.admin.bar.siard2.api.generated.old21;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;



























@XmlType(name = "digestTypeType", namespace = "http://www.bar.admin.ch/xmlns/siard/2/metadata.xsd")
@XmlEnum
public enum DigestTypeType
{
  MD_5("MD5"),
  
  SHA_1("SHA-1"),
  
  SHA_256("SHA-256");
  
  private final String value;
  
  DigestTypeType(String v) {
    this.value = v;
  }
  
  public String value() {
    return this.value;
  }
  
  public static DigestTypeType fromValue(String v) {
    for (DigestTypeType c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    } 
    throw new IllegalArgumentException(v);
  }
}


/* Location:              C:\Users\lenovo\IdeaProjects\siardapi.jar!\ch\admin\bar\siard2\api\generated\old21\DigestTypeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */