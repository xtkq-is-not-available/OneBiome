package de.xtkq.onebiome.provider.instances;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.xtkq.onebiome.OneBiome;
import de.xtkq.onebiome.provider.settings.BiomeProviderSettings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class OneBiomeProvider extends BiomeProvider {

    protected BiomeProviderSettings biomeProviderSettings;
    private static final Random RANDOM = new Random();

    public OneBiomeProvider(String p_identifier) {
        Gson gson = new Gson();

        if (StringUtils.isBlank(p_identifier)) {
            this.biomeProviderSettings = new BiomeProviderSettings();
            OneBiome.getOneBiome().getLogger().info("Biome settings have not been set. A random biome will be used.");
        } else {
            try {
                this.biomeProviderSettings = gson.fromJson(p_identifier, BiomeProviderSettings.class);
            } catch (JsonSyntaxException jse){
                this.biomeProviderSettings = new BiomeProviderSettings();
                OneBiome.getOneBiome().getLogger().info("Biome settings \"" + p_identifier + "\" syntax is not valid. A random biome will be used.");
            }
        }
        if (Objects.isNull(this.biomeProviderSettings.getBiome())){
            this.biomeProviderSettings.setBiome(this.getRandomBiome());
        }
        OneBiome.getOneBiome().getLogger().info("Biome: \"" + this.biomeProviderSettings.getBiome().name() + "\" will be used.");
    }

    private Biome getRandomBiome(){
        return Biome.values()[RANDOM.nextInt(Biome.values().length)];
    }

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        return this.biomeProviderSettings.getBiome();
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return List.of(this.biomeProviderSettings.getBiome());
    }
}
