package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

public class RedeCliente extends Cliente {

	public static final String TABLE_NAME = "TBLVPREDECLIENTE";
	
	public RedeCliente() {
		this(TABLE_NAME);
	}

	public RedeCliente(String tabelName) {
		super(tabelName);
	}
	
	@Override
	public String getFlStatusCliente() throws SQLException {
        return this.flStatusCliente;
	}
	
	@Override
    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdCliente);
        return strBuffer.toString();
    }
	
}