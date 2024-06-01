package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import totalcross.sql.ResultSet;

public class ClienteEndAtuaDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteEndAtua();
	}

    private static ClienteEndAtuaDbxDao instance;

    public ClienteEndAtuaDbxDao() {
        super(ClienteEndAtua.TABLE_NAME);
    }
    
    public static ClienteEndAtuaDbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteEndAtuaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClienteEndAtua clienteEndAtua = new ClienteEndAtua();
        clienteEndAtua.rowKey = rs.getString("rowkey");
        clienteEndAtua.cdEmpresa = rs.getString("cdEmpresa");
        clienteEndAtua.cdRepresentante = rs.getString("cdRepresentante");
        clienteEndAtua.cdCliente = rs.getString("cdCliente");
        clienteEndAtua.cdEndereco = rs.getString("cdEndereco");
        clienteEndAtua.cdRegistro = rs.getString("cdRegistro");
        clienteEndAtua.flOrigemAtualizacao = rs.getString("flOrigemAtualizacao");
        clienteEndAtua.dsLogradouro = rs.getString("dsLogradouro");
        clienteEndAtua.nuLogradouro = rs.getString("nuLogradouro");
        clienteEndAtua.dsComplemento = rs.getString("dsComplemento");
        clienteEndAtua.dsBairro = rs.getString("dsBairro");
        clienteEndAtua.dsCidade = rs.getString("dsCidade");
        clienteEndAtua.dsEstado = rs.getString("dsEstado");
        clienteEndAtua.dsPais = rs.getString("dsPais");
        clienteEndAtua.dsCep = rs.getString("dsCep");
        clienteEndAtua.dtAtualizacao = rs.getDate("dtAtualizacao");
        clienteEndAtua.flTipoRegistro = rs.getString("flTipoRegistro");
        clienteEndAtua.cdPeriodoEntrega = rs.getString("cdPeriodoEntrega");
        clienteEndAtua.nuCarimbo = rs.getInt("nuCarimbo");
        clienteEndAtua.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteEndAtua.cdUsuarioAlteracao = rs.getString("cdUsuarioAlteracao");
        clienteEndAtua.cdUsuario = rs.getString("cdUsuario");
        return clienteEndAtua;
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
        sql.append(" CDENDERECO,");
        sql.append(" CDREGISTRO,");
        sql.append(" FLORIGEMATUALIZACAO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" NULOGRADOURO,");
        sql.append(" DSCOMPLEMENTO,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSCIDADE,");
        sql.append(" DSESTADO,");
        sql.append(" DSPAIS,");
        sql.append(" DSCEP,");
        sql.append(" DTATUALIZACAO,");
        sql.append(" FLTIPOREGISTRO,");
        sql.append(" CDPERIODOENTREGA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		ClienteEndAtua clienteEndAtua = (ClienteEndAtua) domain;
		if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(clienteEndAtua.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(clienteEndAtua.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(clienteEndAtua.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("CDCLIENTE")) {
			return Sql.getValue(clienteEndAtua.cdCliente);
		}
		if (columnName.equalsIgnoreCase("CDENDERECO")) {
			return Sql.getValue(clienteEndAtua.cdEndereco);
		}
		if (columnName.equalsIgnoreCase("CDREGISTRO")) {
			return Sql.getValue(clienteEndAtua.cdRegistro);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMATUALIZACAO")) {
			return Sql.getValue(clienteEndAtua.flOrigemAtualizacao);
		}
		if (columnName.equalsIgnoreCase("DTATUALIZACAO")) {
			return Sql.getValue(clienteEndAtua.dtAtualizacao);
		}
		if (columnName.equalsIgnoreCase("FLTIPOREGISTRO")) {
			return Sql.getValue(clienteEndAtua.flTipoRegistro);
		}
		if (columnName.equalsIgnoreCase("CDPERIODOENTREGA")) {
			return Sql.getValue(clienteEndAtua.cdPeriodoEntrega);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(clienteEndAtua.flTipoAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIOALTERACAO")) {
			return Sql.getValue(clienteEndAtua.cdUsuarioAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(clienteEndAtua.cdUsuario);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteEndAtua clienteEndAtua = (ClienteEndAtua) domain;
        addUpdateValuesPerson(clienteEndAtua, sql);
        sql.append(" DTATUALIZACAO = ").append(Sql.getValue(clienteEndAtua.dtAtualizacao)).append(",");
        sql.append(" FLTIPOREGISTRO = ").append(Sql.getValue(clienteEndAtua.flTipoRegistro)).append(",");
        sql.append(" CDPERIODOENTREGA = ").append(Sql.getValue(clienteEndAtua.cdPeriodoEntrega)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(clienteEndAtua.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteEndAtua.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIOALTERACAO = ").append(Sql.getValue(clienteEndAtua.cdUsuarioAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteEndAtua.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteEndAtua clienteendatua = (ClienteEndAtua) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteendatua.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteendatua.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", clienteendatua.cdCliente);
		sqlWhereClause.addAndCondition("CDENDERECO = ", clienteendatua.cdEndereco);
		sqlWhereClause.addAndCondition("CDREGISTRO = ", clienteendatua.cdRegistro);
		sqlWhereClause.addAndCondition("FLORIGEMATUALIZACAO = ", clienteendatua.flOrigemAtualizacao);
		sqlWhereClause.addAndCondition("DTATUALIZACAO <= ", clienteendatua.dtAtualizacaoMenorIgualFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}