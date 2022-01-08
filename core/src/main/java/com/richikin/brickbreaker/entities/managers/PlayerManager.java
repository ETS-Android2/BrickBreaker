package com.richikin.brickbreaker.entities.managers;

import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.characters.Paddle;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.logging.Trace;

public class PlayerManager extends BasicEntityManager
{
    private SpriteDescriptor descriptor;

    public PlayerManager()
    {
    }

    @Override
    public void init()
    {
        Trace.fileInfo();

        super.init();

        setSpawnPoint();
        createPaddle();
    }

    public void createPaddle()
    {
        App.getEntityManager().playerIndex = 0;
        App.getEntityManager().playerReady = false;

        App.getEntities().paddle = new Paddle();
        App.getEntities().paddle.initialise(descriptor);
        App.getEntityData().addEntity(App.getEntities().paddle);

        App.getEntityManager().updateIndexes();
        App.getEntityManager().playerIndex = descriptor._INDEX;
        App.getEntityManager().playerReady = true;
    }

    public void setSpawnPoint()
    {
        descriptor             = App.getEntities().getDescriptor(GraphicID.G_PADDLE);
        descriptor._POSITION.x = (Gfx._VIEW_WIDTH - descriptor._SIZE.x) / 2;
        descriptor._POSITION.y = 5 * descriptor._SIZE.y;
        descriptor._POSITION.z = App.getEntityUtils().getInitialZPosition(GraphicID.G_PADDLE);
        descriptor._INDEX      = App.getEntityData().getEntityMap().size;
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._PLAYER_MANAGER;
    }
}
