import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class MainClass {

	public static PagesManagment pm = new PagesManagment();
	public static RelationsManagment rm=new RelationsManagment();
	public static Page page = new Page();
	public static String[] newOutlinks = new String[100];
	public static String[] oldOutlinks = new String[100];
	public static int newOutlinksCount;
	public static int oldOutlinksCount;
	public static int[][] relations;

	public static void getPageInfo(File file) {

		newOutlinksCount = 0;
		oldOutlinksCount = 0;
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] tokens;
		while (scan.hasNextLine()) {
			tokens = scan.nextLine().split(": ");

			if (tokens.length > 1) {
				if (tokens[0].compareTo("url") == 0) {
					page.setUrl(tokens[1]);
				} else if (tokens[0].compareTo("fetch_time") == 0) {
					page.setFetch_time(tokens[1]);
				} else if (tokens[0].compareTo("length") == 0) {
					page.setLength(Integer.valueOf(tokens[1]));
				} else if (tokens[0].compareTo("modified_time") == 0) {
					page.setModified_time(tokens[1]);
				} else if (tokens[0].compareTo("type") == 0) {
					page.setType(tokens[1]);
				}
			} else if (tokens.length==1 &&  tokens[0].contains("http")) {
				if (pm.pageExist(tokens[0])[0] == -1) {
					newOutlinks[newOutlinksCount] = tokens[0];
					newOutlinksCount++;
				} else {
					oldOutlinks[oldOutlinksCount] = tokens[0];
					oldOutlinksCount++;
				}

			}

		}

		insetToDB();

	}

	public static void fileRead(String fileName) {
		File totalFiles = new File(fileName);
		String[] fileNames = null;
		int length = 0;

		if (!totalFiles.isDirectory()) {
			length = 1;
			fileNames = new String[1];
			fileNames[0] = fileName;

		} else {
			fileNames = totalFiles.list();
			length = fileNames.length;
		}
		File[] files = new File[length];

		int i = 0;
		String pathOfFolder = totalFiles.getPath();

		while (i < 1) {
			files[i] = new File(pathOfFolder + "/" + fileNames[i]);
			getPageInfo(files[i]);
			i++;
		}

	}

	public static void insetToDB() {

		int pageId = 0;
		int[] result = pm.pageExist(page.getUrl());

		if (result[0] == -1) {
			pageId = pm.addPage(page);
		} else if (result[0] == 0) {
			pageId = pm.updatePage(page);
		} else {
			pageId = result[1];
		}

		int[] newKeys=null;
		if (newOutlinksCount > 0)
			newKeys = pm.addOutlinks(newOutlinks, newOutlinksCount);
		int[] oldKeys=null;
		if (oldOutlinksCount > 0)
			oldKeys = pm.getKeys_oldOutlinks(oldOutlinks, oldOutlinksCount);

		relations = new int[newOutlinksCount + oldOutlinksCount][2];
		int relationCount = 0;
		int i = 0;
		while (i < oldOutlinksCount) {

			if (rm.relationExist(pageId, oldKeys[i])) {
				relations[relationCount][0] = pageId;
				relations[relationCount][1] = oldKeys[i];
				relationCount++;
			}
			i++;
		}
		i = 0;
		while (i < newOutlinksCount) {

			if (!rm.relationExist(pageId, newKeys[i])) {
			
				relations[relationCount][0] = pageId;
				relations[relationCount][1] = newKeys[i];
				relationCount++;
			}
			i++;

		}
		
		if(relationCount>0)
			rm.addRelation(relations, relationCount);

	}

	public static void main(String[] args) {

		fileRead("F:\\Javaworkspace\\SavePagese\\data");

	}
}
