package pixelmon.storage;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pixelmon.PixelmonEntityList;
import pixelmon.comm.ChatHandler;
import pixelmon.comm.EnumPackets;
import pixelmon.comm.PacketCreator;
import pixelmon.comm.PixelmonDataPacket;
import pixelmon.entities.EntityTrainer;
import pixelmon.entities.pixelmon.helpers.IHaveHelper;
import pixelmon.entities.pixelmon.helpers.PixelmonEntityHelper;
import pixelmon.storage.PokeballManager.PokeballManagerMode;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.World;
import net.minecraft.src.mod_Pixelmon;
import net.minecraft.src.forge.MinecraftForge;

public class PlayerStorage {
	public NBTTagCompound[] partyPokemon = new NBTTagCompound[6];
	private static final int carryLimit = 6;
	public EntityPlayer player;
	public EntityTrainer trainer;
	private PokeballManagerMode mode;
	public boolean guiOpened = false;

	public PlayerStorage(EntityPlayer player) {
		this.mode = PokeballManagerMode.Player;
		this.player = player;
	}

	public PlayerStorage(EntityTrainer trainer) {
		this.mode = PokeballManagerMode.Trainer;
		this.trainer = trainer;
	}

	public boolean hasSpace() {
		for (NBTTagCompound n : partyPokemon) {
			if (n == null) {
				return true;
			}
		}
		return false;
	}

	public int getNextOpen() {
		for (int i = 0; i < partyPokemon.length; i++) {
			if (partyPokemon[i] == null) {
				return i;
			}
		}
		return 0;
	}

	public void setPokemon(NBTTagCompound[] pokemon) {
		partyPokemon = pokemon;
	}

	public void addToParty(PixelmonEntityHelper p) {
		if (p.moveset.size() == 0)
			p.loadMoveset();
		if (!hasSpace()) {
			ChatHandler.sendChat(p.getOwner(), "Your party is full, " + p.getName() + " is sent to your computer!");
			mod_Pixelmon.computerManager.getPlayerStorage(player).addToComputer(p);
			return;
		}
		if (mode == PokeballManagerMode.Player)
			p.setOwner(player);
		else if (mode == PokeballManagerMode.Trainer)
			p.setTrainer(trainer);
		NBTTagCompound n = new NBTTagCompound();
		int id = 0;
		if (mode == PokeballManagerMode.Player)
			id = ModLoader.getUniqueEntityId();
		else if (mode == PokeballManagerMode.Trainer)
			id = new Random().nextInt(10000) * -1 - 1;
		boolean isUsed = false;
		do {
			isUsed = false;
			for (NBTTagCompound nbt : partyPokemon) {
				if (nbt != null) {
					if (mode == PokeballManagerMode.Player) {
						id = ModLoader.getUniqueEntityId();
					} else if (mode == PokeballManagerMode.Trainer) {
						id = new Random().nextInt(10000) * -1 - 1;
					}
				}
			}
		} while (contains(id));

		p.setPokemonID(id);
		p.writeEntityToNBT(n);
		Entity entity1 = (Entity) p.getEntity();
		entity1.writeToNBT(n);
		n.setString("id", p.getName());
		n.setName(p.getName());
		n.setString("Nickname", n.getName());
		n.setBoolean("IsInBall", true);
		n.setBoolean("IsShiny", p.getIsShiny());
		n.setInteger("PixelmonOrder", getNextOpen());
		partyPokemon[getNextOpen()] = n;
		if (n.getShort("Health") > 0)
			n.setBoolean("IsFainted", false);
		ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username, new PixelmonDataPacket(n, mod_Pixelmon.instance, EnumPackets.AddToStorage).getPacket());
	}

	public void retrieve(IHaveHelper currentPixelmon) {
		for (NBTTagCompound n : partyPokemon) {
			if (n != null) {
				if (n.getInteger("pixelmonID") == currentPixelmon.getHelper().getPokemonId()) {
					currentPixelmon.getHelper().writeEntityToNBT(n);
					Entity entity1 = (Entity) currentPixelmon;
					entity1.writeToNBT(n);
					n.setName(currentPixelmon.getHelper().getName());
					n.setBoolean("IsInBall", true);
				}
			}
		}
	}

	public boolean contains(int id) {
		for (NBTTagCompound n : partyPokemon) {
			if (n != null) {
				if (n.getInteger("pixelmonID") == id)
					return true;
			}
		}
		return false;
	}

	public IHaveHelper sendOut(int id, World world) {
		for (NBTTagCompound n : partyPokemon) {
			if (n != null) {
				if (n.getInteger("pixelmonID") == id) {
					n.setBoolean("IsInBall", false);
					IHaveHelper e = (IHaveHelper) PixelmonEntityList.createEntityFromNBT(n, world);
					e.setOwner(player);
					e.getHelper().lvl.updateEntityString();
					e.getHelper().clearVelocity();
					e.getHelper().setIsDead(false);
					return e;
				}
			}
		}
		return null;
	}

	public NBTTagCompound getNBT(int id) {
		for (NBTTagCompound n : partyPokemon) {
			if (n != null) {
				if (n.getInteger("pixelmonID") == id)
					return n;
			}
		}
		return null;
	}

	public NBTTagCompound[] getList() {
		return partyPokemon;
	}

	public void replace(PixelmonEntityHelper entityPixelmon, PixelmonEntityHelper entityCapturedPixelmon) {
		for (int i = 0; i < partyPokemon.length; i++) {
			NBTTagCompound nbt = partyPokemon[i];
			if (nbt != null) {
				if (nbt.getInteger("pixelmonID") == entityPixelmon.getPokemonId()) {
					entityCapturedPixelmon.setPokemonID(entityPixelmon.getPokemonId());
					entityCapturedPixelmon.writeEntityToNBT(nbt);
					Entity entity1 = (Entity) entityCapturedPixelmon.getEntity();
					entity1.writeToNBT(nbt);
					nbt.setString("id", entityCapturedPixelmon.getName());
					nbt.setName(entityCapturedPixelmon.getName());
					ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username, new PixelmonDataPacket(nbt, mod_Pixelmon.instance, EnumPackets.UpdateStorage).getPacket());
				}
			}
		}
	}

	public void changePokemon(int pos, NBTTagCompound n) {
		if (partyPokemon[pos] != null)
			ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username, PacketCreator.createPacket(EnumPackets.RemoveFromStorage, partyPokemon[pos].getInteger("pixelmonID")));
		if (n != null) {
			n.setInteger("PixelmonOrder", pos);
			ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username, new PixelmonDataPacket(n,mod_Pixelmon.instance, EnumPackets.AddToStorage).getPacket());
		}
		partyPokemon[pos] = n;
	}

	public int count() {
		int count = 0;
		for (int i = 0; i < partyPokemon.length; i++)
			if (partyPokemon[i] != null)
				count++;
		return count;
	}

	public int countAblePokemon() {
		int c = 0;
		for (NBTTagCompound e : partyPokemon)
			if (e != null) {
				if (!e.getBoolean("IsFainted"))
					c++;
			}

		return c;
	}

	public boolean isIn(PixelmonEntityHelper entityHit) {
		return contains(entityHit.getPokemonId());
	}

	public boolean hasSentOut(int pixelmonID) {
		for (NBTTagCompound n : partyPokemon)
			if (n != null) {
				if (n.getInteger("pixelmonID") == pixelmonID)
					if (!n.getBoolean("IsInBall"))
						return true;
			}
		return false;
	}

	public boolean isFainted(int pokemonId) {
		for (NBTTagCompound nbt : partyPokemon)
			if (nbt != null) {
				if (nbt.getInteger("pixelmonID") == pokemonId)
					if (nbt.getBoolean("IsFainted"))
						return true;
			}
		return false;
	}

	public void updateNBT(PixelmonEntityHelper helper) {
		for (NBTTagCompound nbt : partyPokemon) {
			if (nbt != null) {
				if (nbt.getInteger("pixelmonID") == helper.getPokemonId()) {
					helper.writeEntityToNBT(nbt);
					Entity entity1 = (Entity) helper.getEntity();
					entity1.writeToNBT(nbt);
					nbt.setString("id", helper.getName());
					nbt.setName(helper.getName());
					ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username, new PixelmonDataPacket(nbt, mod_Pixelmon.instance, EnumPackets.UpdateStorage).getPacket());
					helper.getLvl().updateEntityString();
				}
			}
		}
	}

	public int getIDFromPosition(int pos) {
		for (NBTTagCompound n : partyPokemon)
			if (n != null) {
				if (n.getInteger("PixelmonOrder") == pos)
					return n.getInteger("pixelmonID");
			}
		return -1;
	}

	public boolean EntityAlreadyExists(int id, World world) {

		@SuppressWarnings("unchecked")
		List<Entity> EntityList = world.loadedEntityList;
		for (Entity e : EntityList) {
			if (e instanceof IHaveHelper) {
				if (((IHaveHelper) e).getHelper().getPokemonId() == id) {
					return true;
				}
			}
		}
		return false;
	}

	public IHaveHelper getAlreadyExists(int id, World world) {
		if (id == -1) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Entity> EntityList = world.loadedEntityList;
		for (Entity e : EntityList) {
			if (e instanceof IHaveHelper) {
				if (((IHaveHelper) e).getHelper().getPokemonId() == id) {
					return (IHaveHelper) e;
				}
			}
		}
		return null;
	}

	public void writeToNBT(NBTTagCompound var1) {
		for (int i = 0; i < partyPokemon.length; i++) {
			NBTTagCompound e = partyPokemon[i];
			if (e != null) {
				e.setInteger("PixelmonOrder", i);
				var1.setCompoundTag("" + e.getInteger("pixelmonID"), e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void readFromNBT(NBTTagCompound var1) {
		Iterator iterator = var1.getTags().iterator();
		do {
			if (!iterator.hasNext())
				break;

			NBTBase nbtbase = (NBTBase) iterator.next();

			if (nbtbase instanceof NBTTagCompound) {
				NBTTagCompound pokemonData = (NBTTagCompound) nbtbase;
				pokemonData.setName(pokemonData.getString("Name"));
				partyPokemon[pokemonData.getInteger("PixelmonOrder")] = pokemonData;
				ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username,
						new PixelmonDataPacket(pokemonData, mod_Pixelmon.instance, EnumPackets.AddToStorage).getPacket());
			}
		} while (true);
	}

	public IHaveHelper getFirstAblePokemon(World world) {
		for (NBTTagCompound n : partyPokemon) {
			if (n != null && !n.getBoolean("IsFainted")) {
				n.setBoolean("IsInBall", false);
				IHaveHelper e = (IHaveHelper) PixelmonEntityList.createEntityFromNBT(n, world);
				return e;
			}
		}
		return null;
	}

	public void healAllPokemon() {
		for (NBTTagCompound nbt : partyPokemon) {
			if (nbt != null) {
				heal(nbt);
			}
		}
	}

	public IHaveHelper sendOutFromPosition(int pos, World worldObj) {
		return sendOut(getIDFromPosition(pos), worldObj);
	}

	public void heal(int index) {
		for (NBTTagCompound nbt : partyPokemon) {
			if (nbt != null) {
				if (nbt.getInteger("pixelmonID") == index)
					heal(nbt);
			}
		}
	}

	private void heal(NBTTagCompound nbt) {
		nbt.setShort("Health", (short) nbt.getInteger("StatsHP"));
		nbt.setBoolean("IsFainted", false);
		int numMoves = nbt.getInteger("PixelmonNumberMoves");
		for (int i = 0; i < numMoves; i++) {
			nbt.setInteger("PixelmonMovePP" + i, nbt.getInteger("PixelmonMovePPBase" + i));
		}
		ModLoader.getMinecraftServerInstance().configManager.sendPacketToPlayer(player.username, new PixelmonDataPacket(nbt, mod_Pixelmon.instance, EnumPackets.UpdateStorage).getPacket());
	}
}
