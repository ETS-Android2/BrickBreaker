package com.richikin.gdxutils.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.richikin.gdxutils.maths.Item;

import java.util.Arrays;
import java.util.Comparator;

public class HighScoreUtils
{
    private static final String filePath = "hiscore_data.json";

    /**
     * The HighScore Table.
     * The table is 10 entrys in size PLUS one extra.
     * This extra entry is where a new hiscore is placed so that
     * array sorting can move it into its correct place.
     * <p>
     * Array index 0 is the highest value.
     * <p>
     * I thought about using Array but it just seemed
     * to be a bit much for such a small number of entries.
     */
    private       HighScore[] highScores;
    private final int         maxHiscores;
    private final Json        json;

    public HighScoreUtils(int max)
    {
        this.maxHiscores = max;
        this.json        = new Json();
        this.highScores  = new HighScore[maxHiscores + 1];

        for (int i = 0; i < (maxHiscores + 1); i++)
        {
            highScores[i] = new HighScore();
        }
    }

    /**
     * Adds a value to the extra position at the end of the table,
     * then sorts the table
     */
    public void addHighScore(HighScore highScore)
    {
        if (highScore.score >= highScores[maxHiscores - 1].score)
        {
            highScores[maxHiscores].level = highScore.level;
            highScores[maxHiscores].score = highScore.score;
            highScores[maxHiscores].name  = highScore.name;

            sortTable();

            writeTable();
        }
    }

    public int findInsertLevel(HighScore highScore)
    {
        int index = maxHiscores;

        while (highScores[index].score > highScore.score)
        {
            index--;
        }

        return index;
    }

    /**
     * Loads the hiscore data from the json file into the
     * hiscore table array.
     * <p>
     * NOTE: If the Json file does not exist then the table is set up
     * with default data and then written out as a Json file
     * for future use.
     */
    public void loadTableData()
    {
        if (!Gdx.files.local(filePath).exists())
        {
            setDefaultHiscoreTable();
        }

        highScores = json.fromJson(HighScore[].class, Gdx.files.local(filePath));
    }

    /**
     * Writes the hiscore table back to the json file
     */
    private void writeTable()
    {
        FileHandle handle = Gdx.files.local(filePath);

        handle.writeString(json.prettyPrint(json.toJson(highScores)), false);
    }

    /**
     * Sorts the table
     */
    private void sortTable()
    {
        HighScore[] temp = new HighScore[maxHiscores + 1];

        for (int i = 0; i < (maxHiscores + 1); i++)
        {
            temp[i] = new HighScore();

            temp[i].level = highScores[i].level;
            temp[i].score = highScores[i].score;
            temp[i].name  = highScores[i].name;
        }

        Arrays.sort(temp, new SortByScore());

        int j = maxHiscores;

        for (int i = 0; i < (maxHiscores + 1); i++)
        {
            highScores[i].rank  = i + 1;
            highScores[i].level = temp[j].level;
            highScores[i].score = temp[j].score;
            highScores[i].name  = temp[j--].name;
        }
    }

    /**
     * Checks if a new hiscore entry
     * can be added.
     *
     * @return boolean - TRUE if can be added.
     */
    public boolean canAddNewEntry(Item score)
    {
        boolean available = false;

        if (score != null)
        {
            if (score.getTotal() >= highScores[maxHiscores - 1].score)
            {
                available = true;
            }
        }

        return available;
    }

    /**
     * Gets the hiscore table
     *
     * @return a reference to the table
     */
    public HighScore[] getHighScoreTable()
    {
        return highScores;
    }

    /**
     * Reset the hiscore table to default values.
     */
    public void resetTable()
    {
        setDefaultHiscoreTable();
    }

    /**
     * Writes default values to the hiscore table if the
     * json file does not exist, ie because of a
     * fresh installation
     */
    private void setDefaultHiscoreTable()
    {
        //@formatter:off
        HighScore[] defaultTable =
            {
                new HighScore(11, 1,     0, "TABLE END"),
                new HighScore(10, 1,  1000, "CHUCKY"),
                new HighScore( 9, 1,  2000, "THING"),
                new HighScore( 8, 1,  3000, "LUIGI"),
                new HighScore( 7, 1,  4000, "PACMAN"),
                new HighScore( 6, 1,  5000, "KONG"),
                new HighScore( 5, 1,  6000, "HORACE"),
                new HighScore( 4, 1,  7000, "MARIO"),
                new HighScore( 3, 1,  8000, "JS WILLY"),
                new HighScore( 2, 1,  9000, "MONTY"),
                new HighScore( 1, 1, 10000, "JETMAN"),
                };
        //@formatter:on

        for (int i = 0; i < maxHiscores + 1; i++)
        {
            highScores[i].rank  = defaultTable[i].rank;
            highScores[i].level = defaultTable[i].level;
            highScores[i].score = defaultTable[i].score;
            highScores[i].name  = defaultTable[i].name;
        }

        sortTable();
        writeTable();
    }

    static class SortByScore implements Comparator<HighScore>
    {
        public int compare(HighScore a, HighScore b)
        {
            return Integer.compare(a.score, b.score);
        }
    }
}
