package betterquesting.api.questing;

import betterquesting.api.enums.EnumQuestState;
import betterquesting.api.properties.IPropertyContainer;
import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.storage.IDatabaseNBT;
import betterquesting.api2.storage.INBTProgress;
import betterquesting.api2.storage.INBTSaveLoad;
import betterquesting.api2.utils.QuestTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.UUID;

public interface IQuest extends INBTSaveLoad<NBTTagCompound>, INBTProgress<NBTTagCompound>, IPropertyContainer
{
	EnumQuestState getState(EntityPlayer player);
	
	@Nullable
	NBTTagCompound getCompletionInfo(UUID uuid);
	void setCompletionInfo(UUID uuid, @Nullable NBTTagCompound nbt);
	
	void update(EntityPlayer player);
	void detect(EntityPlayer player);
	
	boolean isUnlocked(UUID uuid);
	boolean canSubmit(EntityPlayer player);

	boolean showParentConnection();
	
	boolean isComplete(UUID uuid);
	void setComplete(UUID uuid, long timeStamp);

	/**
	 * Can claim now. (Basically includes info from rewards (is choice reward chosen, for example))
	 */
	boolean canClaim(EntityPlayer player);

	/**
	 * Can we claim reward at all. (If reward available but we can't claim cuz a rewards not ready (choice reward not chosen, for example))
	 */
	boolean canClaimBasically(EntityPlayer player);
	boolean hasClaimed(UUID uuid);
	void claimReward(EntityPlayer player);
	void setClaimed(UUID uuid, long timestamp);
	
	void resetUser(@Nullable UUID uuid, boolean fullReset);
	
	IDatabaseNBT<ITask, NBTTagList, NBTTagList> getTasks();
	IDatabaseNBT<IReward, NBTTagList, NBTTagList> getRewards();
	
	@Nonnull
	int[] getRequirements();
	void setRequirements(@Nonnull int[] req);

	@Nonnull
	RequirementType getRequirementType(int req);
	void setRequirementType(int req, @Nonnull RequirementType kind);

	enum RequirementType {
		NORMAL(PresetIcon.ICON_VISIBILITY_NORMAL),
		HIDDEN(PresetIcon.ICON_VISIBILITY_HIDDEN);

		private final PresetIcon icon;
		private final String buttonTooltip;

		private static final RequirementType[] VALUES = values();

		RequirementType(PresetIcon icon) {
			this.icon = icon;
			buttonTooltip = "betterquesting.btn.prereq_visbility." + name().toLowerCase(Locale.ROOT);
		}

		public byte id() {
			return (byte) ordinal();
		}

		public PresetIcon getIcon() {
			return icon;
		}

		public String getButtonTooltip() {
			return QuestTranslation.translate(buttonTooltip);
		}

		public RequirementType next() {
			return VALUES[(ordinal()+1) % VALUES.length];
		}

		public static RequirementType from(byte id) {
			return id >=0 && id < VALUES.length ? VALUES[id] : NORMAL;
		}
	}
	int[] getVisRequirements();
	void setVisRequirements(@Nonnull int[] req);
}
