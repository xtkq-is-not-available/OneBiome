package de.xtkq.onebiome.provider.settings;

import com.google.gson.annotations.SerializedName;
import org.bukkit.block.Biome;

public class BiomeProviderSettings {

    @SerializedName("biome")
    private Biome biome = null;

    public Biome getBiome() {
        return this.biome;
    }

    public void setBiome(Biome p_biome){
        this.biome = p_biome;
    }
}
