package com.wanhive.iot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingException;

import com.wanhive.iot.Constants;
import com.wanhive.iot.bean.Domain;
import com.wanhive.iot.bean.PagedList;
import com.wanhive.iot.provider.DataSourceProvider;

public class DomainDao {

	public static PagedList list(long userUid, long limit, long offset, String order, String orderBy)
			throws SQLException, NamingException {
		if (limit < 0 || limit > Constants.getSettings().getMaxItemsInList()) {
			throw new IllegalArgumentException("Invalid limit");
		}

		if (offset < 0) {
			throw new IllegalArgumentException("Invalid offset");
		}

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select uid, createdon, modifiedon, name, description, type, status, flag, count(*) over() as totalrecords from wh_domain where useruid=?");
		String sqlParam = "name".equalsIgnoreCase(orderBy) ? "name"
				: "createdon".equalsIgnoreCase(orderBy) ? "createdon" : "uid";
		queryBuilder.append(" order by ");
		queryBuilder.append(sqlParam);

		sqlParam = "desc".equalsIgnoreCase(order) ? "desc" : "asc";
		queryBuilder.append(" ");
		queryBuilder.append(sqlParam);

		queryBuilder.append(" limit ");
		queryBuilder.append(limit);
		queryBuilder.append(" offset ");
		queryBuilder.append(offset);

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			ps.setLong(1, userUid);
			List<Domain> list = new ArrayList<>();
			long totalRecords = 0;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Domain domain = new Domain();
					domain.setUid(rs.getLong(1));
					domain.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					domain.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					domain.setName(rs.getString(4));
					domain.setDescription(rs.getString(5));
					domain.setType(rs.getInt(6));
					domain.setStatus(rs.getInt(7));
					domain.setFlag(rs.getInt(8));
					totalRecords = rs.getLong(9);
					list.add(domain);
				}
			}
			PagedList pl = new PagedList();
			pl.setRecordsTotal(totalRecords);
			pl.setRecordsFiltered(list.size());
			pl.setData(list);
			return pl;
		}
	}

	public static PagedList search(long userUid, String keyword, long limit, long offset, String order, String orderBy)
			throws SQLException, NamingException {
		if (limit < 0 || limit > Constants.getSettings().getMaxItemsInList()) {
			throw new IllegalArgumentException("Invalid limit");
		}

		if (offset < 0) {
			throw new IllegalArgumentException("Invalid offset");
		}

		if (keyword == null || keyword.length() < Constants.getSettings().getMinSearchKeywordLength()
				|| keyword.length() > Constants.getSettings().getMaxSearchKeywordLength()) {
			throw new IllegalArgumentException("Invalid keyword");
		}

		keyword = "%" + keyword.toLowerCase() + "%";
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(
				"select uid, createdon, modifiedon, name, description, type, status, flag, count(*) over() as totalrecords from wh_domain where useruid=? and (lower(name) like ? or lower(description) like ?) ");
		String sqlParam = "name".equalsIgnoreCase(orderBy) ? "name"
				: "createdon".equalsIgnoreCase(orderBy) ? "createdon" : "uid";
		queryBuilder.append(" order by ");
		queryBuilder.append(sqlParam);

		sqlParam = "desc".equalsIgnoreCase(order) ? "desc" : "asc";
		queryBuilder.append(" ");
		queryBuilder.append(sqlParam);

		queryBuilder.append(" limit ");
		queryBuilder.append(limit);
		queryBuilder.append(" offset ");
		queryBuilder.append(offset);

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			ps.setLong(1, userUid);
			ps.setString(2, keyword);
			ps.setString(3, keyword);

			List<Domain> list = new ArrayList<>();
			long totalRecords = 0;
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Domain domain = new Domain();
					domain.setUid(rs.getLong(1));
					domain.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					domain.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					domain.setName(rs.getString(4));
					domain.setDescription(rs.getString(5));
					domain.setType(rs.getInt(6));
					domain.setStatus(rs.getInt(7));
					domain.setFlag(rs.getInt(8));
					totalRecords = rs.getLong(9);
					list.add(domain);
				}
			}
			PagedList pl = new PagedList();
			pl.setRecordsTotal(totalRecords);
			pl.setRecordsFiltered(list.size());
			pl.setData(list);
			return pl;
		}
	}

	public static long count(long userUid) throws SQLException, NamingException {
		long count = 0;
		String query = "select count(uid) from wh_domain where useruid=?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, userUid);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					count = rs.getLong(1);
				}
			}
			return count;
		}
	}

	public static Domain info(long userUid, long uid) throws SQLException, NamingException {
		String query = "select uid, createdon, modifiedon, name, description, type, status, flag from wh_domain where uid = ? and userUid=?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, uid);
			ps.setLong(2, userUid);
			Domain domain = null;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					domain = new Domain();
					domain.setUid(rs.getLong(1));
					domain.setCreatedOn(rs.getObject(2, OffsetDateTime.class));
					domain.setModifiedOn(rs.getObject(3, OffsetDateTime.class));
					domain.setName(rs.getString(4));
					domain.setDescription(rs.getString(5));
					domain.setType(rs.getInt(6));
					domain.setStatus(rs.getInt(7));
					domain.setFlag(rs.getInt(8));
				}
			}
			if (domain != null) {
				return domain;
			} else {
				throw new NoSuchElementException("Not found");
			}
		}
	}

	public static long create(long userUid, String name, String description) throws SQLException, NamingException {
		String query = "insert into wh_domain (useruid, name, description) values(?, ?, ?) returning uid";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, userUid);
			ps.setString(2, name);
			ps.setString(3, description);

			long uid = 0;
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					uid = rs.getLong(1);
				}
			}
			return uid;
		}
	}

	public static void update(long userUid, long uid, String name, String description)
			throws SQLException, NamingException {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("update wh_domain set modifiedon= now(),");
		int params = 0;
		if (name != null) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("name= ?");
			params += 1;
		}
		if (description != null) {
			queryBuilder.append(params > 0 ? " , " : " ");
			queryBuilder.append("description= ?");
			params += 1;
		}
		queryBuilder.append(" where uid=? and useruid=?");

		if (params == 0) {
			throw new IllegalArgumentException("Invalid parameters");
		}

		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());) {
			int index = 1;
			if (name != null) {
				ps.setString(index++, name);
			}
			if (description != null) {
				ps.setString(index++, description);
			}

			ps.setLong(index++, uid);
			ps.setLong(index, userUid);
			ps.executeUpdate();
		}
	}

	public static void delete(long userUid, long uid) throws SQLException, NamingException {
		String query = "delete from wh_domain where uid=? and useruid=?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, uid);
			ps.setLong(2, userUid);
			ps.executeUpdate();
		}
	}

	public static void purge(long userUid) throws SQLException, NamingException {
		String query = "delete from wh_domain where useruid=?";
		try (Connection conn = DataSourceProvider.get().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setLong(1, userUid);
			ps.executeUpdate();
		}
	}
}
