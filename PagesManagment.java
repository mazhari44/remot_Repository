import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PagesManagment {

	int[] outlinksId;
	int[] pageId;
	int length = 0;


	public int[] pageExist(String url) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int[] result =new int[2] ;
		result[0]=-1;
		try {

			ps = DBConnection.connection().prepareStatement(
					"select id,fetch_time from page where url like ?");
			ps.setString(1, url);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getString(2) != null)
					result[0] = 1;
				else {
					result[0] = 0;
				}
				result[1]=rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public int updatePage(Page page) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int key = 0;

		try {
			ps = DBConnection.connection()
					.prepareStatement(
							"UPDATE page SET fetch_time=?,length=?,modified_time=?,type=? WHERE url like ?",
							Statement.RETURN_GENERATED_KEYS);

			
			ps.setString(1, page.getFetch_time());
			ps.setInt(2, page.getLength());
			ps.setString(3, page.getModified_time());
			ps.setString(4, page.getType());
			ps.setString(5, page.getUrl());
			
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			key = rs.getInt(1);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return key;
		
	}

	public int addPage(Page page) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String url=page.getUrl();
		String query="INSERT INTO page(url,fetch_time, length,modified_time, type) VALUES (?,?,?,?,?)";
		int key = 0;

		try {
			
			ps = DBConnection.connection()
					.prepareStatement(query	,
							Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, url);
			ps.setString(2, page.getFetch_time());
			ps.setInt(3, page.getLength());
			ps.setString(4, page.getModified_time());
			ps.setString(5, page.getType());
			
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			key = rs.getInt(1);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return key;

	}

	public int[] addOutlinks(String[] outlinks, int outlinksCount) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int i = 0;

		try {
			ps = DBConnection.connection()
					.prepareStatement(
							"INSERT INTO page(url, fetch_time, length, modified_time, type) VALUES (?,null,null,null,null)",
							Statement.RETURN_GENERATED_KEYS);
			while (i < outlinksCount) {

				ps.setString(1, outlinks[i]);
				ps.addBatch();
		
				i++;
			}

			ps.executeBatch();
			rs = ps.getGeneratedKeys();

			outlinksId = new int[i];
			i = 0;
			while (rs.next()) {
				outlinksId[i] = rs.getInt(1);
				i++;
			}

		} catch (NullPointerException |SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return outlinksId;

	}

	public int[] getKeys_oldOutlinks(String[] oldOutlinks, int oldOutlinksCount) {

		PreparedStatement ps = null;
		int[] keys = new int[oldOutlinksCount];
		try {
			String query = "select id from page where url like ?";

			for (int i = 0; i < oldOutlinksCount-1; i++) {
				query = query + " or ?";
			}

			ps = DBConnection.connection().prepareStatement(query);
			for (int i = 0; i < oldOutlinksCount; i++) {
				ps.setString(i + 1, oldOutlinks[i]);
			}

			ResultSet rs = ps.executeQuery();

			int i = 0;
			while (rs.next()) {
				keys[i] = rs.getInt(1);
				i++;
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return keys;
	}
	


}
