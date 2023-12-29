package useless.dragonfly.model.block;

import useless.dragonfly.model.block.processed.BlockModel;

public class InternalModel{
	public BlockModel model;
	public int rotationX;
	public int rotationY;
	public InternalModel(BlockModel model, int rotationX, int rotationY){
        this.model = model;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
    }
}
