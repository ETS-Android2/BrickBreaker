package com.richikin.brickbreaker.scenes;

public enum ISceneType
{
    _MENU_PAGE      (0),
    _OPTIONS_PAGE   (1),
    _CREDITS_PAGE   (2),
    _DEVELOPER_PAGE (3),
    _STATS_PAGE     (4),
    _PRIVACY_PAGE   (5),
    _ABOUT_PAGE     (6);

    int value;

    ISceneType(int i)
    {
        value = i;
    }
}
