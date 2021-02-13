package com.wanhive.iot.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingException;

import com.nimbusds.srp6.BigIntegerUtils;
import com.nimbusds.srp6.SRP6CryptoParams;
import com.wanhive.iot.Constants;
import com.wanhive.iot.bean.PagedList;
import com.wanhive.iot.bean.Thing;
import com.wanhive.iot.provider.DataSourceProvider;
import com.wanhive.iot.util.Agreement;

public class ThingDao {
	public static PagedList list(long userUid, long domainUid, long limit, long offset, String order, String orderBy)
			throws SQLException, NamingException {
		if (limit < 0 || limit > Constants.getMaxItemsInList()) {
			throw new IllegalArgumentException("Invalid limit");
		}

		if (offset < 0) {
			throw new IllegalArgumentException("Invalid offset");
		}

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select wh_thing.uid, wh_thing.createdon, wh_thing.modifiedon, wh_thing.name, wh_thing.type, wh_thing.status, wh_thing.flag, count(*) over() as totalrecords "
						+ "from wh_thing, wh_domain where wh_thing.domainuid=? and wh_domain.uid = wh_thing.domainuid and wh_domain.useruid= ?");
		String sqlParam = orderBy.equalsIgnoreCase("name") ? "wh_thing.name"
				: orderBy.equalsIgnoreCase("uid") ? "wh_thing.uid" : "wh_thing.createdon";
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
			ps.setLong(1, domainUid);
			ps.setLong(2, userUid);

			List<Thing> list = new ArrayList<>();
			long totalRecords = 0;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Thing thing = new Thing();
					thing.setUid(rs.getLong(1));
					thing.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					thing.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					thing.setName(rs.getString(4));
					thing.setType(rs.getInt(5));
					thing.setStatus(rs.getInt(6));
					thing.setFlag(rs.getInt(7));
					totalRecords = rs.getLong(8);
					list.add(thing);
				}
			}
			PagedList pl = new PagedList();
			pl.setRecordsTotal(totalRecords);
			pl.setRecordsFiltered(list.size());
			pl.setData(list);
			return pl;
		}
	}

	public static PagedList search(long userUid, long domainUid, String keyword, long limit, long offset, String order,
			String orderBy) throws SQLException, NamingException {
		if (limit < 0 || limit > Constants.getMaxItemsInList()) {
			throw new IllegalArgumentException("Invalid limit");
		}

		if (offset < 0) {
			throw new IllegalArgumentException("Invalid offset");
		}

		if (keyword == null || keyword.length() < Constants.getMinSearchKeywordLength()
				|| keyword.length() > Constants.getMaxSearchKeywordLength()) {
			throw new IllegalArgumentException("Invalid keyword");
		}

		keyword = "%" + keyword.toLowerCase() + "%";
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select wh_thing.uid, wh_thing.createdon, wh_thing.modifiedon, wh_thing.name, wh_thing.type, wh_thing.status, wh_thing.flag, count(*) over() as totalrecords "
						+ "from wh_thing, wh_domain where wh_thing.domainuid=? and wh_domain.uid = wh_thing.domainuid and "
						+ "wh_domain.useruid= ? and lower(wh_thing.name) like ? ");
		String sqlParam = orderBy.equalsIgnoreCase("name") ? "wh_thing.name"
				: orderBy.equalsIgnoreCase("uid") ? "wh_thing.uid" : "wh_thing.createdon";
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
			ps.setLong(1, domainUid);
			ps.setLong(2, userUid);
			ps.setString(3, keyword);

			List<Thing> list = new ArrayList<>();
			long totalRecords = 0;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Thing thing = new Thing();
					thing.setUid(rs.getLong(1));
					thing.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					thing.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					thing.setName(rs.getString(4));
					thing.setType(rs.getInt(5));
					thing.setStatus(rs.getInt(6));
					thing.setFlag(rs.getInt(7));
					totalRecords = rs.getLong(8);
					list.add(thing);
				}
			}
			PagedList pl = new PagedList();
			pl.setRecordsTotal(totalRecords);
			pl.setRecordsFiltered(list.size());
			pl.setData(list);
			return pl;
		}
	}

	public static long count(long userUid, long domainUid) throws SQLException, NamingException {
		String query = "select count(wh_thing.uid) from wh_thing, wh_domain where wh_thing.domainuid=? and wh_domain.uid = wh_thing.domainuid and wh_domain.useruid= ?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, domainUid);
			ps.setLong(2, userUid);

			long count = 0;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					count = rs.getLong(1);
				}
			}
			return count;
		}
	}

	public static long count(long userUid) throws SQLException, NamingException {
		String query = "select count(wh_thing.uid) from wh_thing where wh_thing.domainuid in (select wh_domain.uid from wh_domain where wh_domain.useruid=?)";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, userUid);

			long count = 0;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					count = rs.getLong(1);
				}
			}
			return count;
		}
	}

	public static Thing info(long userUid, long uid) throws SQLException, NamingException {
		String query = "select wh_thing.uid, wh_thing.createdon, wh_thing.modifiedon, wh_thing.name, wh_thing.type, wh_thing.status, wh_thing.flag "
				+ "from wh_thing, wh_domain where wh_thing.uid = ? and wh_domain.uid = wh_thing.domainuid and wh_domain.useruid= ?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, uid);
			ps.setLong(2, userUid);

			Thing thing = null;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					thing = new Thing();
					thing.setUid(rs.getLong(1));
					thing.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					thing.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					thing.setName(rs.getString(4));
					thing.setType(rs.getInt(5));
					thing.setStatus(rs.getInt(6));
					thing.setFlag(rs.getInt(7));
				}
			}
			if (thing != null) {
				return thing;
			} else {
				throw new NoSuchElementException("Not found");
			}
		}
	}

	public static long create(long userUid, long domainUid, String name, int type)
			throws SQLException, NamingException {
		if (Constants.getMaxThingsPerDomain() <= 0) {
			throw new IllegalStateException("Invalid application settings");
		}

		if (type < 0) {
			type = 0;
		}

		String query = "insert into wh_thing (uid, name, type, domainuid) values "
				+ "((select min(series) from (select series, wh_thing.uid from generate_series(?, ?, ?) series left join wh_thing on series = wh_thing.uid  and wh_thing.domainuid=? where uid is null) as x"
				+ " where exists(select uid from wh_domain where uid = ? and useruid=?))" + ", ?, ? ,?) returning uid";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, domainUid);
			ps.setLong(2, (domainUid + Constants.getMaxThingsPerDomain() - 1));
			ps.setLong(3, 1);
			ps.setLong(4, domainUid);
			ps.setLong(5, domainUid);
			ps.setLong(6, userUid);
			ps.setString(7, name);
			ps.setInt(8, type);
			ps.setLong(9, domainUid);

			long uid = 0;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					uid = rs.getLong(1);
				}
			}
			return uid;
		}
	}

	public static void update(long userUid, long uid, String name, int type, String salt, String verifier)
			throws SQLException, NamingException {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("update wh_thing set modifiedon= now(),");
		int params = 0;
		if (name != null) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("name= ?");
			params += 1;
		}

		if (type > -1) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("type= ?");
			params += 1;
		}

		if (salt != null && salt.length() > 0) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("salt= ?");
			params += 1;
		}
		if (verifier != null && verifier.length() > 0) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("verifier= ?");
			params += 1;
		}
		queryBuilder.append(
				" from wh_domain where wh_domain.uid = wh_thing.domainuid and wh_domain.useruid=? and wh_thing.uid = ?");

		if (params == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			int index = 1;
			if (name != null) {
				ps.setString(index++, name);
			}
			if (type > -1) {
				ps.setInt(index++, type);
			}
			if (salt != null && salt.length() > 0) {
				ps.setString(index++, salt);
			}
			if (verifier != null && verifier.length() > 0) {
				ps.setString(index++, verifier);
			}
			ps.setLong(index++, userUid);
			ps.setLong(index, uid);
			ps.executeUpdate();

		}
	}

	public static void updateVerifier(long userUid, String uid, int rounds, String password)
			throws SQLException, NamingException {
		if (password == null || rounds < 0 || rounds > Constants.getMaxPasswordHashRounds()) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		String query = "update wh_thing set modifiedon= now(), salt= ?, verifier= ? from wh_domain where wh_domain.uid = wh_thing.domainuid and wh_domain.useruid=? and wh_thing.uid = ?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			Agreement agreement = new Agreement(SRP6CryptoParams.getInstance(2048, "SHA-512"));
			byte[] salt = agreement.generateRandomSalt(16);
			BigInteger v = agreement
					.computeVerifier(agreement.computeX(rounds, uid.getBytes(), salt, password.getBytes()));
			String vHex = BigIntegerUtils.toHex(v);
			String saltHex = BigIntegerUtils.toHex(BigIntegerUtils.bigIntegerFromBytes(salt));

			ps.setString(1, saltHex);
			ps.setString(2, vHex);
			ps.setLong(3, userUid);
			ps.setLong(4, Long.parseLong(uid));

			ps.executeUpdate();
		}
	}

	public static void delete(long userUid, long uid) throws SQLException, NamingException {
		String query = "delete from wh_thing where uid = ? and "
				+ "exists(select wh_thing.uid from wh_thing, wh_domain where "
				+ "wh_thing.uid=? and wh_domain.uid=wh_thing.domainuid and wh_domain.useruid=?)";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, uid);
			ps.setLong(2, uid);
			ps.setLong(3, userUid);
			ps.executeUpdate();
		}
	}

	public static void purge(long userUid, long domainUid) throws SQLException, NamingException {
		String query = "delete from wh_thing where domainuid = ? and exists(select wh_domain.uid from wh_domain where wh_domain.uid=? and wh_domain.useruid=?)";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, domainUid);
			ps.setLong(2, domainUid);
			ps.setLong(3, userUid);
			ps.executeUpdate();
		}
	}

	public static void purge(long userUid) throws SQLException, NamingException {
		String query = "delete from wh_thing where domainuid in (select wh_domain.uid from wh_domain where wh_domain.useruid=?)";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, userUid);
			ps.executeUpdate();
		}
	}
}
