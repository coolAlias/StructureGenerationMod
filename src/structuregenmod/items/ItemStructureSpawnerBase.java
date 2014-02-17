/**
    Copyright (C) <2014> <coolAlias>

    This file is part of coolAlias' Structure Generation Tool; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package structuregenmod.items;

import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import structuregenmod.lib.SGTKeyBindings;
import coolalias.structuregenapi.util.LogHelper;
import coolalias.structuregenapi.util.Structure;

public abstract class ItemStructureSpawnerBase extends BaseModItem
{
	/** Enumerates valid values for increment / decrement offset methods */
	public static enum Offset { OFFSET_X, OFFSET_Y, OFFSET_Z }
	
	/** String identifiers for NBT storage and retrieval */
	private static final String[] data = {"Structure", "Rotations", "OffsetX", "OffsetY", "OffsetZ", "InvertY", "Remove"};

	/** Indices for data variables */
	public static final int STRUCTURE_INDEX = 0, ROTATIONS = 1, OFFSET_X = 2, OFFSET_Y = 3, OFFSET_Z = 4, INVERT_Y = 5, REMOVE = 6;

	/**
	 * StructureGeneratorBase parameter supplies the appropriate List<Structure> to use 
	 */
	public ItemStructureSpawnerBase(int id) {
		super(id);
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	/**
	 * Called when item is crafted/smelted. Not called from Creative Tabs.
	 */
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		initNBTCompound(stack);
	}
	
	/**
	 * Returns the Structure List used by this ItemStack
	 */
	public abstract List<Structure> getStructureList(ItemStack stack);
	
	/**
	 * Increments the structure index and returns the new value for convenience.
	 */
	public static final int nextStructure(ItemStack stack) {
		List list = ((ItemStructureSpawnerBase) stack.getItem()).getStructureList(stack);
		if (list != null) {
			int index = getData(stack, STRUCTURE_INDEX) + 1;
			if (index >= list.size()) index = 0;
			setData(stack, STRUCTURE_INDEX, index);
			return index;
		} else {
			return 0;
		}
	}

	/**
	 * Decrements the structure index and returns the new value for convenience.
	 */
	public static final int prevStructure(ItemStack stack) {
		List list = ((ItemStructureSpawnerBase) stack.getItem()).getStructureList(stack);
		if (list != null) {
			int index = getData(stack, STRUCTURE_INDEX) - 1;
			if (index < 0) index = list.size() - 1;
			setData(stack, STRUCTURE_INDEX, index);
			return index;
		} else {
			return 0;
		}
	}

	/**
	 * Returns the name of the structure at provided index, or "" if index out of bounds
	 */
	public static final String getStructureName(ItemStack stack, int index) {
		List list = ((ItemStructureSpawnerBase) stack.getItem()).getStructureList(stack);
		return (list != null && index < list.size() ? ((Structure) list.get(index)).name : "");
	}
	
	/**
	 * Returns index of currently selected structure
	 */
	public static final int getCurrentStructureIndex(ItemStack stack) {
		List list = ((ItemStructureSpawnerBase) stack.getItem()).getStructureList(stack);
		return getData(stack, STRUCTURE_INDEX) >= list.size() ? 0 : getData(stack, STRUCTURE_INDEX);
	}
	
	/**
	 * Returns currently selected structure
	 */
	public static final Structure getCurrentStructure(ItemStack stack) {
		List list = ((ItemStructureSpawnerBase) stack.getItem()).getStructureList(stack);
		return (Structure) list.get(getCurrentStructureIndex(stack));
	}

	/**
	 * Increments the appropriate Offset and returns the new value for convenience.
	 */
	public static final int incrementOffset(ItemStack stack, Offset type) {
		int offset;
		switch(type) {
		case OFFSET_X:
			offset = getData(stack, OFFSET_X) + 1;
			stack.stackTagCompound.setInteger(data[OFFSET_X], offset);
			return offset;
		case OFFSET_Y:
			offset = getData(stack, OFFSET_Y) + 1;
			stack.stackTagCompound.setInteger(data[OFFSET_Y], offset);
			return offset;
		case OFFSET_Z:
			offset = getData(stack, OFFSET_Z) + 1;
			stack.stackTagCompound.setInteger(data[OFFSET_Z], offset);
			return offset;
		default: return 0;
		}
	}

	/**
	 * Decrements the appropriate Offset and returns the new value for convenience.
	 */
	public static final int decrementOffset(ItemStack stack, Offset type) {
		int offset;
		switch(type) {
		case OFFSET_X:
			offset = getData(stack, OFFSET_X) - 1;
			stack.stackTagCompound.setInteger(data[OFFSET_X], offset);
			return offset;
		case OFFSET_Y:
			offset = getData(stack, OFFSET_Y) - 1;
			stack.stackTagCompound.setInteger(data[OFFSET_Y], offset);
			return offset;
		case OFFSET_Z:
			offset = getData(stack, OFFSET_Z) - 1;
			stack.stackTagCompound.setInteger(data[OFFSET_Z], offset);
			return offset;
		default: return 0;
		}
	}

	/**
	 * Returns true if offset y is inverted (i.e. y will decrement)
	 */
	public static final boolean isInverted(ItemStack stack) {
		return getData(stack, INVERT_Y) == 1;
	}

	/**
	 * Inverts Y axis for offset adjustments; returns new value for convenience.
	 */
	public static final boolean invertY(ItemStack stack) {
		setData(stack, INVERT_Y, (getData(stack, INVERT_Y) + 1) % 2);
		return stack.stackTagCompound.getInteger(data[INVERT_Y]) == 1;
	}

	/**
	 * Resets all manual offsets to 0.
	 */
	public static final void resetOffset(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			initNBTCompound(stack);
		}
		stack.stackTagCompound.setInteger(data[OFFSET_X], 0);
		stack.stackTagCompound.setInteger(data[OFFSET_Y], 0);
		stack.stackTagCompound.setInteger(data[OFFSET_Z], 0);
	}

	/**
	 * Rotates structure's facing by 90 degrees clockwise; returns number of rotations for convenience.
	 */
	public static final int rotate(ItemStack stack) {
		int rotations = (getData(stack, ROTATIONS) + 1) % 4;
		stack.stackTagCompound.setInteger(data[ROTATIONS], rotations);
		return rotations;
	}
	
	/**
	 * Returns data field at index
	 */
	public static final int getData(ItemStack stack, int index) {
		if (stack.stackTagCompound == null) {
			initNBTCompound(stack);
		}
		if (index < data.length) {
			return stack.stackTagCompound.getInteger(data[index]);
		} else {
			LogHelper.log(Level.WARNING, "Index " + index + " out of bounds while trying to get data for ItemStructureSpawnerBase");
			return 0;
		}
	}
	
	/**
	 * Returns data field at index
	 */
	public static final void setData(ItemStack stack, int index, int value) {
		if (stack.stackTagCompound == null) {
			initNBTCompound(stack);
		}
		if (index < data.length) {
			stack.stackTagCompound.setInteger(data[index], value);
		} else {
			LogHelper.log(Level.WARNING, "Index " + index + " out of bounds while trying to set data for ItemStructureSpawnerBase");
		}
	}

	/**
	 * Returns true if structure will be removed
	 */
	public static final boolean getRemove(ItemStack stack) {
		return stack.stackTagCompound.getInteger(data[REMOVE]) == 1;
	}
	
	/**
	 * Toggles between generate and remove structure setting. Returns new value for convenience.
	 */
	public static final boolean toggleRemove(ItemStack stack) {
		setData(stack, REMOVE, (getData(stack, REMOVE) + 1) % 2);
		return stack.stackTagCompound.getInteger(data[REMOVE]) == 1;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}

	/**
	 * Creates a new NBTTagCompound for the stack if none exists
	 */
	protected static final void initNBTCompound(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
		for (int i = 0; i < data.length; ++i) {
			stack.stackTagCompound.setInteger(data[i], 0);
		}
	}

	/**
	 * Updates spawners's data when key pressed and adds chat message for player
	 */
	public static final void handleKeyPressPacket(int keyCode, ItemStack stack, EntityPlayer player) {
		ItemStructureSpawnerBase spawner = (ItemStructureSpawnerBase) stack.getItem();
		if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.PLUS_X].keyCode) {
			player.addChatMessage("Incremented offset x: " + incrementOffset(stack, Offset.OFFSET_X));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.MINUS_X].keyCode) {
			player.addChatMessage("Decremented offset x: " + decrementOffset(stack, Offset.OFFSET_X));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.PLUS_Z].keyCode) {
			player.addChatMessage("Incremented offset z: " + incrementOffset(stack, Offset.OFFSET_Z));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.MINUS_Z].keyCode) {
			player.addChatMessage("Decremented offset z: " + decrementOffset(stack, Offset.OFFSET_Z));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.OFFSET_Y].keyCode) {
			if (spawner.isInverted(stack)) { player.addChatMessage("Decremented offset y: " + decrementOffset(stack, Offset.OFFSET_Y)); }
			else { player.addChatMessage("Incremented offset y: " + incrementOffset(stack, Offset.OFFSET_Y)); }
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.INVERT_Y].keyCode) {
			player.addChatMessage("Offset y will now " + (invertY(stack) ? "decrement." : "increment."));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.RESET_OFFSET].keyCode) {
			spawner.resetOffset(stack);
			player.addChatMessage("Offsets x/y/z reset to 0.");
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.ROTATE].keyCode) {
			player.addChatMessage("Structure orientation rotated by " + (rotate(stack) * 90) + " degrees.");
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.PREV_STRUCT].keyCode) {
			player.addChatMessage("Selected structure: " + spawner.getStructureName(stack, spawner.prevStructure(stack)) + " at index " + (spawner.getCurrentStructureIndex(stack) + 1));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.NEXT_STRUCT].keyCode) {
			player.addChatMessage("Selected structure: " + spawner.getStructureName(stack, spawner.nextStructure(stack)) + " at index " + (spawner.getCurrentStructureIndex(stack) + 1));
		} else if (keyCode == SGTKeyBindings.keys[SGTKeyBindings.TOGGLE_REMOVE].keyCode) {
			player.addChatMessage("Structure will " + (toggleRemove(stack) ? "be removed" : "generate") + " on right click.");
		}
	}
}
