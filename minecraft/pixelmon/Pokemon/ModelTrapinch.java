// Date: 8/5/2012 11:29:47 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package pixelmon.Pokemon;

import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;


public class ModelTrapinch extends ModelBase
{
  //fields
    ModelRenderer body1;
    ModelRenderer body2;
    ModelRenderer Leg1;
    ModelRenderer Leg2;
    ModelRenderer Leg3;
    ModelRenderer Leg4;
    ModelRenderer HEADBASE;
    ModelRenderer TopOfHead;
    ModelRenderer Head;
   
  
  public ModelTrapinch()
  {
    textureWidth = 64;
    textureHeight = 32;
    setTextureOffset("HEADBASE.Delete", 0, 0);
    
      body1 = new ModelRenderer(this, 0, 0);
      body1.addBox(-2F, -2F, -2F, 5, 5, 6);
      body1.setRotationPoint(0F, 18F, 0.5F);
      body1.setTextureSize(64, 32);
      body1.mirror = true;
      setRotation(body1, 0F, 0F, 0F);
      body2 = new ModelRenderer(this, 0, 11);
      body2.addBox(-3F, -2F, -2F, 6, 3, 4);
      body2.setRotationPoint(0.5F, 19F, 1.5F);
      body2.setTextureSize(64, 32);
      body2.mirror = true;
      setRotation(body2, 0F, 0F, 0F);
      Leg1 = new ModelRenderer(this, 24, 0);
      Leg1.addBox(-1F, 0F, -1F, 2, 4, 2);
      Leg1.setRotationPoint(2F, 20.5F, 0F);
      Leg1.setTextureSize(64, 32);
      Leg1.mirror = true;
      setRotation(Leg1, 0F, 0F, -0.2094395F);
      Leg2 = new ModelRenderer(this, 24, 0);
      Leg2.addBox(-1F, 0F, -1F, 2, 4, 2);
      Leg2.setRotationPoint(2F, 20.5F, 3F);
      Leg2.setTextureSize(64, 32);
      Leg2.mirror = true;
      setRotation(Leg2, 0F, 0F, -0.2094395F);
      Leg3 = new ModelRenderer(this, 24, 0);
      Leg3.addBox(-1F, 0F, -1F, 2, 4, 2);
      Leg3.setRotationPoint(-1F, 20.5F, 0F);
      Leg3.setTextureSize(64, 32);
      Leg3.mirror = true;
      setRotation(Leg3, 0F, 0F, 0.2094395F);
      Leg4 = new ModelRenderer(this, 24, 0);
      Leg4.addBox(-1F, 0F, -1F, 2, 4, 2);
      Leg4.setRotationPoint(-1F, 20.5F, 3F);
      Leg4.setTextureSize(64, 32);
      Leg4.mirror = true;
      setRotation(Leg4, 0F, 0F, 0.2094395F);
    HEADBASE = new ModelRenderer(this, "HEADBASE");
    HEADBASE.setRotationPoint(0.5F, 16F, -2F);
    setRotation(HEADBASE, 0F, 0F, 0F);
    HEADBASE.mirror = true;
      TopOfHead = new ModelRenderer(this, 30, 21);
      TopOfHead.addBox(-2F, -2.5F, -2.5F, 4, 6, 5);
      TopOfHead.setRotationPoint(0F, -0.5F, -2.5F);
      TopOfHead.setTextureSize(64, 32);
      TopOfHead.mirror = true;
      setRotation(TopOfHead, -0.0698132F, 0F, 0F);
      Head = new ModelRenderer(this, 0, 19);
      Head.addBox(-1.5F, -11.5F, -6F, 6, 6, 7);
      Head.setRotationPoint(-1.5F, 9F, -1F);
      Head.setTextureSize(64, 32);
      Head.mirror = true;
      setRotation(Head, -0.0698132F, 0F, 0F);
      HEADBASE.addChild(TopOfHead);
      HEADBASE.addChild(Head);
      
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    body1.render(f5);
    body2.render(f5);
    Leg1.render(f5);
    Leg2.render(f5);
    Leg3.render(f5);
    Leg4.render(f5);
    HEADBASE.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5);
    HEADBASE.rotateAngleX = f4 / 57.29578F;
    HEADBASE.rotateAngleY = f3 / 57.29578F;
    Leg1.rotateAngleX =  MathHelper.cos(f * 0.7F) * 1F * f1;
    Leg2.rotateAngleX =  MathHelper.cos(f * 0.7F + 3.14159F) * 1F * f1;
    Leg3.rotateAngleX =  MathHelper.cos(f * 0.7F + 3.14159F) * 1F * f1;
    Leg4.rotateAngleX =  MathHelper.cos(f * 0.7F) * 1F * f1;
  }

}
