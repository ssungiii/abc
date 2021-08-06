package com.kosa.myapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UploadFileRepository implements IUploadFileRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int getMaxFileId() {
		String sql = "SELECT NVL(MAX(file_id), 0) FROM upload_file";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public void uploadFile(UploadFileVO file) {
		String sql = "INSERT INTO upload_file (file_id, directory_name, "
				   + "file_name, file_size, file_content_type, "
				   + "file_upload_date, file_data) "
				   + "VALUES (?, ?, ?, ?, ?, SYSTIMESTAMP, ?)";
		jdbcTemplate.update(sql, file.getFileId(),
								 file.getDirectoryName(),
								 file.getFileName(),
								 file.getFileSize(),
								 file.getFileContentType(),
								 file.getFileData());
	}

	@Override
	public List<UploadFileVO> getAllFileList() {
		String sql = " SELECT "
				   + " file_id AS fileId,"
				   + " directory_name AS directoryName, "
				   + " file_name AS fileName, "
				   + " file_size AS fileSize, "
				   + " file_content_type AS fileContentType, "
				   + " file_upload_date AS fileUploadDate "
				   + " FROM upload_file "
				   + " ORDER BY fileUploadDate DESC";
		return jdbcTemplate.query(sql, new RowMapper<UploadFileVO>() {
			@Override
			public UploadFileVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadFileVO file = new UploadFileVO();
				file.setFileId(rs.getInt("fileId"));
				file.setDirectoryName(rs.getString("directoryName"));
				file.setFileName(rs.getString("fileName"));
				file.setFileSize(rs.getLong("fileSize"));
				file.setFileContentType(rs.getString("fileContentType"));
				file.setFileUploadDate(rs.getTimestamp("fileUploadDate"));
				return file;
		}
			
	});
}
		
	@Override
	public UploadFileVO getFile(int fileId) {
		String sql = " SELECT "
				   + " file_id AS fileId, "
				   + " directory_name AS directoryName, "
				   + " file_name AS fileName, "
				   + " file_size AS fileSize, "
				   + " file_content_type AS fileContentType, "
				   + " file_data AS fileData "
				   + " FROM upload_file WHERE file_id=?";
		return jdbcTemplate.queryForObject(sql, new RowMapper<UploadFileVO>() {
			@Override
			public UploadFileVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UploadFileVO file = new UploadFileVO();
				file.setFileId(rs.getInt("fileId"));
				file.setDirectoryName(rs.getString("directoryName"));
				file.setFileName(rs.getString("fileName"));
				file.setFileSize(rs.getLong("fileSize"));
				file.setFileContentType(rs.getString("fileContentType"));
				file.setFileData(rs.getBytes("fileData"));
				return file;
			}
	}, fileId);
}
	@Override
	public void updateFile(UploadFileVO file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFile(int fileId) {
		String sql = "DELETE upload_file WHERE file_id=?";
		jdbcTemplate.update(sql, fileId);

	}

}
