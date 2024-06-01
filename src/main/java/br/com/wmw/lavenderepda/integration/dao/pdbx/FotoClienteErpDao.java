package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class FotoClienteErpDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoClienteErp();
	}

    private static FotoClienteErpDao instance;
    

    
    public FotoClienteErpDao() {
		super(FotoClienteErp.TABLE_NAME);
	}
    
    public static FotoClienteErpDao getInstance() {
        if (instance == null) {
            instance = new FotoClienteErpDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	FotoClienteErp fotoClienteErp = new FotoClienteErp();
        fotoClienteErp.rowKey = rs.getString("rowkey");
        fotoClienteErp.cdEmpresa = rs.getString("cdEmpresa");
        fotoClienteErp.cdRepresentante = rs.getString("cdRepresentante");
        fotoClienteErp.cdCliente = rs.getString("cdCliente");
        fotoClienteErp.nmFoto = rs.getString("nmFoto");
        fotoClienteErp.nmFotoRelacionada = rs.getString("nmFotoRelacionada");
        fotoClienteErp.cdFotoCliente = rs.getInt("cdFotoCliente");
        fotoClienteErp.nuTamanho = rs.getInt("nuTamanho");
        fotoClienteErp.dtModificacao = rs.getDate("dtModificacao");
        fotoClienteErp.flFotoExcluida = rs.getString("flFotoExcluida");
        fotoClienteErp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        fotoClienteErp.nuCarimbo = rs.getInt("nuCarimbo");
        fotoClienteErp.cdUsuario = rs.getString("cdUsuario");
        return fotoClienteErp;
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
        sql.append(" NMFOTO,");
        sql.append(" NMFOTORELACIONADA,");
        sql.append(" CDFOTOCLIENTE,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" FLFOTOEXCLUIDA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	FotoClienteErp fotoClienteErp = (FotoClienteErp) domain;
    	sql.append(" FLFOTOEXCLUIDA = ").append(Sql.getValue(fotoClienteErp.flFotoExcluida)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoClienteErp.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	FotoClienteErp fotoClienteErp = (FotoClienteErp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoClienteErp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoClienteErp.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", fotoClienteErp.cdCliente);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoClienteErp.nmFoto);
		sqlWhereClause.addAndCondition("NMFOTORELACIONADA = ", fotoClienteErp.nmFotoRelacionada);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", fotoClienteErp.flTipoAlteracao);
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllNaoAlterados() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(null, sql);
    	sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        sql.append(" where cdEmpresa = ");
        sql.append(Sql.getValue(SessionLavenderePda.cdEmpresa));
        sql.append(" and cdRepresentante = ");
        sql.append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante));
    	sql.append(" and  (fltipoalteracao = ");
    	sql.append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
    	sql.append(" or fltipoalteracao is null)");
    	addOrderBy(sql, null);
    	return findAll(null, sql.toString());
    }
    
    public void updateAllFlTipoAlteracaoInserido() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("UPDATE ").append(tableName)
    	.append(" SET FLTIPOALTERACAO = '").append(BaseDomain.FLTIPOALTERACAO_INSERIDO)
    	.append("' WHERE FLTIPOALTERACAO IS NOT NULL ");
    	updateAll(sql.toString());
    }
	
	public void updateResetReceberFotosNovamente() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_ALTERADO);
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_INSERIDO);
	}

	private void updateResetFlTipoAlteracao(String flTipoAlteracao, String flTipoAlteracaoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
		.append(" SET FLTIPOALTERACAO = '").append(flTipoAlteracao)
		.append("' WHERE FLTIPOALTERACAO = '").append(flTipoAlteracaoFilter).append("'");
		updateAll(sql.toString());
	}

	public int countFotoCargaInicial() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(*) NUM FROM ").append(tableName).append(" WHERE FLTIPOALTERACAO IS NOT NULL ");
		return getInt(sql.toString());
	}

}