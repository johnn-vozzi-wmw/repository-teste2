package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import totalcross.sql.ResultSet;

public class NovoClientePdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NovoCliente();
	}

    private static NovoClientePdbxDao instance;

    public NovoClientePdbxDao() {
        super(NovoCliente.TABLE_NAME);
    }

    public static NovoClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new NovoClientePdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        NovoCliente novocli = new NovoCliente();
        novocli.rowKey = rs.getString("rowkey");
        novocli.cdEmpresa = rs.getString("cdEmpresa");
        novocli.cdRepresentante = rs.getString("cdRepresentante");
        novocli.flOrigemNovoCliente = rs.getString("flOrigemnovocliente");
        novocli.cdNovoCliente = rs.getString("cdNovocliente");
        novocli.nuCnpj = rs.getString("nuCnpj");
        novocli.flTipoPessoa = rs.getString("flTipoPessoa");
        novocli.dtCadastro = rs.getDate("dtCadastro");
        novocli.hrCadastro = rs.getString("hrCadastro");
        novocli.flTipoFrequencia = rs.getString("flTipoFrequencia");
        novocli.flSemanaMes = rs.getString("flSemanaMes");
        novocli.dtBase = rs.getDate("dtBase");
        novocli.nuDiaSemana = rs.getString("nuDiaSemana");
        novocli.hrAgenda = rs.getString("hrAgenda");
        novocli.cdStatusNovoCliente = rs.getString("cdStatusnovocliente");
        novocli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        novocli.cdLatitude = rs.getDouble("cdLatitude");
        novocli.cdLongitude = rs.getDouble("cdLongitude");
        novocli.cdClienteOriginal = rs.getString("cdClienteOriginal");
        novocli.flCadCoordenadaLiberado = rs.getString("flCadCoordenadaLiberado");
        novocli.nmRazaoSocial = rs.getString("nmRazaoSocial");
        novocli.nmFantasia = rs.getString("nmFantasia");
        novocli.flEmAnalise = rs.getString("flEmAnalise");
        novocli.flOculto = rs.getString("flOculto");
        novocli.cdUsuario = rs.getString("cdUsuario");
        novocli.cdUsuarioCriacao = rs.getString("cdUsuarioCriacao");
        novocli.flStatusCadastro = rs.getString("flStatusCadastro");
        novocli.flPrimeiraEtapa = rs.getString("flPrimeiraEtapa");
        return novocli;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMNOVOCLIENTE,");
        sql.append(" CDNOVOCLIENTE,");
        sql.append(" NUCNPJ,");
        sql.append(" FLTIPOPESSOA,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" FLTIPOFREQUENCIA,");
        sql.append(" FLSEMANAMES,");
        sql.append(" DTBASE,");
        sql.append(" NUDIASEMANA,");
        sql.append(" HRAGENDA,");
        sql.append(" CDSTATUSNOVOCLIENTE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDLATITUDE,");
        sql.append(" CDLONGITUDE,");
        sql.append(" CDCLIENTEORIGINAL,");
        sql.append(" FLCADCOORDENADALIBERADO,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NMFANTASIA,");
        sql.append(" FLEMANALISE,");
        sql.append(" FLOCULTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDUSUARIOCRIACAO,");
        sql.append(" FLSTATUSCADASTRO,");
        sql.append(" FLPRIMEIRAETAPA");
    }

    //@Override
    protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
        NovoCliente novocli = (NovoCliente) domain;
        //--
		if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(novocli.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(novocli.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(novocli.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMNOVOCLIENTE")) {
			return Sql.getValue(novocli.flOrigemNovoCliente);
		}
		if (columnName.equalsIgnoreCase("CDNOVOCLIENTE")) {
			return Sql.getValue(novocli.cdNovoCliente);
		}
		if (columnName.equalsIgnoreCase("NUCNPJ")) {
			return Sql.getValue(novocli.nuCnpj);
		}
		if (columnName.equalsIgnoreCase("FLTIPOPESSOA")) {
			return Sql.getValue(novocli.flTipoPessoa);
		}
		if (columnName.equalsIgnoreCase("DTCADASTRO")) {
			return Sql.getValue(novocli.dtCadastro);
		}
		if (columnName.equalsIgnoreCase("HRCADASTRO")) {
			return Sql.getValue(novocli.hrCadastro);
		}
		if (columnName.equalsIgnoreCase("CDSTATUSNOVOCLIENTE")) {
			return Sql.getValue(novocli.cdStatusNovoCliente);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(novocli.flTipoAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDLATITUDE")) {
			return Sql.getValue(novocli.cdLatitude);
		}
		if (columnName.equalsIgnoreCase("CDLONGITUDE")) {
			return Sql.getValue(novocli.cdLongitude);
		}
		if (columnName.equalsIgnoreCase("CDCLIENTEORIGINAL")) {
			return Sql.getValue(novocli.cdClienteOriginal);
		}
		if (columnName.equalsIgnoreCase("FLCADCOORDENADALIBERADO")) {
			return Sql.getValue(novocli.flCadCoordenadaLiberado);
		}
		if (columnName.equalsIgnoreCase("FLEMANALISE")) {
			return Sql.getValue(novocli.flEmAnalise);
		}
		if (columnName.equalsIgnoreCase("FLOCULTO")) {
			return Sql.getValue(novocli.flOculto);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(novocli.cdUsuario);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIOCRIACAO")) {
			return Sql.getValue(novocli.cdUsuarioCriacao);
		}
		if (columnName.equalsIgnoreCase("FLSTATUSCADASTRO")) {
			return Sql.getValue(novocli.flStatusCadastro);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
    }
    
    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        NovoCliente novocli = (NovoCliente) domain;
        addUpdateValuesPerson(novocli, sql);
        sql.append(" NUCNPJ = ").append(Sql.getValue(novocli.nuCnpj)).append(",");
        sql.append(" DTCADASTRO = ").append(Sql.getValue(novocli.dtCadastro)).append(",");
        sql.append(" HRCADASTRO = ").append(Sql.getValue(novocli.hrCadastro)).append(",");
        sql.append(" CDSTATUSNOVOCLIENTE = ").append(Sql.getValue(novocli.cdStatusNovoCliente)).append(",");
        sql.append(" CDLATITUDE = ").append(Sql.getValue(novocli.cdLatitude)).append(",");
        sql.append(" CDLONGITUDE = ").append(Sql.getValue(novocli.cdLongitude)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(novocli.flTipoAlteracao)).append(",");
        sql.append(" FLCADCOORDENADALIBERADO = ").append(Sql.getValue(novocli.flCadCoordenadaLiberado)).append(",");
        sql.append(" FLEMANALISE = ").append(Sql.getValue(novocli.flEmAnalise)).append(",");
        sql.append(" FLOCULTO = ").append(Sql.getValue(novocli.flOculto)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(novocli.cdUsuario)).append(",");
        sql.append(" CDUSUARIOCRIACAO = ").append(Sql.getValue(novocli.cdUsuarioCriacao)).append(",");
        sql.append(" FLSTATUSCADASTRO = ").append(Sql.getValue(novocli.flStatusCadastro)).append(",");
        sql.append(" FLPRIMEIRAETAPA = ").append(Sql.getValue(novocli.flPrimeiraEtapa));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        NovoCliente novocli = (NovoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", novocli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", novocli.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMNOVOCLIENTE = ", novocli.flOrigemNovoCliente);
		sqlWhereClause.addAndCondition("CDNOVOCLIENTE = ", novocli.cdNovoCliente);
		sqlWhereClause.addAndCondition("NUCNPJ = ", novocli.nuCnpj);
		sqlWhereClause.addAndCondition("CDCLIENTEORIGINAL = ", novocli.cdClienteOriginal);
		sqlWhereClause.addAndCondition("DTCADASTRO < ", novocli.dtCadastro);
		if (novocli.filtraNaoOcultos) {
			sqlWhereClause.addAndCondition("FLOCULTO != ", ValueUtil.VALOR_SIM);
		}
		if (novocli.filtraNaoEnviados) {
			sqlWhereClause.addAndCondition("(FLTIPOALTERACAO <> '' AND FLTIPOALTERACAO IS NOT NULL)");
		}
		if (novocli.filtraStatusCadastro) {
			sqlWhereClause.addAndCondition("(FLSTATUSCADASTRO IS NULL OR FLSTATUSCADASTRO = '')");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addSelectGridColumns(StringBuffer sql) {
    	sql.append(" rowkey,");
    	sql.append(" NUCNPJ,");
        sql.append(" FLTIPOPESSOA");
    }
    
    public static String getSqlDadosEnvioServidor() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT * FROM TBLVPNOVOCLIENTE")
    	.append(" WHERE ").append(NovoCliente.NMCAMPOTIPOALTERACAO)
    	.append(" <> ").append(Sql.getValue(NovoCliente.FLTIPOALTERACAO_ORIGINAL))
    	.append(" AND FLSTATUSCADASTRO <> ").append(Sql.getValue(NovoCliente.FLSTATUSCADASTRO_PENDENTE));
    	return sb.toString(); 
    }
    
    public void updateFlStatusCadastro(NovoCliente novoCliente) throws SQLException {
    	StringBuffer sb = getSqlBuffer();
    	sb.append("UPDATE TBLVPNOVOCLIENTE SET FLSTATUSCADASTRO = ")
    	.append(Sql.getValue(novoCliente.flStatusCadastro))
    	.append(" where rowkey = ").append(Sql.getValue(novoCliente.getRowKey()));
    	executeUpdate(sb.toString());
    }

	public String findCdNovoClienteByCnpj(String nuCnpj) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDNOVOCLIENTE FROM ");
		sql.append(tableName).append(" tb");
		sql.append(" WHERE NUCNPJ = ").append(Sql.getValue(nuCnpj));
		return getString(sql.toString());
	}
	
    public void updateCdUfComercial(Cliente cliente) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" UPDATE ");
        sql.append(tableName);
        sql.append(" SET CDUFCOMERCIAL = ").append(Sql.getValue(cliente.dsEstadoComercial));
        sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante));
        sql.append(" AND NUCNPJ = ").append(Sql.getValue(cliente.cdCliente));
        executeUpdate(sql.toString());
    }

}