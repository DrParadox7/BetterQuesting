package betterquesting.client.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import betterquesting.utils.NBTConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GuiButtonJson extends GuiButtonQuesting
{
	public JsonElement json;
	public ItemStack stack;
	public Entity entity;
	
	public GuiButtonJson(int index, int posX, int posY, JsonElement json)
	{
		this(index, posX, posY, 200, 20, json);
	}
	
	public GuiButtonJson(int index, int posX, int posY, int sizeX, int sizeY, JsonElement json)
	{
		super(index, posX, posY, sizeX, sizeY, "");
		this.json = json;
		
		if(json.isJsonObject())
		{
			JsonObject tmpObj = json.getAsJsonObject();
			if(tmpObj.has("id") && tmpObj.has("Damage") && tmpObj.has("Count")) // Must have at least these 3 to be considered a valid 'item'
			{
				this.stack = ItemStack.loadItemStackFromNBT(NBTConverter.JSONtoNBT_Object(json.getAsJsonObject(), new NBTTagCompound()));
			}
			
			if(stack == null && tmpObj.has("id") && EntityList.stringToClassMapping.get(tmpObj.get("id").getAsString()) != null)
			{
				this.entity = EntityList.createEntityFromNBT(NBTConverter.JSONtoNBT_Object(json.getAsJsonObject(), new NBTTagCompound()), Minecraft.getMinecraft().theWorld);
			}
		}
		
		if(this.entity != null)
		{
			this.displayString = "Entity: " + entity.getCommandSenderName();
		} else if(this.stack != null)
		{
			this.displayString = "Item: " + stack.getDisplayName();
		} else if(json.isJsonPrimitive())
		{
			this.displayString = json.getAsJsonPrimitive().getAsString();
		} else
		{
			this.displayString = json.getClass().getSimpleName();
		}
	}
	
	public boolean isItemStack()
	{
		return stack != null;
	}
	
	public boolean isEntity()
	{
		return entity != null;
	}
}
