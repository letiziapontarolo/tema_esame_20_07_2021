package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Arco;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	public List<Integer> listaAnni() {
		
		String sql = "SELECT YEAR(reviews.review_date) AS anno "
				+ "FROM reviews "
				+ "GROUP BY YEAR(reviews.review_date)";
		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			
				result.add(res.getInt("anno"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void creaVertici(Map<String, User> userIdMap, int numRecensioni) {
		
		String sql = "SELECT u.*, COUNT(DISTINCT r.review_id) AS recensioni "
				+ "FROM reviews r, users u "
				+ "WHERE r.user_id = u.user_id "
				+ "GROUP BY u.user_id "
				+ "HAVING recensioni >= ?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, numRecensioni);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			
				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				userIdMap.put(res.getString("user_id"), user);
				
				
			}
			res.close();
			st.close();
			conn.close();
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		}
		
	}
	
	public List<Arco> listaArchi(Map<String, User> userIdMap, int numRecensioni, int anno) {
		
		String sql = "SELECT u1.user_id AS u1, u2.user_id AS u2, COUNT(DISTINCT r1.review_id) AS grado "
				+ "FROM "
				+ "(SELECT u.*, COUNT(DISTINCT r.review_id) AS recensioni "
				+ "FROM reviews r, users u "
				+ "WHERE r.user_id = u.user_id "
				+ "GROUP BY u.user_id "
				+ "HAVING recensioni >= ?) u1, "
				+ "(SELECT u.*, COUNT(DISTINCT r.review_id) AS recensioni "
				+ "FROM reviews r, users u "
				+ "WHERE r.user_id = u.user_id "
				+ "GROUP BY u.user_id "
				+ "HAVING recensioni >= ?) u2, "
				+ "reviews r1, reviews r2 "
				+ "WHERE r1.user_id = u1.user_id "
				+ "AND r2.user_id = u2.user_id "
				+ "AND r1.business_id = r2.business_id "
				+ "AND year(r1.review_date) = YEAR(r2.review_date) "
				+ "AND year(r1.review_date) = ? "
				+ "AND u1.user_id > u2.user_id "
				+ "GROUP BY u1.user_id, u2.user_id";
		
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, numRecensioni);
			st.setInt(2, numRecensioni);
			st.setInt(3, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				User u1 = userIdMap.get(res.getString("u1"));
				User u2 = userIdMap.get(res.getString("u2"));
				int grado = res.getInt("grado");
				Arco a = new Arco(u1, u2, grado);
			
				result.add(a);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
