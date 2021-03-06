/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.forge.randore.item;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FlexibleItemArmor extends ItemArmor implements FlexibleItem, ISpecialArmor {
    private Map<Long, ItemArmor> backers;
    private int index;
    private int renderIndex;
    private EntityEquipmentSlot slot;
    private Components component;

    public FlexibleItemArmor(int index, Components component) {
        super(Randores.ARMOR_DEFAULT, getRenderIndex(component.getSlot()), component.getSlot());
        this.slot = component.getSlot();
        this.renderIndex = getRenderIndex(this.slot);
        this.index = index;
        this.component = component;
        this.backers = new HashMap<Long, ItemArmor>();
    }

    private static int getRenderIndex(EntityEquipmentSlot slot) {
        if (slot == EntityEquipmentSlot.LEGS) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return super.getItemAttributeModifiers(equipmentSlot);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getDurabilityForDisplay(stack);
        }
        return super.getDurabilityForDisplay(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getRGBDurabilityForDisplay(stack);
        }
        return super.getRGBDurabilityForDisplay(stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getMaxDamage(stack);
        }
        return super.getMaxDamage(stack);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).isDamaged(stack);
        }
        return super.isDamaged(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            this.getBacker(Randores.getRandoresSeed(stack)).setDamage(stack, damage);
            return;
        }
        super.setDamage(stack, damage);
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).hasColor(stack);
        }
        return super.hasColor(stack);
    }

    @Override
    public int getColor(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getColor(stack);
        }
        return super.getColor(stack);
    }

    @Override
    public void removeColor(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            this.getBacker(Randores.getRandoresSeed(stack)).removeColor(stack);
            return;
        }
        super.removeColor(stack);
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            this.getBacker(Randores.getRandoresSeed(stack)).setColor(stack, color);
            return;
        }
        super.setColor(stack, color);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.hasBacker(Randores.getRandoresSeed(toRepair))) {
            return this.getBacker(Randores.getRandoresSeed(toRepair)).getIsRepairable(toRepair, repair);
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (this.hasBacker(worldIn)) {
            return this.getBacker(worldIn).onItemRightClick(worldIn, playerIn, handIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).hasOverlay(stack);
        }
        return super.hasOverlay(stack);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getAttributeModifiers(slot, stack);
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "randores_armor:armor." + this.index + "_" + this.renderIndex + ".png";
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getItemEnchantability(stack);
        }
        return super.getItemEnchantability(stack);
    }

    public void registerBacker(long seed, ArmorMaterial material) {
        this.backers.put(seed, new ItemArmor(material, this.renderIndex, this.slot));
    }

    public boolean hasBacker(long seed) {
        return this.backers.containsKey(seed);
    }

    public void removeBacker(long seed) {
        this.backers.remove(seed);
    }

    public ItemArmor getBacker(long seed) {
        return this.backers.get(seed);
    }

    public void registerBacker(World world, ArmorMaterial material) {
        this.registerBacker(Randores.getRandoresSeed(world), material);
    }

    public boolean hasBacker(World world) {
        return this.hasBacker(Randores.getRandoresSeed(world));
    }

    public void removeBacker(World world) {
        this.removeBacker(Randores.getRandoresSeed(world));
    }

    public ItemArmor getBacker(World world) {
        return this.getBacker(Randores.getRandoresSeed(world));
    }

    @Override
    public int index() {
        return this.index;
    }

    @Override
    public MaterialDefinition getDefinition(long seed) {
        return MaterialDefinitionRegistry.get(seed).get(this.index);
    }

    @Override
    public MaterialDefinition getDefinition(World world) {
        return this.getDefinition(Randores.getRandoresSeed(world));
    }

    @Override
    public Components getType() {
        return this.component;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        if(this.hasBacker(player.world)) {
            ItemArmor backer = this.getBacker(player.world);
            return new ArmorProperties(0, backer.damageReduceAmount / 25d, Integer.MAX_VALUE);
        }
        return new ArmorProperties(0, 1, Integer.MAX_VALUE);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        if(this.hasBacker(player.world)) {
            return this.getBacker(player.world).damageReduceAmount;
        }
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        this.setDamage(stack, this.getDamage(stack) + damage);
        if(this.getDamage(stack) >= this.getMaxDamage(stack)) {
            stack.shrink(1);
        }
    }

}
