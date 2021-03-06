package pixelmon.battles.attacks.specialAttacks;

import java.util.ArrayList;

import pixelmon.battles.attacks.Attack;
import pixelmon.entities.pixelmon.helpers.PixelmonEntityHelper;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;

public class NightShade extends SpecialAttackBase {

	public NightShade() {
		super(SpecialAttackType.NightShade, ApplyStage.During, false);
	}

	@Override
	public boolean ApplyEffect(PixelmonEntityHelper user, PixelmonEntityHelper target, Attack a, ArrayList<String> attackList) {
		target.attackEntityFrom(DamageSource.causeMobDamage((EntityLiving)user.getEntity()), user.getLvl().getLevel());
		return true;
	}

}
