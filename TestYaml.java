import org.bukkit.configuration.file.YamlConfiguration;

public class TestYaml {
    public static void main(String[] args) {
        String yaml = "base:\n  name: <#b499f7>Donate &8- &fKiếm <#89FF63>Thảm Họa Thiên Nhiên\n";
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(yaml);
            System.out.println("Parsed name: " + config.getString("base.name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
