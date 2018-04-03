package com.xiaoyu.lingdian.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioUtil {

	public static String convertAmr2Ilbc(String ffmpegPath, File amr, String path) {
		String savename = amr.getName().substring(0, amr.getName().lastIndexOf("."));
		List<String> cmd = new ArrayList<String>();
		cmd.add(ffmpegPath);
		cmd.add("-i");
		cmd.add(amr.getPath());
		cmd.add("-f");
		cmd.add("caf");
		cmd.add("-acodec");
		cmd.add("ilbc");
		cmd.add(path + savename + ".ilbc");
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.command(cmd);
			builder.start();
			return savename;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String convertIlbc2Amr(String ffmpegPath, File ilbc, String path) {
		List<String> cmd = new ArrayList<String>();
		String savename = ilbc.getName().substring(0, ilbc.getName().lastIndexOf("."));
		cmd.add(ffmpegPath);
		cmd.add("-i");
		cmd.add(ilbc.getPath());
		cmd.add("-vn");
		cmd.add("-ac");
		cmd.add("1");
		cmd.add("-ab");
		cmd.add("4.75k");
		cmd.add("-ar");
		cmd.add("8000");
		cmd.add("-f");
		cmd.add("amr");
		cmd.add(path + savename + ".amr");
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.command(cmd);
			builder.start();
			return savename;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String convertAmrOrIlbcToMp3(String ffmpegPath, File amrOrIlbc, String path) {
		String savename = amrOrIlbc.getName().substring(0, amrOrIlbc.getName().lastIndexOf("."));
		List<String> cmd = new ArrayList<String>();
		cmd.add(ffmpegPath);
		cmd.add("-i");
		cmd.add(amrOrIlbc.getPath());
		cmd.add("-f");
		cmd.add("mp3");
		cmd.add(path + savename + ".mp3");
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.command(cmd);
			builder.start();
			return savename;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}