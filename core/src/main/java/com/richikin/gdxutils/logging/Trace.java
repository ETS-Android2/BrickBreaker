package com.richikin.gdxutils.logging;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Trace
{
    /*
     * NB: Do not make these final, as they can be modified
     */
    private static final String  debugTag = "Debug";
    private static final String  infoTag  = "Info";
    private static final String  errorTag = "ERROR";
    private static       File    logFile;
    private static       boolean isWriteFileActive;

    /**
     * Write a debug string to logcat or console.
     * The string can contain format options.
     *
     * @param formatString The string, or format string, to display.
     * @param args         Arguments for use in format strings.
     */
    public static void dbg(String formatString, Object... args)
    {
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        {
            StringBuilder sb = new StringBuilder();

            sb.append(getTimeStampInfo());
            sb.append(colonDivider());
            sb.append(getFileInfo(new Throwable().getStackTrace()));
            sb.append(colonDivider());

            sb.append(formatString);

            if (args.length > 0)
            {
                for (Object arg : args)
                {
                    sb.append(" ");
                    sb.append(arg);
                }
            }

            Gdx.app.debug(debugTag, sb.toString());

            writeToFile(sb.toString());
        }
    }

    /**
     * Write an error string to logcat or console.
     *
     * @param string The string to write.
     */
    public static void err(String string, Object... args)
    {
        if (Gdx.app.getLogLevel() != Application.LOG_ERROR)
        {
            StringBuilder sb = new StringBuilder();

            sb.append(getTimeStampInfo());
            sb.append(colonDivider());
            sb.append(getFileInfo(new Throwable().getStackTrace()));
            sb.append(colonDivider());

            sb.append(string);

            if (args.length > 0)
            {
                for (Object arg : args)
                {
                    sb.append(" ");
                    sb.append(arg);
                }
            }

            Gdx.app.debug(errorTag, sb.toString());
        }
    }

    public static void fileInfo()
    {
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        {
            StringBuilder sb = new StringBuilder();

            sb.append(getTimeStampInfo());
            sb.append(colonDivider());
            sb.append(getFileInfo(new Throwable().getStackTrace()));

            Gdx.app.debug(debugTag, sb.toString());

            writeToFile(sb.toString());
        }
    }

    public static void info(String formatString, Object... args)
    {
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        {
            StringBuilder sb = new StringBuilder();

            sb.append(formatString);

            if (args.length > 0)
            {
                for (Object arg : args)
                {
                    sb.append(" ");
                    sb.append(arg);
                }
            }

            Gdx.app.debug(infoTag, sb.toString());

            writeToFile(sb.toString());
        }
    }

    public static void logObject(boolean isNewObject)
    {
    }

    private static String createMessage(String formatString, StackTraceElement[] ste, Object... args)
    {
        // TODO: 12/11/2021
        // Methods dbg() and err() should use this instead, ONCE I have
        // decided on an elegant way to store the caller details so that
        // the correct information is written out.

        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    private static String getTimeStampInfo()
    {
        GregorianCalendar c  = new GregorianCalendar();
        StringBuilder     sb = new StringBuilder();

        sb.append(c.get(Calendar.HOUR_OF_DAY)).append(":");
        sb.append(c.get(Calendar.MINUTE)).append(":");
        sb.append(c.get(Calendar.SECOND)).append(":");
        sb.append(c.get(Calendar.MILLISECOND));

        return sb.toString();
    }

    private static String getTimeAndDateInfo()
    {
        GregorianCalendar c  = new GregorianCalendar();
        StringBuilder     sb = new StringBuilder();

        sb.append(c.getTime().toString());

        return sb.toString();
    }

    private static String getFileInfo(StackTraceElement[] ste)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(ste[1].getFileName()).append(doubleColonDivider());
        sb.append(ste[1].getMethodName()).append("()").append(doubleColonDivider());
        sb.append(ste[1].getLineNumber());

        return sb.toString();
    }

    private static String colonDivider()
    {
        return " : ";
    }

    private static String doubleColonDivider()
    {
        return "::";
    }

    /**
     * Writes a divider line to logcat or console.
     */
    public static void divider()
    {
        divider(100);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _length The length of the divider.
     */
    public static void divider(int _length)
    {
        divider('-', _length);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _char   The character to use for the divider.
     */
    public static void divider(char _char)
    {
        divider(_char, 100);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _length The length of the divider.
     * @param _char   The character to use for the divider.
     */
    public static void divider(char _char, int _length)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < _length; i++)
        {
            sb.append(_char);
        }

        Gdx.app.debug(debugTag, sb.toString());

        writeToFile(sb.toString());
    }

    /**
     * Enables or Disables write to file.
     */
    public static void enableWriteToFile(boolean enable)
    {
        isWriteFileActive = enable;
    }

    /**
     * Opens a physical file for writing copies of debug messages to.
     * Note: Only messages output via Trace#dbg() are written to file.
     * Note: An option to specify target directory is to be added.
     *
     * @param fileName       - The filename. This should be filename only,
     *                       - and the file will be created in the working directory.
     * @param deleteExisting - True to delete existing copies of the file.
     *                       - False to append to existing file.
     */
    public static void openDebugFile(String fileName, boolean deleteExisting)
    {
        boolean isSuccess = true;

        if ((Gdx.app.getType() == Application.ApplicationType.Desktop)
            && isWriteFileActive)
        {
            logFile = new File(fileName);

            if (logFile.exists())
            {
                if (deleteExisting)
                {
                    if (logFile.delete())
                    {
                        Gdx.app.debug(debugTag, "Existing logfile deleted.");
                    }
                }
            }

            if (!logFile.exists())
            {
                try
                {
                    //noinspection AssignmentUsedAsCondition
                    if (isSuccess = logFile.createNewFile())
                    {
                        Gdx.app.debug(debugTag, "LOGGER: Logfile created: " + logFile.getName());
                    }
                }
                catch (IOException ioe)
                {
                    err("Cannot create log file: " + logFile);
                    Stats.incMeter(SystemMeters._IO_EXCEPTION.get());
                    isSuccess = false;
                }
            }

            if (isSuccess)
            {
                Date              date = new Date();
                GregorianCalendar c    = new GregorianCalendar();
                c.setTime(date);

                writeToFile("-----------------------------------------------------------");
                writeToFile("Filename: " + logFile.toString());
                writeToFile("Created: " + c.getTime().toString());
                writeToFile("-----------------------------------------------------------");
            }
        }
    }

    /**
     * Writes text to the logFile, if it exists.
     *
     * @param text String holding the text to write.
     */
    public static void writeToFile(String text)
    {
        if (isWriteFileActive && (logFile != null) && logFile.exists())
        {
            try
            {
                //
                // BufferedWriter for performance.
                // Pass TRUE to set append to file flag.
                BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));

                bw.append(text);
                bw.write('\r');
                bw.close();
            }
            catch (IOException ioe)
            {
                Stats.incMeter(SystemMeters._IO_EXCEPTION.get());
            }
        }
    }
}