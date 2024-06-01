package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import totalcross.sql.ResultSet;

public class ClienteAtuaDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteAtua();
	}

    private static ClienteAtuaDbxDao instance;

    public ClienteAtuaDbxDao() {
        super(ClienteAtua.TABLE_NAME);
    }

    public static ClienteAtuaDbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteAtuaDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClienteAtua clienteAtua = new ClienteAtua();
        //--
        clienteAtua.rowKey = rs.getString("rowkey");
        clienteAtua.cdEmpresa = rs.getString("cdEmpresa");
        clienteAtua.cdRepresentante = rs.getString("cdRepresentante");
        clienteAtua.cdCliente = rs.getString("cdCliente");
        clienteAtua.cdRegistro = rs.getString("cdRegistro");
        clienteAtua.dtAtualizacao = rs.getDate("dtAtualizacao");
        clienteAtua.flOrigemAtualizacao = rs.getString("flOrigemAtualizacao");
        clienteAtua.nuCnpj = rs.getString("nuCnpj");
        clienteAtua.flTipoPessoa = rs.getString("flTipoPessoa");
        clienteAtua.flAtualizaCadastroWmw = rs.getString("flAtualizaCadastroWmw");
        clienteAtua.nuCarimbo = rs.getInt("nuCarimbo");
        clienteAtua.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteAtua.cdUsuarioAlteracao = rs.getString("cdUsuarioAlteracao");
        clienteAtua.cdUsuario = rs.getString("cdUsuario");
        return clienteAtua;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDREGISTRO,");
        sql.append(" DTATUALIZACAO,");
        sql.append(" FLORIGEMATUALIZACAO,");
        sql.append(" NUCNPJ,");
        sql.append(" FLTIPOPESSOA,");
        sql.append(" FLATUALIZACADASTROWMW,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
        ClienteAtua clienteAtua = (ClienteAtua) domain;
        //--
		if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(clienteAtua.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(clienteAtua.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(clienteAtua.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("CDCLIENTE")) {
			return Sql.getValue(clienteAtua.cdCliente);
		}
		if (columnName.equalsIgnoreCase("CDREGISTRO")) {
			return Sql.getValue(clienteAtua.cdRegistro);
		}
		if (columnName.equalsIgnoreCase("DTATUALIZACAO")) {
			return Sql.getValue(clienteAtua.dtAtualizacao);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMATUALIZACAO")) {
			return Sql.getValue(clienteAtua.flOrigemAtualizacao);
		}
		if (columnName.equalsIgnoreCase("NUCNPJ")) {
			return Sql.getValue(clienteAtua.nuCnpj);
		}
		if (columnName.equalsIgnoreCase("FLTIPOPESSOA")) {
			return Sql.getValue(clienteAtua.flTipoPessoa);
		}
		if (columnName.equalsIgnoreCase("FLATUALIZACADASTROWMW")) {
			return Sql.getValue(clienteAtua.flAtualizaCadastroWmw);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(clienteAtua.flTipoAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIOALTERACAO")) {
			return Sql.getValue(clienteAtua.cdUsuarioAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(clienteAtua.cdUsuario);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        ClienteAtua clienteAtua = (ClienteAtua) domain;
        addUpdateValuesPerson(clienteAtua, sql);
        sql.append(" DTATUALIZACAO = ").append(Sql.getValue(clienteAtua.dtAtualizacao)).append(",");
        sql.append(" NUCNPJ = ").append(Sql.getValue(clienteAtua.nuCnpj)).append(",");
        sql.append(" FLTIPOPESSOA = ").append(Sql.getValue(clienteAtua.flTipoPessoa)).append(",");
        sql.append(" FLATUALIZACADASTROWMW = ").append(Sql.getValue(clienteAtua.flAtualizaCadastroWmw)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(clienteAtua.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteAtua.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIOALTERACAO = ").append(Sql.getValue(clienteAtua.cdUsuarioAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteAtua.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteAtua clienteAtua = (ClienteAtua) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteAtua.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteAtua.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", clienteAtua.cdCliente);
		sqlWhereClause.addAndCondition("CDREGISTRO = ", clienteAtua.cdRegistro);
		sqlWhereClause.addAndCondition("DTATUALIZACAO > ", clienteAtua.dtAtualizacaoLimite);
		sqlWhereClause.addAndCondition("DTATUALIZACAO < ", clienteAtua.dtAtualizacaoMaxima);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    //@Override
    protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
    	ClienteAtua clienteAtua = (ClienteAtua) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", clienteAtua.flTipoAlteracao);
    	sqlWhereClause.addAndCondition("DTATUALIZACAO < ", clienteAtua.dtAtualizacaoMaxima);
    	sql.append(sqlWhereClause.getSql());
    }
    
}