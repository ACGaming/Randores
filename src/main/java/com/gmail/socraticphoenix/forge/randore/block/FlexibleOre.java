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
package com.gmail.socraticphoenix.forge.randore.block;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlexibleOre extends Block {
    private int index;

    public FlexibleOre(int index) {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.index = index;
    }

    public MaterialDefinition getDefinition(long seed) {
        return MaterialDefinitionRegistry.get(seed).get(this.index);
    }

    public OreComponent getComponent(long seed) {
        return this.getDefinition(seed).getOre();
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        if (world instanceof World) {
            World worldw = (World) world;
            long seed = Randores.getRandoresSeed(worldw);
            Random random = worldw.rand;

            if (this.getComponent(seed).isRequiresSmelting()) {
                ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
                Randores.applyData(stack, worldw);
                drops.add(stack);
            } else {
                int amount = (random.nextInt(this.getComponent(seed).getMaxDrops() - this.getComponent(seed).getMinDrops()) + this.getComponent(seed).getMinDrops()) + (fortune > 0 ? random.nextInt(fortune) * random.nextInt(fortune) : 0);
                ItemStack stack = new ItemStack(this.getDefinition(seed).getMaterial().makeItem());
                Randores.applyData(stack, worldw);
                for (int i = 0; i < amount; i++) {
                    drops.add(stack.copy());
                }
            }
        }
        return drops;
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return this.getComponent(Randores.getRandoresSeed(exploder.getEntityWorld())).getResistance();
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return this.getComponent(Randores.getRandoresSeed(worldIn)).getHardness();
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }


}
