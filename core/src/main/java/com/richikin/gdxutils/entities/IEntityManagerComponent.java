package com.richikin.gdxutils.entities;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.GraphicID;

public interface IEntityManagerComponent extends Disposable
{
    void init();

    void update();

    void create();

    void free();

    void free(final GraphicID gid);

    void reset();

    int getActiveCount();

    void setActiveCount(int numActive);

    void addMaxCount(int add);

    void setMaxCount(int max);

    GraphicID getGID();

    boolean isPlaceable();

    void setPlaceable(boolean placeable);
}
