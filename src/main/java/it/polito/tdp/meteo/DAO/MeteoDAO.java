package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;
import it.polito.tdp.meteo.model.Citta;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			st.close();
			rs.close();
			
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta citta) {
		String sql = "";
		if(mese<10)
			sql = "SELECT Localita, Data, Umidita "
				+ "FROM Situazione "
				+ "WHERE DATA BETWEEN 20130?01 AND 20130?31 AND Localita = ?";
		else 
			sql = "SELECT Localita, Data, Umidita "
				+ "FROM Situazione "
				+ "WHERE Data BETWEEN 2013?01 AND 2013?31 AND Localita = ?";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, mese);
			st.setInt(2, mese);
			st.setString(3, citta.getNome());
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
//			Citta citta = new Citta(rs.getString("Localita"), rilevamenti);
			

			conn.close();
			st.close();
			rs.close();
			
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Citta> getAllCitta(){
		
		final String sql = "SELECT DISTINCT localita "
						+ "FROM situazione "
						+ "ORDER BY localita";
		
		List<Citta> result = new ArrayList<Citta>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				Citta c = new Citta(rs.getString("localita"));
				result.add(c);
			}
			
			conn.close();
			st.close();
			rs.close();
			
			return result;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public String getUmiditaMediaMese(int mese, String localita){
		String sql = "";
		if(mese<10)
			sql = "SELECT localita, AVG(umidita) AS Media "
						+"FROM situazione "
						+"WHERE data between 20130?01 and 20130?31 and localita = ?";
		else
			sql = "SELECT localita, AVG(umidita) AS Media "
					+"FROM situazione "
					+"WHERE data between 2013?01 and 2013?31 and localita = ?";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, mese);
			st.setInt(2, mese);
			st.setString(3, localita);
			ResultSet rs = st.executeQuery();

			Rilevamento r = null;
			while (rs.next()) {

				r = new Rilevamento(rs.getString("localita"), rs.getDouble("Media"));
			}

			conn.close();
			st.close();
			rs.close();
			
			return r.getMediaU()+"";

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
