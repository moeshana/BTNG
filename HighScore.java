package edu.mccc.cos210.ds.fp.bugattitng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import edu.mccc.cos210.ds.SortedList;



public class HighScore {
	private final String FileName = "src/edu/mccc/cos210/ds/fp/bugattitng/database.txt";
	private SortedList<Data> AllScore = new SortedList<Data>(new MyComparator());
	
	public static void main(String... args) {
		HighScore a = new HighScore();
		for (int i = 0; i < 20; i++) {
			a.saveResult("shana" + i + ":" + (20.0 - i) + ":" +  "junfeng"+i);
		}
		HighScore c = new HighScore();
		String [] tm = c.getTop(10);
		for (String da : tm) {
			System.out.println(da);
		}
	}


	
	private SortedList<Data> getHighScoreRank() {
		SortedList<Data> ready = new SortedList<Data>(new MyComparator());
		try(
			BufferedReader br = new BufferedReader(
			new FileReader(FileName)
			)
		){
			String s = "";
			while ((s = br.readLine()) != null) {
				String[] tmp = s.split(":");
				Data i = new Data(tmp[0],Double.valueOf(tmp[1]),tmp[2]);
				ready.add(i);
			}
			br.close();
		}  catch (Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
		
		return ready;
	}

	
	public void saveResult(String score) {
		File testfile = new File(FileName);
		if (testfile.exists()) {
			this.AllScore = this.getHighScoreRank();
		} else {
			this.AllScore = null;
		}
		try (
				PrintWriter pw = new PrintWriter(
					new FileWriter(FileName)
				)
			) {
				if (AllScore != null) {
					for (Data a : AllScore) {
						saveInto(pw,a.toString());
					}
				}
				saveInto(pw, score);
				pw.close();
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
				System.exit(-1);
			}
		}
	private void saveInto(PrintWriter pw, String score) {
		pw.println(score);
	}
	
	public String[] getTop(int top) {
		SortedList<Data> hahaha = new SortedList<Data>(new MyComparator());
		hahaha = getHighScoreRank();
		String[] res = new String[top];
		for (int i = 0; i < top; i++) {
			String a = "TOP " + (i + 1) + ":" + hahaha.getFirst();
			res[i] = a;
			hahaha.removeFirst();
		}
		return res;
	}
	
	private class Data implements Comparable<Data> {
		private String name ;
		private double score;
		private String BugName;
		
		public Data(String name, double score, String BugName) {
			this.name = name;
			this.score = score;
			this.BugName = BugName;
		}
		public double getScore() {
			return this.score;
		}
		@Override
		public int compareTo(Data a) {
			if (this.score > a.getScore()) {
				return -1;
			} else {
				if (this.score == a.getScore()) {
					return 0;
				} 
			}
			return 1;
		}
		public String toString() {
			return this.name + ":" + this.score + ":" + this.BugName;
		}
	}
	
	class MyComparator implements java.util.Comparator<Data> {
		@Override
		public int compare(Data a, Data b) {
			return b.compareTo(a);
		}
	}
}
