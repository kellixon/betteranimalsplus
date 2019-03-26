package its_meow.betteranimalsplus.init;

import static its_meow.betteranimalsplus.init.ModBlocks.genericskulls;
import static its_meow.betteranimalsplus.init.ModBlocks.handoffate;
import static its_meow.betteranimalsplus.init.ModBlocks.hirschgeistskull;
import static its_meow.betteranimalsplus.init.ModBlocks.trillium;
import static its_meow.betteranimalsplus.init.ModEntities.*;
import static its_meow.betteranimalsplus.init.ModItems.antler;
import static its_meow.betteranimalsplus.init.ModItems.goatCheese;
import static its_meow.betteranimalsplus.init.ModItems.goatMilk;
import static its_meow.betteranimalsplus.init.ModItems.itemHirschgeistSkullWearable;
import static its_meow.betteranimalsplus.init.ModItems.pheasantCooked;
import static its_meow.betteranimalsplus.init.ModItems.pheasantRaw;
import static its_meow.betteranimalsplus.init.ModItems.venisonCooked;
import static its_meow.betteranimalsplus.init.ModItems.venisonRaw;

import com.google.common.base.Preconditions;

import its_meow.betteranimalsplus.Ref;
import its_meow.betteranimalsplus.common.block.BlockGenericSkull;
import its_meow.betteranimalsplus.common.entity.EntityLammergeier;
import its_meow.betteranimalsplus.common.entity.projectile.EntityTarantulaHair;
import its_meow.betteranimalsplus.common.tileentity.TileEntityHandOfFate;
import its_meow.betteranimalsplus.common.tileentity.TileEntityHirschgeistSkull;
import its_meow.betteranimalsplus.common.tileentity.TileEntityTrillium;
import its_meow.betteranimalsplus.util.EntityContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Ref.MOD_ID)
public class BetterAnimalsPlusRegistrar {
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();

        ModBlocks.addGenericSkull(ModBlocks.deerhead);
        ModBlocks.addGenericSkull(ModBlocks.wolfhead);
        ModBlocks.addGenericSkull(ModBlocks.reindeerhead);
        ModBlocks.addGenericSkull(ModBlocks.foxhead);
        ModBlocks.addGenericSkull(ModBlocks.boarhead);

        registry.registerAll(trillium, hirschgeistskull, handoffate);

        genericskulls.keySet().forEach(b -> registry.register(b));

        GameRegistry.registerTileEntity(TileEntityTrillium.class, new ResourceLocation(trillium.getRegistryName() + "tileentity"));
        GameRegistry.registerTileEntity(TileEntityHirschgeistSkull.class, new ResourceLocation(hirschgeistskull.getRegistryName() + "tileentity"));
        GameRegistry.registerTileEntity(TileEntityHandOfFate.class, new ResourceLocation(handoffate.getRegistryName() + "tileentity"));

        for (BlockGenericSkull block : genericskulls.keySet()) {
            GameRegistry.registerTileEntity(block.teClass, new ResourceLocation(block.getRegistryName() + "tileentity"));
        }
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        
        // ItemBlocks
        
        final ItemBlock[] items = { new ItemBlock(trillium), hirschgeistskull.getItemBlock(), new ItemBlock(handoffate)};

        final IForgeRegistry<Item> registry = event.getRegistry();

        for (final ItemBlock item : items) {
            final Block block = item.getBlock();
            final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
            registry.register(item.setRegistryName(registryName));
        }

        for (final ItemBlock item : genericskulls.values()) {
            final Block block = item.getBlock();
            final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
            registry.register(item.setRegistryName(registryName));
        }
        
        // Items
        
        registry.registerAll(venisonRaw, venisonCooked, itemHirschgeistSkullWearable, antler, goatMilk, goatCheese, pheasantRaw, pheasantCooked);

    }
    
    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
        final IForgeRegistry<EntityEntry> registry = event.getRegistry();

        for (EntityContainer container : entityList) {
            if (container.doRegister)
                reg(container);
        }
        EntitySpawnPlacementRegistry.setPlacementType(EntityLammergeier.class, SpawnPlacementType.IN_AIR);
        register(EntityTarantulaHair.class, "tarantulahair");

        if (!entrySet.isEmpty()) {
            for (final EntityEntry entityEntry : entrySet) {
                registry.register(entityEntry);
            }
        }
    }
    
    
    
    // Entity Registration Helpers
    
    private static final String LOCALIZE_PREFIX = Ref.MOD_ID + ".";
    
    public static void reg(EntityContainer c) {
        if (c.doSpawning) {
            registerWithSpawnAndEgg(c.entityClazz, c.entityName, c.eggColorSolid, c.eggColorSpot, c.type, c.weight, c.minGroup, c.maxGroup, c.spawnBiomes);
        } else {
            registerWithEgg(c.entityClazz, c.entityName, c.eggColorSolid, c.eggColorSpot);
        }
    }

    public static void registerWithSpawnAndEgg(Class<? extends Entity> EntityClass, String entityNameIn, int solidColorIn, int spotColorIn, EnumCreatureType typeIn, int prob, int min, int max, Biome[] biomes) {
        EntityEntry entry = EntityEntryBuilder.create().entity(EntityClass).id(new ResourceLocation(Ref.MOD_ID, entityNameIn), modEntities++).name(LOCALIZE_PREFIX + entityNameIn).tracker(64, 1, true).egg(solidColorIn, spotColorIn).spawn(typeIn, prob, min, max, biomes).build();
        if (typeIn == EnumCreatureType.WATER_CREATURE) {
            EntitySpawnPlacementRegistry.setPlacementType(EntityClass, SpawnPlacementType.IN_WATER);
        }
        entrySet.add(entry);
    }

    public static void registerWithEgg(Class<? extends Entity> EntityClass, String entityNameIn, int solidColorIn, int spotColorIn) {
        EntityEntry entry = EntityEntryBuilder.create().entity(EntityClass).id(new ResourceLocation(Ref.MOD_ID, entityNameIn), modEntities++).name(LOCALIZE_PREFIX + entityNameIn).tracker(64, 1, true).egg(solidColorIn, spotColorIn).build();
        entrySet.add(entry);
    }

    public static void register(Class<? extends Entity> EntityClass, String entityNameIn) {
        EntityEntry entry = EntityEntryBuilder.create().entity(EntityClass).id(new ResourceLocation(Ref.MOD_ID, entityNameIn), modEntities++).name(LOCALIZE_PREFIX + entityNameIn).tracker(64, 1, true).build();

        entrySet.add(entry);
    }
    
}