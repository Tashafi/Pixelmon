package pixelmon.Trainers;

import pixelmon.entities.EntityTrainer;
import net.minecraft.src.World;

public class EntityTrainerYoungster extends EntityTrainer {

	public EntityTrainerYoungster(World par1World) {
		super(par1World);
		texture = "/pixelmon/texture/trainers/youngster1.png";
		init();
	}

	public void init(){
		name = "Youngster";
		super.init();
	}
}
