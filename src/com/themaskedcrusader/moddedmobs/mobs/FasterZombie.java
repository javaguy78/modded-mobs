package com.themaskedcrusader.moddedmobs.mobs;

import net.minecraft.server.v1_5_R3.*;
import org.bukkit.craftbukkit.v1_5_R3.util.UnsafeList;

import java.lang.reflect.Field;

public class FasterZombie extends EntityZombie {
    float runSpeed = 0.4F;

    public FasterZombie(World world) {
        super(world);
        try {
            Field gsa = net.minecraft.server.v1_5_R3.PathfinderGoalSelector.class.getDeclaredField("a");
            gsa.setAccessible(true);
            gsa.set(this.goalSelector, new UnsafeList());
            gsa.set(this.targetSelector, new UnsafeList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, runSpeed, false));
        this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, runSpeed, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 0.23F));
        this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, 0.23F, false));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.23F));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 32.0F, 0, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
    }
}