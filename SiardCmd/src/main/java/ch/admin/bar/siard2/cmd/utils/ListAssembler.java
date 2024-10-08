package ch.admin.bar.siard2.cmd.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ListAssembler<T> {
  private final Supplier<Integer> nrOfItemsSupplier;
  private final Function<Integer, T> itemSupplier;
  
  public ListAssembler(Supplier<Integer> nrOfItemsSupplier, Function<Integer, T> itemSupplier)
  { this.nrOfItemsSupplier = nrOfItemsSupplier; this.itemSupplier = itemSupplier; }

  public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof ListAssembler)) {
        return false;
      }
      ListAssembler<?> other = (ListAssembler)o;
      Object this$nrOfItemsSupplier = getNrOfItemsSupplier(), other$nrOfItemsSupplier = other.getNrOfItemsSupplier();
      if (!Objects.equals(this$nrOfItemsSupplier, other$nrOfItemsSupplier)) {return false;}
      Object this$itemSupplier = getItemSupplier();
      Object other$itemSupplier = other.getItemSupplier();
      return Objects.equals(this$itemSupplier, other$itemSupplier);
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Object $nrOfItemsSupplier = getNrOfItemsSupplier();
    result = result * 59 + (($nrOfItemsSupplier == null) ? 43 : $nrOfItemsSupplier.hashCode());
    Object $itemSupplier = getItemSupplier();
    return result * 59 + (($itemSupplier == null) ? 43 : $itemSupplier.hashCode()); }

  public String toString() {
    return "ListAssembler(nrOfItemsSupplier=" + getNrOfItemsSupplier() + ", itemSupplier=" + getItemSupplier() + ")";
  }
  
  public Supplier<Integer> getNrOfItemsSupplier() { return this.nrOfItemsSupplier; } public Function<Integer, T> getItemSupplier() {
    return this.itemSupplier;
  }

  public List<T> assemble() {
    return assemble(this.nrOfItemsSupplier, this.itemSupplier);
  }
  
  public static <T> List<T> assemble(Supplier<Integer> nrOfItemsSupplier, Function<Integer, T> itemSupplier) {
    Integer nrOfItems = nrOfItemsSupplier.get();
    return assemble(nrOfItems.intValue(), itemSupplier);
  }
  
  public static <T> List<T> assemble(int nrOfItems, Function<Integer, T> itemSupplier) {
    ArrayList<T> assembledItems = new ArrayList<>();
    for (int i = 0; i < nrOfItems; i++) {
      assembledItems.add(itemSupplier.apply(Integer.valueOf(i)));
    }
    
    return assembledItems;
  }
}


/* Location:              C:\Users\lenovo\IdeaProjects\SIARD_KR_2.2\lib\siardcmd.jar!\ch\admin\bar\siard2\cm\\utils\ListAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */