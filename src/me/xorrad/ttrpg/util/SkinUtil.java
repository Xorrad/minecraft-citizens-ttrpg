package me.xorrad.ttrpg.util;

import me.xorrad.ttrpg.TTRPG;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class SkinUtil {

    public static String getNPCRawTexture(NPC npc) {
        SkinTrait skin = npc.getTraitNullable(SkinTrait.class);
        if(skin == null || skin.getTexture() == null)
            return "ewogICJ0aW1lc3RhbXAiIDogMTcxOTY4MzQ4NzA2NywKICAicHJvZmlsZUlkIiA6ICJjNjM0MTJlZmJiMmY0MmY5OTNjYWQ3YzlhMjQwZDhiNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJjX2hpZWYiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYxYjA0MWRhMDkxYjlmYzY2ZDczZWQ2NDJmNmYxYTljN2FiNmFhMzBkY2RjZDNlODllZGI1YjAzMGI3YzY1MiIKICAgIH0KICB9Cn0=";
            //return "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/393e453e02ed5fb54ab69cbb75a2cb3281de1c877dc65502ef669c09a589294e\"}}}";
        return skin.getTexture();
    }

    public static PlayerProfile getNPCProfile(NPC npc) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try {
            String base64 = getNPCRawTexture(npc);
            URL url = getSkinURL(base64);
            textures.setSkin(url);
            profile.setTextures(textures);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    public static URL getSkinURL(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        String[] parts = decoded.split(":");
        String url = "https:" + parts[parts.length-1].replaceAll(" ", "").replaceAll("\"", "").replaceAll("\n}\n}\n}", "");
        return new URL(url);
    }

}
