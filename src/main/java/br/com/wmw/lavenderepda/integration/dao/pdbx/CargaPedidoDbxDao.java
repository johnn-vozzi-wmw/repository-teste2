package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import totalcross.sql.ResultSet;

public class CargaPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CargaPedido();
	}

    private static CargaPedidoDbxDao instance;
	

    public CargaPedidoDbxDao() {
        super(CargaPedido.TABLE_NAME);
    }

    public CargaPedidoDbxDao(String newTableName) {
    	super(newTableName);
    }
    
    public static CargaPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new CargaPedidoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CargaPedido cargaPedido = new CargaPedido();
        cargaPedido.rowKey = rs.getString("rowkey");
        cargaPedido.cdEmpresa = rs.getString("cdEmpresa");
        cargaPedido.cdRepresentante = rs.getString("cdRepresentante");
        cargaPedido.cdCargaPedido = rs.getString("cdCargaPedido");
        cargaPedido.cdRotaEntrega = rs.getString("cdRotaEntrega");
        cargaPedido.dsCargaPedido = rs.getString("dsCargaPedido");
        cargaPedido.dtCriacao = rs.getDate("dtCriacao");
        cargaPedido.hrCriacao = rs.getString("hrCriacao");
        cargaPedido.dtFechamento = rs.getDate("dtFechamento");
        cargaPedido.hrFechamento = rs.getString("hrFechamento");
        cargaPedido.flPesoMenorLiberado = rs.getString("flPesoMenorLiberado");
        cargaPedido.flCargaVencidaLiberada = rs.getString("flCargaVencidaLiberada");
        cargaPedido.flCargaFechada = rs.getString("flCargaFechada");
        if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) {
        	cargaPedido.dtEntrega = rs.getDate("dtEntrega");
        }
        cargaPedido.nuCarimbo = rs.getInt("nuCarimbo");
        cargaPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cargaPedido.cdUsuario = rs.getString("cdUsuario");
        return cargaPedido;
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
        sql.append(" CDCARGAPEDIDO,");
        sql.append(" CDROTAENTREGA,");
        sql.append(" DSCARGAPEDIDO,");
        sql.append(" DTCRIACAO,");
        sql.append(" HRCRIACAO,");
        sql.append(" DTFECHAMENTO,");
        sql.append(" HRFECHAMENTO,");
        sql.append(" FLPESOMENORLIBERADO,");
        sql.append(" FLCARGAVENCIDALIBERADA,");
        sql.append(" FLCARGAFECHADA,");
        if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) {
			sql.append(" DTENTREGA,");
		}
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCARGAPEDIDO,");
        sql.append(" CDROTAENTREGA,");
        sql.append(" DSCARGAPEDIDO,");
        sql.append(" DTCRIACAO,");
        sql.append(" HRCRIACAO,");
        sql.append(" DTFECHAMENTO,");
        sql.append(" HRFECHAMENTO,");
        sql.append(" FLPESOMENORLIBERADO,");
        sql.append(" FLCARGAVENCIDALIBERADA,");
        sql.append(" FLCARGAFECHADA,");
		sql.append(" DTENTREGA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CargaPedido cargaPedido = (CargaPedido) domain;
        sql.append(Sql.getValue(cargaPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(cargaPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(cargaPedido.cdCargaPedido)).append(",");
        sql.append(Sql.getValue(cargaPedido.cdRotaEntrega)).append(",");
        sql.append(Sql.getValue(cargaPedido.dsCargaPedido)).append(",");
        sql.append(Sql.getValue(cargaPedido.dtCriacao)).append(",");
        sql.append(Sql.getValue(cargaPedido.hrCriacao)).append(",");
        sql.append(Sql.getValue(cargaPedido.dtFechamento)).append(",");
        sql.append(Sql.getValue(cargaPedido.hrFechamento)).append(",");
        sql.append(Sql.getValue(cargaPedido.flPesoMenorLiberado)).append(",");
        sql.append(Sql.getValue(cargaPedido.flCargaVencidaLiberada)).append(",");
        sql.append(Sql.getValue(cargaPedido.flCargaFechada)).append(",");
        sql.append(Sql.getValue(cargaPedido.dtEntrega)).append(",");
        sql.append(Sql.getValue(cargaPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(cargaPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cargaPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CargaPedido cargaPedido = (CargaPedido) domain;
        sql.append(" CDROTAENTREGA = ").append(Sql.getValue(cargaPedido.cdRotaEntrega)).append(",");
        sql.append(" DSCARGAPEDIDO = ").append(Sql.getValue(cargaPedido.dsCargaPedido)).append(",");
        sql.append(" DTCRIACAO = ").append(Sql.getValue(cargaPedido.dtCriacao)).append(",");
        sql.append(" HRCRIACAO = ").append(Sql.getValue(cargaPedido.hrCriacao)).append(",");
        sql.append(" DTFECHAMENTO = ").append(Sql.getValue(cargaPedido.dtFechamento)).append(",");
        sql.append(" HRFECHAMENTO = ").append(Sql.getValue(cargaPedido.hrFechamento)).append(",");
        sql.append(" FLPESOMENORLIBERADO = ").append(Sql.getValue(cargaPedido.flPesoMenorLiberado)).append(",");
        sql.append(" FLCARGAVENCIDALIBERADA = ").append(Sql.getValue(cargaPedido.flCargaVencidaLiberada)).append(",");
        sql.append(" FLCARGAFECHADA = ").append(Sql.getValue(cargaPedido.flCargaFechada)).append(",");
        if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) {
        	sql.append(" DTENTREGA = ").append(Sql.getValue(cargaPedido.dtEntrega)).append(",");
        }
        sql.append(" NUCARIMBO = ").append(Sql.getValue(cargaPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cargaPedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(cargaPedido.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CargaPedido cargaPedido = (CargaPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cargaPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cargaPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCARGAPEDIDO = ", cargaPedido.cdCargaPedido);
		sqlWhereClause.addAndCondition("CDROTAENTREGA = ", cargaPedido.cdRotaEntrega);
		sqlWhereClause.addAndCondition("DTCRIACAO <= ", cargaPedido.dtLimiteExclusao);
		sqlWhereClause.addAndCondition("FLCARGAFECHADA = ", cargaPedido.flCargaFechada);
		if (cargaPedido.forceFlTipoAlteracao) {
			sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", cargaPedido.flTipoAlteracao);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    //@Override
    protected void addWhereMaxKey(BaseDomain domain, StringBuffer sql) {
    	CargaPedido cargaPedido = (CargaPedido) domain;
    	sql.append(" where CDEMPRESA = ").append(Sql.getValue(cargaPedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(cargaPedido.cdRepresentante));
    }
    
}