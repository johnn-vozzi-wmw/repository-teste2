package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import totalcross.sql.ResultSet;

public class NovoCliEnderecoDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NovoCliEndereco();
	}

    private static NovoCliEnderecoDao instance;
    
    public NovoCliEnderecoDao() {
		super(NovoCliEndereco.TABLE_NAME);
	}
    
    public static NovoCliEnderecoDao getInstance() {
        if (instance == null) {
            instance = new NovoCliEnderecoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	NovoCliEndereco novoCliEndereco = new NovoCliEndereco();
        novoCliEndereco.rowKey = rs.getString("rowkey");
        novoCliEndereco.cdEmpresa = rs.getString("cdEmpresa");
        novoCliEndereco.cdRepresentante = rs.getString("cdRepresentante");
        novoCliEndereco.cdNovoCliente = rs.getString("cdNovoCliente");
        novoCliEndereco.flOrigemNovoCliente = rs.getString("flOrigemNovoCliente");
        novoCliEndereco.cdEndereco = rs.getString("cdEndereco");
        novoCliEndereco.dsLogradouro = rs.getString("dsLogradouro");
        novoCliEndereco.nuLogradouro = rs.getString("nuLogradouro");
        novoCliEndereco.dsComplemento = rs.getString("dsComplemento");
        novoCliEndereco.dsBairro = rs.getString("dsBairro");
        novoCliEndereco.dsCep = rs.getString("dsCep");
        novoCliEndereco.dsCidade = rs.getString("dsCidade");
        novoCliEndereco.cdUf = rs.getString("cdUf");
        novoCliEndereco.dsPais = rs.getString("dsPais");
        novoCliEndereco.dsPontoReferencia = rs.getString("dsPontoReferencia");
        novoCliEndereco.dsObservacao = rs.getString("dsObservacao");
        novoCliEndereco.flComercial = rs.getString("flComercial");
        novoCliEndereco.flEntrega = rs.getString("flEntrega");
        novoCliEndereco.flEntregaPadrao = rs.getString("flEntregaPadrao");
        novoCliEndereco.cdPeriodoEntrega = rs.getString("cdPeriodoEntrega");
        novoCliEndereco.nuCarimbo = rs.getInt("nuCarimbo");
        novoCliEndereco.flTipoAlteracao = rs.getString("flTipoAlteracao");
        novoCliEndereco.cdUsuario = rs.getString("cdUsuario");
        return novoCliEndereco;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDNOVOCLIENTE,");
        sql.append(" FLORIGEMNOVOCLIENTE,");
        sql.append(" CDENDERECO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" DSCOMPLEMENTO,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSCEP,");
        sql.append(" DSCIDADE,");
        sql.append(" CDUF,");
        sql.append(" DSPAIS,");
        sql.append(" DSPONTOREFERENCIA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" FLCOMERCIAL,");
        sql.append(" FLENTREGA,");
        sql.append(" CDPERIODOENTREGA,");
        sql.append(" FLENTREGAPADRAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" NULOGRADOURO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}
    
    //@Override
    protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
    	NovoCliEndereco novoCliEndereco = (NovoCliEndereco) domain;
    	if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(novoCliEndereco.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(novoCliEndereco.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(novoCliEndereco.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("CDNOVOCLIENTE")) {
			return Sql.getValue(novoCliEndereco.cdNovoCliente);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMNOVOCLIENTE")) {
			return Sql.getValue(novoCliEndereco.flOrigemNovoCliente);
		}
		if (columnName.equalsIgnoreCase("CDENDERECO")) {
			return Sql.getValue(novoCliEndereco.cdEndereco);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(novoCliEndereco.flTipoAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(novoCliEndereco.cdUsuario);
		}
    	return super.getInsertValue(columnName, columnType, columnSize, domain);
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        NovoCliEndereco novoCliEndereco = (NovoCliEndereco) domain;
        addUpdateValuesPerson(novoCliEndereco, sql);
        sql.append(" CDENDERECO = ").append(Sql.getValue(novoCliEndereco.cdEndereco)).append(",");
        sql.append(" DSLOGRADOURO = ").append(Sql.getValue(novoCliEndereco.dsLogradouro)).append(",");
        sql.append(" DSBAIRRO = ").append(Sql.getValue(novoCliEndereco.dsBairro)).append(",");
        sql.append(" DSCIDADE = ").append(Sql.getValue(novoCliEndereco.dsCidade)).append(",");
        sql.append(" CDUF = ").append(Sql.getValue(novoCliEndereco.cdUf)).append(",");
        sql.append(" CDPERIODOENTREGA = ").append(Sql.getValue(novoCliEndereco.cdPeriodoEntrega)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(novoCliEndereco.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(novoCliEndereco.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(novoCliEndereco.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	NovoCliEndereco novoCliEndereco = (NovoCliEndereco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", novoCliEndereco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", novoCliEndereco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDNOVOCLIENTE = ", novoCliEndereco.cdNovoCliente);
		sqlWhereClause.addAndCondition("FLORIGEMNOVOCLIENTE = ", novoCliEndereco.flOrigemNovoCliente);
		sqlWhereClause.addAndCondition("CDENDERECO = ", novoCliEndereco.cdEndereco);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public static String getSqlDadosEnvioServidor() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT tb.* FROM TBLVPNOVOCLIENDERECO tb ")
    	.append("JOIN TBLVPNOVOCLIENTE cli ON ")
    	.append("tb.CDEMPRESA = cli.CDEMPRESA AND ")
    	.append("tb.CDREPRESENTANTE = cli.CDREPRESENTANTE AND ")
    	.append("tb.CDNOVOCLIENTE = cli.CDNOVOCLIENTE AND ")
    	.append("tb.FLORIGEMNOVOCLIENTE = cli.FLORIGEMNOVOCLIENTE AND ")
    	.append("cli.FLTIPOALTERACAO = ''")
    	.append(" WHERE tb.").append(NovoCliEndereco.NMCAMPOTIPOALTERACAO)
    	.append(" <> ").append(Sql.getValue(NovoCliEndereco.FLTIPOALTERACAO_ORIGINAL));
    	return sb.toString(); 
    }

}