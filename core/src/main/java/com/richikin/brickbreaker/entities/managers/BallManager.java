package com.richikin.brickbreaker.entities.managers;

import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.entities.characters.Ball;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.logging.Trace;

public class BallManager extends BasicEntityManager
{
    public BallManager()
    {
    }

    @Override
    public void init()
    {
        Trace.fileInfo();

        super.init();
    }

    @Override
    public void update()
    {
        if ((App.getGameProgress().lives.getTotal() > 0)
            && (activeCount < Math.max(1, maxCount)))
        {
            create();

            activeCount++;
        }
    }

    @Override
    public void create()
    {
        if (App.getEntityUtils().canUpdate(GraphicID.G_BALL))
        {
            SpriteDescriptor descriptor = App.getEntities().getDescriptor(GraphicID.G_BALL);

            if (getActiveCount() == 0)
            {
                descriptor._POSITION.x = (int) App.getPlayer().getPosition().x;
                descriptor._POSITION.y = (int) App.getPlayer().getPosition().y;
            }
            else
            {
                descriptor._POSITION.x = (int) App.getEntities().balls.get(0).getPosition().x;
                descriptor._POSITION.y = (int) App.getEntities().balls.get(0).getPosition().y;
            }

            descriptor._POSITION.z = App.getEntityUtils().getInitialZPosition(GraphicID.G_BALL);
            descriptor._INDEX      = App.getEntityData().getEntityMap().size;

            int index = App.getEntities().balls.size;

            App.getEntities().balls.add(new Ball());
            App.getEntities().balls.get(index).initialise(descriptor);
            App.getEntities().balls.get(index).ballNumber = index;
            App.getEntityData().addEntity(App.getEntities().balls.get(index));
        }
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._BALL_MANAGER;
    }
}
