package me.xorrad.ttrpg.core.traits;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@TraitName("family")
public class FamilyTrait extends Trait {
    public HashMap<Role, Integer> relatives;
    public ArrayList<Integer> children;

    public FamilyTrait() {
        super("family");
        this.relatives = new HashMap<>();
        this.children = new ArrayList<>();
    }

    public int getRelativeId(Role role) {
        return this.relatives.getOrDefault(role, -1);
    }

    public NPC getRelative(Role role) {
        try {
            return CitizensAPI.getNPCRegistry().getById(this.getRelativeId(role));
        }
        catch (Exception e) {
            return null;
        }
    }

    public void setRelative(Role role, NPC newRelative) {
        NPC formerRelative = this.getRelative(role);
        if(role.isParent() && formerRelative != null) {
            FamilyTrait relativeFamily = formerRelative.getOrAddTrait(FamilyTrait.class);
            relativeFamily.removeChild(this.getNPC());
        }

        this.relatives.put(role, (newRelative == null) ? -1 : newRelative.getId());

        if(role.isParent() && newRelative != null) {
            FamilyTrait relativeFamily = newRelative.getOrAddTrait(FamilyTrait.class);
            relativeFamily.addChild(this.getNPC());
        }
    }

    public List<NPC> getChildren() {
        return children.stream().map(id -> CitizensAPI.getNPCRegistry().getById(id)).toList();
    }

    public void setChildren(ArrayList<NPC> children) {
        this.children = new ArrayList<>(children.stream().map(NPC::getId).toList());
    }

    public void addChild(NPC child) {
        if(child != null && !this.children.contains(child.getId()))
            this.children.add(child.getId());
    }

    public void removeChild(NPC child) {
        if(child != null && !this.children.isEmpty())
            this.children.remove(child.getId());
    }

    @Override
    public void load(DataKey key) {
        for(Role role : Role.values()) {
            this.relatives.put(role, key.getInt(role.name().toLowerCase(), -1));
        }
        this.children = new ArrayList<>(Arrays.stream(key.getString("children", "").split(",")).filter(s -> !s.isBlank()).map(Integer::parseInt).toList());
    }

    @Override
    public void save(DataKey key) {
        for(Role role : Role.values()) {
            key.setInt(role.name().toLowerCase(), this.relatives.getOrDefault(role, -1));
        }
        key.setString("children", children.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    public enum Role {
        FATHER(true),
        MOTHER(true),
        PARTNER(false);

        private final boolean parent;

        Role(boolean parent) {
            this.parent = parent;
        }

        public boolean isParent() {
            return parent;
        }
    }
}
