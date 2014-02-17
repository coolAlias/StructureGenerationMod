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

package structuregenmod.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import structuregenmod.items.ItemStructureSpawnerBase;
import structuregenmod.lib.ModInfo;
import coolalias.structuregenapi.util.LogHelper;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SGTPacketHandler implements IPacketHandler
{
	/** Packet IDs */
	private static final byte PACKET_KEY_PRESS = 1;

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		try {
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			byte packetType;

			try {
				packetType = inputStream.readByte();

				switch (packetType) {
				case PACKET_KEY_PRESS: handlePacketKeyPress(packet, (EntityPlayer) player, inputStream); break;
				default: LogHelper.log(Level.SEVERE, "Unhandled packet exception for packet id " + packetType);
				}
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void sendPacketKeyPress(int keyCode)
	{
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream outputStream = new DataOutputStream(bos);

			try {
				outputStream.writeByte(SGTPacketHandler.PACKET_KEY_PRESS);
				outputStream.writeInt(keyCode);
			} finally {
				outputStream.close();
			}

			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInfo.CHANNEL, bos.toByteArray()));

		} catch (Exception ex) {
			LogHelper.log(Level.SEVERE, "Failed to send key press packet.");
			ex.printStackTrace();
		}
	}

	private void handlePacketKeyPress(Packet250CustomPayload packet, EntityPlayer player, DataInputStream inputStream)
	{
		int keyCode;

		try {
			keyCode = inputStream.readInt();
			if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemStructureSpawnerBase)) {
				LogHelper.log(Level.SEVERE, "Held item is not an instance of ItemStructureSpawnerBase - unable to process key press packet");
			} else {
				ItemStructureSpawnerBase.handleKeyPressPacket(keyCode, player.getHeldItem(), player);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
