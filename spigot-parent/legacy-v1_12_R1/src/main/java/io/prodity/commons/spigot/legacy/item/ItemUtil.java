package io.prodity.commons.spigot.legacy.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.tryto.Try;
import io.prodity.commons.spigot.legacy.version.Version;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ItemUtil {

	private static final LazyValue<ItemProvider> provider = new SimpleLazyValue<>(
			() -> Version.getUtilitiesProvider(ItemProvider.class, "item.ItemProvider"));
	private static Field profileField;

	public static ItemProvider getProvider() {
		return ItemUtil.provider.get();
	}

	private static void setProfileField(Field profileField) {
		ItemUtil.profileField = profileField;
		ItemUtil.profileField.setAccessible(true);
	}

	private static Field getProfileField(SkullMeta meta) {
		if (ItemUtil.profileField != null) {
			return ItemUtil.profileField;
		}

		ItemUtil.setProfileField(Try.to(meta.getClass()::getDeclaredField).apply("profile"));
		return ItemUtil.profileField;
	}

	public static void setSkullOwnerIfSkull(ItemStack itemStack, OfflinePlayer owner) {
		ItemUtil.setSkullOwnerIfSkull(itemStack, () -> owner);
	}

	public static void setSkullOwnerIfSkull(ItemStack itemStack, Supplier<OfflinePlayer> ownerSupplier) {
		final ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof SkullMeta) {
			((SkullMeta) meta).setOwningPlayer(ownerSupplier.get());
			itemStack.setItemMeta(meta);
		}
	}

	public static void applySkullTexture(ItemStack itemStack, String texture) {
		final ItemMeta meta = itemStack.getItemMeta();
		ItemUtil.veriftySkullMeta("itemStack", meta);
		ItemUtil.applySkullTexture((SkullMeta) meta, texture);
		itemStack.setItemMeta(meta);
	}

	public static void applySkullTexture(SkullMeta meta, String texture) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);

		final byte[] encodedData;

		if (texture.toLowerCase().contains("textures.minecraft.net/texture")) {
			encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
		} else {
			encodedData = texture.getBytes();
		}

		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

		Try.biConsumer(ItemUtil.getProfileField(meta)::set).accept(meta, profile);
	}

	public static Optional<String> getSkullTexture(ItemStack itemStack) {
		final ItemMeta meta = itemStack.getItemMeta();
		ItemUtil.veriftySkullMeta("itemStack", meta);
		return ItemUtil.getSkullTexture((SkullMeta) meta);
	}

	public static Optional<String> getSkullTexture(SkullMeta meta) {
		final GameProfile profile = (GameProfile) Try.function(ItemUtil.getProfileField(meta)::get).apply(meta);

		for (Property property : profile.getProperties().get("textures")) {
			if (property.getName().equals("textures") && !property.hasSignature()) {
				return Optional.of(property.getValue());
			}
		}

		return Optional.empty();
	}

	public static void transferSkullTexture(ItemStack origin, ItemStack target) {
		final ItemMeta originMeta = origin.getItemMeta();
		ItemUtil.veriftySkullMeta("origin", originMeta);
		final ItemMeta targetMeta = target.getItemMeta();
		ItemUtil.veriftySkullMeta("target", targetMeta);

		ItemUtil.transferSkullTexture((SkullMeta) originMeta, (SkullMeta) targetMeta);
		target.setItemMeta(targetMeta);
	}

	public static void transferSkullTexture(SkullMeta origin, SkullMeta target) {
		final Optional<String> texture = ItemUtil.getSkullTexture(origin);
		if (!texture.isPresent()) {
			throw new IllegalArgumentException("skull meta does not have a texture: " + origin.toString());
		}

		ItemUtil.applySkullTexture(target, texture.get());
	}

	private static void veriftySkullMeta(String argName, ItemMeta meta) {
		if (!(meta instanceof SkullMeta)) {
			throw new IllegalArgumentException("spcified meta '" + argName + "' is not SkullMeta but is " + meta.getClass().getName());
		}
	}

	public static CraftItemStack toCraftItem(ItemStack item) {
		return (CraftItemStack) ItemUtil.getProvider().toCraftItemStack(item);
	}

	public static ItemStack toBukkitItem(ItemStack item) {
		return ItemUtil.getProvider().toBukkitItemStack(item);
	}

}
