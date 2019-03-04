package com.ericwyn.juzcar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ericwyn on 19-3-3.
 */
public class JuzcarLogs {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static boolean logFalg = true;

    public static void setLogFalg(boolean logFalg) {
        JuzcarLogs.logFalg = logFalg;
    }

    public static enum Level{
        DEBUG("DEBUG"),
        ERROR("ERROR");

        private String level;
        Level(String level){
            this.level = level;
        }

        @Override
        public String toString() {
            return this.level;
        }
    }

    private static String logTitle(){
        return "[ Juzcar LOG] [ DEBUG ]"+sdf.format(new Date());
    }
    private static String logTitle(Level level){
        return "[ Juzcar LOG] [ "+level+" ]"+sdf.format(new Date());
    }

    public static void SOUT(String logMsg){
        System.out.println(logTitle(Level.DEBUG)+" "+logMsg);
    }
}
