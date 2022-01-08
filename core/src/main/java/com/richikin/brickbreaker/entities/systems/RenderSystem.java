package com.richikin.brickbreaker.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.components.SpriteComponent;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem
{
    private final Array<Entity>                    renderQueue;
    private final ComponentMapper<SpriteComponent> spriteMapper;
    private       Comparator<Entity>               comparator;

    public RenderSystem(SpriteBatch spriteBatch)
    {
        // Fetch all entities with SpriteComponent
        super(Family.all(SpriteComponent.class).get(), new ZComparator());

        // Create component mappers
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);

        // renderQueue is the array for sorting entities for display.
        renderQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);

        // Sort the renderQueue based on Z index
        renderQueue.sort(comparator);

        for (Entity entity : renderQueue)
        {
            SpriteComponent spriteComponent = spriteMapper.get(entity);

            if ((spriteComponent.sprite != null)
                && spriteComponent.isDrawable)
            {
                spriteComponent.sprite.draw(App.getSpriteBatch());
            }
        }

        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        renderQueue.add(entity);
    }
}
