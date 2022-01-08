package com.richikin.brickbreaker.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.entities.IEntityComponent;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec2F;
import com.richikin.gdxutils.maths.SimpleVec3;
import com.richikin.gdxutils.maths.SimpleVec3F;
import com.richikin.gdxutils.physics.CollisionObject;
import com.richikin.gdxutils.physics.Direction;
import com.richikin.gdxutils.physics.aabb.AABB;
import com.richikin.gdxutils.physics.aabb.IAABBCollision;
import com.richikin.gdxutils.physics.aabb.ICollisionCallback;

import java.util.Arrays;

public class GdxSprite implements IEntityComponent, ISpriteComponent
{
    // -----------------------------------------------
    // Identity etc.
    //
    public GraphicID    gid;                        // Entity ID
    public GraphicID    type;                       // Entity Type - _ENTITY, _PICKUP, _OBSTACLE, etc.
    public ActionStates entityAction;               // Current action/state
    public int          spriteNumber;               // Position in the EntityMap array
    public int          link;
    public boolean      isLinked;
    public boolean      isMainCharacter;
    public Sprite       sprite;

    // -----------------------------------------------
    // Movement
    //
    public Direction   direction;
    public Direction   lookingAt;
    public SimpleVec2F distance;
    public SimpleVec2F speed;

    // -----------------------------------------------
    // Transform
    //
    public SimpleVec3F initXYZ;                      // Initial Map position, set on creation.
    public int         zPosition;
    public boolean     isFlippedX;
    public boolean     isFlippedY;
    public boolean     canFlip;
    public boolean     isRotating;
    public float       rotateSpeed;

    // -----------------------------------------------
    // Collision Related
    //
    public Body               b2dBody;              // Box2D Physics body
    public CollisionObject    collisionObject;      // ...
    public ICollisionCallback collisionCallback;
    public IAABBCollision     aabb;
    public short              bodyCategory;         // Bit-mask entity collision type (See Gfx()).
    public short              collidesWith;         // Bit-mask of entity types that can be collided with

    // -----------------------------------------------
    // Animation related
    public Animation<TextureRegion> animation;
    public float                    elapsedAnimTime;
    public TextureRegion[]          animFrames;
    public boolean                  isAnimating;
    public int                      frameWidth;           // Width in pixels, or width of frame for animations
    public int                      frameHeight;          // Width in pixels, or width of frame for animations
    public boolean                  isDrawable;

    // --------------------------------------------------------------
    // Code
    // --------------------------------------------------------------

    public GdxSprite()
    {
        this(GraphicID.G_NO_ID);
    }

    public GdxSprite(GraphicID gid)
    {
        this.gid = gid;
    }

    /**
     * Initialise this Sprite.
     * Override in any entity classes and add any
     * other relevant initialisation code AFTER the
     * call to create().
     *
     * @param descriptor The {@link SpriteDescriptor} holding
     *                   all setup information.
     */
    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);
    }

    @Override
    public void create(SpriteDescriptor descriptor)
    {
        sprite    = new Sprite();
        direction = new Direction();
        lookingAt = new Direction();
        speed     = new SimpleVec2F();
        distance  = new SimpleVec2F();
        initXYZ   = new SimpleVec3F();
        aabb      = new AABB();

        isDrawable      = true;
        isRotating      = false;
        isFlippedX      = false;
        isFlippedY      = false;
        canFlip         = true;
        isMainCharacter = false;
        spriteNumber    = descriptor._INDEX;
        isAnimating     = (descriptor._FRAMES > 1);

        setActionState(ActionStates._NO_ACTION);

        if (descriptor._ASSET != null)
        {
            setAnimation(descriptor, descriptor._ANIM_RATE);
        }

        // Determine the initial starting position by
        // multiplying the marker tile position by tile size
        // and then adding on any supplied modifier value.
        SimpleVec3 vec3 = new SimpleVec3
            (
                descriptor._POSITION.x,
                descriptor._POSITION.y,
                descriptor._POSITION.z
            );
        SimpleVec3 vec3m = getPositionModifier();
        vec3.add(vec3m.x, vec3m.y, vec3m.z);

        // Set the initial starting position
        initPosition(vec3);

        setCollisionObject(sprite.getX(), sprite.getY());

        isLinked = (descriptor._LINK > 0);
        link     = descriptor._LINK;
    }

    @Override
    public void create(SpriteDescriptor descriptor, BodyDef.BodyType bodyType)
    {
        create(descriptor);

        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            switch (bodyType)
            {
                case StaticBody:
                {
                    App.getEntityUtils().addStaticPhysicsBody(this);
                }
                break;

                case DynamicBody:
                {
                    App.getEntityUtils().addDynamicPhysicsBody(this);
                }
                break;

                case KinematicBody:
                {
                    App.getEntityUtils().addKinematicPhysicsBody(this);
                }
                break;
            }
        }
    }

    @Override
    public void initPosition(SimpleVec3 vec3F)
    {
        sprite.setSize(frameWidth, frameHeight);
        sprite.setPosition(vec3F.x, vec3F.y);
        sprite.setBounds(sprite.getX(), sprite.getY(), frameWidth, frameHeight);
        sprite.setOriginCenter();

        initXYZ.set(sprite.getX(), sprite.getY(), vec3F.z);

        zPosition = vec3F.z;
    }

    @Override
    public SimpleVec3 getPositionModifier()
    {
        return new SimpleVec3(0, 0, 0);
    }

    @Override
    public void preUpdate()
    {
        if (App.getGameProgress().levelCompleted
            && !isMainCharacter
            && (entityAction != ActionStates._DEAD)
            && (entityAction != ActionStates._DYING))
        {
            entityAction = ActionStates._DYING;
        }
        else
        {
            if (entityAction == ActionStates._RESTARTING)
            {
                sprite.setPosition(initXYZ.getX(), initXYZ.getY());
            }
        }
    }

    @Override
    public void update()
    {
        animate();

        updateCommon();
    }

    @Override
    public void postUpdate()
    {
        updateCollisionCheck();
    }

    /**
     * Common updates for all entities
     */
    @Override
    public void updateCommon()
    {
        if (isRotating)
        {
            sprite.rotate(rotateSpeed);
        }

        if (canFlip)
        {
            sprite.setFlip(isFlippedX, isFlippedY);
        }
    }

    @Override
    public void tidy(int index)
    {
    }

    /**
     * The LibGDX {@link Sprite} class doesn't have a
     * getPosition() method, just getX() and getY(),
     * so this is here to make up for that.
     */
    @Override
    public Vector3 getPosition()
    {
        return new Vector3(sprite.getX(), sprite.getY(), zPosition);
    }

    @Override
    public void preDraw()
    {
        setPositionFromBody();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (isDrawable)
        {
            try
            {
                sprite.draw(spriteBatch);
            }
            catch (NullPointerException npe)
            {
                Trace.dbg(gid.name() + " : " + npe.getMessage());
            }
        }
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            sprite.setRegion(App.getEntityUtils().getKeyFrame(animation, elapsedAnimTime, true));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void setAnimation(SpriteDescriptor descriptor)
    {
        setAnimation(descriptor, descriptor._ANIM_RATE);
    }

    @Override
    public void setAnimation(SpriteDescriptor descriptor, float frameRate)
    {
        animFrames = new TextureRegion[descriptor._FRAMES];

        TextureRegion asset = App.getAssets().getAnimationRegion(descriptor._ASSET);

        if (descriptor._SIZE != null)
        {
            frameWidth  = descriptor._SIZE.x;
            frameHeight = descriptor._SIZE.y;
        }
        else
        {
            frameWidth  = asset.getRegionWidth() / descriptor._FRAMES;
            frameHeight = asset.getRegionHeight();
        }

        TextureRegion[][] tmpFrames = asset.split(frameWidth, frameHeight);

        int i = 0;

        for (final TextureRegion[] tmpFrame : tmpFrames)
        {
            for (final TextureRegion textureRegion : tmpFrame)
            {
                if (i < descriptor._FRAMES)
                {
                    animFrames[i++] = textureRegion;
                }
            }
        }

        animation = new Animation<>(frameRate / 6f, animFrames);
        animation.setPlayMode(descriptor._PLAYMODE);

        sprite.setRegion(animFrames[0]);
    }

    /**
     * Sets the sprite position from the physics body coordinates
     */
    @Override
    public void setPositionFromBody()
    {
        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            if (b2dBody != null)
            {
                sprite.setPosition
                    (
                        (b2dBody.getPosition().x * Gfx._PPM) - (frameWidth >> 1),
                        (b2dBody.getPosition().y * Gfx._PPM) - (frameHeight >> 1)
                    );
            }
        }
    }

    @Override
    public CollisionObject getCollisionObject()
    {
        return collisionObject;
    }

    @Override
    public void setCollisionObject(float xPos, float yPos)
    {
        setCollisionObject((int) xPos, (int) yPos);
    }

    @Override
    public void setCollisionObject(int xPos, int yPos)
    {
        collisionObject = App.getCollisionUtils().newObject
            (
                new Rectangle(xPos, yPos, frameWidth, frameHeight)
            );

        collisionObject.gid          = this.gid;
        collisionObject.type         = GraphicID._ENTITY;
        collisionObject.isObstacle   = false;
        collisionObject.parentEntity = this;
    }

    @Override
    public Rectangle getCollisionRectangle()
    {
        return collisionObject.rectangle;
    }

    @Override
    public void setActionState(ActionStates action)
    {
        this.entityAction = action;
    }

    @Override
    public ActionStates getActionState()
    {
        return this.entityAction;
    }

    @Override
    public void addCollisionCallback(ICollisionCallback callback)
    {
        collisionCallback = callback;
    }

    @Override
    public void updateCollisionCheck()
    {
        if (collisionObject != null)
        {
            //
            // make sure the collision rectangle
            // is where the player is
            updateCollisionBox();

            //
            // Invisibility is set for a period of
            // time whn the entity is not affected
            // by any collisions.
            collisionObject.checkInvisibility();
            collisionObject.clearCollision();

            if (!App.getAppConfig().isUsingBox2DPhysics())
            {
                //
                // All CollisionObjects are collidable by default.
                if (collisionObject.action == ActionStates._HITTABLE)
                {
                    if ((aabb != null) && aabb.checkAABBBoxes(collisionObject))
                    {
                        collisionObject.action = ActionStates._HITTING;

                        if (collisionCallback != null)
                        {
                            //
                            // Pass the collisionObject of the contact entity
                            collisionCallback.onPositiveCollision(collisionObject.contactEntity.getCollisionObject());
                        }

                        if (collisionObject.isInvisibilityAllowed)
                        {
                            collisionObject.setInvisibility(1000);
                        }
                    }

                    //
                    // collisionObject.action might have changed at this point.
                    if (collisionObject.action != ActionStates._HITTING)
                    {
                        if (collisionCallback != null)
                        {
                            collisionCallback.onNegativeCollision();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateCollisionBox()
    {
        if (sprite != null)
        {
            collisionObject.rectangle.x      = sprite.getX();
            collisionObject.rectangle.y      = sprite.getY();
            collisionObject.rectangle.width  = frameWidth;
            collisionObject.rectangle.height = frameHeight;
        }
    }

    @Override
    public float getTopEdge()
    {
        return sprite.getY() + frameHeight;
    }

    @Override
    public float getRightEdge()
    {
        return sprite.getX() + frameWidth;
    }

    @Override
    public short getBodyCategory()
    {
        return bodyCategory;
    }

    @Override
    public short getCollidesWith()
    {
        return collidesWith;
    }

    @Override
    public void setIndex(int index)
    {
        spriteNumber = index;
    }

    @Override
    public int getIndex()
    {
        return spriteNumber;
    }

    @Override
    public GraphicID getGID()
    {
        return gid;
    }

    @Override
    public GraphicID getType()
    {
        return type;
    }

    @Override
    public void dispose()
    {
        aabb.dispose();

        Arrays.fill(animFrames, null);

        gid               = null;
        type              = null;
        entityAction      = null;
        sprite            = null;
        direction         = null;
        lookingAt         = null;
        distance          = null;
        speed             = null;
        initXYZ           = null;
        b2dBody           = null;
        collisionObject   = null;
        collisionCallback = null;
        aabb              = null;
        animation         = null;
        animFrames        = null;
    }
}
