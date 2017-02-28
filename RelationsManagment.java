import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class RelationsManagment {
	
	
	public boolean relationExist(int pageId1,int pageId2){

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean flag=false;
		try {

			ps = DBConnection.connection().prepareStatement(
					"select * from relation where id1=? and id2=?");
			ps.setInt(1, pageId1);
			ps.setInt(2, pageId1);
			rs = ps.executeQuery();
			if (rs.next()) {
				flag=true;
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
		return flag;

	}

	public void addRelation(int[][] relations,int relationCount) {

		PreparedStatement ps = null;

		try {
			ps = DBConnection.connection().prepareStatement(
					"INSERT INTO relation(id1,id2) VALUES (?,?)");

			for (int i = 0; i < relationCount; i++) {
				
				ps.setInt(1, relations[i][0]);
				ps.setInt(2, relations[i][1]);
				ps.addBatch();
			}
			
			
			ps.executeBatch();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				ps.close();

			} catch (SQLException |NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
