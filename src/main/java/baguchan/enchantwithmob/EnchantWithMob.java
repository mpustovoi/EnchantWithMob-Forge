package baguchan.enchantwithmob;

import baguchan.enchantwithmob.capability.ItemMobEnchantCapability;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.command.MobEnchantingCommand;
import baguchan.enchantwithmob.message.AncientMobMessage;
import baguchan.enchantwithmob.message.MobEnchantFromOwnerMessage;
import baguchan.enchantwithmob.message.MobEnchantedMessage;
import baguchan.enchantwithmob.message.RemoveAllMobEnchantMessage;
import baguchan.enchantwithmob.message.RemoveMobEnchantOwnerMessage;
import baguchan.enchantwithmob.message.SoulParticleMessage;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.registry.ModArgumentTypeInfos;
import baguchan.enchantwithmob.registry.ModEntities;
import baguchan.enchantwithmob.registry.ModItems;
import baguchan.enchantwithmob.registry.ModLootItemFunctions;
import baguchan.enchantwithmob.registry.ModSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EnchantWithMob.MODID)
public class EnchantWithMob {

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "enchantwithmob";
	public static final String NETWORK_PROTOCOL = "2";

	public static Capability<MobEnchantCapability> MOB_ENCHANT_CAP = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static Capability<ItemMobEnchantCapability> ITEM_MOB_ENCHANT_CAP = CapabilityManager.get(new CapabilityToken<>() {
	});
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public EnchantWithMob() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		this.setupMessages();
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MobEnchants.MOB_ENCHANT.register(bus);
		ModArgumentTypeInfos.COMMAND_ARGUMENT_TYPES.register(bus);
		ModEntities.ENTITIES_REGISTRY.register(bus);
		ModItems.ITEM_REGISTRY.register(bus);
		ModSoundEvents.SOUND_EVENTS.register(bus);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);


		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnchantConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EnchantConfig.CLIENT_SPEC);
	}

	private void setup(final FMLCommonSetupEvent event) {
		ModLootItemFunctions.init();
		Raid.RaiderType.create("enchanter", ModEntities.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});
	}

	private void setupMessages() {
		CHANNEL.messageBuilder(MobEnchantedMessage.class, 0)
				.encoder(MobEnchantedMessage::serialize).decoder(MobEnchantedMessage::deserialize)
				.consumerMainThread(MobEnchantedMessage::handle)
				.add();
		CHANNEL.messageBuilder(RemoveAllMobEnchantMessage.class, 1)
				.encoder(RemoveAllMobEnchantMessage::serialize).decoder(RemoveAllMobEnchantMessage::deserialize)
				.consumerMainThread(RemoveAllMobEnchantMessage::handle)
				.add();
		CHANNEL.messageBuilder(MobEnchantFromOwnerMessage.class, 2)
				.encoder(MobEnchantFromOwnerMessage::serialize).decoder(MobEnchantFromOwnerMessage::deserialize)
				.consumerMainThread(MobEnchantFromOwnerMessage::handle)
				.add();
		CHANNEL.messageBuilder(RemoveMobEnchantOwnerMessage.class, 3)
				.encoder(RemoveMobEnchantOwnerMessage::serialize).decoder(RemoveMobEnchantOwnerMessage::deserialize)
				.consumerMainThread(RemoveMobEnchantOwnerMessage::handle)
				.add();
		CHANNEL.messageBuilder(SoulParticleMessage.class, 4)
				.encoder(SoulParticleMessage::serialize).decoder(SoulParticleMessage::deserialize)
				.consumerMainThread(SoulParticleMessage::handle)
				.add();
		CHANNEL.messageBuilder(AncientMobMessage.class, 5)
				.encoder(AncientMobMessage::serialize).decoder(AncientMobMessage::deserialize)
				.consumerMainThread(AncientMobMessage::handle)
				.add();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
	}

	private void processIMC(final InterModProcessEvent event) {
	}

	private void registerCommands(RegisterCommandsEvent evt) {
		MobEnchantingCommand.register(evt.getDispatcher());
	}
}
