package com.richikin.brickbreaker.maps;

public interface IMapParser
{
    void initialiseLevelMap();

    void createPositioningData();

    void parseMarkerTiles();

    void parseMapObjects();

    void setGameLevelMap();
}
