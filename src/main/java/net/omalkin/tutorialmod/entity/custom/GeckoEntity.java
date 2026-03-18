package net.omalkin.tutorialmod.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.omalkin.tutorialmod.entity.ModEntities;
import net.omalkin.tutorialmod.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class GeckoEntity extends Animal {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    // EntityDataAccessors are automatically synced between server and client
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.literal("Our Mighty Gecko"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10);

    public GeckoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this)); // Makes the entity want to swim (saves them drowning)
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0)); // Makes the entity panic when hurt
        this.goalSelector.addGoal(2, new BreedGoal(this, .10)); // Makes the entity breed
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, stack -> stack.is(ModItems.GOJI_BERRIES), false)); // Makes the entity follow held breedable food
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25)); // Makes the entity follow parent as a kid
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0)); // Makes the entity randomly walk around
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f)); // Makes the entity look at player
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this)); // Makes the entity look around aimlessly
    }

    // These are the REQUIRED attributes. Must be present for the animal to spawn in the world
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.25d)
                .add(Attributes.FOLLOW_RANGE, 24d);
    }

    // The item used for breeding
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.GOJI_BERRIES);
    }

    // Create the babies
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        GeckoVariant variant = Util.getRandom(GeckoVariant.values(), this.random);
        GeckoEntity baby = ModEntities.GECKO.get().create(level);
        baby.setVariant(variant);
        return baby;
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            this.idleAnimationTimeout--;
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Animations can be done on the client (server doesn't care if its twerking or not...)
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    // Variant stuffs
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public GeckoVariant getVariant() {
        return GeckoVariant.byId(this.getTypeVariant() & 255); // vanilla does this & 255 thing so ¯\_(ツ)_/¯
    }

    private void setVariant(GeckoVariant variant) {
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getTypeVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(VARIANT, compound.getInt("Variant"));
    }

    // This is triggered when an entity is spawned either via egg or natural generation
    // This does NOT trigger upon baby spawning
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        GeckoVariant variant = Util.getRandom(GeckoVariant.values(), this.random);
        this.setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    // Sounds (they don't make sense, just examples)
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ANVIL_FALL;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.SHULKER_DEATH;
    }

    // Boss bar stuff
    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
}
