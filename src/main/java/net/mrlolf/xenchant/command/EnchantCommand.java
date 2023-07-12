package net.mrlolf.xenchant.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class EnchantCommand extends net.minecraft.server.command.EnchantCommand {

    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType((entityName) -> {
        return Text.translatable("commands.enchant.failed.entity", new Object[]{entityName});
    });
    private static final DynamicCommandExceptionType FAILED_ITEMLESS_EXCEPTION = new DynamicCommandExceptionType((entityName) -> {
        return Text.translatable("commands.enchant.failed.itemless", new Object[]{entityName});
    });
    private static final DynamicCommandExceptionType FAILED_INCOMPATIBLE_EXCEPTION = new DynamicCommandExceptionType((itemName) -> {
        return Text.translatable("commands.enchant.failed.incompatible", new Object[]{itemName});
    });
    private static final Dynamic2CommandExceptionType FAILED_LEVEL_EXCEPTION = new Dynamic2CommandExceptionType((level, maxLevel) -> {
        return Text.translatable("commands.enchant.failed.level", new Object[]{level, maxLevel});
    });
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.enchant.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandManager.literal("enchant").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(CommandManager.argument("targets", EntityArgumentType.entities()).then(((RequiredArgumentBuilder) CommandManager.argument("enchantment", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENCHANTMENT)).executes((context) -> {
            return execute((ServerCommandSource) context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryArgumentType.getEnchantment(context, "enchantment"), 1);
        })).then(CommandManager.argument("level", IntegerArgumentType.integer(0)).executes((context) -> {
            return execute((ServerCommandSource) context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryArgumentType.getEnchantment(context, "enchantment"), IntegerArgumentType.getInteger(context, "level"));
        })))));
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry<Enchantment> enchantment, int level) throws CommandSyntaxException {
        Enchantment enchantment2 = (Enchantment) enchantment.value();
        int i = 0;
        Iterator var6 = targets.iterator();

        while (true) {
            while (true) {
                while (true) {
                    while (var6.hasNext()) {
                        Entity entity = (Entity) var6.next();
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            ItemStack itemStack = livingEntity.getMainHandStack();
                            if (!itemStack.isEmpty()) {
                                itemStack.addEnchantment(enchantment2, level);
                                ++i;
                            } else if (targets.size() == 1) {
                                throw FAILED_ITEMLESS_EXCEPTION.create(livingEntity.getName().getString());
                            }
                        } else if (targets.size() == 1) {
                            throw FAILED_ENTITY_EXCEPTION.create(entity.getName().getString());
                        }
                    }

                    if (i == 0) {
                        throw FAILED_EXCEPTION.create();
                    }

                    if (targets.size() == 1) {
                        source.sendFeedback(() -> Text.translatable("commands.enchant.success.single", new Object[]{enchantment2.getName(level), ((Entity) targets.iterator().next()).getDisplayName()}), true);
                    } else {
                        source.sendFeedback(() -> Text.translatable("commands.enchant.success.multiple", new Object[]{enchantment2.getName(level), targets.size()}), true);
                    }

                    return i;
                }
            }
        }
    }
}

