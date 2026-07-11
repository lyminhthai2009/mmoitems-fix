import org.bukkit.ChatColor;

public class TestColor {
    public static void main(String[] args) {
        String itemName = "§f<gradient:gold:yellow>Thương Nhân ThuongNhanVatLieu</gradient>";
        String colorless = ChatColor.stripColor(itemName);
        System.out.println("colorless: " + colorless);
        System.out.println("equals: " + itemName.equals(colorless));
        
        String itemName2 = "§f<#b499f7>Donate §8- §fKiếm <#89FF63>Thảm Họa Thiên Nhiên";
        String colorless2 = ChatColor.stripColor(itemName2);
        System.out.println("colorless2: " + colorless2);
        System.out.println("equals2: " + itemName2.equals(colorless2));
    }
}
