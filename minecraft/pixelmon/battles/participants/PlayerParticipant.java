package pixelmon.battles.participants;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_Pixelmon;
import pixelmon.battles.BattleController;
import pixelmon.battles.attacks.Attack;
import pixelmon.comm.ChatHandler;
import pixelmon.entities.pixelmon.BaseEntityPixelmon;
import pixelmon.entities.pixelmon.helpers.IHaveHelper;
import pixelmon.entities.pixelmon.helpers.PixelmonEntityHelper;
import pixelmon.enums.EnumGui;

public class PlayerParticipant implements IBattleParticipant {
	public EntityPlayer player;
	PixelmonEntityHelper currentPixelmon;
	BattleController bc;
	
	public PlayerParticipant(EntityPlayer p, PixelmonEntityHelper firstPixelmon) {
		player = p;
		currentPixelmon = firstPixelmon;
	}

	@Override
	public boolean canGainXP() {
		return true;
	}

	@Override
	public PixelmonEntityHelper currentPokemon() {
		return currentPixelmon;
	}

	@Override
	public boolean hasMorePokemon() {
		if (mod_Pixelmon.pokeballManager.getPlayerStorage(player).countAblePokemon() > 0)
			return true;
		return false;
	}

	@Override
	public void EndBattle(boolean didWin, IBattleParticipant foe) {
		currentPixelmon.battleStats.clearBattleStats();
		currentPixelmon.EndBattle();
	}

	@Override
	public void getNextPokemon() {
		int y=0;
		if (bc.participant1==this) y=1;
		player.openGui(mod_Pixelmon.instance, EnumGui.ChoosePokemon.getIndex(), player.worldObj,
				mod_Pixelmon.battleRegistry.getIndex(bc), y, 0);
	}

	@Override
	public boolean getIsFaintedOrDead() {
		return currentPixelmon.getIsDead() || currentPixelmon.isFainted;
	}

	@Override
	public String getName() {
		return player.username;
	}

	@Override
	public Attack getMove(IBattleParticipant participant2) {
		int y = 0;
		if (currentPixelmon.bc.participant1.currentPokemon() == currentPixelmon)
			y = 1;
		player.openGui(mod_Pixelmon.instance, EnumGui.ChooseAttack.getIndex(), player.worldObj,
				mod_Pixelmon.battleRegistry.getIndex(bc), y, 0);
		return null;
	}

	@Override
	public void switchPokemon(IBattleParticipant participant2, int newPixelmonId) {
		currentPixelmon.battleStats.clearBattleStats();
		ChatHandler.sendChat(player, participant2.currentPokemon().getOwner(), "That's enough " + currentPixelmon.getName() + "!");
		currentPixelmon.catchInPokeball();

		mod_Pixelmon.pokeballManager.getPlayerStorage(currentPixelmon.getOwner()).retrieve((IHaveHelper) currentPixelmon.getIHaveHelper());
		IHaveHelper newPixelmon = mod_Pixelmon.pokeballManager.getPlayerStorage(currentPixelmon.getOwner()).sendOut(newPixelmonId,
				currentPixelmon.getOwner().worldObj);
		((EntityLiving)newPixelmon).setLocationAndAngles(((EntityLiving)currentPixelmon.getEntity()).posX, ((EntityLiving)currentPixelmon.getEntity()).posY, ((EntityLiving)currentPixelmon.getEntity()).posZ, ((EntityLiving)currentPixelmon.getEntity()).rotationYaw, 0.0F);
		newPixelmon.getHelper().setMotion(0, 0, 0);
		newPixelmon.getHelper().releaseFromPokeball();
		
		ChatHandler.sendChat(player, participant2.currentPokemon().getOwner(), "Go " + newPixelmon.getHelper().getName() + "!");
		currentPixelmon = newPixelmon.getHelper();
	}

	@Override
	public boolean checkPokemon() {
		for (NBTTagCompound n : mod_Pixelmon.pokeballManager.getPlayerStorage(currentPokemon().getOwner()).partyPokemon) {
			if (n!=null && n.getInteger("PixelmonNumberMoves") == 0) {
				ChatHandler.sendChat(currentPixelmon.getOwner(), "Couldn't load pokemon's moves");
				return false;
			}
		}
		return true;
	}

	@Override
	public void setBattleController(BattleController bc) {
		this.bc = bc;
	}

	@Override
	public void updatePokemon() {
		mod_Pixelmon.pokeballManager.getPlayerStorage(currentPixelmon.getOwner()).getNBT(currentPixelmon.getPokemonId()).setBoolean("IsFainted", true);		
	}
}
