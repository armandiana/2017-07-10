package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.smartcardio.ATR;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ObjectAndCount;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects( ) {
		
		String sql = "SELECT DISTINCT o.object_id, o.classification, o.continent, "
				+ "o.country, o.curator_approved, o.dated,  o.department, o.MEDIUM, o.nationality, "
				+ "o.object_name, o.restricted, o.rights_type, o.role, o.room,\n" + 
				"o.style, o.title \n" + 
				"FROM exhibition_objects eo, objects o\n" + 
				"WHERE eo.object_id=o.object_id";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("o.object_id"), res.getString("o.classification"), res.getString("o.continent"), 
						res.getString("o.country"), res.getInt("o.curator_approved"), res.getString("o.dated"), res.getString("o.department"), 
						res.getString("o.medium"), res.getString("o.nationality"), res.getString("o.object_name"), res.getInt("o.restricted"), 
						res.getString("o.rights_type"), res.getString("o.role"), res.getString("o.room"), res.getString("o.style"), res.getString("o.title"));
				//oggettiIdMap.put(artObj.getId(), artObj);
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ObjectAndCount>getConnessione(ArtObject ao, Map<Integer, ArtObject> oggettiIdMap){
		String sql=" SELECT eo2.object_id AS id, count(eo2.exhibition_id) AS cnt " + 
				"FROM exhibition_objects eo1, exhibition_objects eo2 " + 
				"WHERE eo1.exhibition_id=eo2.exhibition_id " + 
				"AND eo1.object_id= ? " + 
				"AND eo2.object_id>eo1.object_id  " + 
				"GROUP BY eo2.object_id";
		
		List<ObjectAndCount>result= new ArrayList<ObjectAndCount>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, ao.getId());
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				//ArtObject a= oggettiIdMap.get(res.getInt("id"));
				
				ObjectAndCount oc= new ObjectAndCount(res.getInt("id"), res.getInt("cnt"));
				result.add(oc);
			}
			conn.close();
			return result;
				
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
