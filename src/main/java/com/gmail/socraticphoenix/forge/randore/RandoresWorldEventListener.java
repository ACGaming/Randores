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
package com.gmail.socraticphoenix.forge.randore;

import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandoresWorldEventListener {
    private List<Long> loaded = new ArrayList<Long>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload ev) {
        World world = ev.getWorld();
        if (!world.isRemote) {
            long seed = Randores.getRandoresSeed(world);
            this.loaded.remove(seed);
            if (!this.loaded.contains(seed)) {
                MaterialDefinitionRegistry.remove(seed);
                for (int i = 0; i < 300; i++) {
                    FlexibleItemRegistry.getHoe(i).removeBacker(seed);
                    FlexibleItemRegistry.getSword(i).removeBacker(seed);
                    FlexibleItemRegistry.getAxe(i).removeBacker(seed);
                    FlexibleItemRegistry.getSpade(i).removeBacker(seed);
                    FlexibleItemRegistry.getPickaxe(i).removeBacker(seed);
                    FlexibleItemRegistry.getHelmet(i).removeBacker(seed);
                    FlexibleItemRegistry.getChestplate(i).removeBacker(seed);
                    FlexibleItemRegistry.getLeggings(i).removeBacker(seed);
                    FlexibleItemRegistry.getBoots(i).removeBacker(seed);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load ev) throws IOException {
        Logger logger = Randores.getInstance().getLogger();
        World world = ev.getWorld();
        if (!world.isRemote) {
            long seed = Randores.getRandoresSeed(world);
            if (!MaterialDefinitionRegistry.contains(seed)) {
                logger.info("Generating definitions for Randores seed: " + seed);
                List<MaterialDefinition> definitions = MaterialDefinitionGenerator.makeDefinitions(MaterialDefinitionGenerator.generateColors(new Random(seed)), seed);
                MaterialDefinitionGenerator.logStatistics(definitions);
                MaterialDefinitionRegistry.put(seed, definitions);
                for (int i = 0; i < 300; i++) {
                    Item.ToolMaterial toolMaterial = definitions.get(i).getToolMaterial();
                    ItemArmor.ArmorMaterial armorMaterial = definitions.get(i).getArmorMaterial();
                    FlexibleItemRegistry.getHoe(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getSword(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getAxe(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getSpade(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getPickaxe(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getHelmet(i).registerBacker(seed, armorMaterial);
                    FlexibleItemRegistry.getChestplate(i).registerBacker(seed, armorMaterial);
                    FlexibleItemRegistry.getLeggings(i).registerBacker(seed, armorMaterial);
                    FlexibleItemRegistry.getBoots(i).registerBacker(seed, armorMaterial);
                }
            }
            this.loaded.add(seed);
        }
    }

}
