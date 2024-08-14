package xyz.mythicalsystems.mcpanelxcore.utils;

import java.util.ArrayList;

import xyz.mythicalsystems.mcpanelxcore.objects.Player;

public class PlayTimeHelper {
	private static ArrayList<Player> playTimePlayers;

	public static String getFormattedPlayTime(long totalSecondsPlayed) {
		final int MINUTES_IN_AN_HOUR = 60;
		final int SECONDS_IN_A_MINUTE = 60;

		int seconds = (int) (totalSecondsPlayed % SECONDS_IN_A_MINUTE);
		int totalMinutes = (int) (totalSecondsPlayed / SECONDS_IN_A_MINUTE);
		int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
		int hours = totalMinutes / MINUTES_IN_AN_HOUR;

		return hours + " hours " + minutes + " minutes " + seconds + " seconds";
	}

	public static void onEnable() {
		playTimePlayers = new ArrayList<Player>();
	}

	public static ArrayList<Player> getPlayTimePlayers() {
		return playTimePlayers;
	}

	public static long getPlayTimeInSeconds(long joinTime) {
		return (System.currentTimeMillis() - joinTime) / 1000;
	}

	public static long getPlayTimeInMinutes(long joinTime) {
		return getPlayTimeInSeconds(joinTime) / 60;
	}

	public static long getPlayTimeInHours(long joinTime) {
		return getPlayTimeInMinutes(joinTime) / 60;
	}

	public static long getPlayTimeInDays(long joinTime) {
		return getPlayTimeInHours(joinTime) / 24;
	}

	public static long getPlayTimeInWeeks(long joinTime) {
		return getPlayTimeInDays(joinTime) / 7;
	}

	public static long getPlayTimeInMonths(long joinTime) {
		return getPlayTimeInDays(joinTime) / 30;
	}

	public static long getPlayTimeInYears(long joinTime) {
		return getPlayTimeInDays(joinTime) / 365;
	}

	public static long getPlayTimeInDecades(long joinTime) {
		return getPlayTimeInYears(joinTime) / 10;
	}

	public static long getPlayTimeInCenturies(long joinTime) {
		return getPlayTimeInDecades(joinTime) / 10;
	}

	public static long getPlayTimeInMillenniums(long joinTime) {
		return getPlayTimeInCenturies(joinTime) / 10;
	}

	public static long getPlayTimeInEons(long joinTime) {
		return getPlayTimeInMillenniums(joinTime) / 10;
	}

	public static long getPlayTimeInAges(long joinTime) {
		return getPlayTimeInEons(joinTime) / 10;
	}

	public static long getPlayTimeInEpochs(long joinTime) {
		return getPlayTimeInAges(joinTime) / 10;
	}

	public static long getPlayTimeInEras(long joinTime) {
		return getPlayTimeInEpochs(joinTime) / 10;
	}

	public static long getPlayTimeInPeriods(long joinTime) {
		return getPlayTimeInEras(joinTime) / 10;
	}

	public static long getPlayTimeInCycles(long joinTime) {
		return getPlayTimeInPeriods(joinTime) / 10;
	}

	public static long getPlayTimeInAstronomicalUnits(long joinTime) {
		return getPlayTimeInCycles(joinTime) / 10;
	}

	public static long getPlayTimeInLightYears(long joinTime) {
		return getPlayTimeInAstronomicalUnits(joinTime) / 10;
	}
}
