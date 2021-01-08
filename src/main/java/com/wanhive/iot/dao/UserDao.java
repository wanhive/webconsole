package com.wanhive.iot.dao;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.NamingException;
import javax.ws.rs.NotFoundException;

import org.apache.commons.validator.routines.EmailValidator;

import com.wanhive.iot.Constants;
import com.wanhive.iot.IotApplication;
import com.wanhive.iot.bean.PagedList;
import com.wanhive.iot.bean.User;
import com.wanhive.iot.provider.DataSourceProvider;

public class UserDao {
	public static PagedList list(long limit, long offset, String order, String orderBy, int type, int status)
			throws SQLException, NamingException {
		if (limit > Constants.getMaxItemsInList()) {
			throw new IllegalArgumentException("Invalid limit");
		}

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select uid, createdon, modifiedon, alias, email, type, status, flag, count(*) over() as totalrecords from wh_user ");

		if (type > -1 || status > -1) { // apply filter
			queryBuilder.append("where ");
			int filters = 0;
			if (type > -1) {
				queryBuilder.append("type=?");
				filters += 1;
			}

			if (status > -1) {
				queryBuilder.append(filters > 0 ? " and " : " ");
				queryBuilder.append("status=?");
			}
		}

		String sqlParam = orderBy.equalsIgnoreCase("email") ? "email"
				: orderBy.equalsIgnoreCase("createdon") ? "createdon" : "uid";
		queryBuilder.append(" order by ");
		queryBuilder.append(sqlParam);

		sqlParam = order.equalsIgnoreCase("desc") ? "desc" : "asc";
		queryBuilder.append(" ");
		queryBuilder.append(sqlParam);

		queryBuilder.append(" limit ");
		queryBuilder.append(limit);
		queryBuilder.append(" offset ");
		queryBuilder.append(offset);

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			int index = 1;
			if (type > -1) {
				ps.setInt(index++, type);
			}
			if (status > -1) {
				ps.setInt(index, status);
			}
			List<User> list = new ArrayList<>();
			long totalRecords = 0;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					User subject = new User();
					subject.setUid(rs.getLong(1));
					subject.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					subject.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					subject.setAlias(rs.getString(4));
					subject.setEmail(rs.getString(5));
					subject.setType(rs.getInt(6));
					subject.setStatus(rs.getInt(7));
					subject.setFlag(rs.getInt(8));
					totalRecords = rs.getLong(9);
					list.add(subject);
				}
			}
			PagedList pl = new PagedList();
			pl.setRecordsTotal(totalRecords);
			pl.setRecordsFiltered(list.size());
			pl.setData(list);
			return pl;
		}
	}

	public static PagedList search(String keyword, long limit, long offset, String order, String orderBy, int type,
			int status) throws SQLException, NamingException {
		if (limit > Constants.getMaxItemsInList()) {
			throw new IllegalArgumentException("Invalid limit");
		}

		if (keyword == null || keyword.length() < Constants.getMinSearchKeywordLength()) {
			throw new IllegalArgumentException("Invalid keyword");
		}

		keyword = "%" + keyword.toLowerCase() + "%";
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select uid, createdon, modifiedon, alias, email, type, status, flag, count(*) over() as totalrecords from wh_user where (lower(alias) like ? or lower(email) like ?) ");
		if (type > -1 || status > -1) { // apply filter
			if (type > -1) {
				queryBuilder.append(" and ");
				queryBuilder.append("type=?");
			}

			if (status > -1) {
				queryBuilder.append(" and ");
				queryBuilder.append("status=?");
			}
		}
		String sqlParam = orderBy.equalsIgnoreCase("email") ? "email"
				: orderBy.equalsIgnoreCase("createdon") ? "createdon" : "uid";
		queryBuilder.append(" order by ");
		queryBuilder.append(sqlParam);

		sqlParam = order.equalsIgnoreCase("desc") ? "desc" : "asc";
		queryBuilder.append(" ");
		queryBuilder.append(sqlParam);

		queryBuilder.append(" limit ");
		queryBuilder.append(limit);
		queryBuilder.append(" offset ");
		queryBuilder.append(offset);

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			int index = 1;
			ps.setString(index++, keyword);
			ps.setString(index++, keyword);
			if (type > -1) {
				ps.setInt(index++, type);
			}
			if (status > -1) {
				ps.setInt(index, status);
			}
			List<User> list = new ArrayList<>();
			long totalRecords = 0;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					User subject = new User();
					subject.setUid(rs.getLong(1));
					subject.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					subject.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					subject.setAlias(rs.getString(4));
					subject.setEmail(rs.getString(5));
					subject.setType(rs.getInt(6));
					subject.setStatus(rs.getInt(7));
					subject.setFlag(rs.getInt(8));
					totalRecords = rs.getLong(9);
					list.add(subject);
				}
			}
			PagedList pl = new PagedList();
			pl.setRecordsTotal(totalRecords);
			pl.setRecordsFiltered(list.size());
			pl.setData(list);
			return pl;
		}
	}

	public static long count() throws SQLException, NamingException {
		long count = 0;
		String query = "select count(uid) from wh_user";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					count = rs.getLong(1);
				}
			}
			return count;
		}
	}

	public static User info(long uid) throws SQLException, NamingException {
		String query = "select uid, createdon, modifiedon, alias, email, type, status, flag from wh_user where uid = ?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, uid);
			User subject = null;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					subject = new User();
					subject.setUid(rs.getLong(1));
					subject.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					subject.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					subject.setAlias(rs.getString(4));
					subject.setEmail(rs.getString(5));
					subject.setType(rs.getInt(6));
					subject.setStatus(rs.getInt(7));
					subject.setFlag(rs.getInt(8));
				}
			}

			if (subject != null) {
				return subject;
			} else {
				throw new NotFoundException("Not found");
			}
		}
	}

	public static long create(String alias, String email) throws SQLException, NamingException {

		if (alias == null || alias.length() == 0) {
			throw new IllegalArgumentException("Invalid alias");
		}

		if (!EmailValidator.getInstance().isValid(email)) {
			throw new IllegalArgumentException("Invalid email");
		}

		String query = "insert into wh_user (alias, email, token, tokentimestamp) values(?, ?, MD5(random()::text), now()) returning uid, token";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, alias);
			ps.setString(2, email);

			long uid = 0;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					uid = rs.getLong(1);
					IotApplication.sendEmail(email, "Wanhive IoT platform: New account",
							"Your security token: " + rs.getString(2));
				}
			}
			return uid;
		}
	}

	public static void update(long uid, String alias, String password, int type, int status, int flag)
			throws SQLException, NamingException {
		StringBuilder queryBuilder = new StringBuilder();
		int params = 0;

		queryBuilder.append("update wh_user set modifiedon= now(),");
		if (alias != null && alias.length() > 0) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("alias=?");
			params += 1;
		}

		if (password != null && password.length() > 0) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("password=crypt(? , gen_salt('bf'))");
			params += 1;
		}

		if (type > -1) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("type=?");
			params += 1;
		}

		if (status > -1) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("status=?");
			params += 1;
		}

		if (flag > -1) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("flag=?");
			params += 1;
		}

		queryBuilder.append(" where uid=?");

		if (params == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			int index = 1;
			if (alias != null && alias.length() > 0) {
				ps.setString(index++, alias);
			}

			if (password != null && password.length() > 0) {
				ps.setString(index++, password);
			}

			if (type > -1) {
				ps.setInt(index++, type);
			}

			if (status > -1) {
				ps.setInt(index++, status);
			}

			if (flag > -1) {
				ps.setInt(index++, flag);
			}

			ps.setLong(index, uid);
			ps.executeUpdate();
		}
	}

	public static void activate(String context, String challenge, String secret) throws SQLException, NamingException {
		if (context == null || context.length() == 0 || challenge == null || challenge.length() == 0 || secret == null
				|| secret.length() == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		String query = "update wh_user set modifiedon= now(), password=crypt(? , gen_salt('bf')), status= 1 where email=? and token=? and (status=0 or status=1) and now() < tokentimestamp + interval '900 seconds'";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, secret);
			ps.setString(2, context);
			ps.setString(3, challenge);
			if (ps.executeUpdate() > 0) {
				IotApplication.sendEmail(context, "Wanhive IoT platform: Account activated",
						"Your account has been activated.");
			} else {
				throw new NotFoundException("Not found");
			}
		}
	}

	/*
	 * Generate a challenge for the given user.
	 */
	public static void generateChallenge(String email) throws SQLException, NamingException {
		if (email == null || email.length() == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		String query = "update wh_user set token=MD5(random()::text), tokentimestamp=now() where email=? returning token";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					IotApplication.sendEmail(email, "Wanhive IoT: Reset password",
							"Your security token (valid for 15 minutes): " + rs.getString(1));
				}
			}
		}
	}

	public static void changePassword(long userUid, String oldPassword, String password)
			throws SQLException, NamingException {
		if (oldPassword == null || oldPassword.length() == 0 || password == null || password.length() == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		String query = "update wh_user set modifiedon= now(), password=crypt(? , gen_salt('bf')) where uid=? and password = crypt(?, password)";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, password);
			ps.setLong(2, userUid);
			ps.setString(3, oldPassword);
			if (ps.executeUpdate() > 0) {
				return;
			} else {
				throw new NotFoundException("Not found");
			}
		}
	}

	/*
	 * Generates a new token string for the given user UID
	 */
	public static String generateToken(long uid) throws SQLException, NamingException {
		String query = "insert into wh_user_token (uid, token) values (?, ?) on conflict(uid) do update set token = ?, modifiedon=now()";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			Random random = new SecureRandom();
			String token = new BigInteger(256, random).toString(16);
			ps.setLong(1, uid);
			ps.setString(2, token);
			ps.setString(3, token);
			ps.executeUpdate();
			return token;
		}
	}

	/*
	 * Deletes existing token associated with the given user UID
	 */
	public static void removeToken(long uid) throws SQLException, NamingException {
		String query = "delete from wh_user_token where uid=?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, uid);
			ps.executeUpdate();
		}
	}

	/*
	 * Deletes all the existing tokens
	 */
	public static void purgeTokens() throws SQLException, NamingException {
		String query = "truncate wh_user_token";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.executeUpdate();
		}
	}

	public static User verifyUser(String email, String password) throws SQLException, NamingException {
		if (email == null || email.length() == 0 || password == null || password.length() == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		// Status: not activated (0), active(1), deactivated(2)
		String query = "select uid, createdon, modifiedon, alias, email, type, status, flag from wh_user where email=? and status=1 and password = crypt(?, password)";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, email);
			ps.setString(2, password);

			User user = null;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					user = new User();
					user.setUid(rs.getLong(1));
					user.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					user.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					user.setAlias(rs.getString(4));
					user.setEmail(rs.getString(5));
					user.setType(rs.getInt(6));
					user.setStatus(rs.getInt(7));
					user.setFlag(rs.getInt(8));
				}
			}
			if (user != null) {
				user.setToken(generateToken(user.getUid()));
				return user;
			} else {
				throw new NotFoundException("Not found");
			}
		}
	}

	public static User verifyToken(String token) throws SQLException, NamingException {
		if (token == null || token.length() == 0) {
			throw new IllegalArgumentException("Invalid token");
		}

		String query = "update wh_user_token set usedon=now() from wh_user where wh_user_token.token = ? and wh_user.uid=wh_user_token.uid and wh_user.status=1 and now() > wh_user_token.usedon + interval '50 milliseconds' and now() < wh_user_token.modifiedon + interval '1 hour' returning wh_user.uid, wh_user.type";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, token);

			User user = null;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					user = new User();
					user.setUid(rs.getLong(1));
					user.setType(rs.getInt(2));
				}
			}
			if (user != null) {
				return user;
			} else {
				throw new NotFoundException("Not found");
			}
		}
	}
}
