package com.example.dcc.helpers.mysql;

import java.io.Serializable;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class PreparedStatements implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4971147653592730665L;

	public enum SQL_COMMANDS {
		createTable("CREATE TABLE IF NOT EXISTS dcc_settings('name','value');"),
		insertInto("INSERT INTO dcc_settings VALUES(?,?);");

		private String str;
		
		private SQL_COMMANDS(String c){
			this.str = c;
		}
		public String toString(){
			return str;
		}		
	}
	
	public enum SQL_PREPARED_STMT {

		add("INSERT INTO dcc_settings VALUES( ?, ?)"),
		get("SELECT value FROM dcc_settings WHERE name = ?");
		
		private String str;
		
		private SQL_PREPARED_STMT(String str) {
			this.str = str;
		}
		public String toString(){
			return str;
		}
		
	}
	
	private SQLiteDatabase db;
	
	public PreparedStatements(SQLiteDatabase database){
		this.db = database;
		
	}
	public SQLiteStatement getStatement(SQL_PREPARED_STMT stmt){
		return db.compileStatement(stmt.toString());
	}
	
	public void runStatement(SQL_COMMANDS command){
		db.execSQL(command.toString());
	}
}
