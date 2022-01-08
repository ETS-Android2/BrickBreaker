package com.richikin.brickbreaker.ui.panels;

import com.badlogic.gdx.utils.Array;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.gdxutils.logging.Trace;

public class PanelManager
{
    private static class Panel
    {
        public IPanel  panel;
        public boolean enabled;
        public String  name;

        public Panel(IPanel _panel, boolean _enabled, String _name)
        {
            panel   = _panel;
            enabled = _enabled;
            name    = _name;
        }
    }

    private final Array<Panel> panels;
    private       boolean      enabled;

    public PanelManager()
    {
        panels  = new Array<>();
        enabled = false;
    }

    public void update()
    {
        if (isEnabled())
        {
            for (int i = 0; i < panels.size; i++)
            {
                if (panels.get(i).enabled)
                {
                    // Returns TRUE if finished...
                    if (panels.get(i).panel.update())
                    {
                        panels.removeIndex(i);
                    }
                }
            }
        }
    }

    public void draw()
    {
        for (int i = 0; i < panels.size; i++)
        {
            if (panels.get(i).enabled)
            {
                panels.get(i).panel.draw(App.getSpriteBatch());
            }
        }
    }

    public void addSlidePanel(String imageName)
    {
        SlidePanel panel = new SlidePanel();

        panel.initialise(App.getAssets().getObjectRegion(imageName), imageName);
        panel.activate();
        panel.action = ActionStates._OPENING;

        panels.add(new Panel(panel, true, imageName));

        enable();
    }

    @SuppressWarnings("EmptyMethod")
    public void closeSlidePanel(String name)
    {
    }

    public void addZoomPanel(String imageName, int displayDelay)
    {
        if (App.getAssets().getTextRegion(imageName) == null)
        {
            Trace.err("ERROR: ", imageName, " not loaded!");
        }
        else
        {
            Trace.dbg(imageName);

            IPanel panel = new ZoomPanel();

            panel.initialise
                (
                    App.getAssets().getTextRegion(imageName),
                    imageName,
                    /* _canPause   */(displayDelay > 0),
                    /* _bounceBack */ true
                );
            panel.setPauseTime(displayDelay);

            panels.add(new Panel(panel, true, imageName));

            enable();

            Trace.dbg("Finished: Active messages = ", panels.size, "(", panel.getNameID(), ")");
        }
    }

    public void addZoomPanel(String fileName, int delay, int x, int y)
    {
        Trace.dbg(fileName);

        addZoomPanel(fileName, delay);
        setPosition(fileName, x, y);
        enable();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean panelExists(String nameID)
    {
//        StringBuilder sb = new StringBuilder(nameID);

        boolean exists = false;

        for (Panel msg : panels)
        {
            if (nameID.equals(msg.name))
            {
//                sb.append(" ");
//                sb.append(msg.enabled);

                exists = true;
                break;
            }
        }

//        sb.append(" ");
//        sb.append(exists);
//        sb.append(" ");

//        Trace.dbg(sb.toString());

        return exists;
    }

    public void setPosition(String nameID, int x, int y)
    {
        for (Panel msg : panels)
        {
            if (nameID.equals(msg.name))
            {
                msg.panel.setPosition(x, y);
            }
        }
    }

    public void enable()
    {
        enabled = true;
    }

    public void disable()
    {
        enabled = false;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
