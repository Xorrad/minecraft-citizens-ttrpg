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
    public static String DEFAULT_SKIN = "ewogICJ0aW1lc3RhbXAiIDogMTcxOTY4MzQ4NzA2NywKICAicHJvZmlsZUlkIiA6ICJjNjM0MTJlZmJiMmY0MmY5OTNjYWQ3YzlhMjQwZDhiNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJjX2hpZWYiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYxYjA0MWRhMDkxYjlmYzY2ZDczZWQ2NDJmNmYxYTljN2FiNmFhMzBkY2RjZDNlODllZGI1YjAzMGI3YzY1MiIKICAgIH0KICB9Cn0=";

    public static String getNPCRawTexture(NPC npc) {
        SkinTrait skin = npc.getTraitNullable(SkinTrait.class);
        if(skin == null || skin.getTexture() == null)
            return DEFAULT_SKIN;
        return skin.getTexture();
    }

    public static PlayerProfile getNPCProfile(NPC npc) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try {
            String base64 = getNPCRawTexture(npc);
            URL url = getSkinURL(base64);
            textures.setSkin(url);
        }
        catch (Exception e) {
            try {
                URL url = getSkinURL(DEFAULT_SKIN);
                textures.setSkin(url);
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        profile.setTextures(textures);
        return profile;
    }

    public static URL getSkinURL(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        String[] parts = decoded.split(":");
        String url = "https:" + parts[parts.length-1].replaceAll(" ", "").replaceAll("\"", "").replaceAll("\n}\n}\n}", "");
        return new URL(url);
    }

}
