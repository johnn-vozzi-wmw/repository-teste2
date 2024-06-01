package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudPersonDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public abstract class LavendereCrudPersonDbxDao extends CrudPersonDbxDao {

    public LavendereCrudPersonDbxDao(String newTableName) {
        super(newTableName);
    }

    public String findColumnValue(String tableName, String columnName, String columnKeyName, String columKeyValue, String cdEmpresa, String cdRepresentante) throws SQLException {
    	StringBuilder sql = getSqlBase(tableName, columnName, columnKeyName, columKeyValue);
    	//--
		if (ValueUtil.isNotEmpty(cdEmpresa)) {
			sql.append(" and (cdEmpresa = ").append(Sql.getValue(cdEmpresa));
			sql.append(" or cdEmpresa = '' or cdEmpresa is null) ");
			if (ValueUtil.isNotEmpty(cdRepresentante)) {
				sql.append(" and cdRepresentante = ").append(Sql.getValue(cdRepresentante));
			}
		}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			return rs.getString(columnName);
    		}
    		return columKeyValue;
    	}
    }

    //@Override
    public Vector findAllPerson(String tableName, String[] columns, String cdEmpresa, String cdRepresentante) throws SQLException {
    	StringBuilder sql = getSqlBuilder();
    	sql.append(" select ");
    	for (int i = 0; i < columns.length; i++) {
    		sql.append(columns[i]);
    		if ((i + 1) != columns.length) {
        		sql.append(", ");
    		}
    	}
    	sql.append(" from ");
    	sql.append(tableName);
    	//--
		if (ValueUtil.isNotEmpty(cdEmpresa)) {
			sql.append(" where (cdEmpresa = ").append(Sql.getValue(cdEmpresa));
			sql.append(" or cdEmpresa = '' or cdEmpresa is null)");
			if (ValueUtil.isNotEmpty(cdRepresentante)) {
				sql.append(" and cdRepresentante = ").append(Sql.getValue(cdRepresentante));
			}
		}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				String[] obj = new String[columns.length];
		    	for (int i = 0; i < columns.length; i++) {
		    		obj[i] = rs.getString(columns[i]);
		    	}
				result.addElement(obj);
			}
			return result;
		}
    }
    
    public Vector findAllPerson(String tableName, String[] columns, String cdEmpresa, String cdRepresentante, String filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	for (int i = 0; i < columns.length; i++) {
    		sql.append(columns[i]);
    		if ((i + 1) != columns.length) {
    			sql.append(", ");
    		}
    	}
    	sql.append(" from ");
    	sql.append(tableName);
    	addWhereByExample(columns, cdEmpresa, cdRepresentante, filter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector result = new Vector(50);
    		while (rs.next()) {
    			String[] obj = new String[columns.length];
    			for (int i = 0; i < columns.length; i++) {
    				obj[i] = rs.getString(columns[i]);
    			}
    			result.addElement(obj);
    		}
    		return result;
    	}
    }

	private void addWhereByExample(String[] columns, String cdEmpresa, String cdRepresentante, String filter, StringBuffer sql) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndOrCondition("CDEMPRESA = ", cdEmpresa, "''");
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cdRepresentante);
    	sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition(columns[0], filter, true);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(columns[1], filter, true);
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		sql.append(sqlWhereClause.getSql());
	}
	
	public Vector findAllComboRelacionadaFilter(String nmEntidadeDependente, String nmEntidadeRelacionada, String dsColunaRelacionada, String dsValorRelacionado, String[] columns, String cdEmpresa, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	for (int i = 0; i < columns.length; i++) {
    		sql.append(columns[i]);
    		if ((i + 1) != columns.length) {
        		sql.append(", ");
    		}
    	}
    	sql.append(" from ");
    	sql.append(nmEntidadeRelacionada);
    	//--
		if (ValueUtil.isNotEmpty(cdEmpresa)) {
			sql.append(" where (cdEmpresa = ").append(Sql.getValue(cdEmpresa));
			sql.append(" or cdEmpresa = '' or cdEmpresa is null)");
			if (ValueUtil.isNotEmpty(cdRepresentante)) {
				sql.append(" and cdRepresentante = ").append(Sql.getValue(cdRepresentante));
			}
			if (isColumnVarchar(nmEntidadeDependente, dsColunaRelacionada)) {
	    		sql.append(" and ").append(dsColunaRelacionada).append(" = '").append(dsValorRelacionado).append("'");
	    	} else {
	    		sql.append(" and ").append(dsColunaRelacionada).append(" = ").append(dsValorRelacionado);
	    	}
		}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				String[] obj = new String[columns.length];
		    	for (int i = 0; i < columns.length; i++) {
		    		obj[i] = rs.getString(columns[i]);
		    	}
				result.addElement(obj);
			}
			return result;
		}
    }

}