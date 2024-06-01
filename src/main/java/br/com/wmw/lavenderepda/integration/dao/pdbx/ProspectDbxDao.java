package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import totalcross.sql.ResultSet;

public class ProspectDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Prospect();
	}

    private static ProspectDbxDao instance;

    public ProspectDbxDao() {
        super(Prospect.TABLE_NAME); 
    }
    
    public static ProspectDbxDao getInstance() {
        if (instance == null) {
            instance = new ProspectDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Prospect prospect = new Prospect();
        prospect.rowKey = rs.getString("rowkey");
        prospect.cdEmpresa = rs.getString("cdEmpresa");
        prospect.cdRepresentante = rs.getString("cdRepresentante");
        prospect.flOrigemProspect = rs.getString("flOrigemProspect");
        prospect.cdProspect = rs.getString("cdProspect");
        prospect.nuCnpj = rs.getString("nuCnpj");
        prospect.dtCadastro = rs.getDate("dtCadastro");
        prospect.cdTipoPropesct = rs.getString("cdTipoPropesct");
        prospect.flTipoPessoa = rs.getString("flTipoPessoa");
        prospect.cdLatitude = ValueUtil.round(rs.getDouble("cdLatitude"));
        prospect.cdLongitude = ValueUtil.round(rs.getDouble("cdLongitude"));
        prospect.dsEmail = rs.getString("dsEmail");
        prospect.hrCadastro = rs.getString("hrCadastro");
        prospect.cdUsuarioCriacao = rs.getString("cdUsuarioCriacao");
        prospect.cdUsuario = rs.getString("cdUsuario");
        prospect.nuCarimbo = rs.getInt("nuCarimbo");
        prospect.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return prospect;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPROSPECT,");
        sql.append(" CDPROSPECT,");
        sql.append(" NUCNPJ,");
        sql.append(" DTCADASTRO,");
        sql.append(" CDTIPOPROPESCT,");
        sql.append(" FLTIPOPESSOA,");
        sql.append(" CDLATITUDE,");
        sql.append(" CDLONGITUDE,");
        sql.append(" DSEMAIL,");
        sql.append(" HRCADASTRO,");
        sql.append(" CDUSUARIOCRIACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		Prospect prospect = (Prospect) domain;
		if (prospect.getHashValuesDinamicos().get(columnName.toUpperCase()) != null) {
			return super.getInsertValue(columnName, columnType, columnSize, domain);
		}
		if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(prospect.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(prospect.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(prospect.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMPROSPECT")) {
			return Sql.getValue(prospect.flOrigemProspect);
		}
		if (columnName.equalsIgnoreCase("CDPROSPECT")) {
			return Sql.getValue(prospect.cdProspect);
		}
		if (columnName.equalsIgnoreCase("NUCNPJ")) {
			return Sql.getValue(prospect.nuCnpj);
		}
		if (columnName.equalsIgnoreCase("DTCADASTRO")) {
			return Sql.getValue(prospect.dtCadastro);
		}
		if (columnName.equalsIgnoreCase("CDTIPOPROPESCT")) {
			return Sql.getValue(prospect.cdTipoPropesct);
		}
		if (columnName.equalsIgnoreCase("FLTIPOPESSOA")) {
			return Sql.getValue(prospect.flTipoPessoa);
		}
		if (columnName.equalsIgnoreCase("CDLATITUDE")) {
			return Sql.getValue(prospect.cdLatitude);
		}
		if (columnName.equalsIgnoreCase("CDLONGITUDE")) {
			return Sql.getValue(prospect.cdLongitude);
		}
		if (columnName.equalsIgnoreCase("DSEMAIL")) {
			return Sql.getValue(prospect.dsEmail);
		}
		if (columnName.equalsIgnoreCase("HRCADASTRO")) {
			return Sql.getValue(prospect.hrCadastro);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIOCRIACAO")) {
			return Sql.getValue(prospect.cdUsuarioCriacao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(prospect.cdUsuario);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(prospect.flTipoAlteracao);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}
	

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        Prospect prospect = (Prospect) domain;
        addUpdateValuesPerson(prospect, sql);
        sql.append(" NUCNPJ = ").append(Sql.getValue(prospect.nuCnpj)).append(",");
        sql.append(" DTCADASTRO = ").append(Sql.getValue(prospect.dtCadastro)).append(",");
        sql.append(" CDTIPOPROPESCT = ").append(Sql.getValue(prospect.cdTipoPropesct)).append(",");
        sql.append(" FLTIPOPESSOA = ").append(Sql.getValue(prospect.flTipoPessoa)).append(",");
        sql.append(" CDLATITUDE = ").append(Sql.getValue(prospect.cdLatitude)).append(",");
        sql.append(" CDLONGITUDE = ").append(Sql.getValue(prospect.cdLongitude)).append(",");
        sql.append(" DSEMAIL = ").append(Sql.getValue(prospect.dsEmail)).append(",");
        sql.append(" HRCADASTRO = ").append(Sql.getValue(prospect.hrCadastro)).append(",");
        sql.append(" CDUSUARIOCRIACAO = ").append(Sql.getValue(prospect.cdUsuarioCriacao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(prospect.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(prospect.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(prospect.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Prospect prospect = (Prospect) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", prospect.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", prospect.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPROSPECT = ", prospect.flOrigemProspect);
		sqlWhereClause.addAndCondition("CDPROSPECT = ", prospect.cdProspect);
		sqlWhereClause.addAndCondition("DTCADASTRO <= ", prospect.dtCadastro);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public boolean existeProspectCnpj(String cnpj) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT COUNT(1) FROM TBLVPPROSPECT WHERE")
    	.append(" NUCNPJ = ").append(Sql.getValue(cnpj));
    	return getInt(sql.toString()) > 0;
    }
    
}