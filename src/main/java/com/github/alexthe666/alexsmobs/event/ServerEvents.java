package com.github.alexthe666.alexsmobs.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.antlr.v4.runtime.misc.Triple;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.EffectClinging;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import com.github.alexthe666.alexsmobs.entity.EntityElephant;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.entity.EntityEndergrade;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.github.alexthe666.alexsmobs.entity.EntityFlyingFish;
import com.github.alexthe666.alexsmobs.entity.EntityGiantSquid;
import com.github.alexthe666.alexsmobs.entity.EntityJerboa;
import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.EntityMoose;
import com.github.alexthe666.alexsmobs.entity.EntitySeaBear;
import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.github.alexthe666.alexsmobs.entity.EntitySnowLeopard;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.alexsmobs.entity.util.FlyingFishBootsUtil;
import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import com.github.alexthe666.alexsmobs.entity.util.RockyChestplateUtil;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemFalconryGlove;
import com.github.alexthe666.alexsmobs.message.MessageSwingArm;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.EmeraldsForItemsTrade;
import com.github.alexthe666.alexsmobs.misc.ItemsForEmeraldsTrade;
import com.github.alexthe666.alexsmobs.world.AMWorldData;
import com.github.alexthe666.alexsmobs.world.BeachedCachalotWhaleSpawner;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber(modid = AlexsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    public static final UUID CARRO_UUID = UUID.fromString("98905d4a-1cbc-41a4-9ded-2300404e2290");
    private static final UUID SAND_SPEED_MODIFIER = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF28E");
    private static final UUID SNEAK_SPEED_MODIFIER = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF28F");
    private static final AttributeModifier SAND_SPEED_BONUS = new AttributeModifier(SAND_SPEED_MODIFIER, "roadrunner speed bonus", 0.1F, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier SNEAK_SPEED_BONUS = new AttributeModifier(SNEAK_SPEED_MODIFIER, "frontier cap speed bonus", 0.1F, AttributeModifier.Operation.ADDITION);
    private static final Map<ServerLevel, BeachedCachalotWhaleSpawner> BEACHED_CACHALOT_WHALE_SPAWNER_MAP = new HashMap<>();
    public static List<Triple<ServerPlayer, ServerLevel, BlockPos>> teleportPlayers = new ArrayList<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick) {
        if (!tick.world.isClientSide && tick.world instanceof ServerLevel serverWorld) {
            BEACHED_CACHALOT_WHALE_SPAWNER_MAP.computeIfAbsent(serverWorld,
                k -> new BeachedCachalotWhaleSpawner(serverWorld));
            BeachedCachalotWhaleSpawner spawner = BEACHED_CACHALOT_WHALE_SPAWNER_MAP.get(serverWorld);
            spawner.tick();

            for (final var triple : teleportPlayers) {
                ServerPlayer player = triple.a;
                ServerLevel endpointWorld = triple.b;
                BlockPos endpoint = triple.c;
                player.teleportTo(endpointWorld, endpoint.getX() + 0.5D, endpoint.getY() + 0.5D, endpoint.getZ() + 0.5D, player.getYRot(), player.getXRot());
                ChunkPos chunkpos = new ChunkPos(endpoint);
                endpointWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getId());
                player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
            }
            teleportPlayers.clear();
        }
        AMWorldData data = AMWorldData.get(tick.world);
        if (data != null) {
            data.tickPupfish();
        }
    }

    protected static BlockHitResult rayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        final float x = player.getXRot();
        final float y = player.getYRot();
        Vec3 vector3d = player.getEyePosition(1.0F);
        final float f2 = Mth.cos(-y * ((float) Math.PI / 180F) - (float) Math.PI);
        final float f3 = Mth.sin(-y * ((float) Math.PI / 180F) - (float) Math.PI);
        final float f4 = -Mth.cos(-x * ((float) Math.PI / 180F));
        final float f5 = Mth.sin(-x * ((float) Math.PI / 180F));
        final float f6 = f3 * f4;
        final float f7 = f2 * f4;
        final double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        Vec3 vector3d1 = vector3d.add(f6 * d0, f5 * d0, f7 * d0);
        return worldIn.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, fluidMode, player));
    }


    private static final Random RAND = new Random();

    @SubscribeEvent
    public static void onItemUseLast(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().getItem() == Items.CHORUS_FRUIT && RAND.nextInt(3) == 0
            && event.getEntityLiving().hasEffect(AMEffectRegistry.ENDER_FLU)) {
            event.getEntityLiving().removeEffect(AMEffectRegistry.ENDER_FLU);
        }
    }

    @SubscribeEvent
    public static void onEntityResize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player entity) {
            final var potions = entity.getActiveEffectsMap();
            if (event.getEntity().level != null && potions != null && !potions.isEmpty()
                && potions.containsKey(AMEffectRegistry.CLINGING)) {
                if (EffectClinging.isUpsideDown(entity)) {
                    float minus = event.getOldSize().height - event.getOldEyeHeight();
                    event.setNewEyeHeight(minus);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (AMConfig.giveBookOnStartup) {
            CompoundTag playerData = event.getPlayer().getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            if (data != null && !data.getBoolean("alexsmobs_has_book")) {
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), new ItemStack(AMItemRegistry.ANIMAL_DICTIONARY.get()));
                if (Objects.equals(event.getPlayer().getUUID(), ALEX_UUID)
                        || Objects.equals(event.getPlayer().getUUID(), CARRO_UUID)) {
                    ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), new ItemStack(AMItemRegistry.BEAR_DUST.get()));
                }
                if (Objects.equals(event.getPlayer().getUUID(), ALEX_UUID)) {
                    ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), new ItemStack(AMItemRegistry.NOVELTY_HAT.get()));
                }
                data.putBoolean("alexsmobs_has_book", true);
                playerData.put(Player.PERSISTED_NBT_TAG, data);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        ItemFalconryGlove.onLeftClick(event.getPlayer(), event.getPlayer().getOffhandItem());
        ItemFalconryGlove.onLeftClick(event.getPlayer(), event.getPlayer().getMainHandItem());
        if (event.getWorld().isClientSide) {
            AlexsMobs.sendMSGToServer(MessageSwingArm.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity().getType() == EntityType.SQUID && !event.getEntity().getLevel().isClientSide) {
            ServerLevel level = (ServerLevel) event.getEntity().getLevel();
            event.setCanceled(true);
            EntityGiantSquid squid = AMEntityRegistry.GIANT_SQUID.get().create(level);
            squid.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity().getYRot(), event.getEntity().getXRot());
            squid.finalizeSpawn(level, level.getCurrentDifficultyAt(squid.blockPosition()), MobSpawnType.CONVERSION, null, null);
            if (event.getEntity().hasCustomName()) {
                squid.setCustomName(event.getEntity().getCustomName());
                squid.setCustomNameVisible(event.getEntity().isCustomNameVisible());
            }
            squid.setBlue(true);
            squid.setPersistenceRequired();
            level.addFreshEntityWithPassengers(squid);
            event.getEntity().discard();
        }
    }

    @SubscribeEvent
    public void onProjectileHit(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult hitResult
            && hitResult.getEntity() instanceof EntityEmu emu && !event.getEntity().level.isClientSide) {
            if (event.getEntity() instanceof AbstractArrow arrow) {
                //fixes soft crash with vanilla
                arrow.setPierceLevel((byte) 0);
            }
            if ((emu.getAnimation() == EntityEmu.ANIMATION_DODGE_RIGHT || emu.getAnimation() == EntityEmu.ANIMATION_DODGE_LEFT) && emu.getAnimationTick() < 7) {
                event.setCanceled(true);
            }
            if (emu.getAnimation() != EntityEmu.ANIMATION_DODGE_RIGHT && emu.getAnimation() != EntityEmu.ANIMATION_DODGE_LEFT) {
                boolean left = true;
                Vec3 arrowPos = event.getEntity().position();
                Vec3 rightVector = emu.getLookAngle().yRot(0.5F * (float) Math.PI).add(emu.position());
                Vec3 leftVector = emu.getLookAngle().yRot(-0.5F * (float) Math.PI).add(emu.position());
                if (arrowPos.distanceTo(rightVector) < arrowPos.distanceTo(leftVector)) {
                    left = false;
                } else if (arrowPos.distanceTo(rightVector) > arrowPos.distanceTo(leftVector)) {
                    left = true;
                } else {
                    left = emu.getRandom().nextBoolean();
                }
                Vec3 vector3d2 = event.getEntity().getDeltaMovement().yRot((float) ((left ? -0.5F : 0.5F) * Math.PI)).normalize();
                emu.setAnimation(left ? EntityEmu.ANIMATION_DODGE_LEFT : EntityEmu.ANIMATION_DODGE_RIGHT);
                emu.hasImpulse = true;
                if (!emu.horizontalCollision) {
                    emu.move(MoverType.SELF, new Vec3(vector3d2.x() * 0.25F, 0.1F, vector3d2.z() * 0.25F));
                }
                if (!event.getEntity().level.isClientSide) {
                    if (event.getEntity() instanceof Projectile projectile) {
                        if (projectile.getOwner() instanceof ServerPlayer serverPlayer) {
                            AMAdvancementTriggerRegistry.EMU_DODGE.trigger(serverPlayer);
                        }
                    }
                }
                emu.setDeltaMovement(emu.getDeltaMovement().add(vector3d2.x() * 0.5F, 0.32F, vector3d2.z() * 0.5F));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDespawnAttempt(LivingSpawnEvent.AllowDespawn event) {
        if (event.getEntityLiving().hasEffect(AMEffectRegistry.DEBILITATING_STING) && event.getEntityLiving().getEffect(AMEffectRegistry.DEBILITATING_STING) != null && event.getEntityLiving().getEffect(AMEffectRegistry.DEBILITATING_STING).getAmplifier() > 0) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onTradeSetup(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FISHERMAN) {
            VillagerTrades.ItemListing ambergrisTrade = new EmeraldsForItemsTrade(AMItemRegistry.AMBERGRIS.get(), 20, 3, 4);
            final var list = event.getTrades().get(2);
            list.add(ambergrisTrade);
            event.getTrades().put(2, list);
        }
    }

    @SubscribeEvent
    public void onWanderingTradeSetup(WandererTradesEvent event) {
        if (AMConfig.wanderingTraderOffers) {
            List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
            List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.ANIMAL_DICTIONARY.get(), 4, 1, 2, 1));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.ACACIA_BLOSSOM.get(), 3, 2, 2, 1));
            if (AMConfig.cockroachSpawnWeight > 0) {
                genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.COCKROACH_OOTHECA.get(), 2, 1, 2, 1));
            }
            if (AMConfig.blobfishSpawnWeight > 0) {
                genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.BLOBFISH_BUCKET.get(), 4, 1, 3, 1));
            }
            if (AMConfig.crocodileSpawnWeight > 0) {
                genericTrades.add(new ItemsForEmeraldsTrade(AMBlockRegistry.CROCODILE_EGG.get().asItem(), 6, 1, 2, 1));
            }
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.BEAR_FUR.get(), 1, 1, 2, 1));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.CROCODILE_SCUTE.get(), 5, 1, 2, 1));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.ROADRUNNER_FEATHER.get(), 1, 2, 2, 2));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.MOSQUITO_LARVA.get(), 1, 3, 5, 1));
            rareTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.SOMBRERO.get(), 20, 1, 1, 1));
            rareTrades.add(new ItemsForEmeraldsTrade(AMBlockRegistry.BANANA_PEEL.get(), 1, 2, 1, 1));
            rareTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.BLOOD_SAC.get(), 5, 2, 3, 1));
        }
    }

    @SubscribeEvent
    public void onLootLevelEvent(LootingLevelEvent event) {
        DamageSource src = event.getDamageSource();
        if (src != null) {
            if (src.getEntity() instanceof EntitySnowLeopard) {
                event.setLootingLevel(event.getLootingLevel() + 2);
            }
        }

    }

    @SubscribeEvent
    public void onUseItem(PlayerInteractEvent.RightClickItem event) {
        final var player = event.getPlayer();
        if (event.getItemStack().getItem() == Items.WHEAT && player.getVehicle() instanceof EntityElephant elephant) {
            if (elephant.triggerCharge(event.getItemStack())) {
                player.swing(event.getHand());
                if (!player.isCreative()) {
                    event.getItemStack().shrink(1);
                }
            }
        }
        if (event.getItemStack().getItem() == Items.GLASS_BOTTLE && AMConfig.lavaBottleEnabled) {
            HitResult raytraceresult = rayTrace(event.getWorld(), player, ClipContext.Fluid.SOURCE_ONLY);
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
                if (event.getWorld().mayInteract(player, blockpos)) {
                    if (event.getWorld().getFluidState(blockpos).is(FluidTags.LAVA)) {
                        event.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
                        player.setSecondsOnFire(6);
                        if (!player.addItem(new ItemStack(AMItemRegistry.LAVA_BOTTLE.get()))) {
                            player.spawnAtLocation(new ItemStack(AMItemRegistry.LAVA_BOTTLE.get()));
                        }
                        player.swing(event.getHand());
                        if (!player.isCreative()) {
                            event.getItemStack().shrink(1);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity living) {
            if (!event.getPlayer().isShiftKeyDown() && VineLassoUtil.hasLassoData(living)) {
                if (!event.getEntity().level.isClientSide) {
                    event.getTarget().spawnAtLocation(new ItemStack(AMItemRegistry.VINE_LASSO.get()));
                }
                VineLassoUtil.lassoTo(null, living);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
            if (!(event.getTarget() instanceof Player) && !(event.getTarget() instanceof EntityEndergrade)
                    && living.hasEffect(AMEffectRegistry.ENDER_FLU)) {
                if (event.getItemStack().getItem() == Items.CHORUS_FRUIT) {
                    if (!event.getPlayer().isCreative()) {
                        event.getItemStack().shrink(1);
                    }
                    event.getTarget().playSound(SoundEvents.GENERIC_EAT, 1.0F, 0.5F + event.getPlayer().getRandom().nextFloat());
                    if (event.getPlayer().getRandom().nextFloat() < 0.4F) {
                        living.removeEffect(AMEffectRegistry.ENDER_FLU);
                        Items.CHORUS_FRUIT.finishUsingItem(event.getItemStack().copy(), event.getWorld(), ((LivingEntity) event.getTarget()));
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
            if (RainbowUtil.getRainbowType(living) > 0 && (event.getItemStack().getItem() == Items.SPONGE)) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                RainbowUtil.setRainbowType(living, 0);
                if (!event.getPlayer().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                ItemStack wetSponge = new ItemStack(Items.WET_SPONGE);
                if (!event.getPlayer().addItem(wetSponge)) {
                    event.getPlayer().drop(wetSponge, true);
                }
            }
            if (living instanceof Rabbit rabbit && event.getItemStack().getItem() == AMItemRegistry.MUNGAL_SPORES.get()
                    && AMConfig.bunfungusTransformation) {
                final var random = ThreadLocalRandom.current();
                if (!event.getEntityLiving().level.isClientSide && random.nextFloat() < 0.15F) {
                    final EntityBunfungus bunfungus = rabbit.convertTo(AMEntityRegistry.BUNFUNGUS.get(), true);
                    if (bunfungus != null) {
                        event.getPlayer().level.addFreshEntity(bunfungus);
                        bunfungus.setTransformsIn(EntityBunfungus.MAX_TRANSFORM_TIME);
                    }
                } else {
                    for (int i = 0; i < 2 + random.nextInt(2); i++) {
                        final double d0 = random.nextGaussian() * 0.02D;
                        final double d1 = 0.05F + random.nextGaussian() * 0.02D;
                        final double d2 = random.nextGaussian() * 0.02D;
                        event.getTarget().level.addParticle(AMParticleRegistry.BUNFUNGUS_TRANSFORMATION, event.getTarget().getRandomX(0.7F), event.getTarget().getY(0.6F), event.getTarget().getRandomZ(0.7F), d0, d1, d2);
                    }
                }
                if (!event.getPlayer().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public void onUseItemAir(PlayerInteractEvent.RightClickEmpty event) {
        ItemStack stack = event.getPlayer().getItemInHand(event.getHand());
        if (stack.isEmpty()) {
            stack = event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND);
        }
        if (RainbowUtil.getRainbowType(event.getPlayer()) > 0 && (stack.is(Items.SPONGE))) {
            event.getPlayer().swing(InteractionHand.MAIN_HAND);
            RainbowUtil.setRainbowType(event.getPlayer(), 0);
            if (!event.getPlayer().isCreative()) {
                stack.shrink(1);
            }
            ItemStack wetSponge = new ItemStack(Items.WET_SPONGE);
            if (!event.getPlayer().addItem(wetSponge)) {
                event.getPlayer().drop(wetSponge, true);
            }
        }
    }

    @SubscribeEvent
    public void onUseItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if (AlexsMobs.isAprilFools() && event.getItemStack().is(Items.STICK)
            && !event.getPlayer().getCooldowns().isOnCooldown(Items.STICK)) {
            BlockState state = event.getPlayer().level.getBlockState(event.getPos());
            boolean flag = false;
            if (state.is(Blocks.SAND)) {
                flag = true;
                event.getPlayer().getLevel().setBlockAndUpdate(event.getPos(), AMBlockRegistry.SAND_CIRCLE.get().defaultBlockState());
            } else if (state.is(Blocks.RED_SAND)) {
                flag = true;
                event.getPlayer().getLevel().setBlockAndUpdate(event.getPos(), AMBlockRegistry.RED_SAND_CIRCLE.get().defaultBlockState());
            }
            if (flag) {
                event.setCanceled(true);
                event.getPlayer().playSound(SoundEvents.SAND_BREAK, 1, 1);
                event.getPlayer().getCooldowns().addCooldown(Items.STICK, 30);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDrops(LivingDropsEvent event) {
        if (VineLassoUtil.hasLassoData(event.getEntityLiving())) {
            VineLassoUtil.lassoTo(null, event.getEntityLiving());
            event.getDrops().add(new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(AMItemRegistry.VINE_LASSO.get())));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity() instanceof WanderingTrader trader && AMConfig.elephantTraderSpawnChance > 0) {
            Biome biome = event.getWorld().getBiome(event.getEntity().blockPosition()).value();
            if (RAND.nextFloat() <= AMConfig.elephantTraderSpawnChance
                && (!AMConfig.limitElephantTraderBiomes || biome.getBaseTemperature() >= 1.0F)) {
                EntityElephant elephant = AMEntityRegistry.ELEPHANT.get().create(trader.level);
                elephant.copyPosition(trader);
                if (elephant.canSpawnWithTraderHere()) {
                    elephant.setTrader(true);
                    elephant.setChested(true);
                    if (!event.getWorld().isClientSide()) {
                        trader.level.addFreshEntity(elephant);
                        trader.startRiding(elephant, true);
                    }
                    elephant.addElephantLoot(null, RAND.nextInt());
                }
            }
        }
        try {
            if (event.getEntity() instanceof final Spider spider && AMConfig.spidersAttackFlies) {
                spider.targetSelector.addGoal(4,
                    new NearestAttackableTargetGoal<>(spider, EntityFly.class, 1, true, false, null));
            }
            else if (event.getEntity() instanceof final Wolf wolf && AMConfig.wolvesAttackMoose) {
                wolf.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(wolf, EntityMoose.class, false, null));
            }
            else if (event.getEntity() instanceof final PolarBear bear && AMConfig.polarBearsAttackSeals) {
                bear.targetSelector.addGoal(6,
                    new NearestAttackableTargetGoal<>(bear, EntitySeal.class, 15, true, true, null));
            }
            else if (event.getEntity() instanceof final Creeper creeper) {
                creeper.targetSelector.addGoal(3, new AvoidEntityGoal<>(creeper, EntitySnowLeopard.class, 6.0F, 1.0D, 1.2D));
                creeper.targetSelector.addGoal(3, new AvoidEntityGoal<>(creeper, EntityTiger.class, 6.0F, 1.0D, 1.2D));
            }
            else if ((event.getEntity() instanceof Fox || event.getEntity() instanceof Cat
                || event.getEntity() instanceof Ocelot) && AMConfig.catsAndFoxesAttackJerboas) {
                Mob mb = (Mob) event.getEntity();
                mb.targetSelector.addGoal(6,
                    new NearestAttackableTargetGoal<>(mb, EntityJerboa.class, 45, true, true, null));
            }
            else if (event.getEntity() instanceof final Rabbit rabbit && AMConfig.bunfungusTransformation) {
                rabbit.goalSelector.addGoal(3, new TemptGoal(rabbit, 1.0D, Ingredient.of(AMItemRegistry.MUNGAL_SPORES.get()), false));
            }
            else if (event.getEntity() instanceof final Dolphin dolphin && AMConfig.dolphinsAttackFlyingFish) {
                dolphin.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(dolphin, EntityFlyingFish.class, 70, true, true, null));
            }
        } catch (Exception e) {
            AlexsMobs.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void onPlayerAttackEntityEvent(AttackEntityEvent event) {
        if (event.getTarget() instanceof LivingEntity living) {
            if (event.getPlayer().getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.MOOSE_HEADGEAR.get()) {
                living.knockback(1F, Mth.sin(event.getPlayer().getYRot() * ((float) Math.PI / 180F)),
                        -Mth.cos(event.getPlayer().getYRot() * ((float) Math.PI / 180F)));
            }
            if (event.getPlayer().hasEffect(AMEffectRegistry.TIGERS_BLESSING)
                    && !event.getTarget().isAlliedTo(event.getPlayer()) && !(event.getTarget() instanceof EntityTiger)) {
                AABB bb = new AABB(event.getPlayer().getX() - 32, event.getPlayer().getY() - 32, event.getPlayer().getZ() - 32, event.getPlayer().getZ() + 32, event.getPlayer().getY() + 32, event.getPlayer().getZ() + 32);
                final var tigers = event.getPlayer().level.getEntitiesOfClass(EntityTiger.class, bb,
                        EntitySelector.ENTITY_STILL_ALIVE);
                for (EntityTiger tiger : tigers) {
                    if (!tiger.isBaby()) {
                        tiger.setTarget(living);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof final LivingEntity attacker) {
            if (event.getAmount() > 0 && attacker.hasEffect(AMEffectRegistry.SOULSTEAL) && attacker.getEffect(AMEffectRegistry.SOULSTEAL) != null) {
                final int level = attacker.getEffect(AMEffectRegistry.SOULSTEAL).getAmplifier() + 1;
                if (attacker.getHealth() < attacker.getMaxHealth()
                    && ThreadLocalRandom.current().nextFloat() < (0.25F + (level * 0.25F))) {
                    attacker.heal(Math.min(event.getAmount() / 2F * level, 2 + 2 * level));
                }
            }

            if (event.getEntityLiving() instanceof final Player player) {
                if (attacker instanceof final EntityMimicOctopus octupus && octupus.isOwnedBy(player)) {
                    event.setCanceled(true);
                    return;
                }
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SPIKED_TURTLE_SHELL.get()) {
                    if (attacker.distanceTo(player) < attacker.getBbWidth() + player.getBbWidth() + 0.5F) {
                        attacker.hurt(DamageSource.thorns(player), 1F);
                        attacker.knockback(0.5F, Mth.sin((attacker.getYRot() + 180) * ((float) Math.PI / 180F)),
                            -Mth.cos((attacker.getYRot() + 180) * ((float) Math.PI / 180F)));
                    }
                }
            }
        }
        if (!event.getEntityLiving().getItemBySlot(EquipmentSlot.LEGS).isEmpty() && event.getEntityLiving().getItemBySlot(EquipmentSlot.LEGS).getItem() == AMItemRegistry.EMU_LEGGINGS.get()) {
            if (event.getSource().isProjectile() && event.getEntityLiving().getRandom().nextFloat() < AMConfig.emuPantsDodgeChance) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onStructureGetSpawnLists(StructureSpawnListGatherEvent event) {
        //EVENT IS UNUSED IN 1.18.2
      /*  if (AMConfig.mimicubeSpawnInEndCity && AMConfig.mimicubeSpawnWeight > 0) {
            if (event.getStructure() == StructureFeature.END_CITY) {
                event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MIMICUBE.get(), AMConfig.mimicubeSpawnWeight, 1, 3));
            }
        }
        if (AMConfig.soulVultureSpawnOnFossil && AMConfig.soulVultureSpawnWeight > 0) {
            if (event.getStructure() == StructureFeature.NETHER_FOSSIL) {
                event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SOUL_VULTURE.get(), AMConfig.soulVultureSpawnWeight, 1, 1));
            }
        }
        if (AMConfig.restrictSkelewagSpawns && AMConfig.skelewagSpawnWeight > 0) {
            if (event.getStructure() == StructureFeature.SHIPWRECK) {
                event.setInsideOnly(false);
                event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SKELEWAG.get(), AMConfig.skelewagSpawnWeight, 1, 2));
            }
        }

       */
    }

    @SubscribeEvent
    public void onLivingSetTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() instanceof Mob mob) {
            if (mob.getMobType() == MobType.ARTHROPOD) {
                if (event.getTarget().hasEffect(AMEffectRegistry.BUG_PHEROMONES) && event.getEntityLiving().getLastHurtByMob() != event.getTarget()) {
                    mob.setTarget(null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (player.getEyeHeight() < player.getBbHeight() * 0.5D) {
                player.refreshDimensions();
            }
            final var attributes = event.getEntityLiving().getAttribute(Attributes.MOVEMENT_SPEED);
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == AMItemRegistry.ROADDRUNNER_BOOTS.get()
                || attributes.hasModifier(SAND_SPEED_BONUS)) {
                final boolean sand = player.level.getBlockState(getDownPos(player.blockPosition(), player.level))
                    .is(BlockTags.SAND);
                if (sand && !attributes.hasModifier(SAND_SPEED_BONUS)) {
                    attributes.addPermanentModifier(SAND_SPEED_BONUS);
                }
                if (player.tickCount % 25 == 0
                    && (player.getItemBySlot(EquipmentSlot.FEET).getItem() != AMItemRegistry.ROADDRUNNER_BOOTS.get()
                        || !sand)
                    && attributes.hasModifier(SAND_SPEED_BONUS)) {
                    attributes.removeModifier(SAND_SPEED_BONUS);
                }
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.FRONTIER_CAP.get()
                || attributes.hasModifier(SNEAK_SPEED_BONUS)) {
                final var shift = player.isShiftKeyDown();
                if (shift && !attributes.hasModifier(SNEAK_SPEED_BONUS)) {
                    attributes.addPermanentModifier(SNEAK_SPEED_BONUS);
                }
                if ((!shift || player.getItemBySlot(EquipmentSlot.HEAD).getItem() != AMItemRegistry.FRONTIER_CAP.get())
                    && attributes.hasModifier(SNEAK_SPEED_BONUS)) {
                    attributes.removeModifier(SNEAK_SPEED_BONUS);
                }
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SPIKED_TURTLE_SHELL.get()) {
                if (!player.isEyeInFluid(FluidTags.WATER)) {
                    player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 210, 0, false, false, true));
                }
            }
        }
        final ItemStack boots = event.getEntityLiving().getItemBySlot(EquipmentSlot.FEET);
        if (!boots.isEmpty() && boots.hasTag() && boots.getOrCreateTag().contains("BisonFur") && boots.getOrCreateTag().getBoolean("BisonFur")) {
            BlockPos pos = new BlockPos(event.getEntityLiving().getX(), event.getEntity().getY() - 0.5F, event.getEntityLiving().getZ());
            if (event.getEntityLiving().level.getBlockState(pos).is(Blocks.POWDER_SNOW)) {
                event.getEntityLiving().setOnGround(true);
                event.getEntityLiving().setTicksFrozen(0);

            }
            if (event.getEntityLiving().isInPowderSnow) {
                event.getEntityLiving().setPos(event.getEntityLiving().getX(), pos.getY() + 1, event.getEntityLiving().getZ());
            }
        }

        if (event.getEntityLiving().getItemBySlot(EquipmentSlot.LEGS).getItem() == AMItemRegistry.CENTIPEDE_LEGGINGS.get()) {
            if (event.getEntityLiving().horizontalCollision && !event.getEntityLiving().isInWater()) {
                event.getEntityLiving().fallDistance = 0.0F;
                Vec3 motion = event.getEntityLiving().getDeltaMovement();
                double d2 = 0.1D;
                if (event.getEntityLiving().isShiftKeyDown() || !event.getEntityLiving().getFeetBlockState().isScaffolding(event.getEntityLiving()) && event.getEntityLiving().isSuppressingSlidingDownLadder()) {
                    d2 = 0.0D;
                }
                motion = new Vec3(Mth.clamp(motion.x, -0.15F, 0.15F), d2, Mth.clamp(motion.z, -0.15F, 0.15F));
                event.getEntityLiving().setDeltaMovement(motion);
            }
        }
        if (event.getEntityLiving().getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SOMBRERO.get() && !event.getEntityLiving().level.isClientSide && AlexsMobs.isAprilFools() && event.getEntityLiving().isInWaterOrBubble()) {
            Random random = event.getEntityLiving().getRandom();
            if (random.nextInt(245) == 0 && !EntitySeaBear.isMobSafe(event.getEntityLiving())) {
                final int dist = 32;
                final var nearbySeabears = event.getEntityLiving().level.getEntitiesOfClass(EntitySeaBear.class,
                    event.getEntityLiving().getBoundingBox().inflate(dist, dist, dist));
                if (nearbySeabears.isEmpty()) {
                    final EntitySeaBear bear = AMEntityRegistry.SEA_BEAR.get().create(event.getEntityLiving().level);
                    final BlockPos at = event.getEntityLiving().blockPosition();
                    BlockPos farOff = null;
                    for (int i = 0; i < 15; i++) {
                        final int f1 = (int) Math.signum(random.nextInt() - 0.5F);
                        final int f2 = (int) Math.signum(random.nextInt() - 0.5F);
                        final BlockPos pos1 = at.offset(f1 * (10 + random.nextInt(dist - 10)), random.nextInt(1),
                            f2 * (10 + random.nextInt(dist - 10)));
                        if (event.getEntityLiving().level.isWaterAt(pos1)) {
                            farOff = pos1;
                        }
                    }
                    if (farOff != null) {
                        bear.setPos(farOff.getX() + 0.5F, farOff.getY() + 0.5F, farOff.getZ() + 0.5F);
                        bear.setYRot(random.nextFloat() * 360F);
                        bear.setTarget(event.getEntityLiving());
                        event.getEntityLiving().level.addFreshEntity(bear);
                    }
                } else {
                    for (EntitySeaBear bear : nearbySeabears) {
                        bear.setTarget(event.getEntityLiving());
                    }
                }
            }
        }
        if (VineLassoUtil.hasLassoData(event.getEntityLiving())) {
            VineLassoUtil.tickLasso(event.getEntityLiving());
        }
        if (RockyChestplateUtil.isWearing(event.getEntityLiving())) {
            RockyChestplateUtil.tickRockyRolling(event.getEntityLiving());
        }
        if (FlyingFishBootsUtil.isWearing(event.getEntityLiving())) {
            FlyingFishBootsUtil.tickFlyingFishBoots(event.getEntityLiving());
        }
    }

    private BlockPos getDownPos(BlockPos entered, LevelAccessor world) {
        int i = 0;
        while (world.isEmptyBlock(entered) && i < 3) {
            entered = entered.below();
            i++;
        }
        return entered;
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVModifierEvent event) {
        if (event.getEntity().hasEffect(AMEffectRegistry.FEAR) || event.getEntity().hasEffect(AMEffectRegistry.POWER_DOWN)) {
            event.setNewfov(1.0F);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!event.getEntityLiving().getUseItem().isEmpty() && event.getSource() != null && event.getSource().getEntity() != null) {
            if (event.getEntityLiving().getUseItem().getItem() == AMItemRegistry.SHIELD_OF_THE_DEEP.get()) {
                if (event.getSource().getEntity() instanceof LivingEntity living) {
                    boolean flag = false;
                    if (living.distanceTo(event.getEntityLiving()) <= 4
                        && !living.hasEffect(AMEffectRegistry.EXSANGUINATION)) {
                        living.addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION, 60, 2));
                        flag = true;
                    }
                    if (event.getEntityLiving().isInWaterOrBubble()) {
                        event.getEntityLiving().setAirSupply(Math.min(event.getEntityLiving().getMaxAirSupply(), event.getEntityLiving().getAirSupply() + 150));
                        flag = true;
                    }
                    if (flag) {
                        event.getEntityLiving().getUseItem().hurtAndBreak(1, event.getEntityLiving(),
                            player -> player.broadcastBreakEvent(event.getEntityLiving().getUsedItemHand()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChestGenerated(LootTableLoadEvent event) {
        if (AMConfig.addLootToChests) {
            if (event.getName().equals(BuiltInLootTables.JUNGLE_TEMPLE)) {
                final var item = LootItem.lootTableItem(AMItemRegistry.ANCIENT_DART.get()).setQuality(40).setWeight(1);
                LootPool.Builder builder = new LootPool.Builder().name("am_dart").add(item).when(LootItemRandomChanceCondition.randomChance(1f)).setRolls(UniformGenerator.between(0, 1)).setBonusRolls(UniformGenerator.between(0, 1));
                event.getTable().addPool(builder.build());
            }
            if (event.getName().equals(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER)) {
                final var item = LootItem.lootTableItem(AMItemRegistry.ANCIENT_DART.get()).setQuality(20).setWeight(3);
                LootPool.Builder builder = new LootPool.Builder().name("am_dart_dispenser").add(item).when(LootItemRandomChanceCondition.randomChance(1f)).setRolls(UniformGenerator.between(0, 2)).setBonusRolls(UniformGenerator.between(0, 1));
                event.getTable().addPool(builder.build());
            }
        }
        if (event.getName().equals(BuiltInLootTables.PIGLIN_BARTERING) && AMConfig.tusklinShoesBarteringChance > 0) {
            final var item = LootItem.lootTableItem(AMItemRegistry.PIGSHOES.get()).setQuality(5).setWeight(8);
            LootPool.Builder builder = new LootPool.Builder().name("am_pigshoes").add(item).when(LootItemRandomChanceCondition.randomChance((float) AMConfig.tusklinShoesBarteringChance)).setRolls(ConstantValue.exactly(1));
            event.getTable().addPool(builder.build());
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        CompoundTag tag = event.getItemStack().getTag();
        if (tag != null && tag.contains("BisonFur") && tag.getBoolean("BisonFur")) {
            event.getToolTip().add(new TranslatableComponent("item.alexsmobs.insulated_with_fur").withStyle(ChatFormatting.AQUA));
        }
    }
}
