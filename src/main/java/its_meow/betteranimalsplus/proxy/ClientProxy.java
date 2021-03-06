package its_meow.betteranimalsplus.proxy;

import its_meow.betteranimalsplus.EventHandlerClient;
import its_meow.betteranimalsplus.Ref;
import its_meow.betteranimalsplus.block.TileEntityHandOfFate;
import its_meow.betteranimalsplus.block.TileEntityHirschgeistSkull;
import its_meow.betteranimalsplus.block.TileEntityTrillium;
import its_meow.betteranimalsplus.block.render.RenderBlockHandOfFate;
import its_meow.betteranimalsplus.block.render.RenderBlockHirschgeistSkull;
import its_meow.betteranimalsplus.block.render.RenderBlockTrillium;
import its_meow.betteranimalsplus.entity.model.ModelHirschgeistSkullArmorPiece;
import its_meow.betteranimalsplus.registry.BlockRegistry;
import its_meow.betteranimalsplus.registry.MobRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	public static final ModelHirschgeistSkullArmorPiece armorModel = new ModelHirschgeistSkullArmorPiece(0.0625F);
	
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MobRegistry.initModels();
	}

	public void init(FMLInitializationEvent event) {
		super.init(event);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrillium.class, new RenderBlockTrillium());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHirschgeistSkull.class, new RenderBlockHirschgeistSkull());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHandOfFate.class, new RenderBlockHandOfFate());
	}
    
	public static ModelBiped getArmorModel(){
		return armorModel;
	}
	
}
