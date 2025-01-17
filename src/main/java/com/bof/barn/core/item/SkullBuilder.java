package com.bof.barn.core.item;

import com.bof.toolkit.skin.Skin;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SkullBuilder extends ItemBuilder {

    public SkullBuilder() {
        super(Material.PLAYER_HEAD);
    }

    public SkullBuilder(@NotNull ItemBuilder builder) {
        super(builder);
    }

    public SkullBuilder(@NotNull SkullBuilder builder) {
        super(builder);
    }

    @Override
    public SkullBuilder material(Material material) {
        super.material(material);
        return this;
    }

    @Override
    public SkullBuilder displayName(Component displayName) {
        super.displayName(displayName);
        return this;
    }

    @Override
    public SkullBuilder lore(@NotNull List<? extends Component> lore) {
        super.lore(lore);
        return this;
    }

    @Override
    public SkullBuilder lore(Component... lines) {
        super.lore(lines);
        return this;
    }

    @Override
    public SkullBuilder appendLoreLine(Component line) {
        super.appendLoreLine(line);
        return this;
    }

    @Override
    public SkullBuilder appendLore(List<Component> appendLore) {
        super.appendLore(appendLore);
        return this;
    }

    @Override
    public SkullBuilder setLoreLine(int index, Component line, boolean override) {
        super.setLoreLine(index, line, override);
        return this;
    }

    @Override
    public SkullBuilder setLoreLine(int index, Component line) {
        super.setLoreLine(index, line);
        return this;
    }

    @Override
    public SkullBuilder removeLoreLine(int index) {
        super.removeLoreLine(index);
        return this;
    }

    @Override
    public SkullBuilder removeLoreLine(Component line) {
        super.removeLoreLine(line);
        return this;
    }

    @Override
    public SkullBuilder clearLore() {
        super.clearLore();
        return this;
    }

    @Override
    public SkullBuilder hideFlag(ItemFlag @NotNull ... flags) {
        super.hideFlag(flags);
        return this;
    }

    @Override
    public SkullBuilder hideFlags() {
        super.hideFlags();
        return this;
    }

    @Override
    public SkullBuilder showFlag(ItemFlag @NotNull ... flags) {
        super.showFlag(flags);
        return this;
    }

    @Override
    public SkullBuilder showFlags() {
        super.showFlags();
        return this;
    }

    @Override
    public SkullBuilder enchantment(Map<Enchantment, Integer> enchantments) {
        super.enchantment(enchantments);
        return this;
    }

    @Override
    public SkullBuilder addEnchantment(Enchantment enchantment, int level) {
        super.addEnchantment(enchantment, level);
        return this;
    }

    @Override
    public SkullBuilder removeEnchantment(Enchantment enchantment, int level) {
        super.removeEnchantment(enchantment, level);
        return this;
    }

    @Override
    public SkullBuilder removeEnchantments(Enchantment... enchantments) {
        super.removeEnchantments(enchantments);
        return this;
    }

    @Override
    public SkullBuilder removeEnchantments(int @NotNull ... levels) {
        super.removeEnchantments(levels);
        return this;
    }

    @Override
    public SkullBuilder amount(int amount) {
        super.amount(amount);
        return this;
    }

    @Override
    public SkullBuilder damage(short durability) {
        super.damage(durability);
        return this;
    }

    @Override
    public SkullBuilder durability(short damage) {
        super.durability(damage);
        return this;
    }

    @Override
    public SkullBuilder unbreakable(boolean unbreakable) {
        super.unbreakable(unbreakable);
        return this;
    }

    /**
     * Sets the skull skin (textures) using the player profile.
     *
     * @param skinData filled in with texture and signature of the skin
     * @return The updated SkullBuilder instance
     */
    public SkullBuilder skin(@NotNull Skin skinData) {
        if (skinData.getTexture() == null) {
            return this;
        }
        SkullMeta skullMeta = this.getMeta();

        PlayerProfile playerProfile = Bukkit.createProfile(java.util.UUID.randomUUID(), null);
        playerProfile.getProperties().add(new ProfileProperty("textures", skinData.getTexture(), skinData.getSignature()));
        skullMeta.setPlayerProfile(playerProfile);
        super.getItemStack().setItemMeta(skullMeta);
        return this;
    }

    /**
     * Set the skull owner
     *
     * @param player Player to be set as owner
     * @return The updated SkullBuilder instance
     */
    public SkullBuilder owner(OfflinePlayer player) {
        SkullMeta skullMeta = this.getMeta();
        skullMeta.setOwningPlayer(player);
        super.getItemStack().setItemMeta(skullMeta);
        return this;
    }

    @Override
    public SkullMeta getMeta() {
        return (SkullMeta) super.getMeta();
    }
}
