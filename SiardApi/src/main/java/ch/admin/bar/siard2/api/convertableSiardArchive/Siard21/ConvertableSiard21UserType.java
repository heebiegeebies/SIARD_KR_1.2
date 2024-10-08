package ch.admin.bar.siard2.api.convertableSiardArchive.Siard21;

import ch.admin.bar.siard2.api.convertableSiardArchive.Siard22.ConvertableSiard22UserType;
import ch.admin.bar.siard2.api.convertableSiardArchive.Siard22.Siard21ToSiard22Transformer;
import ch.admin.bar.siard2.api.generated.old21.UserType;

public class ConvertableSiard21UserType extends UserType {
  public ConvertableSiard21UserType(UserType user) {
    this.name = user.getName();
    this.description = user.getDescription();
  }
  
  public ConvertableSiard22UserType accept(Siard21ToSiard22Transformer visitor) {
    return visitor.visit(this);
  }
}


/* Location:              C:\Users\lenovo\IdeaProjects\siardapi.jar!\ch\admin\bar\siard2\api\convertableSiardArchive\Siard21\ConvertableSiard21UserType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */