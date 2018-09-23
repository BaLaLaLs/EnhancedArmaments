package com.jannesoon.enhancedarmaments.essentials;

import java.util.ArrayList;

import com.jannesoon.enhancedarmaments.config.Config;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public enum Ability
{
		// weapon
	// active
	FIRE("weapon", "active", Config.fire, TextFormatting.RED, 0xFF5555, 1, 3),
	FROST("weapon", "active", Config.frost, TextFormatting.AQUA, 0x55FFFF, 1, 3),
	POISON("weapon", "active", Config.poison, TextFormatting.DARK_GREEN, 0x00AA00, 1, 3),
	INNATE("weapon", "active", Config.innate, TextFormatting.DARK_RED, 0xAA0000, 2, 3),
	BOMBASTIC("weapon", "active", Config.bombastic, TextFormatting.GRAY, 0xAAAAAA, 3, 3),
	CRITICAL_POINT("weapon", "active", Config.criticalpoint, TextFormatting.DARK_GRAY, 0x555555, 3, 3),
	// passive
	ILLUMINATION("weapon", "passive", Config.illumination, TextFormatting.YELLOW, 0xFFFF55, 2, 1),
	ETHEREAL("weapon", "passive", Config.ethereal, TextFormatting.GREEN, 0x55FF55, 2, 2),
	BLOODTHIRST("weapon", "passive", Config.bloodthirst, TextFormatting.DARK_PURPLE, 0xAA00AA, 3, 2),
	
		// armor
	// active
	MOLTEN("armor", "active", Config.molten, TextFormatting.RED, 0xFF5555, 2, 2),
	FROZEN("armor", "active", Config.frozen, TextFormatting.AQUA, 0x55FFFF, 2, 2),
	TOXIC("armor", "active", Config.toxic, TextFormatting.DARK_GREEN, 0x00AA00, 2, 2),
	// passive
	BEASTIAL("armor", "passive", Config.beastial, TextFormatting.DARK_RED, 0xAA0000, 2, 1),
	REMEDIAL("armor", "passive", Config.remedial, TextFormatting.LIGHT_PURPLE, 0xFF55FF, 2, 2),
	HARDENED("armor", "passive", Config.hardened, TextFormatting.GRAY, 0xAAAAAA, 3, 1),
	ADRENALINE("armor", "passive", Config.adrenaline, TextFormatting.GREEN, 0x55FF55, 3, 1);
	
	public static int WEAPON_ABILITIES;
	public static int ARMOR_ABILITIES;
	public static final ArrayList<Ability> WEAPONS = new ArrayList<Ability>();
	public static final ArrayList<Ability> ARMORS = new ArrayList<Ability>();
	
	private String category;
	private String type;
	private boolean enabled;
	private String color;
	private int hex;
	private int tier;
	private int maxlevel;
	
	Ability(String category, String type, boolean enabled, Object color, int hex, int tier, int maxlevel)
	{
		this.category = category;
		this.type = type;
		this.enabled = enabled;
		this.color = color.toString();
		this.hex = hex;
		this.tier = tier;
		this.maxlevel = maxlevel;
	}
	
	/**
	 * Returns true if the player has enough experience to unlock the ability.
	 * @param nbt
	 * @param player
	 * @param ability
	 * @return
	 */
	public static boolean hasEnoughExp (NBTTagCompound nbt, EntityPlayer player, Ability ability)
	{
		return getExpLevel(ability, nbt) <= player.experienceLevel || player.isCreative();
	}
	
	/**
	 * Returns the abilitys requiring experience level.
	 * @param ability
	 * @param nbt
	 * @return
	 */
	public static int getExpLevel (Ability ability, NBTTagCompound nbt)
	{
		int requiredExpLevel = ability.getTier() + ability.getMaxLevel() + ability.getLevel(nbt) + 1;
		
		return requiredExpLevel;
	}
	
	/**
	 * Returns true if the stack has the ability.
	 * @param nbt
	 * @return
	 */
	public boolean hasAbility(NBTTagCompound nbt)
	{
		return nbt != null && nbt.getBoolean(toString());
	}
	
	/**
	 * Adds the specified ability to the stack.
	 * @param nbt
	 */
	public void addAbility(NBTTagCompound nbt, int level)
	{
		nbt.setBoolean(toString(), true);
		setLevel(nbt, level);
	}
	
	/**
	 * Removes the specified ability from the stack.
	 * @param nbt
	 */
	public void removeAbility(NBTTagCompound nbt)
	{
		nbt.removeTag(toString());
		nbt.removeTag(toString() + "_level");
	}
	
	/**
	 * Sets the level of the specified ability.
	 * @param nbt
	 * @param level
	 */
	public void setLevel(NBTTagCompound nbt, int level)
	{
		if (level <= 3)
		{
			nbt.setInteger(toString() + "_level", level);
		}
	}
	
	/**
	 * Returns the level of the specified ability.
	 * @param nbt
	 * @return
	 */
	public int getLevel(NBTTagCompound nbt)
	{
		if (nbt != null) return nbt.getInteger(toString() + "_level");
		else return 0;
	}
	
	public boolean canUpgradeLevel(NBTTagCompound nbt)
	{
			if (getLevel(nbt) < this.maxlevel)
				return true;
			else
				return false;
	}
	
	public int getTier()
	{
		return tier;
	}
	
	public int getMaxLevel()
	{
		return maxlevel;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public int getHex()
	{
		return hex;
	}
	
	public String getName()
	{
		return this.toString();
	}
	
	public String getName(NBTTagCompound nbt)
	{
		if (getLevel(nbt) == 2)
			return new TextComponentTranslation("enhancedarmaments.ability." + this.toString()).getFormattedText() + " II";
		else if (getLevel(nbt) == 3)
			return new TextComponentTranslation("enhancedarmaments.ability." + this.toString()).getFormattedText() + " III";
		else
			return new TextComponentTranslation("enhancedarmaments.ability." + this.toString()).getFormattedText();
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getTypeName()
	{
		return new TextComponentTranslation("enhancedarmaments.ability.type." + type.toString()).getFormattedText(); 
	}
	
	public String getCategory()
	{
		return category;
	}
	
	static
	{
		for (int i = 0; i < Ability.values().length; i++)
		{		
			if (Ability.values()[i].getCategory().equals("weapon") && Ability.values()[i].enabled)
			{
				Ability.WEAPONS.add(Ability.values()[i]);
				Ability.WEAPON_ABILITIES++;
			}
			else if (Ability.values()[i].getCategory().equals("armor") && Ability.values()[i].enabled)
			{
				Ability.ARMORS.add(Ability.values()[i]);
				Ability.ARMOR_ABILITIES++;
			}
		}
	}
}