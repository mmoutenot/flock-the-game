package flock;

/**
 * A block that lemmings can't pass through.
 * Doesn't do anything when used, but can be places in strategic positions.
 * 
 * FIXME: If player hits 'use' by accident, the Block will have no effect,
 * and the player will lose the block (since tools are single-use).
 */
public class BlockEntity extends ToolEntity
{
	public BlockEntity(double x, double y)
	{
		super("block", x, y);
		_collide = true;
	}
	
	@Override
	protected void doUpdate(long msElapsed)
	{
	}

	@Override
	public void use()
	{
	}
}
